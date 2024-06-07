package dk.nuuday.digitalCommunications.voxbone.httpModels
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests._
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests._
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests
import dk.nuuday.digitalCommunications.voxbone.jsonModel.{CartJsonProtocol, DidJsonProtocol, VoiceUrisJsonProtocol}
import dk.nuuday.digitalCommunications.voxbone.models._
import org.scalatest.funspec.AnyFunSpec

import java.util.Optional
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

class VoxboneHttpRequestsTest extends AnyFunSpec {
  val f = Fixture

  describe(s"A ${classOf[VoxboneHttpRequests].getSimpleName}") {

    it(s"should be able to parse queryparameters if there is an empty set of queryParameters") {
      assert(QueryParameters(Set.empty[QueryParameter].asJava).toCombinedQueryString === "")
    }

    it("should be able to parse a QueryParameters to String if there is only 1. ") {
      val expected = "?countryCodeA3=DNK"
      val actual =
        QueryParameters(Set(f.queryParameterCountryCodeDenmark).asJava).toCombinedQueryString
      assert(actual === expected)
    }

    it("should be able to parse several QueryParameters in correct format") {
      val expected = s"?countryCodeA3=DNK&didIds=1,1000&pageNumber=1&pageSize=20"
      val actual = f.queryParams.toCombinedQueryString
      assert(actual === expected)
    }

    it(s"should not create queryParameters if there is None") {

      val actual = QueryParameters(Set(f.queryParameterDIDids).asJava)
      val expected = "?didIds=1,1000"
      assert(actual.toCombinedQueryString === expected)
    }
    it(s"Should generate a valid HttpRequest using listNumberInventoryRequest method") {
      val expected = true
      import scala.jdk.CollectionConverters._
      val actual = f.requests.inventoryRequests.listNumberInventoryRequest(
        QueryParameters(Set(
          QueryParameter(CountryCodesA3Enum.Algeria),
          QueryParameter(f.didIds),
          QueryParameter(DidGroupIdsParameter(1, 10)),
          QueryParameter(f.pageNumber),
          QueryParameter(f.pageSize),
          QueryParameter(ServiceType.VOX800)
        ).asJava))

      assert(actual.isRequest() === expected)
      assert(
        actual.toString() === """HttpRequest(HttpMethod(GET),https://localhost/v1/inventory/did?didGroupIds=1,10&countryCodeA3=VOX800&didIds=1,1000&countryCodeA3=DZA&pageNumber=1&pageSize=20,List(apikey: testApiKey, Authorization, Accept: application/json),HttpEntity.Strict(none/none,0 bytes total),HttpProtocol(HTTP/1.1))""")

    }
  }


  describe("json parser") {
    it(
      s"should be able to parse and serialize json as provided from the API and parse it into a Did class") {
      assert(f.testSprayJsonProtocol$.didDummy === f.testSprayJsonProtocol$.didParsedFromJsonString)
    }

    it(s"should be able to serialize jsonString to NumberInventory") {
      f.testSprayJsonProtocol$.numberInventorySerializedFromObject
    }

    it(s"should be able to serialize Cart") {
      assert(f.testSprayJsonProtocol$.cart === f.testSprayJsonProtocol$.cartJson)
    }

    it(s"should be able to parse voiceUris") {
      assert(f.testSprayJsonProtocol$.voiceUrisJson.isInstanceOf[VoiceUris])
    }
    it(s"should be able to parse ChangeVoiceResponse") {
      object testSprayJsonForChangeVoiceResponse extends VoiceUrisJsonProtocol {
        import spray.json._
        val response: ChangeVoiceResponse =
          f.changeVoiceResponsejsonString.parseJson.convertTo[ChangeVoiceResponse]

//        assert(response.isInstanceOf[ChangeVoiceResponse])
      }
      assert(testSprayJsonForChangeVoiceResponse.response.isInstanceOf[ChangeVoiceResponse])

    }
  }
}
object Fixture {
  import spray.json._
  implicit val serviceInfo = ServerInfo("localhost")
  implicit val apiKey = ApikeyAuthentication("testApiKey")
  implicit val basic = BasicAuthenticationCredentials("testUsername", "testPassword")
  val requests = new VoxboneHttpRequests()


