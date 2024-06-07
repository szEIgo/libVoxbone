package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait CliPrivacyEnum extends EnumEntry
object CliPrivacyEnum extends Enum[CliPrivacyEnum] {
  override def values: IndexedSeq[CliPrivacyEnum] = findValues
  case object DISABLED extends CliPrivacyEnum
  case object REMOTE_PARTY_ID extends CliPrivacyEnum
  case object P_ASSERTED_IDENTITY extends CliPrivacyEnum
}
