package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.load.*
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.util.FiktivtNavn
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer

class Mapper(val identer: LokalIdentIndeks, val adresseIndeks: AdresseIndeks, val vars: VariabelContainer) {
    fun mapFromLoad(l: PersonopplysningerTemplate): Personopplysninger {
        val fallbackName = if (l.søker.kjønn == Kjønn.K) FiktivtNavn.getRandomFemaleName() else FiktivtNavn.getRandomMaleName()
        val fallbackAnnenPartName = TestdataUtil.getAnnenPartName(l.søker, l.annenPart)

        val annenPart = l.annenPart?.let { personModell(it, fallbackAnnenPartName) }

        return Personopplysninger(
                søker = personModell(l.søker, fallbackName),
                annenPart = annenPart,
                familierelasjoner = l.familierelasjoner.map { familierelasjonModell(it) }
        )
    }

    private fun familierelasjonModell(it: FamilierelasjonTemplate): FamilierelasjonModell {
        return FamilierelasjonModell(
                rolle = it.rolle,
                sammeBosted = it.sammeBosted,
                til = identer.getIdent(it.til)
        )
    }

    private fun geografiskTilknytningModell(it: GeografiskTilknytningTemplate): GeografiskTilknytningModell {
        return GeografiskTilknytningModell(
                fom = it.fom,
                tom = it.tom,
                endringstidspunkt = it.endringstidspunkt,
                endringstype = it.endringstype,
                kode = it.kode,
                type = it.type
        )
    }

    private fun personModell(i: PersonTemplate, fallbackName: PersonNavn): PersonModell {
        return PersonModell(
                ident = getPersonIdent(i),
                aktørIdent = getAktørIdent(i),
                fornavn = i.fornavn ?: fallbackName.fornavn,
                etternavn = i.etternavn ?: fallbackName.etternavn,
                fødselsdato = i.fødselsdato,
                dødsdato = i.dødsdato,
                diskresjonskode = i.diskresjonskode,
                språk = i.språk,
                kjønn = i.kjønn,
                gjeldendeAdresseType = i.gjeldendeAdresseType,
                geografiskTilknytning = i.geografiskTilknytning?.let { geografiskTilknytningModell(it) },
                statsborgerskap = i.statsborgerskap?.map { statsborgerskapModell(it) },
                sivilstand = i.sivilstand?.map { sivilstandModell(it) },
                personstatus = i.personstatus?.map { personstatusModell(it) },
                adresser = getAdresser(i),
                medlemskap = i.medlemskap
        )
    }

    fun getPersonIdent(i: PersonTemplate): String {
        return getFødselsdatoFraVars(i)
                ?.let { identer.getVoksenIdentForLokalIdent(i.ident, i.kjønn, it) }
                ?:  identer.getVoksenIdentForLokalIdent(i.ident, i.kjønn)
    }

    private fun getFødselsdatoFraVars(i: PersonTemplate): String? {
        return vars.getVar(i.ident.replace("\${", "").replace("}", "") + "_fødselsdato")
    }

    fun getAktørIdent(i: PersonTemplate): String {
        if (i.aktørIdent != null) {
            return i.aktørIdent
        }
        // aktørId er pt. 13 siffer. bruker FNR som utgangspunkt som er 11 slik at det er enkelt å spore
        return "99${getPersonIdent(i)}"
    }

    fun getAdresser(i: PersonTemplate): List<AdresseModell> {
        return i.adresser
                ?.map {
                    if (it is AdresseRefModell) {
                        return@map adresseIndeks.finnFra(it)
                    } else {
                        return@map it
                    }
                }
                ?: emptyList()
    }


    private fun personstatusModell(it: PersonstatusTemplate): PersonstatusModell {
        return PersonstatusModell(
                fom = it.fom,
                tom = it.tom,
                endringstidspunkt = it.endringstidspunkt,
                endringstype = it.endringstype,
                kode = it.kode
        )
    }

    private fun sivilstandModell(template: SivilstandTemplate): SivilstandModell {
        return SivilstandModell(
                fom = template.fom,
                tom = template.tom,
                endringstidspunkt = template.endringstidspunkt,
                endringstype = template.endringstype,
                kode = template.kode
        )
    }

    private fun statsborgerskapModell(load: StatsborgerskapTemplate): StatsborgerskapModell {
        return StatsborgerskapModell(
                fom = load.fom,
                tom = load.tom,
                endringstype = load.endringstype,
                endringstidspunkt = load.endringstidspunkt,
                land = load.land
        )
    }
}
