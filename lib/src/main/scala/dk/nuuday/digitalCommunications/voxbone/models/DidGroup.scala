package dk.nuuday.digitalCommunications.voxbone.models
import java.util.Optional
case class StateId(id: Int)
case class RateCenter(value: String)
case class Stock(value: Int)
case class Setup100(value: Int)
case class Monthly100(value: Int)
case class Available(value: Boolean)
case class ProofRequired(value: Boolean)
case class DidGroupRegulationRequirement(addressType: AddressTypeEnum, proofRequired: ProofRequired)
case class Feature(featureId: Int, name: String, description: String)
case class Features(values: java.util.List[Feature])

case class DidGroup(
    didGroupId: DidGroupId,
    countryCodesA3Enum: CountryCodesA3Enum,
    stateId: Optional[StateId],
    didTypeEnum: DidTypeEnum,
    cityName: Optional[CityName],
    areaCode: AreaCode,
    rateCenter: Optional[RateCenter],
    stock: Stock,
    setup100: Setup100,
    monthly100: Monthly100,
    available: Available,
    didGroupRegulationRequirement: DidGroupRegulationRequirement,
    features: Features)


case class DidGroupsResponse(didGroups: java.util.List[DidGroup], resultCount: Int)