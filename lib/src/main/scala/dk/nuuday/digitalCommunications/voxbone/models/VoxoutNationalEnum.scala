package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait VoxoutNationalEnum extends EnumEntry

object VoxoutNationalEnum extends Enum[VoxoutNationalEnum] {
  override def values: IndexedSeq[VoxoutNationalEnum] = findValues

  case object DISABLED_EMERGENCY_REQUIRED extends VoxoutNationalEnum
  case object DISABLED extends VoxoutNationalEnum
  case object ENABLED_EMERGENCY_REQUIRED extends VoxoutNationalEnum
  case object ENABLED extends VoxoutNationalEnum
  case object NOT_SUPPORTED extends VoxoutNationalEnum
}
