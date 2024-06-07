package dk.nuuday.digitalCommunications.voxbone.authentication

case class ApikeyAuthentication(key: String) extends VoxboneAuthentication {
  def get:
  String = key
  def set(key: String): ApikeyAuthentication = ApikeyAuthentication(key)
}
