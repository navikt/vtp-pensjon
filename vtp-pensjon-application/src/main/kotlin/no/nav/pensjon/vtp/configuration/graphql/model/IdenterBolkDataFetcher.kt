package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository

class IdenterBolkDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<List<IdentlisteBolk>> {

    override fun get(env: DataFetchingEnvironment): List<IdentlisteBolk> {
        val identer = env.variables.get("identer") as ArrayList<String>
        val grupper = (env.variables.get("grupper") as ArrayList<String>).map { IdentGruppe.valueOf(it) }

        return identer
            .filter { personModellRepository.findByIdentOrAktørIdent(ident = it, aktørIdent = it) != null }
            .map {
                with(personModellRepository.findByIdentOrAktørIdent(ident = it, aktørIdent = it)) {
                    IdentlisteBolk(
                        ident = it,
                        identer = grupper.map { gruppe ->
                            when (gruppe) {
                                IdentGruppe.FOLKEREGISTERIDENT -> IdentInformasjon(
                                    ident = this!!.ident,
                                    gruppe = gruppe,
                                    historisk = false
                                )
                                IdentGruppe.AKTORID -> IdentInformasjon(
                                    ident = this!!.aktørIdent,
                                    gruppe = gruppe,
                                    historisk = false
                                )
                                IdentGruppe.NPID -> IdentInformasjon(
                                    ident = this!!.ident,
                                    gruppe = gruppe,
                                    historisk = false
                                )
                            }
                        }.toSet(),
                        code = HentIdenterBolkCode.ok.toString()
                    )
                }
            }.toList()
    }
}
