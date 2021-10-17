package no.nav.pensjon.vtp.snitch

import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/snitch")
class SnitchController(
    private val snitchService: SnitchService,
    private val requestResponseRepository: RequestResponseRepository,
) {
    @GetMapping("/preferences")
    fun preferences() = snitchService.preferences()

    @GetMapping("/ignoredPaths")
    fun ignoredPaths(): Set<String> = snitchService.preferences().ignoredPaths

    @PutMapping("/ignoredPaths/**")
    fun ignorePath(request: HttpServletRequest) {
        val path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
        println("path = $path")
        snitchService.ignorePath(path.removePrefix("/snitch/ignoredPaths"))
    }

    @DeleteMapping("/requestResponses")
    fun dropRequestResponses() {
        requestResponseRepository.deleteAll()
    }
}
