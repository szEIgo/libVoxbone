package dk.nuuday.digitalCommunications.voxbone.httpModels.requests.ConfigurationRequests
import java.net.URLEncoder

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests._
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ConfigurationRequestsTrait.CapacityGroupTrait
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.jsonModel.CapacityGroupJsonProtocol
import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._
class CapacityGroupRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends CapacityGroupTrait
    with CapacityGroupJsonProtocol
    with SprayJsonSupport {
  override def listCapacityGroupsRequest(queryParameters: QueryParameters): HttpRequest = {
    val httpRequest = {
      val request = HttpRequest(
        HttpMethods.GET,
        akka.http.scaladsl.model.Uri(
          s"$sslPrefix${serverInfo.address}/v1/configuration/capacitygroup${queryParameters.toCombinedQueryString}"),
        Seq(
          RawHeader("apikey", apikey.get),
          Authorization(akkaBasicAuthenticationCredentials),
          RawHeader("Accept", "application/json"))
      )
      request
    }
    httpRequest
  }
  override def getSpecificCapacityGroupByIdRequest(
      capacityGroupId: CapacityGroupId): HttpRequest = {
    HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/configuration/capacitygroup/${capacityGroupId.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
  }
  override def createANewCapacityGroupRequest(capacityGroup: CapacityGroup): HttpRequest =
    HttpRequest(
      HttpMethods.PUT,
      akka.http.scaladsl.model
        .Uri(s"$sslPrefix${serverInfo.address}/v1/configuration/capacitygroup"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")),
      HttpEntity(
        ContentTypes.`application/json`,
        DtoCapacityGroup(
          capacityGroup.capacityGroupId.value,
          capacityGroup.maximumCapacity.value,
          capacityGroup.description.value).toJson.compactPrint
      )
    )
  override def updateExistingCapacityGroupRequest(
      capacityGroupId: CapacityGroupId,
      capacityGroup: CapacityGroup): HttpRequest = HttpRequest(
    HttpMethods.PUT,
    akka.http.scaladsl.model.Uri(
      s"$sslPrefix${serverInfo.address}/v1/configuration/capacitygroup/${capacityGroupId.value}"),
    Seq(
      RawHeader("apikey", apikey.get),
      Authorization(akkaBasicAuthenticationCredentials),
      RawHeader("Accept", "application/json"))
  )
  override def removeACapacityGroupRequest(capacityGroupId: CapacityGroupId): HttpRequest =
    HttpRequest(
      HttpMethods.PUT,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/configuration/capacitygroup/${capacityGroupId.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
}
object CapacityGroupRequests {

  sealed trait CapacityGroupParameter extends VoxboneQueryParameter

  final case class CapacityGroupIdParameter(capacityGroupId: CapacityGroupId)
      extends CapacityGroupParameter {
    override def getQueryName: String = "capacityGroupId"
    override def getQueryValue: String = s"${capacityGroupId.value}"
  }

  final case class DescriptionParameter(description: Description) extends CapacityGroupParameter {
    override def getQueryName: String = "description"
    override def getQueryValue: String = s"${description.value}"
  }
  final case class E164Parameter(e164: E164) extends CapacityGroupParameter {
    override def getQueryName: String = "e164"
    override def getQueryValue: String = URLEncoder.encode(e164.value, "UTF-8")
  }
  /*
          Set(
        CapacityGroupIdParameter(capacityGroupId),
        DescriptionParameter(description),
        E164Parameter(e164),
        pageNumberParameter,
        pageSizeParameter).map(QueryParameter(_)).asJava)
 */

}
