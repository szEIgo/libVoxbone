package dk.nuuday.digitalCommunications.voxbone.services
import akka.actor.ActorSystem
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.models.ServerInfo
import dk.nuuday.digitalCommunications.voxbone.services.java.VoxboneClientInterface

trait VoxboneClientFactoryInterface {

  def createVoxboneJavaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      requestTimeoutSeconds: Long): VoxboneClientInterface

  def createVoxboneJavaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      requestTimeoutSeconds: Long,
      actorSystem: ActorSystem): VoxboneClientInterface

  def createVoxboneScalaClient(
      hostname: ServerInfo,
      apikey: ApikeyAuthentication,
      basicAuthentication: BasicAuthenticationCredentials,
      actorSystem: ActorSystem)
    : dk.nuuday.digitalCommunications.voxbone.services.scala.VoxboneClientInterface



}
