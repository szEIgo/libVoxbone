package dk.nuuday.digitalCommunications.voxbone.jsonModel


import dk.nuuday.digitalCommunications.voxbone.models._

trait ServiceAndCoverageProtocol extends SharedJsonProtocol {

  implicit val countryNameFormat = jsonFormat1(CountryName)
  implicit val phoneCodeFormat = jsonFormat1(PhoneCode)
  implicit val hasStatesFormat = jsonFormat1(HasStates)
  implicit val hasRegulationRequirementFormat = jsonFormat1(HasRegulationRequirement)
  implicit val countriesFormat = jsonFormat5(Countries)
  implicit val tmpCountriesFormat = jsonFormat5(DtoCountries)
  implicit val tmpCoverageByCountryResponseFormat = jsonFormat2(DtoCoverageByCountryResponse)

}
