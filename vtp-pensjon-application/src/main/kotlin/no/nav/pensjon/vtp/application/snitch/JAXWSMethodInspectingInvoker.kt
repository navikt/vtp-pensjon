package no.nav.pensjon.vtp.application.snitch

import org.apache.cxf.jaxws.JAXWSMethodInvoker
import org.apache.cxf.message.Exchange
import org.apache.cxf.service.invoker.Factory
import org.slf4j.LoggerFactory
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Method

/**
 * Adds the VTP Handler Meta Data to the HTTP Response Header for calls
 * processed by Apache CXF.
 *
 * This is later used by the access log and the Snitch.
 */
class JAXWSMethodInspectingInvoker(factory: Factory) : JAXWSMethodInvoker(factory) {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(exchange: Exchange, serviceObject: Any, method: Method, params: List<Any>): Any {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes is ServletRequestAttributes) {
            requestAttributes.response?.let { addVtpHandlerMetaData(it, method) }
        }

        return try {
            super.invoke(exchange, serviceObject, method, params)
        } catch (e: Exception) {
            logger.error("Error invoking soap web service", e)
            throw e
        }
    }
}
