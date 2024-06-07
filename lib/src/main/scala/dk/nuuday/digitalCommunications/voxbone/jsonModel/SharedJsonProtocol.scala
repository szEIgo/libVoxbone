package dk.nuuday.digitalCommunications.voxbone.jsonModel
import java.util.Optional

import dk.nuuday.digitalCommunications.voxbone.models.{CountryCodesA3Enum, Description, DidGroupId, Status, VoiceUriId}
import spray.json._

trait SharedJsonProtocol  extends DefaultJsonProtocol {

  implicit object statusFormat extends RootJsonFormat[Status] {
    override def read(json: JsValue): Status = json match {
      case JsString(raw) => Status.withName(raw)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: Status): JsValue = obj.entryName.toJson
  }
  implicit val voiceUrIidFormat = jsonFormat1(VoiceUriId)

  implicit object voiceUriIdFormat extends RootJsonFormat[Optional[VoiceUriId]] {
    import scala.jdk.OptionConverters._
    override def write(obj: Optional[VoiceUriId]): JsValue =
      obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[VoiceUriId] = json match {
      case JsNumber(rawNumber) => Some(VoiceUriId(rawNumber.toLongExact)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object didGroupIdFormat extends RootJsonFormat[DidGroupId] {
    override def write(obj: DidGroupId): JsValue = obj.value.toJson
    override def read(json: JsValue): DidGroupId = json match {
      case JsNumber(rawValue) => DidGroupId(rawValue.toLongExact)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object CountryCodeA3Format extends RootJsonFormat[CountryCodesA3Enum] {
    override def write(obj: CountryCodesA3Enum): JsValue = JsString(obj.entryName)
    override def read(json: JsValue): CountryCodesA3Enum =
      json match {
        case JsString(rawValue) => CountryCodesA3Enum.withName(rawValue)
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }

  implicit object DescriptionFormat extends RootJsonFormat[Description] {
    override def read(json: JsValue): Description = json match {
      case JsString(rawString) => Description(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: Description): JsValue = obj.value.toJson
  }

}
