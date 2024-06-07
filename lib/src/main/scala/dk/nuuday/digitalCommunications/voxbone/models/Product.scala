package dk.nuuday.digitalCommunications.voxbone.models
import enumeratum._

sealed trait ProductType extends EnumEntry

object ProductType extends Enum[ProductType] {
  val values = findValues
  case object DID extends ProductType
  case object CAPACITY extends ProductType
  case object CREDIT_PACKAGE extends ProductType
}
case class Quantity(value: Long)
case class ProductDescription(description: String)

case class OrderProductId(value: Long)

case class Product(
    orderProductId: OrderProductId,
    productType: ProductType,
    productDescription: ProductDescription,
    quantity: Quantity,
    didGroupid: DidGroupId)
