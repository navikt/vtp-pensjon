package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v2

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.core.util.toLocalDate
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFooRepository
import no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell
import no.nav.virksomhet.tjenester.oppgavebehandling.feil.v2.ObjectFactory
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.*
import no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreOppgaveOppgaveIkkeFunnet
import no.nav.virksomhet.tjenester.oppgavebehandling.v2.Oppgavebehandling
import org.springframework.dao.OptimisticLockingFailureException
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper


@SoapService(path = ["/esb/nav-tjeneste-oppgavebehandling_v2Web/sca/OppgavebehandlingWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", name = "Oppgavebehandling")
@XmlSeeAlso(ObjectFactory::class, no.nav.virksomhet.tjenester.oppgavebehandling.v2.ObjectFactory::class, no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class OppgaveBehandlingMock(private val enheterIndeks: EnheterIndeks, private val oppgaveRepository: OppgaveFooRepository) : Oppgavebehandling {
    /**
     *
     * Tjenesten lagreOppgaveBolk leveres av FGSAK. *
     *
     *Tilbyr å masseendre oppgaver, for bruk i batcher *
     *
     *Operasjonen vil ferdigstille de oppgavene den klarer, de som ikke er mulig å ferdigstille vil bli samlet opp og det returneres en liste av disse med tilhørende feilkode og beskrivelse. Det vil ikke bli returnert feil (faults) bortsett fra generiske feil hvis systemene er nede
     *
     *
     */
    @WebMethod
    @RequestWrapper(localName = "lagreOppgaveBolk", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreOppgaveBolk")
    @ResponseWrapper(localName = "lagreOppgaveBolkResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreOppgaveBolkResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun lagreOppgaveBolk(request: LagreOppgaveBolkRequest): LagreOppgaveBolkResponse {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Operasjonen som tilbyr å opprette en oppgave.
     */
    @WebMethod
    @RequestWrapper(localName = "opprettOppgave", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgave")
    @ResponseWrapper(localName = "opprettOppgaveResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgaveResponse")
    @WebResult(name = "response")
    override fun opprettOppgave(@WebParam(name = "request") request: OpprettOppgaveRequest): OpprettOppgaveResponse {
        val sporing = Sporing("saksbeh", getNorg2Modell(request.opprettetAvEnhetId)) // XXX: Get actual user from security context

        return with(request.opprettOppgave) {
            wrap(oppgaveRepository.save(OppgaveFoo(
                    opprettetSporing = sporing,
                    endretSporing = sporing,
                    aktivFra = aktivFra?.toLocalDate(),
                    aktivTil = aktivTil?.toLocalDate(),
                    ansvarligEnhetId = ansvarligEnhetId,
                    ansvarligId = ansvarligId,
                    beskrivelse = beskrivelse,
                    brukerId = brukerId,
                    brukertypeKode = brukertypeKode,
                    dokumentId = dokumentId,
                    fagomradeKode = fagomradeKode,
                    henvendelseId = henvendelseId,
                    kravId = kravId,
                    isLest = lest,
                    mappeId = mappeId,
                    mottattDato = mottattDato?.toLocalDate(),
                    normDato = normDato?.toLocalDate(),
                    oppfolging = oppfolging,
                    oppgavetypeKode = oppgavetypeKode,
                    prioritetKode = prioritetKode,
                    revurderingstype = revurderingstype,
                    saksnummer = saksnummer,
                    skannetDato = skannetDato?.toLocalDate(),
                    soknadsId = soknadsId,
                    underkategoriKode = underkategoriKode
            )).oppgaveId)
        }
    }

    /**
     *
     * Tilbyr funksjonalitet for å feilregistrere en oppgave.
     */
    @WebMethod
    @RequestWrapper(localName = "feilregistrerOppgave", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.FeilregistrerOppgave")
    @ResponseWrapper(localName = "feilregistrerOppgaveResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.FeilregistrerOppgaveResponse")
    override fun feilregistrerOppgave(request: FeilregistrerOppgaveRequest) {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Operasjon for å endre en mappe
     */
    @WebMethod
    @RequestWrapper(localName = "lagreMappe", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreMappe")
    @ResponseWrapper(localName = "lagreMappeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreMappeResponse")
    override fun lagreMappe(request: LagreMappeRequest) {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Operasjon som tilbyr sletting av en oppgavemappe.
     */
    @WebMethod
    @RequestWrapper(localName = "slettMappe", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.SlettMappe")
    @ResponseWrapper(localName = "slettMappeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.SlettMappeResponse")
    override fun slettMappe(request: SlettMappeRequest) {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Tjenesten er en bestillingstjeneste mot en gitt komponent, forespørselen er å opprette en komplett oppgave i komponenten. Det finnes flere komponenter som har oppgaver i sin løsning og det vil være mulig å spesifisere hvilken komponent som man ønsker oppgaven opprettet i, støttede komponenter er dokumentert i input.
     *
     ***Behandlingsregler:**
     *
     *EierkomponentKode i input styrer hvilken komponent (baksystem) man ønsker at bestillingen skal skje mot.
     *
     *HVIS eierkomponentKode er lik "AO01" SÅ skal bestillOppgave gå mot Arena<br></br>HVIS ukjent eierkomponentKode sendes inn SÅ skal bestillOppgave <span style="color:#000000;"><span style="color:#000000;">kaste teknisk feil</span></span>
     */
    @WebMethod
    @RequestWrapper(localName = "bestillOppgave", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.BestillOppgave")
    @ResponseWrapper(localName = "bestillOppgaveResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.BestillOppgaveResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun bestillOppgave(request: BestillOppgaveRequest): BestillOppgaveResponse {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     * Operasjon som tilbyr endring av en oppgave.
     */
    @WebMethod
    @RequestWrapper(localName = "lagreOppgave", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreOppgave")
    @ResponseWrapper(localName = "lagreOppgaveResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.LagreOppgaveResponse")
    override fun lagreOppgave(request: LagreOppgaveRequest) {
        val oppgave = oppgaveRepository.findById(request.endreOppgave.oppgaveId)
                ?: throw LagreOppgaveOppgaveIkkeFunnet("Oppgave med id=$request.endreOppgave.oppgaveId ikke funnet")

        with(request.endreOppgave) {
            try {
                oppgaveRepository.save(oppgave.copy(
                        version = versjon,
                        endretSporing = Sporing("saksbeh", getNorg2Modell(request.endretAvEnhetId)), // XXX: Get actual user from security context
                        aktivFra = aktivFra?.toLocalDate(),
                        aktivTil = aktivTil?.toLocalDate(),
                        ansvarligEnhetId = ansvarligEnhetId,
                        ansvarligId = ansvarligId,
                        beskrivelse = beskrivelse,
                        brukerId = brukerId,
                        brukertypeKode = brukertypeKode,
                        dokumentId = dokumentId,
                        fagomradeKode = fagomradeKode,
                        henvendelseId = henvendelseId,
                        kravId = kravId,
                        isLest = lest,
                        mappeId = mappeId,
                        mottattDato = mottattDato?.toLocalDate(),
                        normDato = normDato?.toLocalDate(),
                        oppfolging = oppfolging,
                        oppgavetypeKode = oppgavetypeKode,
                        prioritetKode = prioritetKode,
                        revurderingstype = revurderingstype,
                        saksnummer = saksnummer,
                        skannetDato = skannetDato?.toLocalDate(),
                        soknadsId = soknadsId,
                        underkategoriKode = underkategoriKode
                ))
            } catch (e: OptimisticLockingFailureException) {
                // Translate to one of the optimistic locking exceptions that are thrown by GSAK
                throw IllegalArgumentException("Feil ved endring. Optimistic Lock.")
            }
        }
    }

    /**
     *
     * Operasjon for å opprette en oppgavemappe.
     */
    @WebMethod
    @RequestWrapper(localName = "opprettMappe", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettMappe")
    @ResponseWrapper(localName = "opprettMappeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettMappeResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun opprettMappe(request: OpprettMappeRequest): OpprettMappeResponse {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Tjenesten opprettOppgaveBolk leveres av FGSAK. *
     *
     *Tilbyr å masseopprette oppgaver, for bruk i batcher *
     *
     *Operasjonen lagrer alt eller ingen ting, hvis en oppgaveopprettelse feiler vil de som eventuelt allerede er opprettet bli rullet tilbake
     *
     *
     */
    @WebMethod
    @RequestWrapper(localName = "opprettOppgaveBolk", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgaveBolk")
    @ResponseWrapper(localName = "opprettOppgaveBolkResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgaveBolkResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun opprettOppgaveBolk(request: OpprettOppgaveBolkRequest): OpprettOppgaveBolkResponse {
        throw UnsupportedOperationException("Ikke implementert")
    }

    /**
     *
     * Tjenesten ferdigstillOppgaveBolk leveres av FGSAK. *
     *
     *Operasjon som tilbyr sletting av oppgaver, tilpasset for batch-bruk *
     *
     *Operasjonen vil ferdigstille de oppgavene den klarer, de som ikke er mulig å ferdigstille vil bli samlet opp og det returneres en liste av disse med tilhørende feilkode og beskrivelse. Det vil ikke bli returnert feil (faults) bortsett fra hvis systemene er nede
     *
     *
     */
    @WebMethod
    @RequestWrapper(localName = "ferdigstillOppgaveBolk", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.FerdigstillOppgaveBolk")
    @ResponseWrapper(localName = "ferdigstillOppgaveBolkResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.FerdigstillOppgaveBolkResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun ferdigstillOppgaveBolk(request: FerdigstillOppgaveBolkRequest): FerdigstillOppgaveBolkResponse {
        throw UnsupportedOperationException("Ikke implementert")
    }

    private fun getNorg2Modell(enhetId: Int): Norg2Modell =
            enheterIndeks.finnByEnhetId("" + enhetId)
                    .orElseThrow { IllegalArgumentException("Unknown enhet $enhetId") }

    private fun wrap(oppgaveId: String): OpprettOppgaveResponse {
        val response = OpprettOppgaveResponse()
        response.oppgaveId = oppgaveId
        return response
    }
}