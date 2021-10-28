package no.nav.pensjon.vtp.auth.idporten

import org.springframework.data.repository.Repository

interface IdPortenSessionRepository : Repository<IdPortenSession, String> {
    fun findByCode(code: String): IdPortenSession?
    fun save(idPortenSession: IdPortenSession): IdPortenSession
}
