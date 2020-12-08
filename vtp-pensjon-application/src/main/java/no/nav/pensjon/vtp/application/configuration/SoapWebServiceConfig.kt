package no.nav.pensjon.vtp.application.configuration

import org.apache.cxf.Bus
import javax.annotation.PostConstruct
import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.utilities.description
import org.apache.cxf.jaxws.EndpointImpl
import org.apache.cxf.jaxws.JAXWSMethodInvoker
import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import org.apache.cxf.message.Exchange
import org.apache.cxf.service.invoker.Factory
import org.apache.cxf.service.invoker.Invoker
import org.apache.cxf.service.invoker.SingletonFactory
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.Exception
import java.lang.reflect.Method

@Configuration
class SoapWebServiceConfig(private val applicationContext: ApplicationContext, private val bus: Bus) {
    @PostConstruct
    fun registerSoapServices() {
        applicationContext.getBeansWithAnnotation(SoapService::class.java)
                .values
                .parallelStream()
                .forEach { service ->
                    service.javaClass.getAnnotation(SoapService::class.java)
                            ?.path?.forEach { EndpointImpl(bus, service, JaxWsMethodInspectingInvokerServerFactoryBean()).publish(it) }
                }
    }
}

class JaxWsMethodInspectingInvokerServerFactoryBean : JaxWsServerFactoryBean() {
    override fun createInvoker(): Invoker {
        return if (serviceBean == null) {
            JAXWSMethodInspectingInvoker(SingletonFactory(serviceClass))
        } else JAXWSMethodInspectingInvoker(SingletonFactory(serviceBean))
    }
}

/**
 * Adds the x-vtp-handler to make it easy to figure which java method
 * that was invoked. Logs any exception that occurs during invocation
 * for easier development and debugging.
 */
class JAXWSMethodInspectingInvoker(factory: Factory) : JAXWSMethodInvoker(factory) {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(exchange: Exchange, serviceObject: Any, method: Method, params: List<Any>): Any {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes is ServletRequestAttributes) {
            requestAttributes.response?.addHeader("x-vtp-handler", description(method))
        }

        return try {
            super.invoke(exchange, serviceObject, method, params)
        } catch (e: Exception) {
            logger.error("Error invoking soap web service", e)
            throw e
        }
    }
}
