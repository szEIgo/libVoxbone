package dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages
import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class CommonErrorCodes(val value: Int, desciption: String) extends IntEnumEntry{
  def description(): String = desciption
}

object CommonErrorCodes extends IntEnum[CommonErrorCodes] {
  val values = findValues

  case object WRONG_DATE_FORMAT extends CommonErrorCodes(10,"The date format is not correct. Please format all dates in the ‘YYYY-MM-DD HH:MM:SS’ format")
  case object VOXWRONG_PAGE_NUMBERING800 extends CommonErrorCodes(11, "The page number must be a positive integer.")
  case object WRONG_PAGE_SIZE extends CommonErrorCodes(12,"The page size must be a positive integer with a maximum of 5000.")
  case object PAGINATION_OVERFLOW extends CommonErrorCodes(13,"Pagination overflow : the given page does not exist.")
  case object ILLEGAL_ARGUMENT extends CommonErrorCodes(14,"Illegal value.")
  case object NOT_FOUND extends CommonErrorCodes(15,"Not Found.")
  case object FIELD_REQUIRED extends CommonErrorCodes(16,"A required field is missing.")
  case object ILLEGAL_NUMBER_VALUE extends CommonErrorCodes(17,"Illegal value for a numeric field.")
  case object ILLEGAL_BOOLEAN_VALUE extends CommonErrorCodes(18,"Illegal value for a boolean field.")
  case object ILLEGAL_DATE_VALUE extends CommonErrorCodes(19,"Illegal value for a date field.")
  case object ILLEGAL_VALUE_JSON extends CommonErrorCodes(20,"The system can not process this value. Please make sure to use double quotes if the value is a string.")
  case object ILLEGAL_FIELD extends CommonErrorCodes(21,"Illegal element name. Please make sure that the given element is not already defined in your request or that it is an allowed element for this request.")
  case object MISSING_TAG_END extends CommonErrorCodes(22,"An end tag is missing in the request.")
  case object ILLEGAL_REQUEST_TYPE extends CommonErrorCodes(23,"This request type is not allowed for this URL. Please consult the API documentation for a list of URLs and allowed request types.")
  case object FILE_UPLOAD_FAILED extends CommonErrorCodes(24,"The file attached to this request could not be uploaded.")
  case object ILLEGAL_CONTENT extends CommonErrorCodes(25,"Illegal content:")
  case object EMPTY_REQUEST_BODY extends CommonErrorCodes(26,"The request body is empty. Your request is missing a body. Sandbox users: this can also occur when the Sandbox environment is down.")
  case object SERVER_DOWN extends CommonErrorCodes(500, "It seems that the server is down, and therefore not behaving properly")
}
