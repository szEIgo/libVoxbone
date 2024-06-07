package dk.nuuday.digitalCommunications.voxbone.models
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.VoxboneQueryParameter
import enumeratum._

sealed trait ServiceType extends EnumEntry with VoxboneQueryParameter {
  override def getQueryName = "countryCodeA3"
  override def getQueryValue: String = entryName
}

object ServiceType extends Enum[ServiceType] {
  val values = findValues

  def getQueryName = "serviceType"
  case object VOXDID extends ServiceType
  case object VOX800 extends ServiceType
}
