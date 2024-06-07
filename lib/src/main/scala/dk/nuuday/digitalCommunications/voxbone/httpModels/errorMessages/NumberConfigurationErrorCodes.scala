package dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages
import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class NumberConfigurationErrorCodes(val value: Int, description: String)
    extends IntEnumEntry {
  def description(): String = description
}

object NumberConfigurationErrorCodes extends IntEnum[NumberConfigurationErrorCodes] {
  override def values: IndexedSeq[NumberConfigurationErrorCodes] =
    findValues

  case object INVALID_DID
      extends NumberConfigurationErrorCodes(
        300,
        "The did id is not valid, either it does not exist or the did does not belong to you.")
  case object INVALID_VOICE_URI
      extends NumberConfigurationErrorCodes(
        301,
        "The voice uri id is not valid, either it does not exist or the voice uri does not belong to you.")
  case object INVALID_SMS_URI
      extends NumberConfigurationErrorCodes(
        302,
        "The sms uri id is not valid, either it does not exist or the sms uri does not belong to you.")
  case object INVALID_FAX_URI
      extends NumberConfigurationErrorCodes(
        303,
        "The fax uri id is not valid, either it does not exist or the fax uri does not belong to you.")
  case object SMS_NOT_SUPPORTED
      extends NumberConfigurationErrorCodes(304, "Some dids do not support the VoxSMS feature.")
  case object FAX_NOT_SUPPORTED
      extends NumberConfigurationErrorCodes(305, "Some dids do not support the VoxFAX feature.")
  case object INVALID_CAPACITY_GROUP
      extends NumberConfigurationErrorCodes(
        306,
        "The capacity group id is not valid, either it does not exist or the capacity group does not belong to you.")
  case object INVALID_TRUNK
      extends NumberConfigurationErrorCodes(
        307,
        "The trunk id is not valid, either it does not exist or the trunk does not belong to you.")
  case object TRUNK_NOT_ALLOWED
      extends NumberConfigurationErrorCodes(308, "Some dids can not be mapped to the given trunk.")
  case object INVALID_SIP_DELIVERY
      extends NumberConfigurationErrorCodes(
        309,
        "The pop id is not valid, the referenced pop does not exist.")
  case object INVALID_COUNTRY
      extends NumberConfigurationErrorCodes(
        310,
        "The country code is not valid, the referenced country does not exist.")
  case object INVALID_CLI_PREFIX
      extends NumberConfigurationErrorCodes(
        311,
        "The cli prefix is not valid, please use a maximum of 7 characters from the following list : 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, *, ~, #, +, -.")
  case object INVALID_CPC
      extends NumberConfigurationErrorCodes(
        312,
        "The calls blocking configuration is not valid, please either specify all three options (ordinary, cellular, payphone) or none of them.")
  case object CPC_NOT_SUPPORTED
      extends NumberConfigurationErrorCodes(
        313,
        "Some dids do not support the call blocking feature.")
  case object T38_NOT_SUPPORTED
      extends NumberConfigurationErrorCodes(314, "Some dids do not support the t38 feature.")
  case object PROPAGATION_EXCEPTION
      extends NumberConfigurationErrorCodes(
        315,
        "The server encountered an error while applying the configuration options. Some of the configuration might have been applied successfully. You can safely execute the configuration operation again, if the problem persists please contact the voxbone support team."
      )
  case object NO_VALID_DID_FOUND
      extends NumberConfigurationErrorCodes(
        316,
        "The system could not find any valid did id in the request.")
  case object CLI_PRIVACY_ACCOUNT_NOT_TRUSTED
      extends NumberConfigurationErrorCodes(
        317,
        "Your account is not authorised to use this option.")
  case object CLI_PRIVACY_NOT_SUPPORTED
      extends NumberConfigurationErrorCodes(318, "Some dids do not support the cli privacy option.")
  case object EMPTY_OTHER_OPTIONS
      extends NumberConfigurationErrorCodes(
        319,
        "Please specify at least one valid option in the ‘otherOptions’ element.")
  case object CAPACITY_GROUP_WCS
      extends NumberConfigurationErrorCodes(
        320,
        "The system could not save the capacity group. You can safely execute the operation again, if the problem persists please contact the voxbone support team."
      )
  case object INVALID_HTTP_FAX_URI
      extends NumberConfigurationErrorCodes(
        321,
        "The given uri is not valid for the delivery method HTTP_POST, it has to start with ‘http’.")
  case object INVALID_SMS_URI_FORMAT
      extends NumberConfigurationErrorCodes(
        322,
        "The system could not determine which protocol to use for the given uri. Please specify a url that starts with one of the allowed values.")
  case object INVALID_SMS_URI_LOGIN
      extends NumberConfigurationErrorCodes(323, "The specified login already exists.")
  case object INVALID_BACKUP_VOICE_URI
      extends NumberConfigurationErrorCodes(
        324,
        "The backup voice uri id is not valid, either it does not exist or the backup voice uri does not belong to you.")
  case object VOICE_URI_BACKUP_CONFLICT
      extends NumberConfigurationErrorCodes(
        325,
        "A voice uri cannot be set as its own backup. Please use a different backup uri.")
  case object VOICE_URI_DUPLICATE
      extends NumberConfigurationErrorCodes(
        326,
        "There is an existing voice uri in your account with the same protocol and uri.")
  case object VOICE_URI_CONFIGURATION
      extends NumberConfigurationErrorCodes(
        327,
        "The voice uri was successfully updated, however the dids mapped to this uri might not have been updated. You can safely execute this operation again to update the dids, if the problem persists please contact the voxbone support team."
      )
  case object VOICE_URI_MAPPED
      extends NumberConfigurationErrorCodes(
        328,
        "The voice uri cannot be removed, it is mapped to at least one number in your account.")
  case object VOICE_URI_DEFAULT
      extends NumberConfigurationErrorCodes(
        329,
        "The voice uri cannot be removed, it is set as the default uri for any new number you purchase.")
  case object VOICE_URI_BACKUP
      extends NumberConfigurationErrorCodes(
        330,
        "The voice uri cannot be removed, it is set as the backup uri for another voice uri in your account.")
  case object VOICE_URI_DELETE_UNEXPECTED
      extends NumberConfigurationErrorCodes(
        331,
        "The server encountered an error while removing the voice uri. You can safely execute the configuration operation again, if the problem persists please contact the voxbone support team."
      )
  case object INVALID_VOICE_URI_FORMAT
      extends NumberConfigurationErrorCodes(332, "The voice uri format is not valid.")
  case object SMS_OUTBOUND_NOT_ENABLED
      extends NumberConfigurationErrorCodes(333, "Some dids cannot be flagged as SMS Outbound.")
  case object SMS_LINK_GROUP_NAME_ALREADY_EXISTS
      extends NumberConfigurationErrorCodes(334, "The name for the smsLinkGroup already exists")
  case object SMS_LINK_GROUP_STILL_REFERENCED
      extends NumberConfigurationErrorCodes(
        335,
        "The smsLinkGroup is still referenced by smsLinks. Please delete these first.")
  case object SMS_LINK_DELETE_ERROR
      extends NumberConfigurationErrorCodes(336, "The SmsLink cannot be deleted.")
  case object WCS_EXCEPTION
      extends NumberConfigurationErrorCodes(337, "An error occurred saving the smsLink.")
  case object INVALID_SMS_LINK_GROUP
      extends NumberConfigurationErrorCodes(
        338,
        "The sms link group id is not valid, either it does not exist or it does not belong to you.")
  case object SMS_LINK_GROUP_DELETE_UNEXPECTED
      extends NumberConfigurationErrorCodes(
        339,
        "An unexpected error occurred when deleting the sms link group")
  case object SMS_LINK_GROUP_VALIDATION_UNEXPECTED_ERROR
      extends NumberConfigurationErrorCodes(
        341,
        "An unexpected error occurred during the validation of the SMS Link Group")
  case object SMS_LINK_FORBIDDEN_DIRECTION
      extends NumberConfigurationErrorCodes(
        342,
        "The specified sms link direction is not authorized for your account.")
  case object DUPLICATES_IN_DIDS_ID
      extends NumberConfigurationErrorCodes(343, "The list of did ids contains duplicates.")
  case object SMS_LINK_IMPOSSIBLE_DIRECTION
      extends NumberConfigurationErrorCodes(
        344,
        "The specified sms link direction is not available for the specified protocol.")
  case object SMS_ACCOUNT_PROBLEM
      extends NumberConfigurationErrorCodes(
        345,
        "There is a problem with the SMS link group, it’s not possible to delete it.")
  case object SMS_LINK_GROUP_DEFAULT
      extends NumberConfigurationErrorCodes(
        346,
        "The smsLinkGroup is the default for the current customer, it cannot be deleted.")
  case object WEBRTC_UNAUTHORIZED_ACCOUNT
      extends NumberConfigurationErrorCodes(
        347,
        "The WebRTC configuration could not be applied because the account is not WebRTC enabled.")
  case object WEBRTC_EXCEPTION
      extends NumberConfigurationErrorCodes(
        348,
        "There was a problem applying the WebRTC configuration option to the DID(s).")
  case object MISSING_VOICE_CODEC
      extends NumberConfigurationErrorCodes(349, "At least 1 voice codec should be selected.")
  case object INVALID_SMTP_FAX_URI
      extends NumberConfigurationErrorCodes(
        350,
        "The given uri is not valid for the delivery method SMTP.")
  case object OBSOLETE_CODEC
      extends NumberConfigurationErrorCodes(351, "Codec is obsolete and is not supported anymore.")
  case object SMS_LINK_GROUP_WITH_NO_LINKS
      extends NumberConfigurationErrorCodes(
        352,
        "The sms link group has no sms links. Please add at least one sms link.")
  case object FAX_URI_MAPPED
      extends NumberConfigurationErrorCodes(
        353,
        "The fax uri cannot be removed, it is mapped to at least one number in your account.")
  case object FAX_URI_DEFAULT
      extends NumberConfigurationErrorCodes(
        354,
        "The fax uri cannot be removed, it is set as the default fax uri for any new number you purchase.")
  case object CAPACITY_GROUP_MAPPED
      extends NumberConfigurationErrorCodes(
        355,
        "The capacity group cannot be removed, it is mapped to at least one number in your account.")
  case object CHANNELS_LIMIT_INVALID_VALUE
      extends NumberConfigurationErrorCodes(
        356,
        "The channels limit should be a positive integer value or empty string for no limit.")
  case object SMS_LINK_CREATE_UNEXPECTED
      extends NumberConfigurationErrorCodes(
        357,
        "The server encountered an error while creating the sms link.")
  case object CHANNELS_OUTBOUND_LIMIT_INVALID_VALUE
      extends NumberConfigurationErrorCodes(
        358,
        "The channels limit for outbound should be a positive integer value or empty string for no limit.")
  case object FAX_URI_DELETE_ERROR
      extends NumberConfigurationErrorCodes(359, "The fax uri cannot be deleted.")
  case object BULK_DID_CONFIGURATION_LIMIT_EXCEEDED
      extends NumberConfigurationErrorCodes(
        360,
        "The number of dids to be configured exceeds the limit allowed.")
  case object DID_CONFIGURATION_FOR_TRUNK_FORBIDDEN
      extends NumberConfigurationErrorCodes(
        361,
        "You are not allowed to configure trunk for did(s). Please remove trunk id field.")
  case object FAX_URI_CREATE_UNEXPECTED
      extends NumberConfigurationErrorCodes(
        362,
        "The server encountered an error while creating the fax uri.")
}
