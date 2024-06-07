package dk.nuuday.digitalCommunications.voxbone.httpModels.requests

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, RawHeader}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.OrderingRequestTrait
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.jsonModel.CartJsonProtocol
import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._
class OrderingRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends OrderingRequestTrait
    with CartJsonProtocol {

  def createCartRequest(
      customerReference: CustomerReference,
      description: Description): HttpRequest = {
    val bodyContent = JsObject(
      Map("customerReference" -> customerReference.toJson, "description" -> description.toJson))
    val request = HttpRequest(
      HttpMethods.PUT,
      akka.http.scaladsl.model.Uri(s"$sslPrefix${serverInfo.address}/v1/ordering/cart"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")
      ),
      HttpEntity(ContentTypes.`application/json`, bodyContent.compactPrint)
    )
    request
  }

  def addProductsToCartRequest(
      cartIdentifier: CartIdentifier,
      didCartItem: DidCartItem,
      capacityCartItem: CapacityCartItem,
      credeitPackageCartItem: CreditPackageCartItem): HttpRequest = {
    val bodyContent =
      JsObject(
        "cartIdentifier" -> cartIdentifier.value.toJson,
        "didCartItem" -> didCartItem.toJson,
        "capacityCartItem" -> capacityCartItem.toJson,
        "credeitPackageCartItem" -> credeitPackageCartItem.toJson
      )
    val request = HttpRequest(
      HttpMethods.POST,
      akka.http.scaladsl.model
        .Uri(s"$sslPrefix${serverInfo.address}/v1/ordering/cart/${cartIdentifier.value}/product"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")
      ),
      HttpEntity(ContentTypes.`application/json`, bodyContent.compactPrint)
    )
    request
  }

  def destroyACartAndAllItsContentsRequest(cartIdentifier: CartIdentifier): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.DELETE,
      akka.http.scaladsl.model
        .Uri(s"$sslPrefix${serverInfo.address}/v1/ordering/cart/${cartIdentifier.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")
      )
    )
    request
  }

  def removeProductsFromCartOrChangeQuantityRequest(
      cartIdentifier: CartIdentifier,
      orderProductId: OrderProductId,
      quantity: Quantity): HttpRequest = {
    val bodyContent =
      JsObject(
        "cartIdentifier" -> cartIdentifier.value.toJson,
        "orderProductId" -> orderProductId.value.toJson,
        "quantity" -> quantity.value.toJson
      )
    val request = HttpRequest(
      HttpMethods.POST,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/ordering/cart/${cartIdentifier.value}/product/${orderProductId.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")
      ),
      HttpEntity(ContentTypes.`application/json`, bodyContent.compactPrint)
    )
    request
  }

  def checkoutCartRequest(cartIdentifier: CartIdentifier): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model
        .Uri(s"$sslPrefix${serverInfo.address}/v1/ordering/cart/${cartIdentifier.value}/checkout"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }
}
