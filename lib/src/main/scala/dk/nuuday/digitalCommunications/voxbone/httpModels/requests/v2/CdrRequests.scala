package dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2
import java.net.URLEncoder
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import dk.nuuday.digitalCommunications.voxbone.authentication.{
  ApikeyAuthentication,
  BasicAuthenticationCredentials
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequests.{
  QueryParameter,
  QueryParameters,
  VoxboneQueryParameter
}
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.VoxboneHttpRequestsTrait.CdrRequestsTrait
import dk.nuuday.digitalCommunications.voxbone.httpModels.requests.v2.CdrRequests.CdrParameters
import dk.nuuday.digitalCommunications.voxbone.models.{
  CallTypeEnum,
  CountryCodesA3Enum,
  E164,
  ServerInfo
}

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

final class CdrRequests()(
    implicit
    override val serverInfo: ServerInfo,
    override val apikey: ApikeyAuthentication,
    override val voxboneBasicCredentials: BasicAuthenticationCredentials)
    extends CdrRequestsTrait {
  def listCDRsWithFilters(cdrParameters: CdrParameters): HttpRequest = {
    /*
 'https://api.voxbone.com/v2/cdr/calls?from=2021-05-10T12:00:00.000&to=2021-05-10T23:00:00.000'
     */
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model
        .Uri(
          s"$sslPrefix${serverInfo.address}/v2/cdr/calls${cdrParameters.toQueryParameters().toCombinedQueryString}"),
      Seq(
        RawHeader("apiKey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  /**
   * If the request is paginated then the link to the next set of data
   * will be present in the previous response within the data.links list,
   * attribute rel named "next"
   */
  def listCDRsWithFiltersPaginationRequest(nextLink: String): HttpRequest = {
    /*
 'https://api.voxbone.com/v2/cdr/calls?from=2021-11-29T12:35:28.768&to=2021-11-30T13:35:28.768&offset=20&limit=20'
     */
    val request = HttpRequest(
      HttpMethods.GET,
      akka.http.scaladsl.model.Uri(nextLink),
      Seq(
        RawHeader("apiKey", apikey.get),
        Authorization(akkaBasicAuthenticationCredentials),
        RawHeader("Accept", "application/json"))
    )
    request
  }

  /*
  'https://api.voxbone.com/v2/cdr/calls'
  */
  val uriMinusQueryParams: String = s"$sslPrefix${serverInfo.address}/v2/cdr/calls"

}
object CdrRequests {
  abstract class CdrParameter extends VoxboneQueryParameter

  case class From(value: ZonedDateTime){
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd::HH:mm:ss")
    override val toString = value.format(formatter).replace("::", "T")+".000"
  }
  case class To(value: ZonedDateTime) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd::HH:mm:ss")
    override val toString = value.format(formatter).replace("::", "T")+".000"
  }
  case class Id(value: String)
  case class CountryCodesA3(values: Vector[CountryCodesA3Enum])
  case class E164Dids(values: Vector[E164])
  case class CallType(value: CallTypeEnum)
  case class Offset(value: Int)
  case class Limit(value: Int)
  case class Anonymized(value: Boolean)

  final case class FromParameter(value: From) extends CdrParameter {
    override def getQueryName: String = "from"
    override def getQueryValue: String = value.toString
  }

  final case class ToParameter(value: To) extends CdrParameter {
    override def getQueryName: String = "to"
    override def getQueryValue: String = value.toString
  }

  final case class IdParameter(value: Id) extends CdrParameter {
    override def getQueryName: String = "id"
    override def getQueryValue: String = s"${URLEncoder.encode(value.value, "UTF-8")}"
  }

  final case class CountryCodeA3Parameter(values: CountryCodesA3) extends CdrParameter {
    override def getQueryName: String = "countryCodeA3"
    override def getQueryValue: String =
      s"${URLEncoder.encode(values.values.mkString(","), "UTF-8")}"
  }

  case class CdrParameters(
                            from: Option[FromParameter] = None,
                            to: Option[ToParameter] = None,
                            id: Option[IdParameter] = None,
                            countryCodesA3: Option[CountryCodeA3Parameter] = None,
                            e164: Option[E164Parameter] = None,
                            callType: Option[CallTypeParameter] = None,
                            offset: Option[OffsetParameter] = None,
                            limit: Option[LimitParameter] = Some(LimitParameter(Limit(1000))),
                            anonymized: Option[AnonymizedParameter] = None
                          ) {

    def toQueryParameters(): QueryParameters = {

      import scala.jdk.CollectionConverters._
      QueryParameters(
        this.productIterator
          .asInstanceOf[Iterator[Option[CdrParameter]]]
          .flatten
          .toSet
          .map(QueryParameter)
          .asJava)
    }
  }

  final case class E164Parameter(values: E164Dids) extends CdrParameter {
    override def getQueryName: String = "e164"
    override def getQueryValue: String =
      s"${URLEncoder.encode(values.values.mkString(","), "UTF-8")}"
  }

  final case class CallTypeParameter(value: CallType) extends CdrParameter {
    override def getQueryName: String = "callType"
    override def getQueryValue: String = s"${URLEncoder.encode(value.value.entryName, "UTF-8")}"
  }

  final case class OffsetParameter(value: Offset) extends CdrParameter {
    override def getQueryName: String = "offset"
    override def getQueryValue: String = s"${URLEncoder.encode(value.value.toString, "UTF-8")}"
  }
  case class LimitParameter(value: Limit) extends CdrParameter {
    override def getQueryName: String = "limit"
    override def getQueryValue: String = s"${URLEncoder.encode(value.value.toString, "UTF-8")}"
  }

  case class AnonymizedParameter(value: Anonymized) extends CdrParameter {
    override def getQueryName: String = "anonymized"
    override def getQueryValue: String = s"${URLEncoder.encode(value.value.toString, "UTF-8")}"
  }

}
