package dk.nuuday.digitalCommunications.voxbone.jsonModel
import dk.nuuday.digitalCommunications.voxbone.models.{CountryRestriction, CountryRestrictions}

trait CountryRestrictionProtocol extends SharedJsonProtocol {
  implicit val countryRestrictionFormat = jsonFormat3(CountryRestriction)
  implicit val countryRestrictionsFormat = jsonFormat1(CountryRestrictions)
}
