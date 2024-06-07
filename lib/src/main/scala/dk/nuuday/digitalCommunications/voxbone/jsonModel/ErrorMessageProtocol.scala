package dk.nuuday.digitalCommunications.voxbone.jsonModel
import java.util

import dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages.{
  ApiErrorCode,
  ApiErrorMessage,
  ErrorMessage,
  ApiError,
}
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.jdk.CollectionConverters._

trait ErrorMessageProtocol extends DefaultJsonProtocol {

  implicit object apiErrorTraitFormat extends RootJsonFormat[ApiError] {
    override def read(json: JsValue): ApiError = json match {
      case jsValue @ JsObject(fields) =>
        ApiError(
          ApiErrorCode(fields("apiErrorCode").convertTo[String]),
          ApiErrorMessage(fields("apiErrorMessage").convertTo[String]))
      case other =>
        throw deserializationError(s"This Parser does not know this kind of ApiError $other")
    }
    override def write(obj: ApiError): JsValue = obj match {
      case err @ ApiError(code, msg) =>
        JsObject("apiErrorCode" -> code.toString.toJson, "apiErrorMessage" -> msg.toString.toJson)
      case other =>
        throw DeserializationException(
          s"Not a type of ApiErrorCode, therefore Unknown ErrorCode that needs to be implemented; $other")
    }
  }

  implicit object apiErrorFormat extends RootJsonFormat[java.util.List[ApiError]] {
    override def read(json: JsValue): util.List[ApiError] = json match {
      case JsArray(elements) => elements.map(_.convertTo[ApiError]).asJava
      case _ => throw DeserializationException("Not a valid JsArray")
    }
    override def write(obj: util.List[ApiError]): JsValue =
      obj.asScala.toVector.map(_.toJson).toJson
  }

  implicit val errorMessageFormat = jsonFormat4(ErrorMessage)

}
