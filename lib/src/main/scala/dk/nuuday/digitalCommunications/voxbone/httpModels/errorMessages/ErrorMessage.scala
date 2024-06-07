package dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages

sealed trait Error

case class ApiErrorCode(override val toString: String)
case class ApiErrorMessage(override val toString: String)
case class ApiError(apiErrorCode: ApiErrorCode, apiErrorMessage: ApiErrorMessage) extends Error

case class ErrorMessage(httpStatusCode: Int, apiModule: String, genericErrorMessage: String, errors: java.util.List[ApiError])
