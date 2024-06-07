package dk.nuuday.digitalCommunications.voxbone.services.scala
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.InventoryRequests.NumberInventory
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.QueryParameters
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests.CdrParameters
import dk.nuuday.digitalCommunications.voxbone.models._

import scala.concurrent.Future

trait VoxboneClientInterface {
  def listNumberInventory(queryParameters: QueryParameters): Future[NumberInventory]
  def createCart(customerReference: CustomerReference, description: Description): Future[Cart]
  def addProductsToCart(
      cartIdentifier: CartIdentifier,
      didCartItem: DidCartItem,
      capacityCartItem: CapacityCartItem,
      credeitPackageCartItem: CreditPackageCartItem): Future[Status]
  def destroyACartAndAllItsContent(cartIdentifier: CartIdentifier): Future[Status]
  def removeProductsFromCartOrChangeQuantity(
      cartIdentifier: CartIdentifier,
      orderProductId: OrderProductId,
      quantity: Quantity): Future[Status]

  def listCdrsWithFilters(cdrParameters: CdrParameters): Future[List[Cdr]]
}
