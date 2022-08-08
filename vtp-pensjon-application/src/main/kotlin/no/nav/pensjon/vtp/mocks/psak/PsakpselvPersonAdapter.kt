package no.nav.pensjon.vtp.mocks.psak

import no.nav.lib.pen.psakpselv.asbo.person.*
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType.BOSTEDSADRESSE
import no.nav.pensjon.vtp.util.asGregorianCalendar
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class PsakpselvPersonAdapter(
    private val personIndeks: PersonIndeks,
    private val personModellRepository: PersonModellRepository
) {
    fun getASBOPenPerson(fodselsnummer: String): ASBOPenPerson? {
        val personModell = personModellRepository.findById(fodselsnummer)
        val personopplysninger = personIndeks.findById(fodselsnummer)

        return if (personModell != null && personopplysninger != null) {
            toASBOPerson(personModell, personopplysninger)
        } else if (personModell != null && personopplysninger == null) {
            ASBOPenPerson().apply { personUtland = ASBOPenPersonUtland().apply { statsborgerKode = "NOR" } }
        } else {
            null
        }
    }

    fun toASBOPerson(person: PersonModell, personopplysninger: Personopplysninger): ASBOPenPerson {
        return populateAsboPenPerson(person).apply {
            relasjoner = fetchRelasjoner(personopplysninger)
            samboer = fetchSamboer(person)
        }
    }

    private fun fetchRelasjoner(personopplysninger: Personopplysninger): ASBOPenRelasjonListe {
        return ASBOPenRelasjonListe().apply {
            relasjoner = personopplysninger.familierelasjoner
                .map { populateAsboPenRelasjon(personopplysninger, it) }
                .toTypedArray()
        }
    }

    private fun fetchSamboer(person: PersonModell) = person.samboerforhold
        .filter { it.datoTom == null && !it.annullert }.map {
        ASBOPenSamboer().apply {
            fodselsnummer = it.pidSamboer
            fomDato = localDateToCalendar(it.datoFom)
            tomDato = it.datoTom?.let { localDateToCalendar(it) }
            endretAvSaksbehandler = it.opprettetAv
            endretAvSystem = "PP01"
        }
    }.firstOrNull() ?: ASBOPenSamboer().apply {
        fodselsnummer = ""
        fomDato = null
        tomDato = null
        endretAvSaksbehandler = ""
        endretAvSystem = ""
    }

    private fun populateAsboPenRelasjon(
        personopplysninger: Personopplysninger,
        fr: FamilierelasjonModell
    ): ASBOPenRelasjon {
        val relasjon = ASBOPenRelasjon().apply {
            relasjonsType = fr.rolle.name
        }

        val annen = personopplysninger.annenPart
            ?.takeIf { it.ident == personModellRepository.findById(fr.til)?.ident }
            ?.let { populateAsboPenPerson(it) }
            ?.also {
                relasjon.fom = personopplysninger.søker.getSivilstandFoo().fom?.asGregorianCalendar()
                relasjon.tom = personopplysninger.søker.getSivilstandFoo().tom?.asGregorianCalendar()
            }
            ?: createShallowASBOPerson(fr)
        relasjon.person = annen

        // annen part må ha en lik relasjon også

        annen.relasjoner = ASBOPenRelasjonListe().apply {
            relasjoner = arrayOf(
                ASBOPenRelasjon().apply {
                    relasjonsType = relasjon.relasjonsType
                    fom = relasjon.fom
                    fom = relasjon.tom
                    adresseStatus = relasjon.adresseStatus
                    person = ASBOPenPerson().apply {
                        fodselsnummer = personopplysninger.søker.ident
                    }
                }
            )
        }
        return relasjon
    }

    private fun createShallowASBOPerson(familierelasjon: FamilierelasjonModell): ASBOPenPerson {
        return ASBOPenPerson().apply {
            fodselsnummer = personModellRepository.findById(familierelasjon.til)?.ident
                ?: throw RuntimeException("No person with ident " + familierelasjon.til)
        }
    }

    companion object {
        private fun populateAsboPenPerson(søker: PersonModell): ASBOPenPerson {
            return ASBOPenPerson().apply {
                fodselsnummer = søker.ident
                fornavn = søker.fornavn
                etternavn = søker.etternavn
                kortnavn = "${søker.fornavn} ${søker.etternavn}"
                status = søker.getPersonstatusFoo().kode.name
                statusKode = søker.getPersonstatusFoo().kode.name
                diskresjonskode = søker.diskresjonskode?.toString()
                dodsdato = søker.dødsdato?.asGregorianCalendar()
                sivilstand = søker.getSivilstandFoo().kode.name
                sivilstandDato = søker.getSivilstandFoo().endringstidspunkt?.asGregorianCalendar() ?: søker.fødselsdato?.asGregorianCalendar()
                sprakKode = søker.getSpråk2Bokstaver()
                sprakBeskrivelse = søker.språk
                bostedsAdresse = søker.getAdresse(BOSTEDSADRESSE)?.asASBOPenBostedsAdresseOrNull()
                erEgenansatt = false
                brukerprofil = ASBOPenBrukerprofil()
                personUtland = ASBOPenPersonUtland().apply {
                    statsborgerKode = søker.statsborgerskap?.firstOrNull()?.land?.toString()
                    statsborgerskap = søker.statsborgerskap?.firstOrNull()?.land?.toString()
                }
                historikk = createHistorikk()

                utbetalingsinformasjon = ASBOPenUtbetalingsinformasjon().apply {
                    utbetalingsType = "NORKNT"
                    norskKonto = ASBOPenNorskKonto().apply {
                        kontonummer = "1234.45678.0232.7777"
                    }
                }
            }
        }

        private fun createHistorikk(): ASBOPenHistorikk {
            return ASBOPenHistorikk().apply {
                bostedsadresser = null // må være enten være populert eller null
                adresseLinjer = ASBOPenAnnenAdresseListe().apply { adresseLinjer = arrayOfNulls(0) }
                historiskeFnr = ASBOPenHistoriskFnrListe().apply { historiskeFnr = arrayOfNulls(0) }
                navnEndringer = ASBOPenNavnEndringListe().apply { navnEndringer = arrayOfNulls(0) }
            }
        }

        private fun localDateToCalendar(date: LocalDate) = Calendar.getInstance().apply {
            set(date.year, date.monthValue - 1, date.dayOfMonth)
        }
    }
}

fun AdresseModell.asASBOPenBostedsAdresseOrNull(): ASBOPenBostedsAdresse? {
    return if (this is GateadresseModell) {
        ASBOPenBostedsAdresse().apply {
            adresseType = BOSTEDSADRESSE.name
            bolignr = husnummer.toString()
            boadresse1 = "$gatenavn $husnummer"
            postnummer = this@asASBOPenBostedsAdresseOrNull.postnummer
            boadresse2 = this@asASBOPenBostedsAdresseOrNull.tilleggsadresse
            kommunenr = kommunenummer
        }
    } else {
        null
    }
}
