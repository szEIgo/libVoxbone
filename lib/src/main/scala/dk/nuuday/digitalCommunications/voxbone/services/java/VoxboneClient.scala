package dk.nuuday.digitalCommunications.voxbone.services.java

import java.util.Optional
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode}
import dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages._
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.{
  NumberInventory,
  NumberInventoryJava
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.QueryParameters
import dk.nuuday.digitalCommunications.voxbone.exceptions.VoxboneException
import dk.nuuday.digitalCommunications.voxbone.jsonModel.{
  CombinedVoxboneJsonProtocol,
  ErrorMessageProtocol
}
import dk.nuuday.digitalCommunications.voxbone.models._
import org.slf4j.{Logger, LoggerFactory}
import spray.json._

import java.io.IOException
import scala.concurrent.duration.{Duration, DurationLong}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._
import scala.util.Try

class VoxboneClient(requestTimeoutSeconds: Long)(
    implicit actorSystem: ActorSystem,
    requests: VoxboneHttpRequests
) extends VoxboneClientInterface
    with CombinedVoxboneJsonProtocol
    with ErrorMessageProtocol {
  private implicit val ec: ExecutionContext = actorSystem.dispatcher
  private[this] def log: Logger = LoggerFactory.getLogger(this.getClass)
  private def fireRequest(httpRequest: HttpRequest): Future[HttpResponse] = {
    log.debug(s"Fired the following ${httpRequest.method.value} request: ${httpRequest.uri.toString()}")
    Http().singleRequest(httpRequest).map { resp =>
      log.debug(s"Response received: $resp")
      resp
    }
  }

  private def akkaHttpStatusCodeMapper(httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if statusCodeInt == 429 =>
        httpResponse.entity.discardBytes()
        throw VoxboneException("HTTP 429 Too Many Requests")
      case statusCodeInt if statusCodeInt == 200 => httpResponse
      case statusCodeInt if Try(StatusCode.int2StatusCode(statusCodeInt)).isSuccess =>
        val eventualTriedString: Future[String] = httpResponse.entity
          .toStrict(requestTimeoutSeconds.seconds)
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
                  .getOrElse(throw new IOException(bodyUtf8String)))
              .map(_.description())
              .getOrElse(bodyUtf8String)
          }

        throw VoxboneException(Await.result(eventualTriedString, 30.seconds))
      case _ =>
        httpResponse.entity.discardBytes()
        throw new RuntimeException("Unknown Response - no relevant mapping accessible")

    }
  }

  private def mapServiceActivationErrorCodesToException(
      httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if ServiceActivationErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val serviceActivationErrorCodes =
          ServiceActivationErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(serviceActivationErrorCodes.description())
        log.error(serviceActivationErrorCodes.toString, exception)
        httpResponse.entity.discardBytes()
        throw exception
      case _ => akkaHttpStatusCodeMapper(httpResponse)
    }
  }

  private def mapNumberConfigurationErrorCodestoException(
      httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if NumberConfigurationErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val numberConfigurationErrorCodes =
          NumberConfigurationErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(numberConfigurationErrorCodes.description())
        log.error(numberConfigurationErrorCodes.toString, exception)
        httpResponse.entity.discardBytes()
        throw exception
      case _ => akkaHttpStatusCodeMapper(httpResponse)
    }
  }

  private def mapCommonErrorCodesToException(httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if CommonErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val commonErrorCodes = CommonErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(commonErrorCodes.description())
        log.error(commonErrorCodes.toString, exception)
        httpResponse.entity.discardBytes()
        throw exception
      case _ => akkaHttpStatusCodeMapper(httpResponse)
    }
  }

  private def mapOrderingErrorCodesToException(httpResponse: HttpResponse): HttpResponse = {
    httpResponse.status.intValue() match {
      case statusCodeInt if OrderingErrorCodes.withValueOpt(statusCodeInt).isDefined =>
        val orderingErrorCodes = OrderingErrorCodes.valuesToEntriesMap(statusCodeInt)
        val exception = VoxboneException(orderingErrorCodes.description())
        log.error(orderingErrorCodes.toString, exception)
        httpResponse.entity.discardBytes()
        throw exception
      case _ => akkaHttpStatusCodeMapper(httpResponse)
    }
  }

  val s: Optional[CountryCodesA3Enum.Denmark.type] = Some(CountryCodesA3Enum.Denmark).toJava

  override def listNumberInventory(queryParameters: QueryParameters): NumberInventoryJava = {
    log.debug(s"listNumberInventory() has been called with $queryParameters")
    val eventualInventory =
      fireRequest(requests.inventoryRequests.listNumberInventoryRequest(queryParameters)).flatMap {
        resp =>
          val mappedResp = akkaHttpStatusCodeMapper(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(_.data)
            .map { bs =>
              val jsonStringUTF8 = bs.utf8String
              log.debug(s"Data Entity received in UTF8-String ${jsonStringUTF8}")
              log.debug(s"Entity: parsed to JsValue ${jsonStringUTF8.toJson}")
              log.debug(s"Entity: Compact Printed ${jsonStringUTF8.toJson.compactPrint}")
              jsonStringUTF8.parseJson.convertTo[NumberInventory]
            }
      }
    val numberInventory = Await.result(eventualInventory, requestTimeoutSeconds.seconds)
    NumberInventoryJava(numberInventory.dids.asJava, numberInventory.resultCount)

  }
  override def createCart(
      customerReference: CustomerReference,
      description: Description): CartJava = {

    log.debug(s"createCart() has been called with $customerReference, $description")
    val eventualCart: Future[Cart] =
      fireRequest(requests.orderingRequests().createCartRequest(customerReference, description))
        .flatMap { resp =>
          val mappedResp = mapOrderingErrorCodesToException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[CartDto].cart
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds).toJava
  }
  override def addProductsToCart(
      cartIdentifier: CartIdentifier,
      didCartItem: DidCartItem,
      capacityCartItem: CapacityCartItem,
      creditPackageCartItem: CreditPackageCartItem): Status = {
    log.debug(
      s"addProductsToCart() has been called with $cartIdentifier,$didCartItem,$capacityCartItem,$creditPackageCartItem")

    val eventualCart: Future[Status] =
      fireRequest(
        requests
          .orderingRequests()
          .addProductsToCartRequest(
            cartIdentifier,
            didCartItem,
            capacityCartItem,
            creditPackageCartItem))
        .flatMap { resp =>
          val mappedResp = mapOrderingErrorCodesToException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[StatusDto].status
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }
  override def destroyACartAndAllItsContent(cartIdentifier: CartIdentifier): Status = {
    log.debug(s"destroyACartAndAllItsContent() has been called with $cartIdentifier")

    val eventualCart: Future[Status] =
      fireRequest(
        requests
          .orderingRequests()
          .destroyACartAndAllItsContentsRequest(cartIdentifier))
        .flatMap { resp =>
          val mappedResp = mapOrderingErrorCodesToException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[StatusDto].status
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }
  override def removeProductsFromCartOrChangeQuantity(
      cartIdentifier: CartIdentifier,
      orderProductId: OrderProductId,
      quantity: Quantity): Status = {
    log.debug(
      s"removeProductsFromCartOrChangeQuantity() has been called with $cartIdentifier,$orderProductId,$quantity")
    val eventualCart: Future[Status] =
      fireRequest(
        requests
          .orderingRequests()
          .removeProductsFromCartOrChangeQuantityRequest(cartIdentifier, orderProductId, quantity))
        .flatMap { resp =>
          val mappedResp = mapOrderingErrorCodesToException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[StatusDto].status
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }

  override def checkoutCart(cartIdentifier: CartIdentifier): CheckoutCartDtoJava = {
    log.debug(s"checkoutCart() has been called with $cartIdentifier")
    val eventualCart: Future[CheckoutCartDtoJava] =
      fireRequest(
        requests
          .orderingRequests()
          .checkoutCartRequest(cartIdentifier))
        .flatMap { resp =>
          val mappedResp = mapOrderingErrorCodesToException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[CheckoutCartDto].toJava
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }

  override def listVoiceUris(queryParameters: QueryParameters): VoiceUrisJava = {
    log.debug(s"listVoiceUris() has been called with $queryParameters")
    val eventualCart: Future[VoiceUrisJava] =
      fireRequest(
        requests
          .configurationRequests()
          .voiceRequests()
          .listVoiceUriRequest(queryParameters))
        .flatMap { resp =>
          val mappedResp = mapNumberConfigurationErrorCodestoException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[VoiceUris].toJava()
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }
  override def getASpecificVoiceUriById(voiceUriId: VoiceUriId): VoiceUrisJava = {
    log.debug(s"getASpecificVoiceURIById() has been called with $voiceUriId")
    val eventualCart: Future[VoiceUrisJava] =
      fireRequest(
        requests
          .configurationRequests()
          .voiceRequests()
          .getASpecificVoiceUriByIdRequest(voiceUriId))
        .flatMap { resp =>
          val mappedResp = mapNumberConfigurationErrorCodestoException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[VoiceUris].toJava()
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }
  override def changeVoiceUri(voiceUri: VoiceUri): ChangeVoiceResponse = {
    log.debug(s"changeVoiceUri() has been called with $voiceUri")
    val eventualCart: Future[ChangeVoiceResponse] =
      fireRequest(requests.configurationRequests().voiceRequests().changeVoiceUriRequest(voiceUri))
        .flatMap { resp =>
          val mappedResp = mapNumberConfigurationErrorCodestoException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[ChangeVoiceResponse]
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }

  override def createVoiceUri(newVoiceUri: NewVoiceUri): VoiceUriResponse = {
    log.debug(s"createVoiceUri() has been called with $newVoiceUri")
    val eventualCart: Future[VoiceUriResponse] =
      fireRequest(
        requests.configurationRequests().voiceRequests().createVoiceUriRequest(newVoiceUri))
        .flatMap { resp =>
          val mappedResp = mapNumberConfigurationErrorCodestoException(resp)
          mappedResp.entity
            .toStrict(requestTimeoutSeconds.seconds)
            .map(strict => {
              val utf8String = strict.data.utf8String
              utf8String.parseJson.convertTo[VoiceUriResponseDto].voiceUri
            })
        }
    Await.result(eventualCart, requestTimeoutSeconds.seconds)
  }

  override def searchForCoverageByCountry(
      queryParameters: QueryParameters): CoverageByCountryResponse = {
    log.debug(s"searchForCoverageByCountry() has been called with ${queryParameters}")
    val eventualResponse = fireRequest(
      requests
        .serviceAndCoverageRequests()
        .searchForCoverageByCountryRequest(queryParameters)).flatMap { resp =>
      val mappedResp = mapServiceActivationErrorCodesToException(resp)
      mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
        strict.data.utf8String.parseJson
          .convertTo[DtoCoverageByCountryResponse]
          .toCoverageByCountryResponse
      }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def listCapacityGroups(queryParameters: QueryParameters): CapacityGroupsResponse = {
    log.debug(s"listCapacityGroups() has been called with ${queryParameters.parameters.asScala}")
    val eventualResponse = fireRequest(
      requests.capacityGroupRequests().listCapacityGroupsRequest(queryParameters)).flatMap { resp =>
      val mappedResp = akkaHttpStatusCodeMapper(resp)
      mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
        strict.data.utf8String.parseJson
          .convertTo[DtoCapacityGroupsResponse]
          .toCapacityGroupsResponse()
      }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }

  override def getSpecificCapacityGroupById(
      capacityGroupId: CapacityGroupId): CapacityGroupsResponse = {
    log.debug(s"getSpecificCapacityGroupById() has been called with $capacityGroupId}")
    val eventualResponse = fireRequest(
      requests.capacityGroupRequests().getSpecificCapacityGroupByIdRequest(capacityGroupId))
      .flatMap { resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          strict.data.utf8String.parseJson
            .convertTo[DtoCapacityGroupsResponse]
            .toCapacityGroupsResponse()
        }
      }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def createANewCapacityGroup(capacityGroup: CapacityGroup): CapacityGroupResponse = {
    log.debug(s"createANewCapacityGroup() has been called with ${capacityGroup}")
    val eventualResponse = fireRequest(
      requests.capacityGroupRequests().createANewCapacityGroupRequest(capacityGroup)).flatMap {
      resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          strict.data.utf8String.parseJson
            .convertTo[DtoCapacityGroupResponse]
            .toCapacityGroupResponse()
        }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def updateExistingCapacityGroup(
      capacityGroupId: CapacityGroupId,
      capacityGroup: CapacityGroup): CapacityGroupResponse = {
    log.debug(s"updateExistingCapacityGroup() has been called $capacityGroupId}, $capacityGroup")
    val eventualResponse = fireRequest(
      requests
        .capacityGroupRequests()
        .updateExistingCapacityGroupRequest(capacityGroupId, capacityGroup)).flatMap { resp =>
      val mappedResp = akkaHttpStatusCodeMapper(resp)
      mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
        strict.data.utf8String.parseJson
          .convertTo[DtoCapacityGroupResponse]
          .toCapacityGroupResponse()
      }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def removeACapacityGroup(capacityGroupId: CapacityGroupId): Status = {
    log.debug(s"removeACapacityGroup() has been called $capacityGroupId}")
    val eventualResponse = fireRequest(
      requests.capacityGroupRequests().removeACapacityGroupRequest(capacityGroupId)).flatMap {
      resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          strict.data.utf8String.parseJson.convertTo[Status]
        }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def applySetOfConfigurationToAdid(
      didConfiguration: DidConfiguration): DidConfigurationResponse = {
    log.debug(s"applySetOfConfigurationToAdid() has been called $didConfiguration}")
    val eventualResponse = fireRequest(
      requests.didsRequest().applySetOfConfigurationsToADidRequest(didConfiguration)).flatMap {
      resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          val bodyUtf8String = strict.data.utf8String
          bodyUtf8String.parseJson.convertTo[DidConfigurationResponse].copy(jsonString = Optional.of(bodyUtf8String))
        }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def getVoxboneConnectivityInformation: Pops = {
    log.debug(s"getVoxboneConnectivityInformation() has been called")
    val eventualResponse =
      fireRequest(requests.didsRequest().getVoxboneConnectivityInformation()).flatMap { resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          val string = strict.data.utf8String
          string.parseJson.convertTo[Pops]
        }
      }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def listAvailableTrunks(): Trunks = {
    log.debug(s"listAvailableTrunks() has been called")
    val eventualResponse = fireRequest(requests.inventoryRequests.listSipTrunksRequest()).flatMap {
      resp =>
        val mappedResp = akkaHttpStatusCodeMapper(resp)
        mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
          strict.data.utf8String.parseJson.convertTo[Trunks]
        }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }
  override def listDidGroups(queryParameters: QueryParameters): DidGroupsResponse = {
    log.debug(s"listDidGroups() has been called with queryParameters $queryParameters")
    val eventualResponse =
      fireRequest(requests.inventoryRequests.listDidGroupsRequest(queryParameters)).flatMap {
        resp =>
          val mappedResp = akkaHttpStatusCodeMapper(resp)
          mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
            strict.data.utf8String.parseJson.convertTo[DidGroupsResponse]
          }
      }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)

  }

  override def listCountryRestrictions(country: CountryCodesA3Enum): CountryRestrictions = {
    log.debug(s"listCountryRestrictions() has been called with country ${country.entryName}")

  val eventualResponse =
      fireRequest(requests.inventoryRequests.listCountryRestrictions(country)).flatMap {
        resp =>
          val mappedResp = akkaHttpStatusCodeMapper(resp)
          mappedResp.entity.toStrict(requestTimeoutSeconds.seconds).map { strict =>
            strict.data.utf8String.parseJson.convertTo[CountryRestrictions]
          }
    }
    Await.result(eventualResponse, requestTimeoutSeconds.seconds)
  }

}
