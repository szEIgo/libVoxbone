package dk.nuuday.digitalCommunications.voxbone.httpModels.requests

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials, RawHeader}
import dk.nuuday.digitalCommunications.voxbone.authentication.{ApikeyAuthentication, BasicAuthenticationCredentials}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.QueryParameters
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests.CdrParameters
import dk.nuuday.digitalCommunications.voxbone.models._

trait VoxboneHttpRequestsTrait {
  implicit val serverInfo: ServerInfo
  implicit val apikey: ApikeyAuthentication
  implicit val voxboneBasicCredentials: BasicAuthenticationCredentials
  private[requests] val akkaBasicAuthenticationCredentials: BasicHttpCredentials =
    BasicHttpCredentials(voxboneBasicCredentials.username, voxboneBasicCredentials.password)
  private[requests] def sslPrefix(): String = {
    if (serverInfo.sslEnabled) "https://" else "http://"
  }
}
object VoxboneHttpRequestsTrait {

  trait InventoryRequestsTrait extends VoxboneHttpRequestsTrait {
    def listNumberInventoryRequest(queryParameters: QueryParameters): HttpRequest
    def listDidGroupsRequest(queryParameters: QueryParameters): HttpRequest
    def listSipTrunksRequest(): HttpRequest
    def cancelNumberSubscription(): HttpRequest
    def RemoveNumberFromYourInventoryRequest(): HttpRequest
    def listCountryRestrictions(country : CountryCodesA3Enum): HttpRequest
  }

  trait ServiceAndCoverageRequestsTrait extends VoxboneHttpRequestsTrait {
    def searchForCoverageByCountryRequest(queryParameters: QueryParameters): HttpRequest
    def getVoxboneCoverageByCityAndAreaCode(queryParameters: QueryParameters): HttpRequest
    def getActivationRequirementsAndAvailableAreaCodeWithinACountryRequest(
        countryCodesA3Enum: CountryCodesA3Enum): HttpRequest
    def searchForCoverageByStatesInAmericaAndCanada(
        countryCodesA3Enum: CountryCodesA3Enum): HttpRequest
  }

  trait CdrRequestsTrait extends VoxboneHttpRequestsTrait {
    def listCDRsWithFilters(cdrParameters: CdrParameters): HttpRequest
  }

  trait OrderingRequestTrait extends VoxboneHttpRequestsTrait {
    def createCartRequest(
        customerReference: CustomerReference,
        description: Description): HttpRequest
    def addProductsToCartRequest(
        cartIdentifier: CartIdentifier,
        didCartItem: DidCartItem,
        capacityCartItem: CapacityCartItem,
        credeitPackageCartItem: CreditPackageCartItem): HttpRequest
    def destroyACartAndAllItsContentsRequest(cartIdentifier: CartIdentifier): HttpRequest
    def removeProductsFromCartOrChangeQuantityRequest(
        cartIdentifier: CartIdentifier,
        orderProductId: OrderProductId,
        quantity: Quantity): HttpRequest
    def checkoutCartRequest(cartIdentifier: CartIdentifier): HttpRequest
  }
  trait ConfigurationRequestsTrait extends VoxboneHttpRequestsTrait

  object ConfigurationRequestsTrait {
    trait DidsTrait extends ConfigurationRequestsTrait {
      def applySetOfConfigurationsToADidRequest(didConfiguration: DidConfiguration): HttpRequest
      def getVoxboneConnectivityInformation: HttpRequest
    }
    trait VoiceTrait extends ConfigurationRequestsTrait {
      def listVoiceUriRequest(queryParameters: QueryParameters): HttpRequest
      def getASpecificVoiceUriByIdRequest(voiceUriId: VoiceUriId): HttpRequest
      def changeVoiceUriRequest(voiceUri: VoiceUri): HttpRequest
      def createVoiceUriRequest(newVoiceUri: NewVoiceUri): HttpRequest
    }
    trait SmsTrait extends ConfigurationRequestsTrait {}
    trait FaxTrait extends ConfigurationRequestsTrait {}
    trait NumberConfiguration extends ConfigurationRequestsTrait {
      def listAvailableTrunksRequest(): HttpRequest
    }
    trait CapacityGroupTrait extends ConfigurationRequestsTrait {
      def listCapacityGroupsRequest(queryParameters: QueryParameters): HttpRequest
      def getSpecificCapacityGroupByIdRequest(capacityGroupId: CapacityGroupId): HttpRequest
      def createANewCapacityGroupRequest(capacityGroup: CapacityGroup): HttpRequest
      def updateExistingCapacityGroupRequest(
          capacityGroupId: CapacityGroupId,
          capacityGroup: CapacityGroup): HttpRequest
      def removeACapacityGroupRequest(capacityGroupId: CapacityGroupId): HttpRequest

    }
  }
}
