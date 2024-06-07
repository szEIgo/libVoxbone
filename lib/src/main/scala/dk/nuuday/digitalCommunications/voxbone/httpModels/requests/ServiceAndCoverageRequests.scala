package dk.nuuday.digitalCommunications.voxbone.httpModels.requests
import java.net.URLEncoder

import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.{QueryParameters, VoxboneQueryParameter}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ServiceAndCoverageRequestsTrait
import dk.nuuday.digitalCommunications.voxbone.models.{CountryCodesA3Enum, DidTypeEnum, ServerInfo}
final class ServiceAndCoverageRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends ServiceAndCoverageRequestsTrait {
  override def searchForCoverageByCountryRequest(
      queryParameters: QueryParameters
  ): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/country${queryParameters.toCombinedQueryString}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  override def getVoxboneCoverageByCityAndAreaCode(
      queryParameters: QueryParameters)
    : HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/inventory/country${queryParameters.toCombinedQueryString}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }




  override def getActivationRequirementsAndAvailableAreaCodeWithinACountryRequest(
      countryCodesA3Enum: CountryCodesA3Enum): HttpRequest = ???

  override def searchForCoverageByStatesInAmericaAndCanada(
      countryCodesA3Enum: CountryCodesA3Enum): HttpRequest = ???
}
object ServiceAndCoverageRequests {
  abstract class ServiceAndCoverageParameter extends VoxboneQueryParameter

  final case class CountryCodeA3Parameter(value: CountryCodesA3Enum)
      extends ServiceAndCoverageParameter {
    override def getQueryName: String = "countryCodeA3"
    override def getQueryValue: String = s"${URLEncoder.encode(value.entryName, "UTF-8")}"
  }

  final case class DidTypeParameter(value: DidTypeEnum) extends ServiceAndCoverageParameter {
    override def getQueryName: String = "didType"
    override def getQueryValue: String = s"${URLEncoder.encode(value.entryName, "UTF-8")}"
  }

}
