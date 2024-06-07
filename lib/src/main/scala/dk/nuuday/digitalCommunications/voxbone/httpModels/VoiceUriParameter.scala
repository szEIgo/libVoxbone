package dk.nuuday.digitalCommunications.voxbone.httpModels

import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.VoxboneQueryParameter


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


