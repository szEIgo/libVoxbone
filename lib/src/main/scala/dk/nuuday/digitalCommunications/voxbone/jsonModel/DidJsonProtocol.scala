package dk.nuuday.digitalCommunications.voxbone.jsonModel
import java.util.Optional

import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.NumberInventory
import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._

import scala.jdk.OptionConverters._
trait DidJsonProtocol extends SharedJsonProtocol {
  implicit object CliFormatFormat extends RootJsonFormat[CliFormat] {
    override def write(obj: CliFormat): JsValue =
      obj.value.toScala.map(s => s.toJson).getOrElse(JsNull)
    override def read(json: JsValue): CliFormat =
      json match {
        case JsString(rawValue) => CliFormat(Some(rawValue).toJava)
        case JsNull => CliFormat(None.toJava)
        case other => throw DeserializationException(s"Could not find JsType CliFormat $other")
      }
  }
  implicit object cliValueFormat extends RootJsonFormat[CliValue] {
    override def write(obj: CliValue): JsValue =
      obj.value.toScala.map(s => s.toJson).getOrElse(JsNull)
    override def read(json: JsValue): CliValue = json match {
      case JsString(rawString) => CliValue(Some(rawString).toJava)
      case JsNull => CliValue(None.toJava)
      case other => throw DeserializationException(s"Could not find JsType CliValue $other")
    }

  }
  implicit val callerIdFormat = jsonFormat2(CallerId)