  val queryParameterCountryCodeDenmark =
    QueryParameter(CountryCodesA3Enum.Denmark)

  val queryParameterCountryCodeSweden =
    QueryParameter(CountryCodesA3Enum.Sweden)

  val didIds: DidIdsParameter = DidIdsParameter(1, 1000)

  val queryParameterDIDids = QueryParameter(didIds)

  val pageSize: PageSizeParameter = PageSizeParameter(20)

  val queryParameterPageSize = QueryParameter(pageSize)

  val pageNumber: PageNumberParameter = PageNumberParameter(1)

  val queryParameterPageNumber = QueryParameter(pageNumber)

  val queryParams = QueryParameters(
    Set(
      queryParameterCountryCodeDenmark,
      queryParameterDIDids,
      queryParameterPageNumber,
      queryParameterPageSize).asJava)

  import java.text.SimpleDateFormat
  val formatter = new SimpleDateFormat("dd/MM/yyyy")

  val didJsonString =
    """
      |{
      |            "didId": 8265643,
      |            "e164": "+541159845617",
      |            "type": "GEOGRAPHIC",
      |            "countryCodeA3": "ARG",
      |            "cityName": "BUENOS AIRES",
      |            "areaCode": "11",
      |            "voiceUriId": 157479,
      |            "faxUriId": null,
      |            "smsLinkGroupId": null,
      |            "orderReference": "75155DS2483085",
      |            "channels": -1,
      |            "channelsOutbound": 0,
      |            "delivery": "BE",
      |            "trunkId": 3324,
      |            "capacityGroupId": null,
      |            "didGroupId": 3,
      |            "regulationAddressId": null,
      |            "srvLookup": false,
      |            "callerId": {
      |                "cliFormat": "E164",
      |                "cliValue": "+"
      |            },
      |            "cliPrivacy": "DISABLED",
      |            "otherOptions": {
      |                "t38Enabled": "false",
      |                "dtmf": "RFC2833",
      |                "dtmfInbandMute": "false",
      |                "codecs": [
      |                    "G711A",
      |                    "G711U",
      |                    "G729"
      |                ]
      |            },
      |            "ringback": "STANDARD",
      |            "dnisEnabled": "false",
      |            "blockOrdinary": false,
      |            "blockCellular": false,
      |            "blockPayphone": false,
      |            "smsOutbound": false,
      |            "webRtc": false,
      |            "voxoutNational": "NOT_SUPPORTED",
      |            "voxoutInternationalEnabled": false,
      |            "cancellationAvailable": "25/08/2018"
      |        }""".stripMargin

  val numberInventoryJsonString =
    """
      |{"dids":[{"didId":4287936,"e164":"+61384004587","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"MELBOURNE","areaCode":"38","voiceUriId":369375,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS1895555","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19893,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"07/03/2018"},{"didId":9639101,"e164":"+61272018431","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"27","voiceUriId":603789,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS3462661","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":27337,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"09/07/2019"},{"didId":4311353,"e164":"+61285203002","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":null,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS2146471","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"27/05/2018"},{"didId":4313080,"e164":"+61283104729","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":157518,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"24403DS609624","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":3836,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"RINGING","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"16/11/2013"},{"didId":6656450,"e164":"+61283107433","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":459721,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS2188015","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"08/06/2018"},{"didId":6656451,"e164":"+61283107434","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":491761,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS2188015","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"08/06/2018"},{"didId":6656452,"e164":"+61283107435","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":491769,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS2188015","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"08/06/2018"},{"didId":4311348,"e164":"+61286078997","type":"GEOGRAPHIC","countryCodeA3":"AUS","cityName":"SYDNEY","areaCode":"28","voiceUriId":null,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"75155DS2146471","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":3324,"capacityGroupId":null,"didGroupId":19894,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"STANDARD","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"27/05/2018"},{"didId":5744278,"e164":"+43720115443","type":"NATIONAL","countryCodeA3":"AUT","cityName":null,"areaCode":"720","voiceUriId":157482,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"24403DS609624","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":165,"capacityGroupId":3835,"didGroupId":6425,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"RINGING","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"16/11/2013"},{"didId":5745681,"e164":"+43720116846","type":"NATIONAL","countryCodeA3":"AUT","cityName":null,"areaCode":"720","voiceUriId":626621,"faxUriId":null,"smsLinkGroupId":null,"orderReference":"24403DS746377","channels":-1,"channelsOutbound":0,"delivery":"BE","trunkId":165,"capacityGroupId":3835,"didGroupId":6425,"regulationAddressId":null,"srvLookup":false,"callerId":{"cliFormat":"E164","cliValue":"+"},"cliPrivacy":"DISABLED","otherOptions":{"t38Enabled":"false","dtmf":"RFC2833_INBAND","dtmfInbandMute":"false","codecs":["G711A","G711U","G729"]},"ringback":"RINGING","dnisEnabled":"false","blockOrdinary":false,"blockCellular":false,"blockPayphone":false,"smsOutbound":false,"webRtc":false,"voxoutNational":"DISABLED","voxoutInternationalEnabled":false,"cancellationAvailable":"09/07/2014"}],"resultCount":8682}
      |""".stripMargin

