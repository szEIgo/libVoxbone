package dk.nuuday.digitalCommunications.voxbone.httpModels
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.VoxboneQueryParameter
import dk.nuuday.digitalCommunications.voxbone.models.ServiceType

abstract class NumberInventoryParameter extends VoxboneQueryParameter

final case class DidIdsParameter(from: Int, to: Int) extends NumberInventoryParameter {
  override def getQueryName = "didIds"
  override def getQueryValue: String = s"$from,$to"
}
final case class DidGroupIdsParameter(from: Int, to: Int) extends NumberInventoryParameter {
  override def getQueryName = "didGroupIds"
  override def getQueryValue: String = s"$from,$to"
}

final case class E164PatternParameter(value: String) extends NumberInventoryParameter{
  override def getQueryName: String = "e164Pattern"
  override def getQueryValue: String = s"$value"
}

final case class RegulationAddressIdParamter(value: Int) extends NumberInventoryParameter {
  override def getQueryName: String = "regulationAddressId"
  override def getQueryValue: String = s"$value"
}


final case class FaxUriIdParameter(value: Int) extends NumberInventoryParameter {
  override def getQueryName: String = "faxUriId"
  override def getQueryValue: String = s"$value"
}

final case class SmsLinkGroupIdParameter(value: Int) extends NumberInventoryParameter {
  override def getQueryName: String = "smsLinkGroupId"
  override def getQueryValue: String = s"$value"
}

final case class NeedAddressLinkParameter(value: Boolean) extends NumberInventoryParameter {
  override def getQueryName: String = "needAddressLink"
  override def getQueryValue: String = s"$value"
}

final case class ServiceTypeParameter(value: ServiceType) extends NumberInventoryParameter {
  override def getQueryName: String = "serviceType"
  override def getQueryValue: String = s"${value.entryName}"
}

final case class OrderReferenceParameter(value: String) extends NumberInventoryParameter {
  override def getQueryName: String = "orderReference"
  override def getQueryValue: String = s"$value"
}

final case class PortingReferenceParamter(value: String) extends NumberInventoryParameter {
  override def getQueryName: String = "portingReference"
  override def getQueryValue: String = s"$value"
}

final case class DeliveryIdParameter(value: Int) extends NumberInventoryParameter {
  override def getQueryName: String = "deliveryId"
  override def getQueryValue: String = s"$value"
}

final case class SmsOutboundParameter(value: Boolean) extends NumberInventoryParameter {
  override def getQueryName: String = "smsOutbound"
  override def getQueryValue: String = s"$value"
}

final case class WebRtcEnabledParameter(value: Boolean) extends NumberInventoryParameter {
  override def getQueryName: String = "webRtcEnabled"
  override def getQueryValue: String = s"$value"
}

final case class VoxoutNationalEnabledParameter(value: Boolean) extends NumberInventoryParameter {
  override def getQueryName: String = "voxoutNationalEnabled"
  override def getQueryValue: String = s"$value"
}
final case class VoxoutInternationalEnabledParameter(value: Boolean) extends NumberInventoryParameter {
  override def getQueryName: String = "voxoutInternationalEnabled"
  override def getQueryValue: String = s"$value"
}








