package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait CallTypeEnum extends EnumEntry
object CallTypeEnum extends Enum[CallTypeEnum] {
  override def values: IndexedSeq[CallTypeEnum] = findValues
  case object VOXDID extends CallTypeEnum
  case object VOXPREMIUM extends CallTypeEnum
  case object VOX800 extends CallTypeEnum
}
