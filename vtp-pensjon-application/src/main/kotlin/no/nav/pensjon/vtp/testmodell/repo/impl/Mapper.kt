package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks
import no.nav.pensjon.vtp.testmodell.krr.DigitalKontaktinformasjon
import no.nav.pensjon.vtp.testmodell.load.*
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.util.FiktivtNavn.getAnnenPartName
import no.nav.pensjon.vtp.testmodell.util.FiktivtNavn.getRandomName
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer
import java.time.*
import java.util.*

class Mapper(val identer: LokalIdentIndeks, val adresseIndeks: AdresseIndeks, val vars: VariabelContainer) {
    fun mapFromLoad(l: PersonopplysningerTemplate): Personopplysninger {
        val fallbackName = getRandomName(l.søker.kjønn)
        val annenPart = l.annenPart?.let {
            personModell(
                it,
                getAnnenPartName(l.søker, l.søker.etternavn ?: fallbackName.etternavn, it.kjønn),
                getPersonIdent(it)
            )
        }

        val foreldre = l.foreldre?.map {
            personModell(
                it,
                getAnnenPartName(l.søker, l.søker.etternavn ?: fallbackName.etternavn, it.kjønn),
                getPersonIdent(it)
            )
        }

        return Personopplysninger(
            søker = personModell(l.søker, fallbackName, annenPart?.ident),
            annenPart = annenPart,
            foreldre = foreldre,
            familierelasjoner = l.familierelasjoner.map { familierelasjonModell(it) }
        )
    }

    fun mapDigitalkontaktinformasjon(load: DigitalKontaktinformasjon, ident: String) = DigitalKontaktinformasjon(
        personident = ident,
        aktiv = load.aktiv,
        kanVarsles = load.kanVarsles,
        reservert = load.reservert,
        spraak = load.spraak,
        epostadresse = load.epostadresse,
        epostadresseOppdatert = load.epostadresseOppdatert,
        mobiltelefonnummer = load.mobiltelefonnummer,
        mobiltelefonnummerOppdatert = load.mobiltelefonnummerOppdatert,
        sikkerDigitalPostkasse = load.sikkerDigitalPostkasse
    )

    private fun familierelasjonModell(it: FamilierelasjonTemplate): FamilierelasjonModell {
        return FamilierelasjonModell(
            rolle = it.rolle,
            sammeBosted = it.sammeBosted,
            til = identer.getIdent(it.til)
                ?: throw IllegalArgumentException("Unknown relation with identifier ${it.til}")
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

    private fun personModell(i: PersonTemplate, fallbackName: PersonNavn, annenPartIdent: String?): PersonModell {
        if (i.fødselsdato == null) {
            i.fødselsdato = randomFødselsdato()
        }
        return PersonModell(
            ident = getPersonIdent(i),
            aktørIdent = getAktørIdent(i),
            fornavn = i.fornavn ?: fallbackName.fornavn,
            etternavn = i.etternavn ?: fallbackName.etternavn,
            fødselsdato = i.fødselsdato,
            dødsdato = i.dødsdato,
            diskresjonskode = i.diskresjonskode,
            egenansatt = i.egenansatt,
            språk = i.språk,
            kjønn = i.kjønn,
            gjeldendeAdresseType = i.gjeldendeAdresseType,
            geografiskTilknytning = i.geografiskTilknytning?.let { geografiskTilknytningModell(it) },
            statsborgerskap = i.statsborgerskap?.map { statsborgerskapModell(it) },
            sivilstand = i.sivilstand?.map { sivilstandModell(it) },
            personstatus = i.personstatus?.map { personstatusModell(it) },
            adresser = getAdresser(i),
            medlemskap = i.medlemskap,
            samboerforhold = i.samboerforhold?.map {
                samboerforholdModell(
                    load = it,
                    innmelder = getPersonIdent(i),
                    motpart = annenPartIdent,
                )
            } ?: emptyList(),
            innflyttingTilNorge = i.innflyttingTilNorge,
            utflyttingFraNorge = i.utflyttingFraNorge
        )
    }

    fun getPersonIdent(i: PersonTemplate) = identer.getVoksenIdentForLokalIdent(i.ident, i.kjønn, i.fødselsdato.toString())

    private fun randomFødselsdato() = LocalDate.ofYearDay(
        (LocalDate.now().minusYears(100).year .. LocalDate.now().minusYears(18).year).random(),
        (1 .. 365).random()
    )

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

    private fun samboerforholdModell(load: SamboerforholdTemplate, innmelder: String, motpart: String?): SamboerforholdModell {
        return SamboerforholdModell(
            id = UUID.randomUUID().toString(),
            pidSamboer = motpart!!,
            datoFom = load.fraOgMed,
            datoTom = load.tilOgMed,
            opprettetAv = load.opprettetAv,
        )
    }
}
