package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed abstract class VoiceUriProtocolEnum extends EnumEntry

object VoiceUriProtocolEnum extends Enum[VoiceUriProtocolEnum] {
  override def values: IndexedSeq[
    VoiceUriProtocolEnum] = findValues

  case object TEL extends VoiceUriProtocolEnum
  case object SIP extends VoiceUriProtocolEnum
}




