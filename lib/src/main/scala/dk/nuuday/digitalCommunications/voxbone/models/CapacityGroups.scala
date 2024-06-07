package dk.nuuday.digitalCommunications.voxbone.models

case class MaximumCapacity(value: Long)
case class AmountOfDidsMapped(value: Long)
case class CapacityGroups(
    capacityGroupId: CapacityGroupId,
    maximumCapacity: MaximumCapacity,
    description: Description,
    amountOfDidsMapped: AmountOfDidsMapped)

case class CapacityGroup(
    capacityGroupId: CapacityGroupId,
    maximumCapacity: MaximumCapacity,
    description: Description)

case class CapacityGroupResponse(
    capacityGroupId: CapacityGroupId,
    maximumCapacity: MaximumCapacity,
    description: Description,
    amountOfDidsMapped: AmountOfDidsMapped)

case class DtoCapacityGroupResponse(
    capacityGroupId: Long,
    maximumCapacity: Long,
    description: String,
    amountOfDidsMapped: Long) {
  def toCapacityGroupResponse(): CapacityGroupResponse =
    CapacityGroupResponse(
      CapacityGroupId(capacityGroupId),
      MaximumCapacity(maximumCapacity),
      Description(description),
      AmountOfDidsMapped(amountOfDidsMapped))
}

case class CapacityGroupsResponse(capacityGroups: java.util.List[CapacityGroups], resultCount: Long)


case class DtoCapacityGroupsResponse(
    capacityGroups: Vector[DtoCapacityGroups],
    resultCount: Long){
  import scala.jdk.CollectionConverters._
  def toCapacityGroupsResponse(): CapacityGroupsResponse = CapacityGroupsResponse(capacityGroups.map(_.toCapacityGroups()).asJava,resultCount)
}

case class DtoCapacityGroup(capacityGroupId: Long, maximumCapacity: Long, description: String) {
  def toCapacityGroup(): CapacityGroup =
    CapacityGroup(
      CapacityGroupId(capacityGroupId),
      MaximumCapacity(maximumCapacity),
      Description(description))
}
case class DtoCapacityGroups(
    capacityGroupId: Long,
    maximumCapacity: Long,
    description: String,
    amountOfDidsMapped: Long) {
  def toCapacityGroups(): CapacityGroups =
    CapacityGroups(
      CapacityGroupId(capacityGroupId),
      MaximumCapacity(maximumCapacity),
      Description(description),
      AmountOfDidsMapped(amountOfDidsMapped))
}
