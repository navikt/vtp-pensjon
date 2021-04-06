package no.nav.pensjon.vtp.configuration

import no.nav.pensjon.vtp.util.CORRELATION_ID_HEADER_NAME
import no.nav.pensjon.vtp.util.withCorrelationId
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class CorrelationIdFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain) {
        val correlationId: String? = findCorrelationId(request)

        correlationId
            ?.let {
                withCorrelationId(correlationId) {
                    if (response is HttpServletResponse) {
                        response.addHeader(CORRELATION_ID_HEADER_NAME, correlationId)
                    }
                    filterChain.doFilter(request, response)
                }
            }
            ?: filterChain(filterChain).doFilter(request, response)
    }

    private fun findCorrelationId(request: ServletRequest?): String? {
        return if (request is HttpServletRequest) {
            request.getHeader("nav-call-id")
        } else {
            null
        }
    }

    private fun filterChain(filterChain: FilterChain) = filterChain
}
