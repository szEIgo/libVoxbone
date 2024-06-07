package dk.nuuday.digitalCommunications.voxbone.models

import scala.jdk.CollectionConverters._

case class CartIdentifier(value: Long)
case class CustomerReference(value: String)

case class DateAdded(value: String)

case class OrderProducts(list: Vector[Product]) {
  def toJava: OrderProductsJava = OrderProductsJava(list.asJava)
}
case class Zone(value: String)
case class CreditPackageId(value: Long)
case class DidCartItem(didGroupId: DidGroupId, quantity: Quantity)
case class DidCartItemDto(didCartItem: DidCartItem)
case class CapacityCartItem(zone: Zone, quantity: Quantity)
case class CapacityCartItemDto(capacityCartItem: CapacityCartItem)
case class CreditPackageCartItem(creditPackageId: CreditPackageId, quantity: Quantity)
case class CreditPackageCartItemDto(creditPackageCartItem: CreditPackageCartItem)
case class OrderProductsJava(list: java.util.List[Product])
case class Cart(
    cartIdentifier: CartIdentifier,
    customerReference: CustomerReference,
    description: Description,
    dateAdded: DateAdded,
    orderProducts: OrderProducts) {
  def toJava: CartJava =
    CartJava(cartIdentifier, customerReference, description, dateAdded, orderProducts.toJava)
}

case class CartDto(cart: Cart)

case class CartJava(
    cartIdentifier: CartIdentifier,
    customerReference: CustomerReference,
    description: Description,
    dateAdded: DateAdded,
    orderProducts: OrderProductsJava)

case class ProductCheckoutItem(
    productType: ProductType,
    status: Status,
    orderReference: OrderReference,
    message: String)
case class ProductCheckoutList(list: Vector[ProductCheckoutItem])
case class ProductCheckOutListJava(list: java.util.List[ProductCheckoutItem])
case class CheckoutCartDtoJava(status: Status, productCheckoutList: ProductCheckOutListJava)
case class CheckoutCartDto(status: Status, productCheckoutList: ProductCheckoutList) {
  def toJava: CheckoutCartDtoJava =
    CheckoutCartDtoJava(status, ProductCheckOutListJava(productCheckoutList.list.asJava))

}
