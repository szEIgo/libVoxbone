package dk.nuuday.digitalCommunications.voxbone.services.java
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.NumberInventoryJava
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.QueryParameters

import dk.nuuday.digitalCommunications.voxbone.models._

trait VoxboneClientInterface {

  def listNumberInventory(queryParameters: QueryParameters): NumberInventoryJava

  def createCart(customerReference: CustomerReference, description: Description): CartJava

  def addProductsToCart(
      cartIdentifier: CartIdentifier,
      didCartItem: DidCartItem,
      capacityCartItem: CapacityCartItem,
      credeitPackageCartItem: CreditPackageCartItem): Status

  def destroyACartAndAllItsContent(cartIdentifier: CartIdentifier): Status

  def removeProductsFromCartOrChangeQuantity(
      cartIdentifier: CartIdentifier,
      orderProductId: OrderProductId,
      quantity: Quantity): Status

  def checkoutCart(cartIdentifier: CartIdentifier): CheckoutCartDtoJava

  def listVoiceUris(queryParameters: QueryParameters): VoiceUrisJava

  def getASpecificVoiceUriById(voiceUriId: VoiceUriId): VoiceUrisJava

  def changeVoiceUri(voiceUri: VoiceUri): ChangeVoiceResponse

  def createVoiceUri(newVoiceUri: NewVoiceUri): VoiceUriResponse

  def searchForCoverageByCountry(queryParameters: QueryParameters): CoverageByCountryResponse

  def listCapacityGroups(queryParameters: QueryParameters): CapacityGroupsResponse

  def getSpecificCapacityGroupById(capacityGroupId: CapacityGroupId): CapacityGroupsResponse

  def createANewCapacityGroup(capacityGroup: CapacityGroup): CapacityGroupResponse

  def updateExistingCapacityGroup(
      capacityGroupId: CapacityGroupId,
      capacityGroup: CapacityGroup): CapacityGroupResponse

  def removeACapacityGroup(capacityGroupId: CapacityGroupId): Status

  def applySetOfConfigurationToAdid(didConfiguration: DidConfiguration): DidConfigurationResponse

  def getVoxboneConnectivityInformation: Pops

  def listAvailableTrunks(): Trunks

  def listDidGroups(queryParameters: QueryParameters): DidGroupsResponse

  def listCountryRestrictions(country : CountryCodesA3Enum): CountryRestrictions

}
