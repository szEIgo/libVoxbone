package dk.nuuday.digitalCommunications.voxbone.authentication

case class BasicAuthenticationCredentials(username: String, password: String)
    extends VoxboneAuthentication
