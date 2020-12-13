package no.nav.pensjon.vtp.miscellaneous

import javax.xml.namespace.QName
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPHandler
import javax.xml.ws.handler.soap.SOAPMessageContext

class MockServiceSOAPHandler : SOAPHandler<SOAPMessageContext> {
    override fun getHeaders() = setOf(
        QName(
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
            "Security"
        )
    )

    override fun handleMessage(context: SOAPMessageContext) = true

    override fun handleFault(context: SOAPMessageContext) = true

    override fun close(context: MessageContext) = Unit
}
