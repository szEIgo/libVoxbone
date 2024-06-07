//package dk.nuuday.digitalCommunications.voxbone.HttpModels.errorMessages
//import enumeratum.EnumEntry
//import enumeratum.values.{IntEnum, IntEnumEntry, StringEnumEntry}
//
//sealed abstract class AddressRejectionReasons(val enumEntry: String, description: String)
//    extends StringEnumEntry
//
//object AddressRejectionReasons extends StringEnumEntry[AddressRejectionReasons] {
//  override def values: IndexedSeq[AddressRejectionReasons] = findValues
//
//  case object INVALID_DOC_NOT_RECENT_ENOUGH
//      extends AddressRejectionReasons("Document", " not accepted - Expired/Not recent enough.")
//  case object INVALID_DOC_CORRUPT
//      extends AddressRejectionReasons(
//        "Document",
//        " not accepted - File cannot be opened/ corrupt file.")
//  case object INVALID_DOC_ILLEGIBLE
//      extends AddressRejectionReasons(
//        "Document",
//        " not accepted - Document illegible/blank (details unclear to read/blank document).")
//  case object INVALID_DOC_MISSING
//      extends AddressRejectionReasons(
//        "Document",
//        " not accepted - Please submit full scan/copy of entire proof.")
//  case object INVALID_DOC_WHOLE
//      extends AddressRejectionReasons(
//        "Document",
//        " not accepted - Please submit front and back of ID.")
//  case object INVALID_DOCTYPE_INVOICES
//      extends AddressRejectionReasons("Document", " type not accepted - Invoices not accepted.")
//  case object INVALID_DOCTYPE_GOVERNMENT_DOCS
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Government issued document required.")
//  case object INVALID_DOCTYPE_VIRTUAL_ADDRESS
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Virtual Office address proofs not accepted.")
//  case object INVALID_DOCTYPE_ATTESTATIONS
//      extends AddressRejectionReasons("Document", " type not accepted - Attestations not accepted.")
//  case object INVALID_DOCTYPE_THIRD_PARTY
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Document must be issued by a third party.")
//  case object INVALID_DOCTYPE_REQUIRED_ID
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Proof of ID required instead of proof of address.")
//  case object INVALID_DOCTYPE_REQUIRED_ADDRESS
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Proof of address required, instead of proof of ID.")
//  case object INVALID_DOCTYPE_FIXED_LINE_PROOF
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Recent proof of fixed line telecom services required.")
//  case object INVALID_DOCTYPE_ID_TYPE
//      extends AddressRejectionReasons("Document", " type not accepted - ID type not accepted.")
//  case object INVALID_DOCTYPE_ID_TYPE_ADDRESS
//      extends AddressRejectionReasons(
//        "Document",
//        " type not accepted - Proof of address type not accepted.")
//  case object INVALID_DOCTYPE_PASSPORT
//      extends AddressRejectionReasons("Document", " type not accepted - Passport required.")
//  case object INCOMPLETE_INFO_DATE
//      extends AddressRejectionReasons("Incomplete", " Information - Date missing.")
//  case object INCOMPLETE_INFO_ADDRESS
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Document does not provide address information.")
//  case object INCOMPLETE_INFO_PROOFS
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Both proof of address and proof of ID required.")
//  case object INCOMPLETE_INFO_BUSINESS_REG
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Business registration also required.")
//  case object INCOMPLETE_INFO_LEGAL_REP
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Document linking legal representative also required.")
//  case object INCOMPLETE_INFO_LEGAL_REP_ID
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Valid ID of legal representative also required.")
//  case object INCOMPLETE_INFO_LEGAL_ZIP
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - ZIP file with both valid ID of legal representative and business registration required.")
//  case object INCOMPLETE_INFO_PO_BOX
//      extends AddressRejectionReasons("Incomplete", " Information - PO Boxes not accepted.")
//  case object INCOMPLETE_INFO_REP_PICTURE
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Picture of representative holding ID also required.")
//  case object INCOMPLETE_INFO_CHINESE_RECEIPT
//      extends AddressRejectionReasons(
//        "Incomplete",
//        " Information - Chinese mobile store confirmation receipt also required.")
//  case object INFO_MISMATCH_ADDRESS_PROOF
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Information on address proof differs from portal information.")
//  case object INFO_MISMATCH_ID_PROOF
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Information on ID proof differs from portal information.")
//  case object INFO_MISMATCH_DETAILS
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Address information differs (company name/end user name).")
//  case object INFO_MISMATCH_ADDRESS
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Address information differs (street name, street number, etc.).")
//  case object INFO_MISMATCH_LOCATION
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Address information differs (city or zip code).")
//  case object INFO_MISMATCH_RESIDE
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Customer cannot operate/reside within country.")
//  case object INFO_MISMATCH_FATHER
//      extends AddressRejectionReasons("Information", "mismatch - Father’s name not valid.")
//  case object INFO_MISMATCH_MOTHER
//      extends AddressRejectionReasons("Information", "mismatch - Mother’s name not valid.")
//  case object INFO_MISMATCH_COMPANY_REG
//      extends AddressRejectionReasons(
//        "Information",
//        "mismatch - Company registration status: not currently valid.")
//  case object INFO_MISMATCH_ID_NUMBER
//      extends AddressRejectionReasons("Information", "mismatch - ID number not valid.")
//  case object INFO_MISMATCH_COMPANY_NUMBER
//      extends AddressRejectionReasons("Information", "mismatch - Company number not valid.")
//  case object INFO_MISMATCH_COMPANY_TAX_NUMBER
//      extends AddressRejectionReasons("Information", "mismatch - Company tax number not valid.")
//  case object INFO_MISMATCH_BIRTH_PLACE
//      extends AddressRejectionReasons("Information", "mismatch - Birth place not valid.")
//  case object INFO_MISMATCH_BIRTH_DATE
//      extends AddressRejectionReasons("Information", "mismatch - Birth date not valid.")
//  case object INFO_MISMATCH_ID
//      extends AddressRejectionReasons("Information", "mismatch - ID information not valid.")
//  case object OTHER_ABUSE
//      extends AddressRejectionReasons("Other", " (please contact abuse.telecom@bandwidth.com).")
//  case object OTHER_REGULATIONS
//      extends AddressRejectionReasons("Other", "(please contact regulations@bandwidth.com).")
//}
