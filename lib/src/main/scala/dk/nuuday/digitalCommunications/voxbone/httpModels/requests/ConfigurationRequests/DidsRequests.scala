package dk.nuuday.digitalCommunications.voxbone.httpModels.requests.ConfigurationRequests
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ConfigurationRequestsTrait.DidsTrait
import dk.nuuday.digitalCommunications.voxbone.jsonModel.DidJsonProtocol
import dk.nuuday.digitalCommunications.voxbone.models.{DidConfiguration, ServerInfo}
import spray.json._
class DidsRequests()(
  implicit
  override val serverInfo: ServerInfo,
  override val apikey: ApikeyAuthentication,
  override val voxboneBasicCredentials: BasicAuthenticationCredentials) extends DidsTrait with DidJsonProtocol {
  override def applySetOfConfigurationsToADidRequest(didConfiguration: DidConfiguration)
    : HttpRequest = {
    val httpRequest = {
      val request = HttpRequest(
        HttpMethods.POST,
        akka.http.scaladsl.model.Uri(
          s"$sslPrefix${serverInfo.address}/v1/configuration/didConfiguration"),
        Seq(
          RawHeader("apikey", apikey.get),
          Authorization(akkaBasicAuthenticationCredentials),
          RawHeader("Accept", "application/json")),
        HttpEntity(ContentTypes.`application/json`,didConfiguration.toJson.compactPrint)
      )
      request
    }
    httpRequest
  }
  override def getVoxboneConnectivityInformation(): HttpRequest =
   {
    HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/configuration/pop"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
  }
}
