package no.nav.pensjon.vtp.application.configuration;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JAXWSMethodInvoker;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.service.invoker.Factory;
import org.apache.cxf.service.invoker.Invoker;
import org.apache.cxf.service.invoker.SingletonFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nav.pensjon.vtp.core.annotations.SoapService;

@Configuration
public class SoapWebServiceConfig {
    private final ApplicationContext applicationContext;
    private final Bus bus;

    public SoapWebServiceConfig(ApplicationContext applicationContext, Bus bus) {
        this.applicationContext = applicationContext;
        this.bus = bus;
    }

    @PostConstruct
    public void registerSoapServices() {
        applicationContext.getBeansWithAnnotation(SoapService.class)
                .values()
                .stream().parallel()
                .forEach(service ->
                        stream(ofNullable(service.getClass().getAnnotation(SoapService.class))
                                .map(SoapService::path)
                                .orElseThrow(() -> new RuntimeException("Missing @" + SoapService.class.getSimpleName() + " on class " + service.getClass().getSimpleName())))
                                .forEach(path -> new EndpointImpl(bus, service, new JaxWsMethodInspectingInvokerServerFactoryBean()).publish(path))
                );
    }

    private static class JaxWsMethodInspectingInvokerServerFactoryBean extends JaxWsServerFactoryBean {
        @Override
        protected Invoker createInvoker() {
            if (getServiceBean() == null) {
                return new JAXWSMethodInspectingInvoker(new SingletonFactory(getServiceClass()));
            }
            return new JAXWSMethodInspectingInvoker(getServiceBean());
        }
    }

    /**
     * Adds the x-vtp-handler to make it easy to figure which java method
     * that was invoked. Logs any exception that occurs during invocation
     * for easier development and debugging.
     */
    private static class JAXWSMethodInspectingInvoker extends JAXWSMethodInvoker {
        private final Logger logger = getLogger(getClass());

        public JAXWSMethodInspectingInvoker(Object bean) {
            super(bean);
        }

        public JAXWSMethodInspectingInvoker(Factory factory) {
            super(factory);
        }

        @Override
        protected Object invoke(Exchange exchange, Object serviceObject, Method m, List<Object> params) {
            getHttpServletResponse()
                    .ifPresent(response -> response.addHeader("x-vtp-handler", formatHandlerString(m)));

            try {
                return super.invoke(exchange, serviceObject, m, params);
            } catch (Exception e) {
                logger.error("Error invoking soap web service", e);
                throw e;
            }
        }

        @NotNull
        private String formatHandlerString(Method method) {
            final String classCanonicalName = method.getDeclaringClass().getCanonicalName();
            final String methodName = method.getName();
            final String parameters = join(",", (Iterable<String>) stream(method.getParameterTypes()).map(Class::getSimpleName)::iterator);
            return classCanonicalName + "#" + methodName + "(" + parameters + ")";
        }

        @NotNull
        private Optional<HttpServletResponse> getHttpServletResponse() {
            return ofNullable(RequestContextHolder.getRequestAttributes())
                    .filter(ServletRequestAttributes.class::isInstance)
                    .map(ServletRequestAttributes.class::cast)
                    .map(ServletRequestAttributes::getResponse);
        }
    }
}
