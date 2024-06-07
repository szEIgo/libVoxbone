package dk.nuuday.digitalCommunications.voxbone.services.scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode}
import akka.http.scaladsl.unmarshalling.Unmarshal
import dk.nuuday.digitalCommunications.voxbone.exceptions.VoxboneException
import dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages._
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.NumberInventory
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.QueryParameters
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests.CdrParameters
import dk.nuuday.digitalCommunications.voxbone.jsonModel.{CombinedVoxboneJsonProtocol, ErrorMessageProtocol}
import dk.nuuday.digitalCommunications.voxbone.models._
import org.slf4j.LoggerFactory
import spray.json._

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.util.Try

class VoxboneClient()(implicit actorSystem: ActorSystem, requests: VoxboneHttpRequests)
    extends VoxboneClientInterface
    with CombinedVoxboneJsonProtocol
    with ErrorMessageProtocol {
  private[this] def log = LoggerFactory.getLogger(this.getClass)
  private implicit val ec = actorSystem.dispatcher

  private def fireRequest(httpRequest: HttpRequest): Future[HttpResponse] = {
    log.debug(s"Fired the following ${httpRequest.method.value} request: ${httpRequest.uri.toString()}")
    Http().singleRequest(httpRequest).map { resp =>
      log.debug(s"Response received: $resp")
      akkaHttpStatusCodeMapper(resp)
    }

  }

  private def akkaHttpStatusCodeMapper(httpResponse: HttpResponse): HttpResponse = {
    import scala.jdk.CollectionConverters._
    httpResponse.status.intValue() match {
      case statusCodeInt if statusCodeInt == 429 =>
        httpResponse.entity.discardBytes()
        throw VoxboneException("HTTP 429 Too Many Requests")
      case statusCodeInt if statusCodeInt == 200 => httpResponse
      case statusCodeInt if Try(StatusCode.int2StatusCode(statusCodeInt)).isSuccess =>
        val eventualTriedString: Future[String] = httpResponse.entity
          .toStrict(10.seconds)
          .map { body =>
            val bodyUtf8String = body.data.utf8String
            CommonErrorCodes
              .withValueOpt(
                bodyUtf8String.parseJson
                  .convertTo[ErrorMessage]
                  .errors
                  .asScala
                  .head
                  .apiErrorCode
                  .toString
                  .toIntOption
                  .getOrElse(throw new NumberFormatException("Not a valid Int inside the string")))
              .map(_.description())
              .getOrElse(bodyUtf8String)
          }
          .map { str =>
            println(str)
            str
          }
        throw VoxboneException(Await.result(eventualTriedString, Duration.Inf))
      case _ =>
        httpResponse.entity.discardBytes()
        throw new RuntimeException("Unknown Response - no relevant mapping accessible")

    }
  }

  private def mapErrorCodesToException(httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if CommonErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val commonErrorCodes = CommonErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(commonErrorCodes.description())
        log.error(commonErrorCodes.toString, exception)
        httpResponse.entity.discardBytes();
        throw exception
      case statusCodeInt if NumberConfigurationErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val numberConfigurationErrorCodes =
          NumberConfigurationErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(numberConfigurationErrorCodes.description())
        log.error(numberConfigurationErrorCodes.toString, exception)
        httpResponse.entity.discardBytes();
        throw exception
      case statusCodeInt if OrderingErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val orderingErrorCodes = OrderingErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(orderingErrorCodes.description())
        log.error(orderingErrorCodes.toString, exception)
        httpResponse.entity.discardBytes();
        throw exception
      case statusCodeInt if ServiceActivationErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val serviceActivationErrorCodes =
          ServiceActivationErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(serviceActivationErrorCodes.description())
        log.error(serviceActivationErrorCodes.toString, exception)
        httpResponse.entity.discardBytes();
        throw exception
      case statusCodeInt if statusCodeInt == 429 =>
        httpResponse.entity.discardBytes();
        throw VoxboneException("HTTP 429 Too Many Requests")
      case statusCodeInt if (statusCodeInt == 200) => httpResponse
      case other =>
        httpResponse.entity.discardBytes();
        throw new RuntimeException("Unknown Response - no relevant mapping accessible")
    }
  }

  override def listNumberInventory(queryParameters: QueryParameters): Future[NumberInventory] =
    fireRequest(requests.inventoryRequests.listNumberInventoryRequest(queryParameters)).flatMap {
      resp =>
        println(s"response received $resp")
        resp.entity
          .toStrict(10.seconds)
          .map(_.data)
          .map { bs =>
            println(bs.utf8String)
            val inventory: NumberInventory = bs.utf8String.parseJson.convertTo[NumberInventory]
            println(inventory)
            inventory
          }
    }
  override def createCart(
      customerReference: CustomerReference,
      description: Description): Future[Cart] =
    fireRequest(requests.orderingRequests.createCartRequest(customerReference, description))
      .flatMap { resp =>
        println(s"response received $resp")
        resp.entity
          .toStrict(10.seconds)
          .map(_.data)
          .map { bs =>
            println(bs.utf8String)
            bs.utf8String.parseJson.convertTo[CartDto].cart
          }
      }
  override def addProductsToCart(
      cartIdentifier: CartIdentifier,
      didCartItem: DidCartItem,
      capacityCartItem: CapacityCartItem,
      credeitPackageCartItem: CreditPackageCartItem): Future[Status] =
    fireRequest(
      requests.orderingRequests
        .addProductsToCartRequest(
          cartIdentifier,
          didCartItem,
          capacityCartItem,
          credeitPackageCartItem))
      .flatMap { resp =>
        println(s"response received $resp")
        resp.entity
          .toStrict(10.seconds)
          .map(_.data)
          .map { bs =>
            println(bs.utf8String)
            bs.utf8String.parseJson.convertTo[StatusDto].status
          }
      }
  override def destroyACartAndAllItsContent(cartIdentifier: CartIdentifier): Future[Status] =
    fireRequest(
      requests.orderingRequests
        .destroyACartAndAllItsContentsRequest(cartIdentifier))
      .flatMap { resp =>
        println(s"response received $resp")
        resp.entity
          .toStrict(10.seconds)
          .map(_.data)
          .map { bs =>
            println(bs.utf8String)
            bs.utf8String.parseJson.convertTo[StatusDto].status
          }
      }
  override def removeProductsFromCartOrChangeQuantity(
      cartIdentifier: CartIdentifier,
      orderProductId: OrderProductId,
      quantity: Quantity): Future[Status] =
    fireRequest(
      requests
        .orderingRequests()
        .removeProductsFromCartOrChangeQuantityRequest(cartIdentifier, orderProductId, quantity))
      .flatMap { resp =>
        println(s"response received $resp")
        resp.entity
          .toStrict(10.seconds)
          .map(_.data)
          .map { bs =>
            println(bs.utf8String)
            bs.utf8String.parseJson.convertTo[StatusDto].status
          }
      }

  override def listCdrsWithFilters(cdrParameters: CdrParameters): Future[List[Cdr]] = {
    val uriMinusQueryParams = requests.cdrRequests().uriMinusQueryParams
    val link = s"$uriMinusQueryParams${cdrParameters.toQueryParameters().toCombinedQueryString}"
    fetchAllCdrsInBatches(requestLink = link)
  }

  /**
   * Fetches all cdr's at the link param. Supports pagination (see getNextLink)
   *
   * @param fetchedCdrs the cdr's which have been fetched so far
   * @param requestLink the link to the next set of cdrs
   * @return a Future list of cdr's
   */
  private def fetchAllCdrsInBatches(fetchedCdrs: List[Cdr] = List.empty, requestLink: String): Future[List[Cdr]] = {
    fireRequest(requests.cdrRequests().listCDRsWithFiltersPaginationRequest(requestLink)).flatMap { resp =>
      Unmarshal(resp.entity).to[String].map(_.parseJson.convertTo[CdrResponse]).flatMap{cdrResponse =>
        val concatList = fetchedCdrs ++ cdrResponse.data
        //log for each batch what was fetched and what remains (total > limit)
        if (cdrResponse.pagination.total > cdrResponse.pagination.limit) {
          val logString =
            if (concatList.size != cdrResponse.pagination.total) {
              s"${cdrResponse.data.size} cdr's fetched this time: a total of ${concatList.size} fetched so far (out of ${cdrResponse.pagination.total})"
            } else {
              s"${cdrResponse.data.size} cdr's fetched this time: fetched all ${cdrResponse.pagination.total} cdr's"
            }
          log.info(logString)
        } else {
          log.info(s"fetched all ${cdrResponse.pagination.total} cdr's")
        }
        val nextLink = getNextLink(cdrResponse)
        if(nextLink.isDefined) {
          log.info(s"${cdrResponse.pagination.total - concatList.size} remaining")
          fetchAllCdrsInBatches(concatList, nextLink.get)
        }
        else {
          Future.successful(concatList)
        }
      }
    }
  }

  /**
   * Voxbone api supports pagination by providing a link to the next set
   * of data -> if any element in the list data.links contains an attribute
   * rel which is equal to "next", that element will contain the link on
   * the attribute href
   *
   * @param cdrResponse the unmarshalled response from Voxbone which may or
   *   may not contain the next link (depending on if there is more data to
   *   be collected)
   * @return Some(next link) if present or None
   */
  private def getNextLink(cdrResponse: CdrResponse): Option[String] = {
    val link: Option[Link] = cdrResponse.links.find(_.rel.equalsIgnoreCase("next"))
    if (link.isDefined && link.get.href != null) {
      Some(link.get.href)
    } else {
      None
    }
  }

}
