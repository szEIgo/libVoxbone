package dk.nuuday.digitalCommunications.voxbone.httpModels
import java.util.Optional
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.errorMessages.{
  CommonErrorCodes,
  ErrorMessage
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.{
  DidGroupIdsParameter,
  DidIdsParameter,
  E164PatternParameter,
  NumberInventory
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.{
  PageNumberParameter,
  PageSizeParameter,
  QueryParameter,
  QueryParameters
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests.{
  Anonymized,
  AnonymizedParameter,
  CdrParameters,
  From,
  FromParameter
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.{
  VoxboneHttpRequests,
  VoxboneHttpRequestsTrait
}
import dk.nuuday.digitalCommunications.voxbone.jsonModel.{
  CdrJsonProtocol,
  CombinedVoxboneJsonProtocol,
  DidGroupsJsonProtocol,
  DidJsonProtocol,
  ErrorMessageProtocol,
  VoiceUrisJsonProtocol
}
import dk.nuuday.digitalCommunications.voxbone.models._
import org.scalatest.funspec.AnyFunSpec
import spray.json.JsString

import java.time.ZonedDateTime
import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

class VoxboneHttpRequestsTraitTest extends AnyFunSpec {

  val f = Fixture

  describe(s"A ${classOf[VoxboneHttpRequestsTrait].getSimpleName}") {

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
    it(s"should be able to parse CommonErrorMessage") {

      final object tmpJsonObject extends ErrorMessageProtocol {
        import spray.json._
        val input =
          """
            |{
            |  "httpStatusCode":400,
            |  "apiModule":"INVENTORY",
            |  "genericErrorMessage":"The system could not successfully execute your request because of an error in the request. Please review the messages below for more information.",
            |  "errors":[
            |              {"apiErrorCode":"13",
            |               "apiErrorMessage":"Pagination overflow : the given page does not exist."}
            |           ]
            |}""".stripMargin.parseJson.convertTo[ErrorMessage]

      }
      assert(tmpJsonObject.input.genericErrorMessage.contains("The system"))
      val string = tmpJsonObject.input.errors.get(0).apiErrorCode.toString
      println(string)
      assert("13" === string)
      val int = string.toInt
      println(CommonErrorCodes)
      assert(tmpJsonObject.input.isInstanceOf[ErrorMessage])
    }
    it(s"should be able to serialize DidConfiguration") {
      val input = f.testSprayJsonProtocol$.didConfigurationSerialized
      println(input.prettyPrint)
    }

    it(s"should be able to parse DidConfiguration from string") {
      val input = f.testSprayJsonProtocol$.didConfigurationJson
      final object testDidJson extends DidJsonProtocol {
        val output = input.convertTo[DidConfiguration]
      }
      assert(testDidJson.output.isInstanceOf[DidConfiguration])
    }

    it(s"should be able to serialize NewVoiceUri") {
      val input = f.testSprayJsonProtocol$.newVoiceUriJson
      val expected = f.testSprayJsonProtocol$.newVoiceUriParsedJson

      assert(input === expected)
    }

    it(s"should be able to serialize a voiceUriResponse") {
      val input = f.testSprayJsonProtocol$.voiceUriJsonParsed
      val expected = f.testSprayJsonProtocol$.voiceUriResponseDtoToJson
      assert(input === expected)
    }

    it(s"should be able to serialize a CountryRestrictions") {
      val input = f.testSprayJsonProtocol$.newCountryRestrictions
      val input2 = f.testSprayJsonProtocol$.newCountryRestrictions2
      assert(input.equals(input2))
    }

    it(
      s"should be able to deserialize voiceUri either when it is wrapped as named or unnamed jsobject") {
      val input1 =
        """
          |{
          |    "voiceUri": {
          |        "voiceUriId": 815562,
          |        "backupUriId": null,
          |        "voiceUriProtocol": "SIP",
          |        "uri": "886362191216237d2@80.72.145.10",
          |        "description": "ISS Ungarn 88870617"
          |    }
          |}""".stripMargin
      val input2 =
        """
          |{
          |    "voiceUri": {
          |        "voiceUriId": 815562,
          |        "backupUriId": null,
          |        "voiceUriProtocol": "SIP",
          |        "uri": "886362191216237d2@80.72.145.10",
          |        "description": "ISS Ungarn 88870617"
          |    }
          |}""".stripMargin

    }
    it(s"should be able to parse DidGroups using Json from their own Api Documentation") {
      object tmpObj extends DidGroupsJsonProtocol {

        val input: DidGroupsResponse =
          f.testSprayJsonProtocol$.didGroupsJsonString.convertTo[DidGroupsResponse]

      }
      assert(tmpObj.input.isInstanceOf[DidGroupsResponse])
    }
    it(s"should be able to parse optional DidConfigurationResponse") {
      object tmpObj extends DidJsonProtocol {
        val input2: DidConfigurationResponse =
          f.testSprayJsonProtocol$.didGroupResponseEmptyJsonString
            .convertTo[DidConfigurationResponse]

      }
      assert(tmpObj.input2.isInstanceOf[DidConfigurationResponse])
    }
  }

  describe("QueryParameters should be able to formatted correctly") {
    it("e164 pattern parameter is escaped correctly if contains space and countrycode") {
      val unescaped = "%45"
      val escaped = E164PatternParameter(unescaped)
      val expected = "%2545"
      assert(escaped.getQueryValue === expected)
    }

  }

  describe(s"a ${classOf[CdrResponse]} should be able to de- & serialize") {
    it(s"should be able to parse Json String to CdrResponse") {
      object tmpObj extends CdrJsonProtocol {
        import spray.json._
        val input = f.cdrResponseJsonString
        val output = input.parseJson.convertTo[CdrResponse]
      }
      assert(tmpObj.output.isInstanceOf[CdrResponse])
    }
    it(s"should be able to serialize CdrResponse to Json String") {
      object tmpObj extends CdrJsonProtocol {
        import spray.json._
        val input = f.cdrResponse
        val output = input.toJson.prettyPrint
      }
      assert(tmpObj.output.isInstanceOf[String])
    }
  }
  describe(s"a ${classOf[CdrParameters]} should be able to create QueryParameters") {
    it(
      s"it should remove all None values, and parse all those who are actually there, and then use toQueryParameters function") {
      val input = f.cdrParameters
      val output1 = input.toQueryParameters().toCombinedQueryString

      assert(output1 === "?anonymized=true")
    }
  }
  describe(
    s"a ${classOf[CdrJsonProtocol]} should be able to parse A voxbone formatted time, to a ${classOf[ZonedDateTime]}") {
    it(s"it should parse and create a model") {
      val input = JsString("2021-05-31T11:34:07.000Z")
      object testCdrJson extends CdrJsonProtocol {
        import spray.json._
        val result = input.convertTo[ZonedDateTime]
        println(result)

      }
      assert(testCdrJson.result.isInstanceOf[ZonedDateTime])
    }

    it("should parse another cdr string") {
      val inputInString = f.AnotherCdrString
      println("Is this pritned?")
      object testCdrJsonString extends CdrJsonProtocol {
        import spray.json._

        val string =
          """
            |[{"id":"FK5B22URJ15IJD05HKGBEHO23S","originationNumber":"31850231014","destinationNumber":"31165315300","e164":"+31165315300","serviceType":"VOICE","direction":"INBOUND","numberType":"GEOGRAPHIC","callType":"VOXDID","destinationCountry":"NLD","result":"SUCCESS","ppe":"0.0000","totalCost":"0.0000","currency":"EUR","zeroRated":true,"pop":"BE","ip":"80.72.145.10:5060","ppm":"0.0000","end":"2021-11-30T13:07:14.000Z","start":"2021-11-30T13:04:09.000Z","endStatus":"NORMAL","dialStatus":"ANSWER","duration":184,"onNet":false,"countDidCalls":1,"zoneId":165,"zoneName":"Zone A","quality":{"customer":{"avgMos":"440","jitter":"3.694","packetLoss":"0","rtt":"1","codec":"G711a"},"carrier":{"avgMos":"440","jitter":"3.691","packetLoss":"0","rtt":"20","codec":"G711a"}},"sipResponseCode":200,"cpc":"UNSUPPORTED","uri":"sip:63357399@80.72.145.10"}]
            |""".stripMargin

        val totalString =
          """
            |{"status":"success","data":[{"id":"OES7NCLRMH7F154O4S263PR7AK","originationNumber":"494087409486","destinationNumber":"01624656371","e164":"+494087409486","serviceType":"VOICE","direction":"OUTBOUND","numberType":"GEOGRAPHIC","callType":"NATIONAL","destinationCountry":"DEU","result":"SUCCESS","zeroRated":true,"pop":"BE","ip":"13.74.248.196","end":"2021-12-02T14:30:23.000Z","start":"2021-12-02T14:30:15.000Z","endStatus":"NORMAL","dialStatus":"CANCEL","duration":-1,"onNet":false,"zoneId":165,"zoneName":"Zone A","capacityGroupId":82049,"capacityGroupName":"10247-1-Users","sipResponseCode":100,"originalNumber":"+494087409486","servicePlan":"Network_Germany Mobile - National","callForwarded":false}],"pagination":{"offset":0,"limit":1,"total":3476},"links":[{"rel":"self","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-12-01T15:30:28.000&to=2021-12-02T15:30:28.000&limit=1"},{"rel":"next","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-12-01T15:30:28.000&to=2021-12-02T15:30:28.000&offset=1&limit=1"},{"rel":"last","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-12-01T15:30:28.000&to=2021-12-02T15:30:28.000&offset=3475&limit=1"},{"rel":"first","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-12-01T15:30:28.000&to=2021-12-02T15:30:28.000&offset=0&limit=1"}]}
            |""".stripMargin.parseJson

        println(totalString.convertTo[CdrResponse])

        val paginationToString = Pagination(1, 0, 20L).toJson.prettyPrint

        print(paginationToString)

        val paginationStr =
          """
            |{"offset": 1,
            | "limit": 1,
            | "total": 3523
            |}
            |""".stripMargin

        val paginationParsed = paginationStr.parseJson.convertTo[Pagination]
        println(paginationParsed)

        val parsed = string.parseJson.convertTo[List[Cdr]]

        println(parsed)

        val data = f.cdrResponse.toJson
        val print1: String = data.compactPrint
        val parsedCdrResponse = print1.parseJson.convertTo[CdrResponse]

      }

      assert(testCdrJsonString.parsedCdrResponse == f.cdrResponse)
    }
  }
  object Fixture {
    import spray.json._
    implicit val serviceInfo = ServerInfo("localhost")
    implicit val apiKey = ApikeyAuthentication("testApiKey")
    implicit val basic = BasicAuthenticationCredentials("testUsername", "testPassword")
    val requests = new VoxboneHttpRequests()

    val e164PatternParameter = E164PatternParameter("+45 51155563")

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
        |                    "SPEEX",
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

    final object testSprayJsonProtocol$ extends CombinedVoxboneJsonProtocol {
      val newCountryRestrictions: CountryRestrictions =
        """
          |{
          |    "restrictions": [
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "LEGAL",
          |            "restrictionMessage": "For geographical numbers, the location of the end user (place of residence/business) must be within the relevant geographic zone. If requested by Voxbone, Customer shall send the location of the end user (place of residence/business) within 24 hours of receiving the request, otherwise the number may be disconnected."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "LEGAL",
          |            "restrictionMessage": "Only \"828\" toll-free numbers can be used for calling card applications. Other toll-free numbers or VoxDID numbers cannot be used for such purposes."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "Vox800 numbers (range 828) are not reachable from Mobiles"
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "Vox800 numbers are not reachable from other countries than Czech Republic."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "Compatibility issues have been reported for international fax transmissions."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxOUT Emergency cannot be enabled on non-geographical or VoxDID Mobile numbers"
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "LEGAL",
          |            "restrictionMessage": "For VoxDID mobile numbers, the customer will submit to Voxbone name, surname, address and copy of ID/Passport of the end user. For business end-users, the data of the company (name, address, registration number) and a copy of the ID/Passport of an authorized representative are required. These should be submitted within 24 hours of receiving the request, otherwise the number may be disconnected."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxDID Mobile numbers only support SMS, support for inbound voice is not available."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "Outbound SMSs are limited to 2000 messages per day for the Czech Republic."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "Porting is currently not available for Mobile numbers."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "International reachability cannot be guaranteed for SMS and inbound voice on VoxDID Mobile numbers. Please contact our NOC team through the ticketing system to report such issues as we are working on extending international reachability."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxDID Mobile numbers do not support delivery reports."
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxSMS is supported only on VoxDID Mobile numbers"
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxOUT National not supported on VoxDID Mobile numbers"
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "SERVICE",
          |            "restrictionMessage": "VoxOUT National currently does not support outbound calling to Premium rate numbers"
          |        },
          |        {
          |            "countryCodeA3": "CZE",
          |            "restrictionType": "LEGAL",
          |            "restrictionMessage": "Customers reselling Electronic Communications Services must be registered operators with the local regulator."
          |        }
          |    ]
          |}
          |""".stripMargin.parseJson.convertTo[CountryRestrictions]

      val newCountryRestrictions2 = newCountryRestrictions.toJson.prettyPrint.parseJson.convertTo[CountryRestrictions]

      val newVoiceUri: NewVoiceUri = NewVoiceUri(
        Uri("886362191216237d2@80.72.145.10"),
        Optional.of(VoiceUriProtocol(VoiceUriProtocolEnum.SIP)),
        BackupUriId(Optional.empty()),
        Description("ISS Ungarn 88870617")
      )
      val newVoiceUriJson: JsValue = newVoiceUri.toJson
      val newVoiceUriParsedJson: JsValue =
        """{"voiceUri":{"uri":"886362191216237d2@80.72.145.10","voiceUriProtocol":"SIP","voiceUriId":null,"backupUriId":null,"description":"ISS Ungarn 88870617"}}""".stripMargin.parseJson

      private val ga: CodecsEnum = CodecsEnum.G711A
      private val g: CodecsEnum = CodecsEnum.G729
      private val speex: CodecsEnum = CodecsEnum.SPEEX
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
        Some(OrderReference("75155DS2483085")).toJava,
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
          Codecs(Set(ga, g, speex).asJava)),
        Ringback(RingbackEnum.withName("STANDARD")),
        DnisEnabled(false),
        BlockOrdinary(false),
        BlockCellular(false),
        BlockPayphone(false),
        SmsOutbound(false),
        WebRtc(false),
        VoxoutNational(VoxoutNationalEnum.withName("NOT_SUPPORTED")),
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

      val popJsonString =
        """
          |{"pops":[{"deliveryId":84,"name":"AU","ips":["185.47.148.45"]},{"deliveryId":2,"name":"BE","ips":["81.201.82.45"]},{"deliveryId":3,"name":"CN","ips":["81.201.86.45"]},{"deliveryId":12,"name":"DE","ips":["81.201.83.45"]},{"deliveryId":14,"name":"US-LA","ips":["81.201.84.195"]},{"deliveryId":6,"name":"US-NY","ips":["81.201.85.45"]}]}""".stripMargin.parseJson
          .convertTo[Pops]

      val didGroupsJsonString =
        """
          |{
          |    "didGroups": [
          |        {
          |            "didGroupId": 7001,
          |            "countryCodeA3": "DNK",
          |            "stateId": null,
          |            "didType": "GEOGRAPHIC",
          |            "cityName": null,
          |            "areaCode": "",
          |            "rateCenter": null,
          |            "stock": 1403,
          |            "setup100": 0,
          |            "monthly100": 52,
          |            "available": true,
          |            "regulationRequirement": {
          |                "addressType": "WORLDWIDE",
          |                "proofRequired": false
          |            },
          |            "features": [
          |                {
          |                    "featureId": 6,
          |                    "name": "VoxFax",
          |                    "description": "VoxFax"
          |                },
          |                {
          |                    "featureId": 50,
          |                    "name": "Voice",
          |                    "description": "Voice"
          |                },
          |                {
          |                    "featureId": 51,
          |                    "name": "VoxOUT International",
          |                    "description": "Used to build the coverage pages in the public website"
          |                },
          |                {
          |                    "featureId": 5,
          |                    "name": "T.38",
          |                    "description": "T.38"
          |                },
          |                {
          |                    "featureId": 29,
          |                    "name": "VoxOUT Emergency",
          |                    "description": "Feature that allows the VoxDID to access emergency services"
          |                },
          |                {
          |                    "featureId": 52,
          |                    "name": "VoxOUT National",
          |                    "description": "Used to build the coverage pages in the public website"
          |                }
          |            ]
          |        }
          |    ],
          |    "resultCount": 23
          |}""".stripMargin.parseJson

      val didGroupResponseEmptyJsonString =
        """
          |{
          |    "failedDidConfigurations": null,
          |    "status": "SUCCESS"
          |}""".stripMargin.parseJson

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

      val didConfiguration = DidConfiguration(
        DidId(1),
        Some(VoiceUriId(2)).toJava,
        Some(SmsLinkGroupId(3)).toJava,
        Some(FaxUriId(4)).toJava,
        VoxFaxEnabled(false),
        Some(CapacityGroupId(4)).toJava,
        Some(TrunkId(5L)).toJava,
        DeliveryId(6),
        SrvLookup(false),
        CallerId(CliFormat(Some("CliFormat").toJava), CliValue(Some("CliValue").toJava)),
        Some(CliPrivacy(CliPrivacyEnum.DISABLED)).toJava,
        Peer(
          T38Enabled("t38Enabled"),
          Dtmf(Optional.of(DtmfEnum.INBAND)),
          DtmfInbandMute("dmtfInbandMute"),
          Codecs(Set(g.asInstanceOf[CodecsEnum], CodecsEnum.H264.asInstanceOf[CodecsEnum]).asJava)
        ),
        Ringback(RingbackEnum.withName("RINGING")),
        DnisEnabled(false),
        BlockOrdinary(false),
        BlockCellular(false),
        BlockPayphone(false),
        SmsOutbound(false),
        WebRtcEnabled(false),
        Some(LimitChannels(7)).toJava,
        Some(LimitChannelsOutbound(8)).toJava,
        VoxoutInternationalEnabled(false),
        VoxoutNationalEnabled(false)
      )

      val voiceUriJsonParsed =
        """
          |{
          |    "voiceUri": {
          |        "voiceUriId": 816116,
          |        "backupUriId": null,
          |        "voiceUriProtocol": "SIP",
          |        "uri": "886362191216237d2@80.72.145.10",
          |        "description": "ISS Ungarn 88870617"
          |    }
          |}""".stripMargin.parseJson

      val voiceUriResponse = VoiceUriResponse(
        VoiceUriId(816116L),
        BackupUriIdFromResponse(Optional.empty()),
        VoiceUriProtocol(VoiceUriProtocolEnum.SIP),
        Uri("886362191216237d2@80.72.145.10"),
        Description("""ISS Ungarn 88870617""")
      )
      val voiceUriResponseDtoToJson = VoiceUriResponseDto(voiceUriResponse).toJson
      val didConfigurationSerialized = didConfiguration.toJson
      val didConfigurationJson =
        """
          |{
          |  "didId": -100000000,
          |  "voiceUriId": -100000000,
          |  "smsLinkGroupId": -100000000,
          |  "faxUriId": -100000000,
          |  "voxFaxEnabled": false,
          |  "capacityGroupId": -100000000,
          |  "trunkId": -100000000,
          |  "deliveryId": -100000000,
          |  "srvLookup": false,
          |  "callerId": {
          |    "cliFormat": "RAW",
          |    "cliValue": ""
          |  },
          |  "cliPrivacy": "DISABLED",
          |  "peer": {
          |    "t38Enabled": "",
          |    "dtmf": "RFC2833_INBAND",
          |    "dtmfInbandMute": "",
          |    "codecs": [
          |      "G711A"
          |    ]
          |  },
          |  "ringback": "STANDARD",
          |  "dnisEnabled": false,
          |  "blockOrdinary": false,
          |  "blockCellular": false,
          |  "blockPayphone": false,
          |  "smsOutbound": false,
          |  "webRtcEnabled": false,
          |  "limitChannels": 1,
          |  "limitChannelsOutbound": 1,
          |  "voxoutInternationalEnabled": false,
          |  "voxoutNationalEnabled": false
          |}""".stripMargin.parseJson

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
    val cdrResponseJsonString =
      """
        |{
        |  "data": [
        |    {
        |      "id": "ADJF4B7S2P4N95FEDG5ANH8G34",
        |      "callType": "VOXDID",
        |      "capacityGroupId": 23,
        |      "capacityGroupName": "Voxbone UK",
        |      "countDidCalls": 242,
        |      "currency": "EUR",
        |      "destinationCountry": "GBR",
        |      "destinationNumber": "+442039668000",
        |      "dialStatus": "ANSWER",
        |      "direction": "OUTBOUND",
        |      "duration": 256,
        |      "e164": "+442039668001",
        |      "end": "2020-03-04T12:04:58.000Z",
        |      "endStatus": "NORMAL",
        |      "ip": "195.88.119.98",
        |      "uri": "vox@sipvox.voxbone.com",
        |      "numberType": "GEOGRAPHIC",
        |      "onNet": false,
        |      "originCountry": "GBR",
        |      "originationNumber": "+442039668001",
        |      "pop": "DE",
        |      "ppe": "200",
        |      "ppm": "50",
        |      "result": "SUCCESS",
        |      "serviceType": "VOICE",
        |      "start": "2020-03-04T12:00:38.000Z",
        |      "totalCost": "460",
        |      "trunkId": 247,
        |      "trunkName": "Zone A",
        |      "zeroRated": false,
        |      "quality": {
        |        "carrier": {
        |          "avgMos": "440",
        |          "codec": "G711µ",
        |          "jitter": "0.175",
        |          "packetLoss": "0.1",
        |          "maxMos": "440",
        |          "minMos": "420",
        |          "rtt": "1"
        |        },
        |        "customer": {
        |          "avgMos": "440",
        |          "codec": "G711µ",
        |          "jitter": "0.175",
        |          "packetLoss": "0.1",
        |          "maxMos": "440",
        |          "minMos": "420",
        |          "rtt": "1"
        |        }
        |      }
        |    }
        |  ],
        |  "links": [
        |    {
        |      "rel": "self",
        |      "href": "https://api.voxbone.com/cdrs/voice",
        |      "hreflang": "",
        |      "media": "",
        |      "deprecation": "",
        |      "templated": false,
        |      "title": "",
        |      "type": ""
        |    }
        |  ],
        |  "pagination": {
        |    "limit": 10000,
        |    "offset": 20,
        |    "total": 9000
        |  },
        |  "status": "success"
        |}""".stripMargin

    val cdrResponse = CdrResponse(List.empty, List.empty, Pagination(2, 2, 2), "good")

    val cdrParameters = CdrParameters(
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      None,
      Some(AnonymizedParameter(Anonymized(true))))

    val cdrString =
      """
        |{
        |      "id": "ADJF4B7S2P4N95FEDG5ANH8G34",
        |      "callType": "VOXDID",
        |      "capacityGroupId": 23,
        |      "capacityGroupName": "Voxbone UK",
        |      "countDidCalls": 242,
        |      "currency": "EUR",
        |      "destinationCountry": "GBR",
        |      "destinationNumber": "+442039668000",
        |      "dialStatus": "ANSWER",
        |      "direction": "OUTBOUND",
        |      "duration": 256,
        |      "e164": "+442039668001",
        |      "end": "2020-03-04T12:04:58.000",
        |      "endStatus": "NORMAL",
        |      "ip": "195.88.119.98",
        |      "uri": "vox@sipvox.voxbone.com",
        |      "numberType": "GEOGRAPHIC",
        |      "onNet": false,
        |      "originCountry": "GBR",
        |      "originationNumber": "+442039668001",
        |      "pop": "DE",
        |      "ppe": "200",
        |      "ppm": "50",
        |      "result": "SUCCESS",
        |      "serviceType": "VOICE",
        |      "start": "2020-03-04T12:00:38.000",
        |      "totalCost": "460",
        |      "trunkId": 247,
        |      "trunkName": "Zone A",
        |      "zeroRated": false,
        |      "quality": {
        |        "carrier": {
        |          "avgMos": "440",
        |          "codec": "G711µ",
        |          "jitter": "0.175",
        |          "packetLoss": "0.1",
        |          "maxMos": "440",
        |          "minMos": "420",
        |          "rtt": "1"
        |        },
        |        "customer": {
        |          "avgMos": "440",
        |          "codec": "G711µ",
        |          "jitter": "0.175",
        |          "packetLoss": "0.1",
        |          "maxMos": "440",
        |          "minMos": "420",
        |          "rtt": "1"
        |        }
        |      }
        |}""".stripMargin

    val AnotherCdrString =
      """
        |{"status":"success","data":[{"id":"FK5B22URJ15IJD05HKGBEHO23S","originationNumber":"31850231014","destinationNumber":"31165315300","e164":"+31165315300","serviceType":"VOICE","direction":"INBOUND","numberType":"GEOGRAPHIC","callType":"VOXDID","destinationCountry":"NLD","result":"SUCCESS","ppe":"0.0000","totalCost":"0.0000","currency":"EUR","zeroRated":true,"pop":"BE","ip":"80.72.145.10:5060","ppm":"0.0000","end":"2021-11-30T13:07:14.000Z","start":"2021-11-30T13:04:09.000Z","endStatus":"NORMAL","dialStatus":"ANSWER","duration":184,"onNet":false,"countDidCalls":1,"zoneId":165,"zoneName":"Zone A","quality":{"customer":{"avgMos":"440","jitter":"3.694","packetLoss":"0","rtt":"1","codec":"G711a"},"carrier":{"avgMos":"440","jitter":"3.691","packetLoss":"0","rtt":"20","codec":"G711a"}},"sipResponseCode":200,"cpc":"UNSUPPORTED","uri":"sip:63357399@80.72.145.10"}],"pagination":{"offset":0,"limit":1,"total":3523},"links":[{"rel":"self","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-11-29T13:07:18.000&to=2021-11-30T13:07:18.000&limit=1"},{"rel":"next","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-11-29T13:07:18.000&to=2021-11-30T13:07:18.000&offset=1&limit=1"},{"rel":"last","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-11-29T13:07:18.000&to=2021-11-30T13:07:18.000&offset=3522&limit=1"},{"rel":"first","href":"https://api.voxbone.com/v2/cdr/calls?from=2021-11-29T13:07:18.000&to=2021-11-30T13:07:18.000&offset=0&limit=1"}]}
        |""".stripMargin
  }

}
