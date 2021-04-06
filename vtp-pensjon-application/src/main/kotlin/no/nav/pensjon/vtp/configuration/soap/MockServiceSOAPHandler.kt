package no.nav.pensjon.vtp.configuration.soap

import no.nav.pensjon.vtp.util.CORRELATION_ID_HEADER_NAME
import no.nav.pensjon.vtp.util.resetCorrelationId
import no.nav.pensjon.vtp.util.setCorrelationId
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.xml.bind.JAXBContext
import javax.xml.namespace.QName
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPHandler
import javax.xml.ws.handler.soap.SOAPMessageContext

fun addCorrelationIdToServletResponseHeader(correlationId: String) {
    (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)
        ?.let { it.response?.addHeader(CORRELATION_ID_HEADER_NAME, correlationId) }
}

class MockServiceSOAPHandler : SOAPHandler<SOAPMessageContext> {
    private val stelvioContextName = QName(
        "http://www.nav.no/StelvioContextPropagation",
        "StelvioContext"
    )

    private var jaxbContext = JAXBContext.newInstance(StelvioContextData::class.java)

    override fun getHeaders() = setOf(
        QName(
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
            "Security"
        ),
        stelvioContextName
    )

    override fun handleMessage(context: SOAPMessageContext): Boolean {
        val outbound = context[SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY] as Boolean?
        if (outbound == false) {
            findCorrelationId(context)
                ?.let {
                    setCorrelationId(it)
                    addCorrelationIdToServletResponseHeader(it)
                }
        }

        return true
    }

    private fun findCorrelationId(context: SOAPMessageContext): String? =
        context.getHeaders(stelvioContextName, jaxbContext, true)
            .filterIsInstance<StelvioContextData>()
            .firstOrNull()
            ?.correlationId

    override fun handleFault(context: SOAPMessageContext) = true

    override fun close(context: MessageContext) = resetCorrelationId()
}
