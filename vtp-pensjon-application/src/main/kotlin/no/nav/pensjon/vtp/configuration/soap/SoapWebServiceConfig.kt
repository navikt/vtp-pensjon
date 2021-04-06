package no.nav.pensjon.vtp.configuration.soap

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.snitch.JAXWSMethodInspectingInvoker
import org.apache.cxf.Bus
import org.apache.cxf.jaxws.EndpointImpl
import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import org.apache.cxf.service.invoker.Invoker
import org.apache.cxf.service.invoker.SingletonFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class SoapWebServiceConfig(private val applicationContext: ApplicationContext, private val bus: Bus) {
    @PostConstruct
    fun registerSoapServices() {
        applicationContext.getBeansWithAnnotation(SoapService::class.java)
            .values
            .parallelStream()
            .forEach { service ->
                service.javaClass.getAnnotation(SoapService::class.java)
                    ?.path?.forEach {
                        EndpointImpl(
                            bus,
                            service,
                            JaxWsMethodInspectingInvokerServerFactoryBean()
                        ).publish(it)
                    }
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
