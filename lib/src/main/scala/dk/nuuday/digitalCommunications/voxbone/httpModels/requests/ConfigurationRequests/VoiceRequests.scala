package dk.nuuday.digitalCommunications.voxbone.httpModels.requests.ConfigurationRequests
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.{
  QueryParameters,
  VoxboneQueryParameter
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ConfigurationRequestsTrait.VoiceTrait
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.jsonModel.VoiceUrisJsonProtocol
import dk.nuuday.digitalCommunications.voxbone.models.{
  NewVoiceUri,
  ServerInfo,
  VoiceUri,
  VoiceUriId
}
import spray.json._

class VoiceRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends VoiceTrait
    with VoiceUrisJsonProtocol {

  def listVoiceUriRequest(queryParameters: QueryParameters): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/configuration/voiceuri${queryParameters.toCombinedQueryString}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  def getASpecificVoiceUriByIdRequest(voiceUriId: VoiceUriId): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model
        .Uri(s"$sslPrefix${serverInfo.address}/v1/configuration/voiceuri/${voiceUriId.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  def changeVoiceUriRequest(voiceUri: VoiceUri): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.PUT,
      akka.http.scaladsl.model.Uri(
        s"$sslPrefix${serverInfo.address}/v1/configuration/voiceuri/${voiceUri.voiceUriId.value}"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")),
      HttpEntity(ContentTypes.`application/json`, voiceUri.toJson.compactPrint)
    )
    request
  }
  override def createVoiceUriRequest(newVoiceUri: NewVoiceUri): HttpRequest = {
    val request = HttpRequest(
      HttpMethods.PUT,
      akka.http.scaladsl.model.Uri(s"$sslPrefix${serverInfo.address}/v1/configuration/voiceuri"),
      Seq(
        RawHeader("apikey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json")),
      HttpEntity(ContentTypes.`application/json`, newVoiceUri.toJson.compactPrint)
    )
    request
  }
}

object VoiceRequests {
  abstract class VoiceUriParameter extends VoxboneQueryParameter

  final case class BackupUriIdParameter(value: Int) extends VoiceUriParameter {
    override def getQueryName: String = "backupUriId"
    override def getQueryValue: String = s"$value"
  }

  final case class VoiceUriProtocolParameter(value: String) extends VoiceUriParameter {
    override def getQueryName: String = "voiceUriProtocol"
    override def getQueryValue: String = s"$value"
  }
  final case class UriParameter(value: String) extends VoiceUriParameter {
    override def getQueryName: String = "uri"
    override def getQueryValue: String = s"$value"
  }
  final case class DescriptionParameter(value: String) extends VoiceUriParameter {
    override def getQueryName: String = "description"
    override def getQueryValue: String = s"$value"
  }
}
