package dk.nuuday.digitalCommunications.voxbone.jsonModel
import dk.nuuday.digitalCommunications.voxbone.models._
import spray.json._

import scala.jdk.CollectionConverters._
trait CapacityGroupJsonProtocol extends DefaultJsonProtocol {
  implicit val capacityGroupIdFormat: RootJsonFormat[CapacityGroupId] = jsonFormat1(CapacityGroupId)
  implicit val maximumCapacityFFormat: RootJsonFormat[MaximumCapacity] = jsonFormat1(MaximumCapacity)
  implicit val descriptionFormat: RootJsonFormat[Description] = jsonFormat1(Description)
  implicit val amountOfDidsMappedFormat: RootJsonFormat[AmountOfDidsMapped] = jsonFormat1(AmountOfDidsMapped)

  implicit val capacityGroupsFormat = jsonFormat4(CapacityGroups)
  implicit val capacityGroupFormat = jsonFormat3(CapacityGroup)
  implicit val dtoCapacityGroupFormat = jsonFormat3(DtoCapacityGroup)
  implicit val dtoCapacityGroupsFormat = jsonFormat4(DtoCapacityGroups)
  implicit val dtoCapacityGroupsResponse = jsonFormat2(DtoCapacityGroupsResponse)
  implicit val dtoCapacityGroupResponse = jsonFormat4(DtoCapacityGroupResponse)


  implicit object listOfCapacityGroups extends RootJsonFormat[java.util.List[CapacityGroups]]  {
    override def write(
                        obj: java.util.List[CapacityGroups])
    : JsValue = obj.asScala.toVector.toJson
    override def read(json: JsValue)
    : java.util.List[CapacityGroups] = json match {
      case JsArray(list) => list.map(_.convertTo[CapacityGroups]).asJava
      case other => throw DeserializationException(s"failed to deserialize $other")
    }
  }
  implicit val capacityGroupsResponseFormat = jsonFormat2(CapacityGroupsResponse)
  implicit val capacityGroupResponseFormat = jsonFormat4(CapacityGroupResponse)






}