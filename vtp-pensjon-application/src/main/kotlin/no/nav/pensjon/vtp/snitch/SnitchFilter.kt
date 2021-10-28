package no.nav.pensjon.vtp.snitch

import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import java.time.LocalDateTime.now
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun fullURL(request: HttpServletRequest): String {
    val requestURL = StringBuilder(request.requestURL.toString())
    val queryString = request.queryString
    return if (queryString == null) {
        requestURL.toString()
    } else {
        requestURL.append('?').append(queryString).toString()
    }
}

fun asHeadersMap(
    namesProducer: () -> Iterator<String>,
    headersFunction: (String) -> Iterator<String>
): Map<String, List<String>> {
    val headers = HashMap<String, List<String>>()

    namesProducer().forEach { headerName ->
        val values = ArrayList<String>()

        headersFunction(headerName).forEach { headerValue -> values.add(headerValue) }

        headers[headerName] = values
    }

    return headers
}

@Component
@Order(HIGHEST_PRECEDENCE + 1)
class SnitchFilter(
    private val snitchService: SnitchService,
) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse &&
            (request.servletPath.startsWith("/rest") || request.servletPath.startsWith("/soap"))
        ) {
            val requestWrapper = ContentCachingRequestWrapper(request)
            val responseWrapper = ContentCachingResponseWrapper(response)

            try {
                filterChain.doFilter(requestWrapper, responseWrapper)

                snitchService.save(requestResponse(requestWrapper, responseWrapper))

                responseWrapper.copyBodyToResponse()
            } catch (e: Exception) {
                val stringWriter = StringWriter()
                e.printStackTrace(PrintWriter(stringWriter))
                snitchService.save(
                    requestResponse(
                        requestWrapper,
                        responseWrapper
                    ).copy(exception = e.message, stackTrace = stringWriter.toString())
                )
                throw e
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private fun requestResponse(
        requestWrapper: ContentCachingRequestWrapper,
        responseWrapper: ContentCachingResponseWrapper
    ) = RequestResponse(
        timestamp = now(),
        path = URL(requestWrapper.requestURL.toString()).path,
        queryString = requestWrapper.queryString,
        url = fullURL(requestWrapper),
        method = requestWrapper.method,
        status = responseWrapper.status,
        handler = findHandler(responseWrapper),
        handlerClass = findClass(responseWrapper),
        handlerMethod = findMethod(responseWrapper),
        exception = null,
        stackTrace = null,

        request = Payload(
            headers = asHeadersMap(
                { requestWrapper.headerNames.asIterator() },
                { header -> requestWrapper.getHeaders(header).asIterator() }
            ),
            contentLength = requestWrapper.contentLength,
            contentType = requestWrapper.contentType,
            content = requestWrapper.contentAsByteArray
        ),

        response = Payload(
            headers = asHeadersMap(
                { responseWrapper.headerNames.iterator() },
                { header -> responseWrapper.getHeaders(header).iterator() }
            ),
            contentLength = responseWrapper.contentSize,
            contentType = responseWrapper.contentType,
            content = responseWrapper.contentAsByteArray
        )
    )
}
