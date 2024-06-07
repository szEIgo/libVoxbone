package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum.{Enum, EnumEntry}

sealed trait Status extends EnumEntry
object Status extends Enum[Status] {
  val values = findValues
  case object SUCCESS extends Status
  case object PARTIAL extends Status
  case object ERROR extends Status
  case object WARNING extends Status
}
