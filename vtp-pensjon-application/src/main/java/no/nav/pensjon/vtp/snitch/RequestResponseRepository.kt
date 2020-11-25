package no.nav.pensjon.vtp.snitch

import org.springframework.stereotype.Repository
import java.util.concurrent.CopyOnWriteArrayList

@Repository
class RequestResponseRepository {
    private val requestResponses: MutableList<RequestResponse> = CopyOnWriteArrayList()

    fun save(requestResponse: RequestResponse): RequestResponse {
        while (requestResponses.size >= 100) {
            requestResponses.removeAt(0)
        }

        requestResponses.add(requestResponse)
        return requestResponse
    }

    fun findAll(): List<RequestResponse> = requestResponses
}
