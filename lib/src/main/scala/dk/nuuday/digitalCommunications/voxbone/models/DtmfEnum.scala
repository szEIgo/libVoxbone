package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait DtmfEnum extends EnumEntry
object DtmfEnum extends Enum[DtmfEnum] {
  override def values: IndexedSeq[
    DtmfEnum] = findValues


  case object INBAND extends DtmfEnum
  case object INFO extends DtmfEnum
  case object RFC2833 extends DtmfEnum
  case object RFC2833_INFO extends DtmfEnum
  case object RFC2833_INBAND extends DtmfEnum

}