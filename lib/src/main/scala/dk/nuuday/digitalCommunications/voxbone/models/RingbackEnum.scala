package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait RingbackEnum extends EnumEntry
object RingbackEnum extends Enum[RingbackEnum] {
  override def values: IndexedSeq[
    RingbackEnum] = findValues


  case object PROGRESS extends RingbackEnum
  case object RINGING extends RingbackEnum
  case object STANDARD extends RingbackEnum
}
