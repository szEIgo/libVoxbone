package dk.nuuday.digitalCommunications.voxbone.jsonModel
import dk.nuuday.digitalCommunications.voxbone.models.{Description, Trunk, Trunks, Zone}
import spray.json._

trait CombinedVoxboneJsonProtocol
    extends CartJsonProtocol
    with CapacityGroupJsonProtocol
    with DidJsonProtocol
    with ServiceAndCoverageProtocol
    with VoiceUrisJsonProtocol
    with DidGroupsJsonProtocol
    with CdrJsonProtocol
    with CountryRestrictionProtocol {
  implicit object TrunkFormat extends RootJsonFormat[Trunk] {
    override def read(json: JsValue): Trunk = json match {
      case JsObject(map) =>
        Trunk(
          map("trunkId").convertTo[Int],
          map("zone").convertTo[Zone],
          map("capacity").convertTo[Int],
          map("description").convertTo[Description],
          map("countDidsOnTrunk").convertTo[Int]
        )
      case other => throw DeserializationException(s"Failed to deserialize $other")
    }
    override def write(obj: Trunk): JsValue = ???
  }

  implicit object TrunksFormat extends RootJsonFormat[Trunks] {
    override def read(json: JsValue): Trunks = ???
    override def write(obj: Trunks): JsValue = ???
  }
}
