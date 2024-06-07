package dk.nuuday.digitalCommunications.voxbone.models
import java.util.Optional

import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

case class BackupUriId(value: Optional[Long])
case class BackupUri(value: Optional[String])
case class BackupUriIdFromResponse(backupUri: Optional[String])
case class VoiceUriProtocol(value: VoiceUriProtocolEnum)

case class Uri(value: String)

/*
    "voiceUri": {
        "uri": "<string>",
        "voiceUriProtocol": "<string>",
        "voiceUriId": "<integer>",
        "backupUriId": "<string>",
        "description": "<string>"
    }
 */

case class VoiceUriResponseDto(voiceUri: VoiceUriResponse)
case class VoiceUriResponse(voiceUriId: VoiceUriId, backupUriId: BackupUriIdFromResponse,voiceUriProtocol: VoiceUriProtocol, uri: Uri, description: Description)

case class NewVoiceUri(
    uri: Uri,
    voiceUriProtocol: Optional[VoiceUriProtocol] = None.toJava,
    backupUriId: BackupUriId,
    description: Description)

case class VoiceUri(
    voiceUriId: VoiceUriId,
    backupUriId: BackupUriId,
    backupUri: BackupUri,
    voiceUriProtocol: Optional[VoiceUriProtocol] = None.toJava,
    uri: Uri,
    description: Description)

case class VoiceUris(voiceUris: Vector[VoiceUri], resultCount: Int) {
  def toJava(): VoiceUrisJava = VoiceUrisJava(voiceUris.asJava, resultCount)
}

case class VoiceUrisJava(voiceUris: java.util.List[VoiceUri], resultCount: Int)

case class VoiceUriRef(`$ref`: String)

case class PropertiesVoiceUri(`$ref`: String)

case class Properties(voiceUri: PropertiesVoiceUri)

case class SaveVoiceUriPropertiesVoiceUriId(
    `type`: String,
    format: String,
    description: Description)
case class SaveVoiceUriPropertiesBackupUriId(`type`: String, description: String)
case class SaveVoiceUriPropertiesVoiceUriProtocol(
    `type`: String,
    description: String,
    enum: Seq[String])
case class SaveVoiceUriPropertiesUri(`type`: String, description: String)
case class SaveVoiceUriPropertiesDescription(`type`: String, description: String)
case class SaveVoiceUriProperties(
    voiceUriId: SaveVoiceUriPropertiesVoiceUriId,
    backupUriId: SaveVoiceUriPropertiesBackupUriId,
    voiceUriProtocol: SaveVoiceUriPropertiesVoiceUriProtocol,
    uri: SaveVoiceUriPropertiesUri,
    description: SaveVoiceUriPropertiesDescription)
case class SaveVoiceUriRequest(
    `type`: String,
    required: java.util.List[String],
    properties: Properties)
case class SaveVoiceUri(
    `type`: String,
    required: Vector[String],
    properties: SaveVoiceUriProperties)
case class Definitions(SaveVoiceUriRequest: SaveVoiceUriRequest, SaveVoiceUri: SaveVoiceUri)
case class ChangeVoiceUriResponse(allOf: java.util.List[VoiceUriRef], definitions: Definitions)

case class AllOfRef(`$ref`: String)
case class ChangeVoiceResponse(allOf: java.util.List[AllOfRef], definitions: Definitions)
