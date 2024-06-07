package dk.nuuday.digitalCommunications.voxbone.services
import akka.actor.ActorSystem
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.{
  InventoryRequests,
  OrderingRequests,
  VoxboneHttpRequests,
  VoxboneHttpRequestsTrait
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.{
  InventoryRequestsTrait,
  OrderingRequestTrait
}
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.models.ServerInfo
import dk.nuuday.digitalCommunications.voxbone.services.java.{VoxboneClient, VoxboneClientInterface}
import org.slf4j.LoggerFactory

class VoxboneClientFactory() extends VoxboneClientFactoryInterface {

  private[this] def log = LoggerFactory.getLogger(this.getClass)

  override def createVoxboneJavaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      requestTimeoutSeconds: Long,
      actorSystem: ActorSystem): VoxboneClientInterface = {

    implicit val actorSystemImplicit: ActorSystem = actorSystem
    implicit val HttpRequest: VoxboneHttpRequests =
      new VoxboneHttpRequests()(hostname, apikey, basicAuthentication)
    log.debug(s"VoxboneClient created for $hostname, timeout:$requestTimeoutSeconds ")
    new VoxboneClient(requestTimeoutSeconds)
  }
  override def createVoxboneJavaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      requestTimeoutSeconds: Long): VoxboneClientInterface = {

    implicit val actorSystemImplicit: ActorSystem = ActorSystem()
    log.debug(s"VoxboneClient created for $hostname, timeout:$requestTimeoutSeconds ")
    implicit val HttpRequest: VoxboneHttpRequests = new VoxboneHttpRequests()(hostname, apikey, basicAuthentication)
    new VoxboneClient(requestTimeoutSeconds)
  }
  override def createVoxboneScalaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      actorSystem: ActorSystem)
    : dk.nuuday.digitalCommunications.voxbone.services.scala.VoxboneClientInterface = {
    log.debug(s"VoxboneClient created for $hostname")

    implicit val implicitHostname: ServerInfo = hostname
    implicit val implicitApiKey: ApikeyAuthentication = apikey
    implicit val implicitBasicAuth: BasicAuthenticationCredentials = basicAuthentication

    new dk.nuuday.digitalCommunications.voxbone.services.scala.VoxboneClient()(
      actorSystem,
      new VoxboneHttpRequests())
  }
}