  final object testSprayJsonProtocol$ extends VoiceUrisJsonProtocol with DidJsonProtocol with CartJsonProtocol {

    val didDummy = Did(
      DidId(8265643L),
      e164 = E164("+541159845617"),
      NumberType("GEOGRAPHIC"),
      CountryCodesA3Enum.withName("ARG"),
      Some(CityName("BUENOS AIRES")).toJava,
      AreaCode("11"),
      Some(VoiceUriId(157479L)).toJava,
      None.toJava,
      None.toJava,
      Optional.of(OrderReference("75155DS2483085")),
      Some(Channels(-1)).toJava,
      ChannelsOutbound(0),
      Delivery("BE"),
      Optional.of(TrunkId(3324L)),
      None.toJava,
      DidGroupId(3L),
      CallerId(CliFormat(Some("E164").toJava), CliValue(Some("+").toJava)),
      CliPrivacy(CliPrivacyEnum.DISABLED),
      OtherOptions(
        T38Enabled("false"),
        Dtmf(Optional.of(DtmfEnum.RFC2833)),
        DtmfInbandMute("false"),
        Codecs(Set[CodecsEnum](CodecsEnum.G711A , CodecsEnum.G711U, CodecsEnum.G729).asJava)),
      Ringback(RingbackEnum.STANDARD),
      DnisEnabled(false),
      BlockOrdinary(false),
      BlockCellular(false),
      BlockPayphone(false),
      SmsOutbound(false),
      WebRtc(false),
      VoxoutNational(VoxoutNationalEnum.NOT_SUPPORTED),
      VoxoutInternationalEnabled(false),
      CancellationAvailable(formatter.parse("25/08/2018")),
      None.toJava,
      SrvLookup(false)
    )
    val didParsedFromJsonString = didJsonString.parseJson.convertTo[Did]
    val didToJsonString = didDummy.toJson.prettyPrint

    val numberInvetoryFromObject: NumberInventory = NumberInventory(Vector(didDummy), 1)

    val numberInventorySerializedFromObject = numberInvetoryFromObject.toJson.prettyPrint
    val numberInventoryParsedFromString = numberInventoryJsonString.parseJson

    val cart = CartDto(
      Cart(
        CartIdentifier(4872589L),
        CustomerReference("<string>"),
        Description("<string>"),
        DateAdded("2021-01-18 13:05:49"),
        OrderProducts(Vector.empty)))

    val cartJson =
      """{"cart":{"cartIdentifier":4872589,"customerReference":"<string>","description":"<string>","dateAdded":"2021-01-18 13:05:49","orderProducts":null}}""".parseJson
        .convertTo[CartDto]

