package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed abstract class DidTypeEnum extends EnumEntry
object DidTypeEnum extends Enum[DidTypeEnum] {
  override def values: IndexedSeq[DidTypeEnum] = findValues
  case object GEOGRAPHIC extends DidTypeEnum
  case object TOLL_FREE extends DidTypeEnum
  case object NATIONAL extends DidTypeEnum
  case object MOBILE extends DidTypeEnum
  case object INUM extends DidTypeEnum
  case object SHARED_COST extends DidTypeEnum
  case object SPECIAL extends DidTypeEnum
}
