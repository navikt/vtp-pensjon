package no.nav.pensjon.vtp.testmodell.krr

import org.springframework.data.repository.Repository

interface DigdirRepository : Repository<DigitalKontaktinformasjon, String> {
    fun findById(ident: String): DigitalKontaktinformasjon?
    fun save(digitalKontaktinformasjon: DigitalKontaktinformasjon)
}
