package dk.nuuday.digitalCommunications.voxbone.httpModels.requests

import java.net.URLEncoder
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.{QueryParameter, QueryParameters, VoxboneQueryParameter}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.InventoryRequestsTrait
import dk.nuuday.digitalCommunications.voxbone.jsonModel.DidJsonProtocol
import dk.nuuday.digitalCommunications.voxbone.models.{CountryCodesA3Enum, Did, DidTypeEnum, ServerInfo, ServiceType}

final class InventoryRequests()(
  implicit
  override val serverInfo: ServerInfo,
  override val apikey: ApikeyAuthentication,
  override val voxboneBasicCredentials: BasicAuthenticationCredentials)
  extends InventoryRequestsTrait
    with DidJsonProtocol {

  override def listNumberInventoryRequest(
      queryParameters: QueryParameters
  ): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/did${queryParameters.toCombinedQueryString}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  override def listDidGroupsRequest(
                                     queryParameters: QueryParameters)
  : HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/didgroup${queryParameters.toCombinedQueryString}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  override def listCountryRestrictions(
                                        country: CountryCodesA3Enum
                                      ): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/country/${country.entryName}/restriction"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  override def listSipTrunksRequest(): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/trunk"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }
  override def cancelNumberSubscription(): HttpRequest = ???
  override def RemoveNumberFromYourInventoryRequest(): HttpRequest =
    ???
}

object InventoryRequests {
  final case class NumberInventory(dids: Vector[Did], resultCount: Int)
  final case class NumberInventoryJava(dids: java.util.List[Did], resultCount: Int)

  abstract class NumberInventoryParameter extends VoxboneQueryParameter









  final case class VoiceUriIdParameter(value: Int) extends NumberInventoryParameter {
    override def getQueryName: String = "voiceUriId"

    override def getQueryValue: String = s"$value"
  }

  final case class DidIdsParameter(from: Int, to: Int) extends NumberInventoryParameter {
    override def getQueryName = "didIds"
    override def getQueryValue: String = s"$from,$to"
  }
  final case class DidGroupIdsParameter(from: Int, to: Int) extends NumberInventoryParameter {
    override def getQueryName = "didGroupIds"
    override def getQueryValue: String = s"$from,$to"
  }

  final case class E164PatternParameter(value: String) extends NumberInventoryParameter {
    override def getQueryName: String = "e164Pattern"
    override def getQueryValue: String = URLEncoder.encode(s"$value", "UTF-8")
  }

  final case class RegulationAddressIdParamter(value: Int) extends NumberInventoryParameter {
    override def getQueryName: String = "regulationAddressId"
    override def getQueryValue: String = s"$value"
  }

  final case class FaxUriIdParameter(value: Int) extends NumberInventoryParameter {
    override def getQueryName: String = "faxUriId"
    override def getQueryValue: String = s"$value"
  }

  final case class SmsLinkGroupIdParameter(value: Int) extends NumberInventoryParameter {
    override def getQueryName: String = "smsLinkGroupId"
    override def getQueryValue: String = s"$value"
  }

  final case class NeedAddressLinkParameter(value: Boolean) extends NumberInventoryParameter {
    override def getQueryName: String = "needAddressLink"
    override def getQueryValue: String = s"$value"
  }

  final case class ServiceTypeParameter(value: ServiceType) extends NumberInventoryParameter {
    override def getQueryName: String = "serviceType"

    override def getQueryValue: String = s"${value.entryName}"
  }

  final case class OrderReferenceParameter(value: String) extends NumberInventoryParameter {
    override def getQueryName: String = "orderReference"
    override def getQueryValue: String = s"$value"
  }

  final case class PortingReferenceParamter(value: String) extends NumberInventoryParameter {
    override def getQueryName: String = "portingReference"
    override def getQueryValue: String = s"$value"
  }

  final case class DeliveryIdParameter(value: Int) extends NumberInventoryParameter {
    override def getQueryName: String = "deliveryId"
    override def getQueryValue: String = s"$value"
  }

  final case class SmsOutboundParameter(value: Boolean) extends NumberInventoryParameter {
    override def getQueryName: String = "smsOutbound"
    override def getQueryValue: String = s"$value"
  }

  final case class WebRtcEnabledParameter(value: Boolean) extends NumberInventoryParameter {
    override def getQueryName: String = "webRtcEnabled"
    override def getQueryValue: String = s"$value"
  }

  final case class VoxoutNationalEnabledParameter(value: Boolean) extends NumberInventoryParameter {
    override def getQueryName: String = "voxoutNationalEnabled"
    override def getQueryValue: String = s"$value"
  }

  final case class VoxoutInternationalEnabledParameter(value: Boolean)
    extends NumberInventoryParameter {
    override def getQueryName: String = "voxoutInternationalEnabled"
    override def getQueryValue: String = s"$value"
  }


  abstract class DidGroupParameter extends VoxboneQueryParameter
  final case class StateIdParameter(value: Boolean)
    extends DidGroupParameter {
    override def getQueryName: String = "stateId"
    override def getQueryValue: String = s"$value"
  }
  final case class CityNamePatternParameter(cityNamePattern: String) extends DidGroupParameter {
    override def getQueryName: String = "cityNamePattern"
    override def getQueryValue: String = s"$cityNamePattern"
  }
  final case class RateCenterParameter(rateCenter: String) extends DidGroupParameter {
    override def getQueryName: String = "rateCenter"
    override def getQueryValue: String = s"${rateCenter}"
  }
  final case class AreaCodeParameter(areaCode: Int) extends DidGroupParameter {
    override def getQueryName: String = "areaCode"
    override def getQueryValue: String = s"$areaCode"
  }
  final case class DidTypeParameter(didTypeEnum: DidTypeEnum) extends DidGroupParameter {
    override def getQueryName: String = "didType"
    override def getQueryValue: String = s"${didTypeEnum.entryName}"
  }

  final case class ShowEmptyParameter(showEmpty: Boolean) extends DidGroupParameter {
    override def getQueryName: String = "showEmpty"
    override def getQueryValue: String = s"$showEmpty"
  }
  case class FeatureId(id: Int) extends DidGroupParameter {
    override def getQueryName: String = "featureIds"
    override def getQueryValue: String = s"id"
  }
  case class FeatureIds(ids: java.util.Set[FeatureId])

  case class FeatureIdsParameters(featureIds: FeatureIds)

}
