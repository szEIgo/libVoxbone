package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait AddressTypeEnum extends EnumEntry

object AddressTypeEnum extends Enum[AddressTypeEnum] {
  override def values: IndexedSeq[
    AddressTypeEnum] = findValues
  case object WORLDWIDE extends AddressTypeEnum
  case object NATIONAL extends AddressTypeEnum
  case object LOCAL extends AddressTypeEnum
}
