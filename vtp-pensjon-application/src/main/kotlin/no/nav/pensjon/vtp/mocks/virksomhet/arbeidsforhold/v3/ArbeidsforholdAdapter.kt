package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsforhold.v3

import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsavtale
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.Arbeidsforhold
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.*

/**
 * Enkel førsteutgave i påvente av infrastruktur for å generere org.nr. Gir et svar med to arbeidsforhold.
 */
object ArbeidsforholdAdapter {
    private val objectFactory = ObjectFactory()

    fun fra(
        fnr: String,
        arbeidsforholdModell: Arbeidsforhold
    ): no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold =
        objectFactory.createArbeidsforhold().apply {
            arbeidsforholdID = arbeidsforholdModell.arbeidsforholdId
            arbeidsforholdIDnav = arbeidsforholdModell.arbeidsforholdIdnav
            opprettelsestidspunkt = arbeidsforholdModell.ansettelsesperiodeFom.asXMLGregorianCalendar()

            arbeidsforholdstype = objectFactory.createArbeidsforholdstyper().apply {
                kodeRef = arbeidsforholdModell.arbeidsforholdstype.name
            }

            ansettelsesPeriode = objectFactory.createAnsettelsesPeriode().apply {
                periode = objectFactory.createGyldighetsperiode().apply {
                    fom = arbeidsforholdModell.ansettelsesperiodeFom.asXMLGregorianCalendar()
                    tom = arbeidsforholdModell.ansettelsesperiodeTom?.asXMLGregorianCalendar()
                }
            }

            arbeidsavtale += arbeidsforholdModell.arbeidsavtaler.map { fra(it) }
            permisjonOgPermittering += arbeidsforholdModell.permisjoner?.map { fra(it) } ?: emptyList()

            arbeidsgiver = arbeidsforholdModell.arbeidsgiverAktorId
                ?.takeIf { it.isNotEmpty() }
                ?.let { lagPersonAktoer(it) }
                ?: arbeidsforholdModell.arbeidsgiverOrgnr
                    ?.let { lagOrganisasjonsAktoer(it) }

            opplysningspliktig = arbeidsforholdModell.opplyserOrgnr
                ?.takeIf { it.length > 9 }
                ?.let { lagPersonAktoer(it) }
                ?: arbeidsforholdModell.opplyserOrgnr
                    ?.let { lagOrganisasjonsAktoer(it) }

            arbeidstaker = lagPersonAktoer(fnr)
            isArbeidsforholdInnrapportertEtterAOrdningen = true
        }

    fun fra(arbeidsavtaleModell: Arbeidsavtale): no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsavtale =
        objectFactory.createArbeidsavtale().apply {
            avtaltArbeidstimerPerUke = arbeidsavtaleModell.avtaltArbeidstimerPerUke?.toBigDecimal()
            stillingsprosent = arbeidsavtaleModell.stillingsprosent?.toBigDecimal()
            beregnetAntallTimerPrUke = arbeidsavtaleModell.beregnetAntallTimerPerUke?.toBigDecimal()
            sisteLoennsendringsdato = arbeidsavtaleModell.sisteLønnnsendringsdato?.asXMLGregorianCalendar()
            fomGyldighetsperiode = arbeidsavtaleModell.fomGyldighetsperiode?.asXMLGregorianCalendar()
            tomGyldighetsperiode = arbeidsavtaleModell.tomGyldighetsperiode?.asXMLGregorianCalendar()
            yrke = Yrker().apply {
                kodeRef = "SnekkerKode"
                value = "SnekkerValue"
            }
        }

    fun fra(permisjonModell: Permisjon): PermisjonOgPermittering =
        objectFactory.createPermisjonOgPermittering().apply {
            this.permisjonOgPermittering = PermisjonsOgPermitteringsBeskrivelse().apply {
                kodeRef = permisjonModell.permisjonstype?.name
            }
            permisjonsprosent = permisjonModell.stillingsprosent?.toBigDecimal()
            permisjonsPeriode = Gyldighetsperiode().apply {
                fom = permisjonModell.fomGyldighetsperiode?.asXMLGregorianCalendar()
                tom = permisjonModell.tomGyldighetsperiode?.asXMLGregorianCalendar()
            }
        }

    private fun lagPersonAktoer(ident: String) = objectFactory.createPerson().apply {
        this.ident = NorskIdent().apply {
            this.ident = ident
        }
    }

    private fun lagOrganisasjonsAktoer(orgnummer: String) = objectFactory.createOrganisasjon().apply {
        this.orgnummer = orgnummer
    }
}
