package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait CodecsEnum extends EnumEntry
object CodecsEnum extends Enum[CodecsEnum] {
  override def values: IndexedSeq[
    CodecsEnum] = findValues
  case object G711A extends CodecsEnum
  case object G711U extends CodecsEnum
  case object G729 extends CodecsEnum
  case object G723 extends CodecsEnum
  case object G722 extends CodecsEnum
  case object SPEEX extends CodecsEnum
  case object SPEEX16 extends CodecsEnum
  case object SPEEX32 extends CodecsEnum
  case object OPUS extends CodecsEnum
  case object VP8 extends CodecsEnum
  case object H264 extends CodecsEnum
  case object H263P extends CodecsEnum
}
