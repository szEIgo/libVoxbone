package dk.nuuday.digitalCommunications.voxbone.jsonModel
import java.util
import java.util.Optional

import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._

import scala.jdk.OptionConverters._
import scala.util.Try
trait VoiceUrisJsonProtocol extends SharedJsonProtocol {

  /*
  case class BackupUriId(value: Optional[String])
case class BackupUri(value: Optional[String])
case class VoiceUriProtocol(value: String)
case class Uri(value: String)
case class Description(value: String)
case class VoiceUri(voiceUriId: VoiceUriId, backupUriId: BackupUriId)
case class VoiceUris (voiceUris: Vector[VoiceUri], resultCount: Int)
   */

  implicit object BackupUriIdFormat extends RootJsonFormat[BackupUriId] {
    override def read(json: JsValue): BackupUriId = json match {
      case JsNumber(rawString) => BackupUriId(Some(rawString.toLongExact).toJava)
      case JsNull => BackupUriId(None.toJava)
      case other =>
        throw new DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: BackupUriId): JsValue =
      if (!obj.value.isPresent) JsNull else obj.value.get().toJson
  }

  implicit object BackupUriFormat extends RootJsonFormat[BackupUri] {
    override def read(json: JsValue): BackupUri = json match {
      case JsString(rawString) => BackupUri(Some(rawString).toJava)
      case JsNull => BackupUri(None.toJava)
      case other =>
        throw new DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: BackupUri): JsValue =
      if (!obj.value.isPresent) JsNull else obj.value.get().toJson
  }

