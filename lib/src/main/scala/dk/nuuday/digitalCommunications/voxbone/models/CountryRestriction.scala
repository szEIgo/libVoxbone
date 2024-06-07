package dk.nuuday.digitalCommunications.voxbone.models

case class CountryRestriction(countryCodeA3: CountryCodesA3Enum, restrictionType: String, restrictionMessage: String)

case class CountryRestrictions(restrictions: List[CountryRestriction])




