package dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages
import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class OrderingErrorCodes(val value: Int, description: String) extends IntEnumEntry {
  def description(): String = description
}

object OrderingErrorCodes extends IntEnum[OrderingErrorCodes] {
  override def values: IndexedSeq[OrderingErrorCodes] =
    findValues

  case object INVALID_CART_IDENTIFIER
      extends OrderingErrorCodes(
        400,
        "	The cart identifier is not valid. Please use the ‘Create cart’ or ‘List cart’ operation to retrieve a valid cart id.")
  case object NO_PRODUCT_DETECTED
      extends OrderingErrorCodes(
        401,
        "	The system could not find any product to add to your cart. Please specify the did, capacity or credit package item you would like to add to your cart."
      )
  case object MULTIPLE_PRODUCT_DETECTED
      extends OrderingErrorCodes(
        402,
        "	The system found more than one product to add to your cart. Please specify only one did, capacity or credit package item you would like to add to your cart."
      )
  case object INVALID_DIDGROUP_ID
      extends OrderingErrorCodes(
        403,
        "	The system could not find the requested didgroup. Please use the ‘List didgroup’ operation to retrieve a valid didgroup id.")
  case object UNAVAILABLE_DIDGROUP
      extends OrderingErrorCodes(
        404,
        "	This didgroup is not available for ordering. Please use the ‘List didgroup’ operation to retrieve a valid didgroup id.")
  case object INVALID_QUANTITY
      extends OrderingErrorCodes(
        405,
        "	The given quantity is not valid. Please use a strictly positive integer.")
  case object INVALID_CAPACITY_QUANTITY
      extends OrderingErrorCodes(
        406,
        "	The given quantity is not valid. Please use a strictly positive multiple of 10.")
  case object INVALID_CREDIT_PACKAGE_ID
      extends OrderingErrorCodes(
        407,
        "	The system could not find the requested credit package. Please use the ‘List credit packages’ operation to retrieve a valid credit package id.")
  case object PRODUCT_CONVERSION_ERROR
      extends OrderingErrorCodes(
        408,
        "	The system could not convert your request into products to order, no order has been placed. Please try again and contact your account manager if the problem persists."
      )
  case object PRODUCT_VALIDATION_ERROR
      extends OrderingErrorCodes(
        409,
        "	The system could not validate your order, no order has been placed. Please try again and contact your account manager if the problem persists.")
  case object INVALID_PRODUCT_ID
      extends OrderingErrorCodes(
        410,
        "	The system could not find the requested product in your cart. Please use the ‘List carts’ operation to retrieve a valid product id.")
  case object INVALID_REMOVE_QUANTITY
      extends OrderingErrorCodes(
        411,
        "	The given quantity is not valid. Please use a strictly positive integer.")
  case object INVALID_REMOVE_CAPACITY_QUANTITY
      extends OrderingErrorCodes(
        412,
        "	The given quantity is not valid. Please use a strictly positive multiple of 10.")
  case object INVALID_REMOVE_QUANTITY_TOO_BIG
      extends OrderingErrorCodes(
        413,
        "	The given quantity is bigger than the quantity currently in your cart.")
  case object INVALID_DID_ID
      extends OrderingErrorCodes(
        414,
        "	The requested did does not exist or is not owned by the user.")
  case object UNEXPECTED_CANCEL_ERROR
      extends OrderingErrorCodes(
        415,
        "	An unexpected error occurred during the execution of the cancellation. Please contact the voxbone support team.")
  case object CUSTOMER_REFERENCE_TOO_LONG
      extends OrderingErrorCodes(
        416,
        "	The customer reference is longer than the                             255 characters limit.")
  case object INVALID_DATE_FORMAT extends OrderingErrorCodes(417, "	The date format is not valid")
  case object TEST_DID_NOT_CANCELLABLE
      extends OrderingErrorCodes(
        418,
        "	Dids that have been assigned to you in test cannot be cancelled using this operation. Please contact your account manager to cancel test dids.")

}
