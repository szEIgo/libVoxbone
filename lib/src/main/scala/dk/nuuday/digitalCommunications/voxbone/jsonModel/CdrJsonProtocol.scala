package dk.nuuday.digitalCommunications.voxbone.jsonModel
import dk.nuuday.digitalCommunications.voxbone.models.{
  Carrier,
  Cdr,
  CdrResponse,
  Link,
  Pagination,
  Quality
}
import spray.json.{DeserializationException, JsObject, JsString, JsValue, RootJsonFormat, enrichAny}

import java.time.{LocalDateTime, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.util.Try

trait CdrJsonProtocol extends SharedJsonProtocol {

  implicit val carrierFormat = jsonFormat7(Carrier)
  implicit val qualityFormat = jsonFormat2(Quality)
  implicit val linksFormat = jsonFormat8(Link)
  implicit val paginationFormat = jsonFormat3(Pagination)


  implicit object zonedDateTimeFormat extends RootJsonFormat[ZonedDateTime] {
    override def read(json: JsValue): ZonedDateTime = json match {
      case JsString(str) => {
        ZonedDateTime
          .parse(str)
      }
      case other =>
        throw DeserializationException(s"failed to map $other while trying to parse LocalDateTime")
    }
    override def write(obj: ZonedDateTime): JsValue = {
      JsString(obj.withFixedOffsetZone().toString)
    }
  }

  implicit object cdrFormat extends RootJsonFormat[Cdr] {
    override def read(json: JsValue): Cdr = json match {
      case JsObject(fields) =>
        Cdr(
          id = fields("id").convertTo[String],
          callType = Try(fields("callType").convertTo[Option[String]]).getOrElse(None),
          capacityGroupId = Try(fields("capacityGroupId").convertTo[Option[Int]]).getOrElse(None),
          capacityGroupName = Try(fields("capacityGroupName").convertTo[Option[String]]).getOrElse(None),
          countDidCalls = Try(fields("countDidCalls").convertTo[Option[Int]]).getOrElse(None),
          currency = Try(fields("currency").convertTo[Option[String]]).getOrElse(None),
          destinationCountry = Try(fields("destinationCountry").convertTo[Option[String]]).getOrElse(None),
          destinationNumber = fields("destinationNumber").convertTo[String],
          dialStatus = Try(fields("dialStatus").convertTo[Option[String]]).getOrElse(None),
          direction = fields("direction").convertTo[String],
          duration = fields("duration").convertTo[Long],
          e164 = fields("e164").convertTo[String],
          end = fields("end").convertTo[ZonedDateTime],
          endStatus = fields("endStatus").convertTo[String],
          ip = Try(fields("ip").convertTo[Option[String]]).getOrElse(None),
          uri = Try(fields("uri").convertTo[Option[String]]).getOrElse(None),
          numberType = Try(fields("numberType").convertTo[Option[String]]).getOrElse(None),
          onNet = fields("onNet").convertTo[Boolean],
          originCountry = Try(fields("originCountry").convertTo[Option[String]]).getOrElse(None),
          originationNumber = fields("originationNumber").convertTo[String],
          pop = fields("pop").convertTo[String],
          ppe= Try(fields("ppe").convertTo[Option[String]]).getOrElse(None),
          ppm= Try(fields("ppm").convertTo[Option[String]]).getOrElse(None),
          result = fields("result").convertTo[String],
          servicePlan = Try(fields("servicePlan").convertTo[Option[String]]).getOrElse(None),
          serviceType = fields("serviceType").convertTo[String],
          start = fields("start").convertTo[ZonedDateTime],
          totalCost= Try(fields("totalCost").convertTo[Option[String]]).getOrElse(None),
          trunkId= Try(fields("trunkId").convertTo[Option[Int]]).getOrElse(None),
          trunkName=Try(fields("trunkName").convertTo[Option[String]]).getOrElse(None),
          zeroRated= fields("zeroRated").convertTo[Boolean],
          quality= Try(fields("quality").convertTo[Option[Quality]]).getOrElse(None)

        )
      case other => throw DeserializationException(s"Failed to serialize $other")
    }
    override def write(obj: Cdr): JsValue =
      JsObject(
        "id" -> obj.id.toJson,
        "callType" -> obj.callType.toJson,
        "capacityGroupId" -> obj.capacityGroupId.toJson,
        "capacityGroupName" -> obj.capacityGroupName.toJson,
        "countDidCalls" -> obj.countDidCalls.toJson,
        "currency" -> obj.currency.toJson,
        "destinationCountry" -> obj.destinationCountry.toJson,
        "destinationNumber" -> obj.destinationNumber.toJson,
        "dialStatus" -> obj.dialStatus.toJson,
        "direction" -> obj.direction.toJson,
        "duration" -> obj.duration.toJson,
        "e164" -> obj.e164.toJson,
        "end" -> obj.end.toJson,
        "endStatus" -> obj.endStatus.toJson,
        "ip" -> obj.ip.toJson,
        "uri" -> obj.uri.toJson,
        "numberType" -> obj.numberType.toJson,
        "onNet" -> obj.onNet.toJson,
        "originCountry" -> obj.originCountry.toJson,
        "originationNumber" -> obj.originationNumber.toJson,
        "pop" -> obj.pop.toJson,
        "ppe" -> obj.ppe.toJson,
        "ppm" -> obj.ppm.toJson,
        "result" -> obj.result.toJson,
        "servicePlan" -> obj.servicePlan.toJson,
        "serviceType" -> obj.serviceType.toJson,
        "start" -> obj.start.toJson,
        "totalCost" -> obj.totalCost.toJson,
        "trunkId" -> obj.trunkId.toJson,
        "trunkName" -> obj.trunkName.toJson,
        "zeroRated" -> obj.zeroRated.toJson,
        "quality" -> obj.quality.toJson
      )
  }

  implicit object cdrResponseFormat extends RootJsonFormat[CdrResponse] {
    override def write(obj: CdrResponse): JsValue =
      JsObject(
        "status" -> obj.status.toJson,
        "data" -> obj.data.toJson,
        "pagination" -> obj.pagination.toJson,
        "links" -> obj.links.toJson
      )

    override def read(json: JsValue): CdrResponse = json match {
      case JsObject(fields) =>
        CdrResponse(
          fields("data").convertTo[List[Cdr]],
          fields("links").convertTo[List[Link]],
          fields("pagination").convertTo[Pagination],
          fields("status").convertTo[String]
        )
      case _ => throw DeserializationException("Did not find necessary Json")

    }
  }

}
