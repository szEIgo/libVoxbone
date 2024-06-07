package dk.nuuday.digitalCommunications.voxbone.httpModels.requests
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.ConfigurationRequests.{CapacityGroupRequests, ConfigurationRequests, DidsRequests}
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests
import dk.nuuday.digitalCommunications.voxbone.models.{ServerInfo, Status}

class VoxboneHttpRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends VoxboneHttpRequestsTrait {
  def inventoryRequests: VoxboneHttpRequestsTrait.InventoryRequestsTrait =
    new InventoryRequests()
  def orderingRequests(): VoxboneHttpRequestsTrait.OrderingRequestTrait =
    new OrderingRequests()
  def configurationRequests(): ConfigurationRequests =
    new ConfigurationRequests()
  def serviceAndCoverageRequests(): ServiceAndCoverageRequests = new ServiceAndCoverageRequests()
  def capacityGroupRequests(): CapacityGroupRequests = new CapacityGroupRequests()
  def didsRequest(): DidsRequests = new DidsRequests()
  def cdrRequests(): CdrRequests = new CdrRequests()
}

object VoxboneHttpRequests {


  trait VoxboneQueryParameter {
    def getQueryName: String
    def getQueryValue: String
  }

  case class QueryParameter(parameter: VoxboneQueryParameter) {
    def toQueryString: String =
      s"${parameter.getQueryName}=${parameter.getQueryValue}"
  }

  final case class QueryParameters(parameters: java.util.Set[QueryParameter]) {
    def toCombinedQueryString: String = {
      import scala.jdk.CollectionConverters._
      val scalaSet = parameters.asScala.toSet
      scalaSet.size match {
        case 0 => ""
        case 1 => s"?${scalaSet.head.toQueryString}"
        case s if (s > 1) => s"?${scalaSet.map(_.toQueryString).mkString("&")}"
      }
    }
  }

  final case class PageSizeParameter(value: Int) extends VoxboneQueryParameter {
    override def getQueryName = "pageSize"
    override def getQueryValue: String = s"$value"
  }
  final case class PageNumberParameter(value: Int) extends VoxboneQueryParameter {
    override def getQueryName = "pageNumber"
    override def getQueryValue: String = s"$value"
  }

}
