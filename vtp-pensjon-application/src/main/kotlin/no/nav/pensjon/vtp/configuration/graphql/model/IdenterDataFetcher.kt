package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository

class IdenterDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Identliste> {

    override fun get(env: DataFetchingEnvironment): Identliste =
        with(
            personModellRepository.findByIdentOrAktørIdent(
                ident = env.getArgument("ident"),
                aktørIdent = env.getArgument("ident")
            )
        ) {
            if (this == null) return@with Identliste(emptyList())

            // TODO: https://github.com/navikt/vtp-pensjon/issues/553
            when (IdentGruppe.valueOf((env.variables.get("grupper") as ArrayList<String>).first())) {
                IdentGruppe.FOLKEREGISTERIDENT -> Identliste(
                    listOf(
                        IdentInformasjon(
                            ident = this.ident,
                            gruppe = IdentGruppe.FOLKEREGISTERIDENT,
                            historisk = false
                        )
                    )
                )
                IdentGruppe.AKTORID -> Identliste(
                    listOf(
                        IdentInformasjon(
                            ident = this.aktørIdent,
                            gruppe = IdentGruppe.AKTORID,
                            historisk = false
                        )
                    )
                )
                else -> Identliste(emptyList())
            }
        }
}
