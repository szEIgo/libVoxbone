package dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages
import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class ServiceActivationErrorCodes(val value: Int, description: String)
    extends IntEnumEntry {
  def description(): String = {
    description
  }
}

object ServiceActivationErrorCodes extends IntEnum[ServiceActivationErrorCodes] {
  override def values: IndexedSeq[ServiceActivationErrorCodes] =
    findValues

  case object FIELD_MISSING extends ServiceActivationErrorCodes(500, "	Missing field.")
  case object FIELD_SIZE extends ServiceActivationErrorCodes(501, "	Maximum field size exceeded.")
  case object EXTRA_FIELD_UNEXISTENT
      extends ServiceActivationErrorCodes(
        502,
        "	The address extra field does not exist for the country.")
  case object FIELD_INVALID
      extends ServiceActivationErrorCodes(503, "	The field value is not correct.")
  case object EXTRA_FIELD_INVALID
      extends ServiceActivationErrorCodes(504, "	The extra field value is not correct.")
  case object TEMPLATE_COUNTRY_INVALID
      extends ServiceActivationErrorCodes(
        505,
        "	The country of the address should be the same as the destination country for the selected didType.")
  case object EXTRA_FIELDS_NOT_ACCEPTED
      extends ServiceActivationErrorCodes(
        506,
        "	Extra fields are not accepted for the destination country and did_type.")
  case object INVALID_REGULATION_ADDRESS
      extends ServiceActivationErrorCodes(
        507,
        "	The regulation address id is not valid, either it does not exist or the regulation address does not belong to you. Invalid regulation address:")
  case object LINKED_REGULATION_ADDRESS
      extends ServiceActivationErrorCodes(
        508,
        "	Error during deletion, the regulation address is linked to one or more DID. Invalid regulation address:")
  case object UPDATE_NOT_ALLOWED extends ServiceActivationErrorCodes(509, "	Update not allowed.")
  case object NONEXISTENT_ADDRESS extends ServiceActivationErrorCodes(510, "	Invalid address id.")
  case object NO_REGULATION_REQUIREMENTS
      extends ServiceActivationErrorCodes(
        511,
        "	This didGroup has no regulation address requirements.")
  case object VERIFICATION_ALREADY_REQUESTED
      extends ServiceActivationErrorCodes(
        512,
        "his regulation address must first be verified. Its verification has already been requested, therefore it will be possible to link dids as soon as it is confirmed."
      )
  case object MULTIPLE_DIDGROUPS
      extends ServiceActivationErrorCodes(513, "	All the DID must belong to the same didgroup.")
  case object DID_ALREADY_LINKED
      extends ServiceActivationErrorCodes(
        514,
        "	The DID is already linked. Please unlink it before trying to link it to a new address. DID id :")
  case object REJECTED_REGULATION_ADDRESS
      extends ServiceActivationErrorCodes(515, "	The proof of address has been rejected.")
  case object INVALID_DID_LIST extends ServiceActivationErrorCodes(516, "	Invalid DID id.")
  case object VERIFICATION_NOT_YET_REQUESTED
      extends ServiceActivationErrorCodes(
        517,
        "o request for verification was sent for this regulation address. It must first be verified before it is possible to link dids.")
  case object REQUESTED_VERIFICATION_TWICE
      extends ServiceActivationErrorCodes(
        518,
        "	The verification for this regulation address has already been requested.")
  case object NOT_ALLOWED extends ServiceActivationErrorCodes(519, "	This behaviour is not allowed")
  case object ADDRESS_VERIFICATION_NOT_REQUIRED
      extends ServiceActivationErrorCodes(
        520,
        "he following DIDs do not require address verification.")
  case object PHONE_NUMBER_WRONG_FORMAT
      extends ServiceActivationErrorCodes(
        521,
        "	The phone number format should start with a + followed by 9 to 15 digits.")
  case object TEMPLATE_SAME_COUNTRY_INVALID
      extends ServiceActivationErrorCodes(
        522,
        "	The country of the address should be different than the destination country for the selected didType.")
  case object STATUS_CHANGE_NOT_ALLOWED
      extends ServiceActivationErrorCodes(
        523,
        "	The status is not allowed to be changed to: STATUS.")
  case object NON_EXISTENT_EXTRA_FIELDS
      extends ServiceActivationErrorCodes(524, "	The country’s extra fields haven’t been provided.")
  case object PERSON_IS_A_MINOR
      extends ServiceActivationErrorCodes(
        525,
        "	The entity must be 18 years old in order to sign a document.")

}
