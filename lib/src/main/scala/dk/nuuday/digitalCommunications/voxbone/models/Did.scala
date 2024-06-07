package dk.nuuday.digitalCommunications.voxbone.models
import java.util.{Date, Optional}


case class TestOption(str: Option[String])
case class DidId(value: Long)
case class E164(value: String)
case class NumberType(value: String)
case class CityName(value: String)
case class AreaCode(value: String)
case class VoiceUriId(value: Long)
case class FaxUriId(faxUriId: Long)
case class SmsLinkGroupId(value: Long)
case class OrderReference(value: String)
case class Channels(value: Int)
case class ChannelsOutbound(value: Int)
case class Delivery(value: String)
case class TrunkId(value: Long)
case class DidGroupId(value: Long)
case class RegulationAddressId(value: Long)
case class SrvLookup(value: Boolean)
case class CliFormat(value: Optional[String])
case class CliValue(value: Optional[String])
case class CallerId(cliFormat: CliFormat, cliValue: CliValue)
case class CliPrivacy(value: CliPrivacyEnum)
case class T38Enabled(value: String)
case class Dtmf(value: Optional[DtmfEnum])
case class DtmfInbandMute(value: String)
case class Codecs(set: java.util.Set[CodecsEnum])
case class OtherOptions(
    t38Enabled: T38Enabled,
    dtmf: Dtmf,
    dtmfInbandMute: DtmfInbandMute,
    codecs: Codecs)
case class Ringback(value: RingbackEnum)
case class DnisEnabled(value: Boolean)
case class BlockOrdinary(value: Boolean)
case class BlockCellular(value: Boolean)
case class BlockPayphone(value: Boolean)
case class SmsOutbound(value: Boolean)
case class WebRtc(value: Boolean)
case class VoxoutNational(value: VoxoutNationalEnum)
case class VoxoutInternationalEnabled(value: Boolean)
case class CancellationAvailable(value: Date)

case class Did(
    didId: DidId,
    e164: E164,
    numberType: NumberType,
    countryCodeA3: CountryCodesA3Enum,
    cityName: Optional[CityName],
    areaCode: AreaCode,
    voiceUriId: Optional[VoiceUriId],
    faxUriId: Optional[FaxUriId],
    smsLinkGroupId: Optional[SmsLinkGroupId],
    orderReference: Optional[OrderReference],
    channels: Optional[Channels],
    channelsOutbound: ChannelsOutbound,
    delivery: Delivery,
    trunkId: Optional[TrunkId],
    capacityGroupId: Optional[CapacityGroupId],
    didGroupId: DidGroupId,
    callerId: CallerId,
    cliPrivacy: CliPrivacy,
    otherOptions: OtherOptions,
    ringback: Ringback,
    dnisEnabled: DnisEnabled,
    blockOrdinary: BlockOrdinary,
    blockCellular: BlockCellular,
    blockPayphone: BlockPayphone,
    smsOutbound: SmsOutbound,
    webRtc: WebRtc,
    voxoutNational: VoxoutNational,
    voxoutInternationalEnabled: VoxoutInternationalEnabled,
    cancellationAvailable: CancellationAvailable,
    regulationAddressId: Optional[RegulationAddressId],
    srvLookup: SrvLookup
)

case class Peer(t38Enabled: T38Enabled, dtmf: Dtmf, dtmfInbandMute: DtmfInbandMute, codecs: Codecs)

case class VoxFaxEnabled(value: Boolean)
case class DeliveryId(value: Long)
case class WebRtcEnabled(value: Boolean)
case class LimitChannels(value: Int)
case class LimitChannelsOutbound(value: Int)
case class VoxoutNationalEnabled(value: Boolean)

case class DidConfiguration(
    didId: DidId,
    voiceUriId: Optional[VoiceUriId],
    smsLinkGroupId: Optional[SmsLinkGroupId],
    faxUriId: Optional[FaxUriId],
    voxFaxEnabled: VoxFaxEnabled,
    capacityGroupId: Optional[CapacityGroupId],
    trunkId: Optional[TrunkId],
    deliveryId: DeliveryId,
    srvLookup: SrvLookup,
    callerId: CallerId,
    cliPrivacy: Optional[CliPrivacy],
    peer: Peer,
    ringback: Ringback,
    dnisEnabled: DnisEnabled,
    blockOrdinary: BlockOrdinary,
    blockCellular: BlockCellular,
    blockPayphone: BlockPayphone,
    smsOutbound: SmsOutbound,
    webRtcEnabled: WebRtcEnabled,
    limitChannels: Optional[LimitChannels],
    limitChannelsOutbound: Optional[LimitChannelsOutbound],
    voxoutInternationalEnabled: VoxoutInternationalEnabled,
    voxoutNationalEnabled: VoxoutNationalEnabled)

case class DidConfigurationResponse(failedDidConfigurations: Optional[java.util.List[String]], status: Status, jsonString: Optional[String] = Optional.empty())

case class Ip(value: String)
case class Ips(values: java.util.Set[Ip])
case class Pop(deliveryId: DeliveryId, name: String, ips: Ips)
case class Pops(pops: java.util.Set[Pop])
