package no.nav.pensjon.vtp.configuration.soap

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "StelvioContext", namespace = "http://www.nav.no/StelvioContextPropagation")
@XmlAccessorType(XmlAccessType.FIELD)
data class StelvioContextData(
    @XmlElement(name = "applicationId")
    val applicationId: String? = null,

    @XmlElement(name = "correlationId")
    val correlationId: String? = null,

    @XmlElement(name = "languageId")
    val languageId: String? = null,

    @XmlElement(name = "userId")
    val userId: String? = null
)
