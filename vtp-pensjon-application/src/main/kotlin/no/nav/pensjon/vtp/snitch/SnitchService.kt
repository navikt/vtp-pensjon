package no.nav.pensjon.vtp.snitch

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class SnitchService(
    private val requestResponseRepository: RequestResponseRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val snitchPreferencesRepository: SnitchPreferencesRepository,
) {
    fun save(requestResponse: RequestResponse) {
        if (!preferences().isIgnored(requestResponse)) {
            requestResponseRepository.save(requestResponse)
                .also {
                    simpMessagingTemplate.convertAndSend("/topic/snitch", it)
                }
        }
    }

    fun preferences() = snitchPreferencesRepository.findById("default").orElse(null) ?: SnitchPreferences()

    fun ignorePath(path: String) {
        val current = preferences()
        snitchPreferencesRepository.save(current.copy(ignoredPaths = current.ignoredPaths union setOf(path)))
    }
}
