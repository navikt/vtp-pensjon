package no.nav.pensjon.vtp.snitch

import org.springframework.data.repository.Repository

interface RequestResponseRepository : Repository<RequestResponse, String>, RequestResponseRespositoryCustom {
    fun save(entity: RequestResponse): RequestResponse
    fun findAll(): Iterable<RequestResponse>

    fun deleteAllByPath(path: String)
}