  implicit object faxUriIdFormat extends RootJsonFormat[Optional[FaxUriId]] {
    override def write(obj: Optional[FaxUriId]): JsValue =
      obj.toScala.map(_.faxUriId.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[FaxUriId] = json match {
      case JsNumber(rawNumber) => Some(FaxUriId(rawNumber.toLongExact)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType faxUriId $other")
    }
  }

  implicit object t38EnabledFormat extends RootJsonFormat[T38Enabled] {
    override def write(obj: T38Enabled): JsValue = obj.value.toJson
    override def read(json: JsValue): T38Enabled = json match {
      case JsString(rawValue) => T38Enabled(rawValue)
      case _ => throw DeserializationException(s"")
    }
  }
  implicit object dtmfFormat extends RootJsonFormat[Dtmf] {
    override def write(obj: Dtmf): JsValue = obj.value.toScala.map(_.entryName.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Dtmf = json match {
      case JsString(rawValue) => Dtmf(Some(DtmfEnum.withName(rawValue)).toJava)
      case JsNull => Dtmf(Optional.empty())
      case other => throw DeserializationException(s"Could not find JsType DTMF $other")
    }
  }
  implicit object dtmfInbandFormat extends RootJsonFormat[DtmfInbandMute] {
    override def write(obj: DtmfInbandMute): JsValue = obj.value.toJson
    override def read(json: JsValue): DtmfInbandMute = json match {
      case JsString(rawValue) => DtmfInbandMute(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object codecsFormat extends RootJsonFormat[Codecs] {
    import scala.jdk.CollectionConverters._
    override def write(obj: Codecs): JsValue =
      JsArray(obj.set.asScala.map(v => JsString(v.entryName)).toVector)
    override def read(json: JsValue): Codecs =
      Codecs(
        json
          .convertTo[JsArray]
          .elements
          .map(s => CodecsEnum.withName(s.convertTo[String]))
          .toSet[CodecsEnum]
          .asJava)
  }
  implicit val otherOptionsFormat = jsonFormat4(OtherOptions)

  implicit object didIdFormat extends RootJsonFormat[DidId] {
    override def write(obj: DidId): JsValue = obj.value.toJson
    override def read(json: JsValue): DidId = json match {
      case JsNumber(rawValue) => DidId(rawValue.toLongExact)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object e164Format extends RootJsonFormat[E164] {
    override def write(obj: E164): JsValue = obj.value.toJson
    override def read(json: JsValue): E164 = json match {
      case JsString(rawValue) => E164(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object numberTypeFormat extends RootJsonFormat[NumberType] {
    override def write(obj: NumberType): JsValue = obj.value.toJson
    override def read(json: JsValue): NumberType = json match {
      case JsString(rawValue) => NumberType(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }

  implicit object areaCodeFormat extends RootJsonFormat[AreaCode] {
    override def write(obj: AreaCode): JsValue = obj.value.toJson
    override def read(json: JsValue): AreaCode = json match {
      case JsString(rawString) => AreaCode(rawString)
      case _ => throw DeserializationException("")
    }
  }

  implicit object cityNameFormat extends RootJsonFormat[Optional[CityName]] {
    override def write(obj: Optional[CityName]): JsValue =
      obj.toScala.map(s => JsString(s.value)).getOrElse(JsNull)
    override def read(json: JsValue): Optional[CityName] = json match {
      case JsString(rawValue) => Some(CityName(rawValue)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType CityName $other")
    }
  }
  implicit object blockCellularFormat extends RootJsonFormat[BlockCellular] {
    override def write(obj: BlockCellular): JsValue = obj.value.toJson
    override def read(json: JsValue): BlockCellular = json match {
      case JsBoolean(rawValue) => BlockCellular(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object blockOrdinaryFormat extends RootJsonFormat[BlockOrdinary] {
    override def write(obj: BlockOrdinary): JsValue = obj.value.toJson
    override def read(json: JsValue): BlockOrdinary = json match {
      case JsBoolean(rawValue) => BlockOrdinary(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object blockPayphoneFormat extends RootJsonFormat[BlockPayphone] {
    override def write(obj: BlockPayphone): JsValue = obj.value.toJson
    override def read(json: JsValue): BlockPayphone = json match {
      case JsBoolean(rawValue) => BlockPayphone(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object CacellationFormat extends RootJsonFormat[CancellationAvailable] {
    import java.text.SimpleDateFormat
    val formatter = new SimpleDateFormat("dd/MM/yyyy")
    override def write(obj: CancellationAvailable): JsValue =
      JsString(s"${formatter.format(obj.value)}")
    override def read(json: JsValue): CancellationAvailable =
      json match {
        case JsString(rawValue) => CancellationAvailable(formatter.parse(rawValue))
        case other => throw DeserializationException(s"Could not find JsType CancellationAvailable $other")
      }
  }

  implicit object optCapacityGroupIdFormat extends RootJsonFormat[Optional[CapacityGroupId]] {
    override def write(obj: Optional[CapacityGroupId]): JsValue =
      obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[CapacityGroupId] = json match {
      case JsNumber(rawValue) => Some(CapacityGroupId(rawValue.toLongExact)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType CapacityGroupId $other")
    }
  }
  implicit object channelsFormat extends RootJsonFormat[Optional[Channels]] {
    override def write(obj: Optional[Channels]): JsValue =
      obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[Channels] = json match {
      case JsNumber(rawValue) => Some(Channels(rawValue.toInt)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType Channels $other")
    }
  }
  implicit object channelsOutboundFormat extends RootJsonFormat[ChannelsOutbound] {
    override def write(obj: ChannelsOutbound): JsValue = obj.value.toJson
    override def read(json: JsValue): ChannelsOutbound = json match {
      case JsNumber(rawValue) => ChannelsOutbound(rawValue.toInt)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object deliveryFormat extends RootJsonFormat[Delivery] {
    override def write(obj: Delivery): JsValue = obj.value.toJson
    override def read(json: JsValue): Delivery = json match {
      case JsString(rawValue) => Delivery(rawValue)
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object trunkIdFormat extends RootJsonFormat[Optional[TrunkId]] {
    override def write(obj: Optional[TrunkId]): JsValue = obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[TrunkId] = json match {
      case JsNumber(rawValue) => Some(TrunkId(rawValue.longValue)).toJava
      case JsNull => None.toJava
      case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
  }
  implicit object cliPrivacyFormat extends RootJsonFormat[CliPrivacy] {
    override def write(obj: CliPrivacy): JsValue = obj.value.entryName.toJson
    override def read(json: JsValue): CliPrivacy =
      json match {
        case JsString(rawValue) => CliPrivacy(CliPrivacyEnum.withName(rawValue))
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object regulationsAddressIdFormat extends RootJsonFormat[Optional[RegulationAddressId]] {
    override def write(obj: Optional[RegulationAddressId]): JsValue =
      obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[RegulationAddressId] =
      json match {
        case JsNumber(rawValue) => Some(RegulationAddressId(rawValue.toLongExact)).toJava
        case JsNull => None.toJava
        case other => throw DeserializationException(s"Could not find JsType RegulationAddressId $other")
      }
  }
  implicit object srvLookUpFormat extends RootJsonFormat[SrvLookup] {
    override def write(obj: SrvLookup): JsValue = obj.value.toJson
    override def read(json: JsValue): SrvLookup =
      json match {
        case JsBoolean(rawValue) => SrvLookup(rawValue)
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object ringbackFormat extends RootJsonFormat[Ringback] {
    override def write(obj: Ringback): JsValue = obj.value.entryName.toJson
    override def read(json: JsValue): Ringback =
      json match {
        case JsString(rawValue) => Ringback(RingbackEnum.withName(rawValue))
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object dnisEnabledFormat extends RootJsonFormat[DnisEnabled] {
    override def write(obj: DnisEnabled): JsValue = obj.value.toJson
    override def read(json: JsValue): DnisEnabled =
      json match {
        case JsBoolean(bool) => DnisEnabled(bool)
        case JsString(rawValue) =>
          DnisEnabled(
            rawValue.toBooleanOption.getOrElse(
              throw DeserializationException("unexpected value type")))
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object smsOutboundFormat extends RootJsonFormat[SmsOutbound] {
    override def write(obj: SmsOutbound): JsValue = obj.value.toJson
    override def read(json: JsValue): SmsOutbound =
      json match {
        case JsBoolean(rawValue) => SmsOutbound(rawValue)
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object voxoutNationalFormat extends RootJsonFormat[VoxoutNational] {
    override def write(obj: VoxoutNational): JsValue = obj.value.entryName.toJson
    override def read(json: JsValue): VoxoutNational =
      json match {
        case JsString(rawValue) => VoxoutNational(VoxoutNationalEnum.withName(rawValue))
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object voxOutInternationFormat extends RootJsonFormat[VoxoutInternationalEnabled] {
    override def write(obj: VoxoutInternationalEnabled): JsValue = obj.value.toJson
    override def read(json: JsValue): VoxoutInternationalEnabled =
      json match {
        case JsBoolean(rawValue) => VoxoutInternationalEnabled(rawValue)
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object webRtcFormat extends RootJsonFormat[WebRtc] {
    override def write(obj: WebRtc): JsValue = obj.value.toJson
    override def read(json: JsValue): WebRtc =
      json match {
        case JsBoolean(rawValue) => WebRtc(rawValue)
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
  }
  implicit object orderReferenceFormat extends RootJsonFormat[Optional[OrderReference]] {
    override def write(obj: Optional[OrderReference]): JsValue = obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[OrderReference] =
      json match {
        case JsString(rawValue) => Some(OrderReference(rawValue)).toJava
        case JsNull => None.toJava
        case other => throw DeserializationException(s"Could not find JsType for orderReference  $other")
      }
  }
  implicit object smsLinkGroupIdFormat extends RootJsonFormat[Optional[SmsLinkGroupId]] {
    override def write(obj: Optional[SmsLinkGroupId]): JsValue =
      obj.toScala.map(_.value.toJson).getOrElse(JsNull)
    override def read(json: JsValue): Optional[SmsLinkGroupId] =
      json match {
        case JsNumber(rawValue) => Some(SmsLinkGroupId(rawValue.toLongExact)).toJava
        case JsNull => None.toJava
        case other => throw DeserializationException(s"Could not find JsType smsLinkGroupId $other")
      }
  }

  implicit val peerFormat = jsonFormat4(Peer)

  implicit object DidFormat extends RootJsonFormat[Did] {
    override def write(obj: Did): JsValue = {
      JsObject(
        "areaCode" -> obj.areaCode.toJson,
        "blockCellular" -> obj.blockCellular.value.toJson,
        "blockOrdinary" -> obj.blockOrdinary.value.toJson,
        "blockPayphone" -> obj.blockPayphone.value.toJson,
        "callerIdFormat" -> obj.callerId.toJson,
        "cancellationAvailable" -> obj.cancellationAvailable.toJson,
        "capacityGroupId" -> obj.capacityGroupId.toScala.map(s => s.value.toJson).getOrElse(JsNull),
        "channels" -> obj.channels.toScala.map(_.value.toJson).getOrElse(JsNull),
        "channelsOutbound" -> obj.channelsOutbound.value.toJson,
        "didId" -> obj.didId.value.toJson,
        "e164" -> obj.e164.value.toJson,
        "type" -> obj.numberType.value.toJson,
        "countryCodeA3" -> obj.countryCodeA3.toJson,
        "channelOutbounds" -> obj.channelsOutbound.value.toJson,
        "deliveryId" -> obj.delivery.value.toJson,
        "trunkId" -> obj.trunkId.toJson,
        "cliPrivacy" -> obj.cliPrivacy.value.entryName.toJson,
        "regulationAddressId" -> obj.regulationAddressId.toScala
          .map(s => s.value.toJson)
          .getOrElse(JsNull),
        "srvLookup" -> obj.srvLookup.value.toJson,
        "cityName" -> obj.cityName.toScala.map(s => s.value.toJson).getOrElse(JsNull),
        "otherOptions" -> obj.otherOptions.toJson,
        "ringback" -> obj.ringback.value.entryName.toJson,
        "dnisEnabled" -> obj.dnisEnabled.value.toJson,
        "smsOutbound" -> obj.smsOutbound.value.toJson,
        "voxoutNational" -> obj.voxoutNational.value.entryName.toJson,
        "voxoutInternationalEnabled" -> obj.voxoutInternationalEnabled.value.toJson,
        "webRtc" -> obj.webRtc.value.toJson,
        "orderReference" -> obj.orderReference.toScala.map(_.value.toJson).getOrElse(JsNull),
        "smsLinkGroupId" -> obj.smsLinkGroupId.toScala.map(s => s.value.toJson).getOrElse(JsNull)
      )
    }
    override def read(json: JsValue): Did = json match {
      case JsObject(fields) =>
        Did(
          didId = DidId(fields("didId").convertTo[Long]),
          e164 = E164(fields("e164").convertTo[String]),
          numberType = NumberType(fields("type").convertTo[String]),
          countryCodeA3 = CountryCodesA3Enum.withName(fields("countryCodeA3").convertTo[String]),
          cityName = fields("cityName").convertTo[Optional[CityName]],
          areaCode = fields("areaCode").convertTo[AreaCode],
          voiceUriId = fields("voiceUriId").convertTo[Optional[VoiceUriId]],
          faxUriId = fields("faxUriId").convertTo[Optional[FaxUriId]],
          smsLinkGroupId = fields("smsLinkGroupId").convertTo[Optional[SmsLinkGroupId]],
          orderReference = fields("orderReference").convertTo[Optional[OrderReference]],
          channels = fields("channels").convertTo[Optional[Channels]],
          channelsOutbound = fields("channelsOutbound").convertTo[ChannelsOutbound],
          delivery = fields("delivery").convertTo[Delivery],
          trunkId = fields("trunkId").convertTo[Optional[TrunkId]],
          capacityGroupId = fields("capacityGroupId").convertTo[Optional[CapacityGroupId]],
          didGroupId = fields("didGroupId").convertTo[DidGroupId],
          callerId = fields("callerId").convertTo[CallerId],
          cliPrivacy = fields("cliPrivacy").convertTo[CliPrivacy],
          otherOptions = fields("otherOptions").convertTo[OtherOptions],
          ringback = fields("ringback").convertTo[Ringback],
          dnisEnabled = fields("dnisEnabled").convertTo[DnisEnabled],
          blockOrdinary = fields("blockOrdinary").convertTo[BlockOrdinary],
          blockCellular = fields("blockCellular").convertTo[BlockCellular],
          blockPayphone = fields("blockPayphone").convertTo[BlockPayphone],
          smsOutbound = fields("smsOutbound").convertTo[SmsOutbound],
          webRtc = fields("webRtc").convertTo[WebRtc],
          voxoutNational = fields("voxoutNational").convertTo[VoxoutNational],
          voxoutInternationalEnabled =
            fields("voxoutInternationalEnabled").convertTo[VoxoutInternationalEnabled],
          cancellationAvailable = fields("cancellationAvailable").convertTo[CancellationAvailable],
          regulationAddressId =
            fields("regulationAddressId").convertTo[Optional[RegulationAddressId]],
          srvLookup = fields("srvLookup").convertTo[SrvLookup]
        )
      case other => throw DeserializationException(s"Expected JsObject, but found ${other}")
    }
  }

  implicit object configurationDidFormat extends RootJsonFormat[DidConfiguration] {

    override def read(json: JsValue): DidConfiguration = json match {
      case JsObject(fields) =>
        DidConfiguration(
          didId = fields("didId").convertTo[DidId],
          voiceUriId = Optional.of(VoiceUriId(fields("voiceUriId").convertTo[Long])),
          smsLinkGroupId = Optional.of(SmsLinkGroupId(fields("smsLinkGroupId").convertTo[Long])),
          faxUriId = Optional.of(FaxUriId(fields("faxUriId").convertTo[Long])),
          voxFaxEnabled = VoxFaxEnabled(fields("voxFaxEnabled").convertTo[Boolean]),
          capacityGroupId = Optional.of(CapacityGroupId(fields("capacityGroupId").convertTo[Long])),
          trunkId = Optional.of(TrunkId(fields("trunkId").convertTo[Long])),
          deliveryId = DeliveryId(fields("deliveryId").convertTo[Long]),
          srvLookup = SrvLookup(fields("srvLookup").convertTo[Boolean]),
          callerId = fields("callerId").convertTo[CallerId],
          cliPrivacy = fields("cliPrivacy")
            .convertTo[Option[String]]
            .map(s => Some(CliPrivacy(CliPrivacyEnum.withName(s))).toJava)
            .getOrElse(None.toJava),
          peer = fields("peer").convertTo[Peer],
          ringback = Ringback(RingbackEnum.withName(fields("ringback").convertTo[String])),
          dnisEnabled = DnisEnabled(fields("dnisEnabled").convertTo[Boolean]),
          blockOrdinary = BlockOrdinary(fields("blockOrdinary").convertTo[Boolean]),
          blockCellular = BlockCellular(fields("blockCellular").convertTo[Boolean]),
          blockPayphone = BlockPayphone(fields("blockPayphone").convertTo[Boolean]),
          smsOutbound = SmsOutbound(fields("smsOutbound").convertTo[Boolean]),
          webRtcEnabled = WebRtcEnabled(fields("webRtcEnabled").convertTo[Boolean]),
          limitChannels = Optional.of(LimitChannels(fields("limitChannels").convertTo[Int])),
          limitChannelsOutbound =
            Optional.of(LimitChannelsOutbound(fields("limitChannelsOutbound").convertTo[Int])),
          voxoutInternationalEnabled =
            VoxoutInternationalEnabled(fields("voxoutInternationalEnabled").convertTo[Boolean]),
          voxoutNationalEnabled =
            VoxoutNationalEnabled(fields("voxoutNationalEnabled").convertTo[Boolean])
        )
      case other => throw DeserializationException(s"Expected JsObject, but found ${other}")
    }
    override def write(obj: DidConfiguration): JsValue = JsObject(
      "didId" -> obj.didId.value.toJson,
      "voiceUriId" -> obj.voiceUriId.toScala.map(_.value.toJson).getOrElse(JsNull),
      "smsLinkGroupId" -> obj.smsLinkGroupId.toScala.map(_.value.toJson).getOrElse(JsNull),
      "faxUriId" -> obj.faxUriId.toScala.map(_.faxUriId.toJson).getOrElse(JsNull),
      "voxFaxEnabled" -> obj.voxFaxEnabled.value.toJson,
      "capacityGroupId" -> obj.capacityGroupId.toScala.map(_.value.toJson).getOrElse(JsNull),
      "trunkId" -> obj.trunkId.toScala.map(_.value.toJson).getOrElse(JsNull),
      "deliveryId" -> obj.deliveryId.value.toJson,
      "srvLookup" -> obj.srvLookup.value.toJson,
      "callerId" -> obj.callerId.toJson,
      "cliPrivacy" -> obj.cliPrivacy.toScala.map(_.value.entryName.toJson).getOrElse(JsNull),
      "peer" -> obj.peer.toJson,
      "ringback" -> obj.ringback.value.entryName.toJson,
      "dnisEnabled" -> obj.dnisEnabled.value.toJson,
      "blockOrdinary" -> obj.blockOrdinary.value.toJson,
      "blockCellular" -> obj.blockCellular.value.toJson,
      "blockPayphone" -> obj.blockPayphone.value.toJson,
      "smsOutbound" -> obj.smsOutbound.value.toJson,
      "webRtcEnabled" -> obj.webRtcEnabled.value.toJson,
      "limitChannels" -> obj.limitChannels.toScala.map(_.value.toJson).getOrElse(JsNull),
      "limitChannelsOutbound" -> obj.limitChannelsOutbound.toScala
        .map(_.value.toJson)
        .getOrElse(JsNull),
      "voxoutInternationalEnabled" -> obj.voxoutInternationalEnabled.value.toJson,
      "voxoutNationalEnabled" -> obj.voxoutNationalEnabled.value.toJson
    )
  }

  implicit val didsFormat: RootJsonFormat[NumberInventory] = jsonFormat2(NumberInventory)

  implicit object didConfigurationResponseFormat extends RootJsonFormat[DidConfigurationResponse] {
    import scala.jdk.CollectionConverters._
    override def write(obj: DidConfigurationResponse): JsValue =
      JsObject(
        "failedDidConfigurations" -> obj.failedDidConfigurations.map(_.asScala.toVector.toJson).toScala.getOrElse(JsNull),
        "status" -> obj.status.entryName.toJson)
    override def read(json: JsValue): DidConfigurationResponse = json match {
      case JsObject(map) =>
          val value = map("failedDidConfigurations")
            .convertTo[Option[Vector[String]]]
            .map(_.asJava).toJava
          DidConfigurationResponse(value,
          Status.withName(map("status").convertTo[String]))
      case other =>
        throw DeserializationException(s"failed to serialize DidConfigurationResponse $other")
    }
  }

  implicit object popFormat extends RootJsonFormat[Pop] {
    import scala.jdk.CollectionConverters._
    override def read(json: JsValue): Pop = json match {
      case JsObject(fields) =>
        Pop(
          DeliveryId(fields("deliveryId").convertTo[Long]),
          fields("name").convertTo[String],
          Ips(fields("ips").convertTo[Vector[String]].map(Ip).toSet.asJava)
        )
      case other => throw DeserializationException(s" failed to serialize $other")
    }
    override def write(obj: Pop): JsValue =
      ???
  }

  implicit object popsFormat extends RootJsonFormat[Pops] {
    import scala.jdk.CollectionConverters._
    override def read(json: JsValue): Pops = json match {
      case JsObject(fields) => Pops(fields("pops").convertTo[Vector[Pop]].toSet.asJava)
      case other => throw DeserializationException(s" failed to serialize $other")
    }
    override def write(obj: Pops): JsValue = ???
  }

}
