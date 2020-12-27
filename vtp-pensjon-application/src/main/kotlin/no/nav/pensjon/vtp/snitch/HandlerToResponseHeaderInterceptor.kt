package no.nav.pensjon.vtp.snitch

import no.nav.pensjon.vtp.utilities.addVtpHandlerMetaData
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Adds the VTP Handler Meta Data to the HTTP Response Header for calls
 * processed by Spring MVC.
 *
 * This is later used by the access log and the Snitch.
 */
class HandlerToResponseHeaderInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            addVtpHandlerMetaData(response, handler.method)
        } else {
            addVtpHandlerMetaData(response, handler.toString())
        }

        return true
    }
}
