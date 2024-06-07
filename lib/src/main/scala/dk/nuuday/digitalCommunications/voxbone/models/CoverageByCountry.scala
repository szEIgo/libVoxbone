package dk.nuuday.digitalCommunications.voxbone.models

case class CoverageByCountry()
case class CountryName(value: String)
case class PhoneCode(value: Long)
case class HasStates(hasStates: Boolean)
case class HasRegulationRequirement(hasRegulationRequirement: Boolean)
case class Countries(
    countryCodesA3Enum: CountryCodesA3Enum,
    countryName: CountryName,
    phoneCode: PhoneCode,
    hasStates: HasStates,
    hasRegulationRequirement: HasRegulationRequirement)

case class DtoCountries(
    countryCodeA3: String,
    countryName: String,
    phoneCode: Long,
    hasStates: Boolean,
    hasRegulationRequirement: Boolean) {
  def toCountries(): Countries = Countries(CountryCodesA3Enum.withName(countryCodeA3),CountryName(countryName),PhoneCode(phoneCode),HasStates(hasStates),HasRegulationRequirement(hasRegulationRequirement))
}
case class DtoCoverageByCountryResponse(countries: Vector[DtoCountries], resultCount: Long) {
  import scala.jdk.CollectionConverters._
  def toCoverageByCountryResponse: CoverageByCountryResponse =
    CoverageByCountryResponse(
      countries.map(_.toCountries()).asJava,
      resultCount
    )
}
case class CoverageByCountryResponse(countries: java.util.List[Countries], resultCount: Long)