    val voiceUrisJson =
      """
        |{
        |    "voiceUris": [
        |        {
        |            "voiceUriId": 25744,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "44540040@77.234.175.202",
        |            "description": "Test 3 klang ingen modtagelse"
        |        },
        |        {
        |            "voiceUriId": 29126,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "echo@ivrs",
        |            "description": "Test echo and one-way"
        |        },
        |        {
        |            "voiceUriId": 34756,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "55454544@80.162.82.166",
        |            "description": "EPG"
        |        },
        |        {
        |            "voiceUriId": 35976,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88180007@80.72.145.10",
        |            "description": "Cirque Test iNum"
        |        },
        |        {
        |            "voiceUriId": 39772,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88631196@80.72.145.10",
        |            "description": "Comway Sweden 33296074"
        |        },
        |        {
        |            "voiceUriId": 41847,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "46108885318@80.72.145.10",
        |            "description": "TEST MBI (NIXpille) - 77337104"
        |        },
        |        {
        |            "voiceUriId": 41901,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88180526@77.234.170.10",
        |            "description": "3PAS TEST - 88180526"
        |        },
        |        {
        |            "voiceUriId": 67337,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "77665695@62.66.245.118",
        |            "description": "Mønthuset"
        |        },
        |        {
        |            "voiceUriId": 71647,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888888@77.234.169.134",
        |            "description": "Bruuns Bazaar SWE"
        |        },
        |        {
        |            "voiceUriId": 75035,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "55555555@77.234.170.10",
        |            "description": "3PAS TEST - 33751589"
        |        },
        |        {
        |            "voiceUriId": 80488,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88528943@80.72.145.10",
        |            "description": "Bjerre Media (Fiskefeber)"
        |        },
        |        {
        |            "voiceUriId": 83190,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888889@77.234.169.134",
        |            "description": "Bruus Bazaar DE"
        |        },
        |        {
        |            "voiceUriId": 83191,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888887@77.234.169.134",
        |            "description": "Bruus Bazaar F"
        |        },
        |        {
        |            "voiceUriId": 83192,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888886@77.234.169.134",
        |            "description": "Bruus Bazaar NL"
        |        },
        |        {
        |            "voiceUriId": 83193,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888885@77.234.169.134",
        |            "description": "Bruus Bazaar No"
        |        },
        |        {
        |            "voiceUriId": 83194,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88888884@77.234.169.134",
        |            "description": "Bruus Bazaar ENG"
        |        },
        |        {
        |            "voiceUriId": 85548,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88349053@80.72.145.10",
        |            "description": "Nortlander Skitours SE"
        |        },
        |        {
        |            "voiceUriId": 85691,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88528945@80.72.145.10",
        |            "description": "Bjerre Media (Ingelise NO)"
        |        },
        |        {
        |            "voiceUriId": 85692,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88528944@80.72.145.10",
        |            "description": "Bjerre Media (Ingelise SE)"
        |        },
        |        {
        |            "voiceUriId": 91153,
        |            "backupUriId": null,
        |            "backupUri": null,
        |            "voiceUriProtocol": "SIP",
        |            "uri": "88528941@80.72.145.10",
        |            "description": "Bjerre Media (Scanorder)"
        |        }
        |    ],
        |    "resultCount": 8895
        |}""".stripMargin.parseJson.convertTo[VoiceUris]

  }

  val changeVoiceResponsejsonString =
    """{"allOf":[{"$ref":"#/definitions/SaveVoiceUriRequest"}],"definitions":{"SaveVoiceUriRequest":{"type":"object","required":["voiceUri"],"properties":{"voiceUri":{"$ref":"#/definitions/SaveVoiceUri"}}},"SaveVoiceUri":{"type":"object","required":["uri","voiceUriProtocol"],"properties":{"voiceUriId":{"type":"integer","format":"int32","description":"The identifier of the voice uri."},"backupUriId":{"type":"string","description":"The identifier of the voice uri acting as backup."},"voiceUriProtocol":{"type":"string","description":"The protocol to use with this voice uri. Supported protocols are `SIP` and `TEL`.","enum":["SIP","TEL"]},"uri":{"type":"string","description":"The actual uri where the call will be delivered."},"description":{"type":"string","description":"A human-readable description of this uri."}}}}}"""

}
