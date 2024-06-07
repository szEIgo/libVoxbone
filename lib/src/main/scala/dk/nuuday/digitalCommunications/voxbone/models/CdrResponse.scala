package dk.nuuday.digitalCommunications.voxbone.models

import java.time.ZonedDateTime

case class Carrier(
    avgMos: String,
    codec: String,
    jitter: String,
    packetLoss: String,
    maxMos: Option[String],
    minMos: Option[String],
    rtt: String
)
case class Quality(
    carrier: Carrier,
    customer: Carrier
)

case class Cdr(
                id: String,
                callType: Option[String],
                capacityGroupId: Option[Int],
                capacityGroupName: Option[String],
                countDidCalls: Option[Int],
                currency: Option[String],
                destinationCountry: Option[String],
                destinationNumber: String,
                dialStatus: Option[String],
                direction: String,
                duration: Long,
                e164: String,
                end: ZonedDateTime,
                endStatus: String,
                ip: Option[String],
                uri: Option[String],
                numberType: Option[String],
                onNet: Boolean,
                originCountry: Option[String],
                originationNumber: String,
                pop: String,
                ppe: Option[String],
                ppm: Option[String],
                result: String,
                servicePlan: Option[String],
                serviceType: String,
                start: ZonedDateTime,
                totalCost: Option[String],
                trunkId: Option[Int],
                trunkName: Option[String],
                zeroRated: Boolean,
                quality: Option[Quality]
)
case class Link(
    rel: String,
    href: String,
    hreflang: Option[String],
    media: Option[String],
    deprecation: Option[String],
    templated: Option[Boolean],
    title: Option[String],
    `type`: Option[String]
)
case class Pagination(
    offset: Int,
    limit: Int,
    total: Long
)
case class CdrResponse(
    data: List[Cdr],
    links: List[Link],
    pagination: Pagination,
    status: String
)
