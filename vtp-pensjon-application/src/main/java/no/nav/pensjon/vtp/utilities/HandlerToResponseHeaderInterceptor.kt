package no.nav.pensjon.vtp.utilities

import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Adds the handler toString of the MVC Handler to the HTTP Response Header
 * so that it can be added to the access log. This is to make it easier to
 * figure out which controller method that was called. For example for
 * use in the access log.
 */
class HandlerToResponseHeaderInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        response.addHeader("x-vtp-handler", handler.toString())
        return true
    }
}
