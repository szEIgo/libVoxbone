package dk.nuuday.digitalCommunications.voxbone.jsonModel
import java.util.Optional

import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._

import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._

trait DidGroupsJsonProtocol extends SharedJsonProtocol {

  implicit object DidGroupRegulationRequirementsFormat
      extends RootJsonFormat[DidGroupRegulationRequirement] {
    override def read(json: JsValue): DidGroupRegulationRequirement = json match {
      case JsObject(map) =>
        DidGroupRegulationRequirement(
          AddressTypeEnum.withName(map("addressType").convertTo[String]),
          ProofRequired(map("proofRequired").convertTo[Boolean]))
      case other => throw DeserializationException(s"failed to deserialize $other")
    }
    override def write(obj: DidGroupRegulationRequirement): JsValue = ???
  }

  implicit object featuresFormat extends RootJsonFormat[Features] {
    override def read(json: JsValue): Features = json match {
      case JsArray(elements) =>
        val value: Seq[Feature] = elements
          .map(element => {
            val jsObject = element.asJsObject.fields
            Feature(
              jsObject("featureId").convertTo[Int],
              jsObject("name").convertTo[String],
              jsObject("description").convertTo[String])
          })
        val asjava = value.asJava
        Features(asjava)
      case other => throw DeserializationException(s"failed to deserialize $other")
    }
    override def write(obj: Features): JsValue = ???
  }

  implicit object DidGroupFormat extends RootJsonFormat[DidGroup] {
    override def read(json: JsValue): DidGroup = json match {
      case JsObject(fields) =>
        DidGroup(
          DidGroupId(fields("didGroupId").convertTo[Long]),
          CountryCodesA3Enum.withName(fields("countryCodeA3").convertTo[String]),
          fields("stateId")
            .convertTo[Option[Int]]
            .map(optInt => Some(StateId(optInt)).toJava)
            .getOrElse(None.toJava),
          DidTypeEnum.withName(fields("didType").convertTo[String]),
          fields("cityName")
            .convertTo[Option[String]]
            .map(optStr => Some(CityName(optStr)).toJava)
            .getOrElse(None.toJava),
          AreaCode(fields("areaCode").convertTo[String]),
          fields("rateCenter")
            .convertTo[Option[String]]
            .map(optStr => Some(RateCenter(optStr)).toJava)
            .getOrElse(None.toJava),
          Stock(fields("stock").convertTo[Int]),
          Setup100(fields("setup100").convertTo[Int]),
          Monthly100(fields("monthly100").convertTo[Int]),
          Available(fields("available").convertTo[Boolean]),
          fields("regulationRequirement").convertTo[DidGroupRegulationRequirement],
          fields("features").convertTo[Features]
        )
      case other => throw DeserializationException(s"failed to deserialize $other")
    }
    override def write(obj: DidGroup): JsValue = JsObject(
      "didGroupId" -> obj.didGroupId.value.toJson,
      "countryCodeA3" -> obj.countryCodesA3Enum.entryName.toJson,
      "stateId" -> obj.stateId.toScala.map(_.id.toJson).getOrElse(JsNull),
      "didType" -> obj.didTypeEnum.entryName.toJson,
      "cityName" -> obj.cityName.toScala.map(_.value.toJson).getOrElse(JsNull),
      "areaCode" -> obj.areaCode.value.toJson,
      "rateCenter" -> obj.rateCenter.toScala.map(_.value.toJson).getOrElse(JsNull),
      "stock" -> obj.stock.value.toJson,
      "setup100" -> obj.setup100.value.toJson,
      "monthly100" -> obj.monthly100.value.toJson,
      "available" -> obj.available.value.toJson,
      "regulationRequirement" -> obj.didGroupRegulationRequirement.toJson,
      "features" -> obj.features.toJson
    )
  }

  implicit object DidGroupsResponseFormat extends RootJsonFormat[DidGroupsResponse] {
    override def read(json: JsValue): DidGroupsResponse = json match {
      case JsObject(keys) =>
        DidGroupsResponse(
          keys("didGroups").convertTo[Vector[DidGroup]].asJava,
          keys("resultCount").convertTo[Int])
      case other => throw DeserializationException(s"failed to deserialize $other")
    }
    override def write(obj: DidGroupsResponse): JsValue = ???
  }

}
