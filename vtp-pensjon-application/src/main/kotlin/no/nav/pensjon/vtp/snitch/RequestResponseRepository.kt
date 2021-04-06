package no.nav.pensjon.vtp.snitch

import org.springframework.data.repository.CrudRepository

interface RequestResponseRepository : CrudRepository<RequestResponse, String>, RequestResponseCustom