  implicit object UriFormat extends RootJsonFormat[Uri] {
    override def write(obj: Uri): JsValue = obj.value.toJson

    override def read(json: JsValue): Uri = json match {
      case JsString(raw) => Uri(raw)
      case other =>
        throw new DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

//  implicit object DescriptionFormat extends RootJsonFormat[Description] {
//    override def write(obj: Description): JsValue = obj.value.toJson
//
//    override def read(json: JsValue): Description = json match {
//      case JsString(raw) => Description(raw)
//      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
//    }
//  }
//
//  implicit object voiceUriIdFormat extends RootJsonFormat[VoiceUriId] {
//    override def write(obj: VoiceUriId): JsValue = obj.value.toJson
//    override def read(json: JsValue): VoiceUriId = json match {
//      case JsNumber(raw) => VoiceUriId(raw.toLongExact)
//      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
//    }
//  }

  implicit object OptionalVoiceUriProtocolFormat
      extends RootJsonFormat[Optional[VoiceUriProtocol]] {
    override def write(obj: Optional[VoiceUriProtocol]): JsValue =
      obj.toScala.map(_.value.entryName.toJson).getOrElse(JsNull)

    override def read(json: JsValue): Optional[VoiceUriProtocol] = json match {
      case JsString(raw) => Some(VoiceUriProtocol(VoiceUriProtocolEnum.withName(raw))).toJava
      case other =>
        throw new DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object VoiceUriProtocolFormat extends RootJsonFormat[VoiceUriProtocol] {
    override def write(obj: VoiceUriProtocol): JsValue = obj.value.entryName.toJson

    override def read(json: JsValue): VoiceUriProtocol = json match {
      case JsString(raw) => VoiceUriProtocol(VoiceUriProtocolEnum.withName(raw))
      case other =>
        throw new DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object VoiceUriFormat extends RootJsonFormat[VoiceUri] {
    override def write(obj: VoiceUri): JsValue =
      JsObject(
        "voiceUriId" -> obj.voiceUriId.value.toJson,
        "backupUriId" -> obj.backupUriId.toJson,
        "backupUri" -> obj.backupUri.toJson,
        "voiceUriProtocol" -> obj.voiceUriProtocol.toScala
          .map(_.value.entryName.toJson)
          .getOrElse(JsNull),
        "uri" -> obj.uri.value.toJson,
        "description" -> obj.description.value.toJson
      )
    override def read(json: JsValue): VoiceUri = json match {
      case JsObject(outerMap) if json.asJsObject.fields.contains("voiceUri") =>
        val map = outerMap("voiceUri").asJsObject.fields
        VoiceUri(
          VoiceUriId(map("voiceUriId").convertTo[Long]),
          map("backupUriId").convertTo[BackupUriId],
          map("backupUri").convertTo[BackupUri],
          Some(map("voiceUriProtocol").convertTo[VoiceUriProtocol]).toJava,
          map("uri").convertTo[Uri],
          map("description").convertTo[Description]
        )
      case JsObject(map) =>
        VoiceUri(
          VoiceUriId(map("voiceUriId").convertTo[Long]),
          map("backupUriId").convertTo[BackupUriId],
          map("backupUri").convertTo[BackupUri],
          Some(map("voiceUriProtocol").convertTo[VoiceUriProtocol]).toJava,
          map("uri").convertTo[Uri],
          map("description").convertTo[Description]
        )
      case other =>
        throw DeserializationException(s"Could not find JsType ${Thread.currentThread
          .getStackTrace()(2)
          .getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit val voiceUrisFormat = jsonFormat2(VoiceUris)

  implicit val voiceUriRefFormat = jsonFormat1(VoiceUriRef)

  implicit val propertiesVoiceUriFormat = jsonFormat1(PropertiesVoiceUri)

  implicit val propertiesFormat = jsonFormat1(Properties)

  implicit object listOfVoiceUriRefFormat extends RootJsonFormat[java.util.List[VoiceUriRef]] {
    import scala.jdk.CollectionConverters._
    override def write(obj: util.List[VoiceUriRef]): JsValue = obj.asScala.toList.toJson
    override def read(json: JsValue): util.List[VoiceUriRef] = json match {
      case JsArray(vector) => vector.map(s => s.convertTo[VoiceUriRef]).asJava
      case _ => throw DeserializationException(s"failed to parse $json")
    }
  }

  implicit object listStringFormat extends RootJsonFormat[java.util.List[String]] {
    import scala.jdk.CollectionConverters._
    override def write(obj: util.List[String]): JsValue = obj.asScala.toList.toJson
    override def read(json: JsValue): util.List[String] = json match {
      case JsArray(vector) => vector.map(s => s.convertTo[String]).asJava
      case _ => throw DeserializationException(s"failed to parse $json")
    }
  }

  implicit val saveVoiceUriRequestFormat = jsonFormat3(SaveVoiceUriRequest)

  implicit val saveVoiceUriPropertiesVoiceUriIdFormat = jsonFormat3(
    SaveVoiceUriPropertiesVoiceUriId)
  implicit val saveVoiceUriPropertiesBackupUriIdFormat = jsonFormat2(
    SaveVoiceUriPropertiesBackupUriId)
  implicit val saveVoiceUriPropertiesVoiceUriProtocolFormat = jsonFormat3(
    SaveVoiceUriPropertiesVoiceUriProtocol)
  implicit val saveVoiceUriPropertiesUriFormat = jsonFormat2(SaveVoiceUriPropertiesUri)
  implicit val saveVoiceUriPropertiesDescriptionFormat = jsonFormat2(
    SaveVoiceUriPropertiesDescription)
//  implicit val saveVoiceUriPropertiesRequestFormat = jsonFormat3(SaveVoiceUriRequest)

  implicit val saveVoiceUriPropertiesFormat = jsonFormat5(SaveVoiceUriProperties)

  implicit val SaveVoiceUriFormat = jsonFormat3(SaveVoiceUri)

  implicit val definitionsVoiceFormat = jsonFormat2(Definitions)

  implicit object NewVoiceUriFormat extends RootJsonFormat[NewVoiceUri] {
    override def read(json: JsValue): NewVoiceUri = ???
    override def write(obj: NewVoiceUri): JsValue =
      JsObject(
        "voiceUri" -> JsObject(
          "backupUriId" -> obj.backupUriId.value.toScala.map(_.toJson).getOrElse(JsNull),
          "description" -> obj.description.value.toJson,
          "uri" -> obj.uri.value.toJson,
          "voiceUriId" -> JsNull,
          "voiceUriProtocol" -> obj.voiceUriProtocol.toScala
            .map(_.value.entryName.toJson)
            .getOrElse(JsNull)
        ))
  }

  implicit object allOfRefFormat extends RootJsonFormat[AllOfRef] {
    override def write(obj: AllOfRef): JsValue = JsObject("$ref" -> obj.`$ref`.toJson)
    override def read(json: JsValue): AllOfRef = json match {
      case JsObject(jsmap) =>
        jsmap("$ref") match {
          case JsString(raw) => AllOfRef(raw)
          case _ => throw DeserializationException(s"Failed to deserialize $json")
        }
      case _ => throw DeserializationException(s"Failed to deserialize $json")
    }
  }

  implicit object listOfAllOfRefFormat extends RootJsonFormat[java.util.List[AllOfRef]] {
    import scala.jdk.CollectionConverters._
    override def write(obj: util.List[AllOfRef]): JsValue = obj.asScala.toList.toJson
    override def read(json: JsValue): util.List[AllOfRef] = json match {
      case JsArray(vector) => vector.map(s => s.convertTo[AllOfRef]).asJava
      case _ => throw DeserializationException(s"failed to parse $json")
    }
  }
  implicit object backupUriIdFormast extends RootJsonFormat[BackupUriIdFromResponse] {
    override def read(json: JsValue): BackupUriIdFromResponse = json match {
      case JsString(s) => BackupUriIdFromResponse(Optional.of(s))
      case JsNull => BackupUriIdFromResponse(Optional.empty)
      case other => throw DeserializationException(s"failed to map $other")
    }
    override def write(obj: BackupUriIdFromResponse): JsValue = obj.backupUri.toScala match {
      case Some(str) => JsString(str)
      case None => JsNull
    }
  }

  implicit object voiceUriResponseFormat extends RootJsonFormat[VoiceUriResponse] {
    override def read(json: JsValue): VoiceUriResponse = json match {
      case JsObject(fields) =>
        VoiceUriResponse(
          VoiceUriId(fields("voiceUriId").convertTo[Long]),
          fields("backupUriId").convertTo[BackupUriIdFromResponse],
          fields("voiceUriProtocol").convertTo[VoiceUriProtocol],
          fields("uri").convertTo[Uri],
          fields("description").convertTo[Description]
        )
      case other => throw DeserializationException(s"Failed to serialize $other")
    }
    override def write(obj: VoiceUriResponse): JsValue =
      JsObject(
        "voiceUriId" -> obj.voiceUriId.value.toJson,
        "backupUriId" -> obj.backupUriId.toJson,
        "voiceUriProtocol" -> obj.voiceUriProtocol.toJson,
        "uri" -> obj.uri.toJson,
        "description" -> obj.description.toJson
      )
  }

  implicit val voiceUriResponseDtoFormat = jsonFormat1(VoiceUriResponseDto)
  implicit val changeVoiceResponseFormat = jsonFormat2(ChangeVoiceResponse)
  /*
 "voiceUriId": 25744,
            "backupUriId": null,
            "backupUri": null,
            "voiceUriProtocol": "SIP",
            "uri": "44540040@77.234.175.202",
            "description": "Test 3 klang ingen modtagelse"
 */

}
