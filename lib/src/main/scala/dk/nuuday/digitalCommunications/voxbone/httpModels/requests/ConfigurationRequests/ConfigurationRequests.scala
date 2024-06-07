package dk.nuuday.digitalCommunications.voxbone.httpModels.requests.ConfigurationRequests
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, RawHeader}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ConfigurationRequestsTrait
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.ConfigurationRequestsTrait.CapacityGroupTrait
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.models.ServerInfo

class ConfigurationRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends ConfigurationRequestsTrait {
  def didsRequests(): ConfigurationRequestsTrait.DidsTrait = new DidsRequests()
  def voiceRequests(): ConfigurationRequestsTrait.VoiceTrait = new VoiceRequests()
  def capacityGroupRequests(): CapacityGroupTrait = new CapacityGroupRequests()

}
