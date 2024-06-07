package dk.nuuday.digitalCommunications.voxbone.jsonModel
import dk.nuuday.digitalCommunications.voxbone.models.{CapacityCartItem, CapacityCartItemDto, Cart, CartDto, CartIdentifier, CheckoutCartDto, CreditPackageCartItem, CreditPackageCartItemDto, CreditPackageId, CustomerReference, DateAdded, DidCartItem, DidCartItemDto, OrderProductId, OrderProducts, OrderReference, Product, ProductCheckoutItem, ProductCheckoutList, ProductDescription, ProductType, Quantity, Status, StatusDto, Zone}
import spray.json._

trait CartJsonProtocol extends SharedJsonProtocol {

  implicit object OrderReferenceFormat extends RootJsonFormat[OrderReference] {
    override def read(json: JsValue): OrderReference = json match {
      case JsString(rawString) => OrderReference(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }

    override def write(obj: OrderReference): JsValue = obj.value.toJson
  }

  implicit object CustomerReferenceFormat extends RootJsonFormat[CustomerReference] {
    override def read(json: JsValue): CustomerReference = json match {
      case JsString(rawString) => CustomerReference(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }

    override def write(obj: CustomerReference): JsValue = obj.value.toJson
  }

  implicit object CartIdentifierFormat extends RootJsonFormat[CartIdentifier] {
    override def read(json: JsValue): CartIdentifier = json match {
      case JsNumber(rawNumber) => CartIdentifier(rawNumber.toLongExact)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: CartIdentifier): JsValue = obj.value.toJson
  }



  implicit object ProductTypeFormat extends RootJsonFormat[ProductType] {
    override def read(json: JsValue): ProductType = json match {
      case JsString(rawString) => ProductType.withName(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: ProductType): JsValue = obj.entryName.toJson
  }

  implicit object QuantityFormat extends RootJsonFormat[Quantity] {
    override def read(json: JsValue): Quantity = json match {
      case JsNumber(rawNumber) => Quantity(rawNumber.toLongExact)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: Quantity): JsValue = obj.value.toJson
  }
  implicit object ProductDescriptionFormat extends RootJsonFormat[ProductDescription] {
    override def read(json: JsValue): ProductDescription = json match {
      case JsString(rawString) => ProductDescription(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: ProductDescription): JsValue = obj.description.toJson
  }

  implicit object OrderProductIdFormat extends RootJsonFormat[OrderProductId] {
    override def read(json: JsValue): OrderProductId = json match {
      case JsNumber(rawNumber) => OrderProductId(rawNumber.toLongExact)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: OrderProductId): JsValue = obj.value.toJson
  }

  implicit val productFormat = jsonFormat5(Product)

  implicit object OrderProductsJavaFormat extends RootJsonFormat[OrderProducts] {
    override def read(json: JsValue): OrderProducts = json match {
      case JsNull => OrderProducts(Vector.empty)
      case JsArray(values) => OrderProducts(values.map(_.convertTo[Product]))
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: OrderProducts): JsValue =
      if (obj.list.isEmpty) JsNull else obj.list.toJson
  }

//  implicit object OrderProductsFormat extends RootJsonFormat[OrderProducts] {
//    override def read(json: JsValue): OrderProducts = json match {
//      case JsArray(raw) => OrderProducts(rawString)
//      case JsNull => OrderProducts(Vector.empty)
//      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
//    }
//    override def write(obj: OrderProducts): JsValue = obj.value.toJson
//  }

  implicit object DateAddedFormat extends RootJsonFormat[DateAdded] {
    override def read(json: JsValue): DateAdded = json match {
      case JsString(rawString) => DateAdded(rawString)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: DateAdded): JsValue = obj.value.toJson
  }

  implicit val cartJsonFormat = jsonFormat5(Cart)
  implicit val cartDtoJsonFormat = jsonFormat1(CartDto)

  implicit object ZoneFormat extends RootJsonFormat[Zone] {
    override def read(json: JsValue): Zone = json match {
      case JsString(raw) => Zone(raw)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: Zone): JsValue = obj.value.toJson
  }

  implicit object CreditPackageIdFormat extends RootJsonFormat[CreditPackageId] {
    override def read(json: JsValue): CreditPackageId = json match {
      case JsNumber(raw) => CreditPackageId(raw.toLongExact)
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: CreditPackageId): JsValue = obj.value.toJson
  }

  implicit val didCartItemFormat = jsonFormat2(DidCartItem)
  implicit val didCartItemDtoFormat = jsonFormat1(DidCartItemDto)

  implicit val capacityCartItemFormat = jsonFormat2(CapacityCartItem)
  implicit val capacityCartItemDtoFormat = jsonFormat1(CapacityCartItemDto)

  implicit val creditPackageCartItemFormat = jsonFormat2(CreditPackageCartItem)
  implicit val creditPackageCartItemDtoFormat = jsonFormat1(CreditPackageCartItemDto)


  implicit val statusDtoFormat = jsonFormat1(StatusDto)

  implicit object ProductCheckoutItemFormat extends RootJsonFormat[ProductCheckoutItem] {
    override def read(json: JsValue): ProductCheckoutItem =
      json match {
        case JsObject(map) =>
          ProductCheckoutItem(
            map("productType").convertTo[ProductType],
            map("status").convertTo[Status],
            map("orderReference").convertTo[OrderReference],
            map("message").convertTo[String]
          )
        case other => throw DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
      }
    override def write(obj: ProductCheckoutItem): JsValue =
      JsObject(
        "productType" -> obj.productType.toJson,
        "status" -> obj.status.toJson,
        "orderReference" -> obj.orderReference.toJson,
        "message" -> obj.message.toJson)
  }
  implicit val productCheckoutListFormat = jsonFormat1(ProductCheckoutList)

  implicit object checkOutCartDtoFormat extends RootJsonFormat[CheckoutCartDto] {

    override def read(json: JsValue): CheckoutCartDto = json match {
      case JsObject(map) =>
        CheckoutCartDto(
          map("status").convertTo[Status],
          map("productCheckoutList").convertTo[ProductCheckoutList])
      case other => throw new DeserializationException(s"Could not find JsType ${Thread.currentThread.getStackTrace()(2).getClassName}().${Thread.currentThread.getStackTrace()(2).getMethodName}() $other")
    }
    override def write(obj: CheckoutCartDto): JsValue =
      JsObject(
        "status" -> obj.status.toJson,
        "productCheckoutList" -> obj.productCheckoutList.toJson)
  }

}
