package no.nav.pensjon.vtp.testmodell.fullmakt

import org.springframework.data.repository.Repository

interface FullmaktRepository : Repository<FullmaktModell, String> {
    fun findById(pid: String): List<FullmaktModell>?
    fun save(fullmaktModell: FullmaktModell)
}
