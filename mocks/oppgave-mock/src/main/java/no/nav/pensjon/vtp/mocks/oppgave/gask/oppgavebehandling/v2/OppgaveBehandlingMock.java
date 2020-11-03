package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v2;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo;
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveRepository;
import no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.BestillOppgaveRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.BestillOppgaveResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.FeilregistrerOppgaveRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.FerdigstillOppgaveBolkRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.FerdigstillOppgaveBolkResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.LagreMappeRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.LagreOppgaveBolkRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.LagreOppgaveBolkResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.LagreOppgaveRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettMappeRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettMappeResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgaveBolkRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgaveBolkResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgaveResponse;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.SlettMappeRequest;
import no.nav.virksomhet.tjenester.oppgavebehandling.v2.ObjectFactory;
import no.nav.virksomhet.tjenester.oppgavebehandling.v2.Oppgavebehandling;

@SoapService(path = "/esb/nav-tjeneste-oppgavebehandling_v2Web/sca/OppgavebehandlingWSEXP")
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", name = "Oppgavebehandling")
@XmlSeeAlso({no.nav.virksomhet.tjenester.oppgavebehandling.feil.v2.ObjectFactory.class, ObjectFactory.class, no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class OppgaveBehandlingMock implements Oppgavebehandling {
    private final AnsatteIndeks ansatteIndeks;
    private final EnheterIndeks enheterIndeks;
    private final OppgaveRepository oppgaveRepository;

    public OppgaveBehandlingMock(AnsatteIndeks ansatteIndeks, EnheterIndeks enheterIndeks, OppgaveRepository oppgaveRepository) {
        this.ansatteIndeks = ansatteIndeks;
        this.enheterIndeks = enheterIndeks;
        this.oppgaveRepository = oppgaveRepository;
    }

    /**
     * <p>Tjenesten lagreOppgaveBolk leveres av FGSAK.</p><ul><li><p>Tilbyr å masseendre oppgaver, for bruk i batcher</p></li><li><p>Operasjonen vil ferdigstille de oppgavene den klarer, de som ikke er mulig å ferdigstille vil bli samlet opp og det returneres en liste av disse med tilhørende feilkode og beskrivelse. Det vil ikke bli returnert feil (faults) bortsett fra generiske feil hvis systemene er nede</p></li></ul><p></p>
     */
    @Override
    public LagreOppgaveBolkResponse lagreOppgaveBolk(LagreOppgaveBolkRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Operasjonen som tilbyr å opprette en oppgave.</p>
     */
    @Override
    @WebMethod
    @RequestWrapper(localName = "opprettOppgave", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgave")
    @ResponseWrapper(localName = "opprettOppgaveResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/oppgavebehandling/v2", className = "no.nav.virksomhet.tjenester.oppgavebehandling.v2.OpprettOppgaveResponse")
    @WebResult(name = "response")
    public no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgaveResponse opprettOppgave(
            @WebParam(name = "request")
                    no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgaveRequest request) {

        Norg2Modell norg2Modell = enheterIndeks.finnByEnhetId("" + request.getOpprettetAvEnhetId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown enhet " + request.getOpprettetAvEnhetId()));

        OpprettOppgaveResponse response = new OpprettOppgaveResponse();
        response.setOppgaveId(oppgaveRepository.saveOppgave(Mapper.asOppaveFoo(new Sporing("saksbeh", norg2Modell), request.getOpprettOppgave())));
        return response;
    }

    /**
     * <p>Tilbyr funksjonalitet for å feilregistrere en oppgave.</p>
     */
    @Override
    public void feilregistrerOppgave(FeilregistrerOppgaveRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Operasjon for å endre en mappe</p>
     */
    @Override
    public void lagreMappe(LagreMappeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Operasjon som tilbyr sletting av en oppgavemappe.</p>
     */
    @Override
    public void slettMappe(SlettMappeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Tjenesten er en bestillingstjeneste mot en gitt komponent, forespørselen er å opprette en komplett oppgave i komponenten. Det finnes flere komponenter som har oppgaver i sin løsning og det vil være mulig å spesifisere hvilken komponent som man ønsker oppgaven opprettet i, støttede komponenter er dokumentert i input.</p><p><b>Behandlingsregler:</b></p><p>EierkomponentKode i input styrer hvilken komponent (baksystem) man ønsker at bestillingen skal skje mot.</p><p>HVIS eierkomponentKode er lik "AO01" SÅ skal bestillOppgave gå mot Arena<br>HVIS ukjent eierkomponentKode sendes inn SÅ skal bestillOppgave <span style="color:#000000;"><span style="color:#000000;">kaste teknisk feil</span></span></p>
     */
    @Override
    public BestillOppgaveResponse bestillOppgave(BestillOppgaveRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Operasjon som tilbyr endring av en oppgave.</p>
     */
    @Override
    public void lagreOppgave(LagreOppgaveRequest request) {
        request.getEndreOppgave().getVersjon();
        //oppgaveRepository.saveOppgave();
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Operasjon for å opprette en oppgavemappe.</p>
     */
    @Override
    public OpprettMappeResponse opprettMappe(OpprettMappeRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Tjenesten opprettOppgaveBolk leveres av FGSAK.</p><ul><li><p>Tilbyr å masseopprette oppgaver, for bruk i batcher</p></li><li><p>Operasjonen lagrer alt eller ingen ting, hvis en oppgaveopprettelse feiler vil de som eventuelt allerede er opprettet bli rullet tilbake</p></li></ul><p></p>
     */
    @Override
    public OpprettOppgaveBolkResponse opprettOppgaveBolk(OpprettOppgaveBolkRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    /**
     * <p>Tjenesten ferdigstillOppgaveBolk leveres av FGSAK.</p><ul><li><p>Operasjon som tilbyr sletting av oppgaver, tilpasset for batch-bruk</p></li><li><p>Operasjonen vil ferdigstille de oppgavene den klarer, de som ikke er mulig å ferdigstille vil bli samlet opp og det returneres en liste av disse med tilhørende feilkode og beskrivelse. Det vil ikke bli returnert feil (faults) bortsett fra hvis systemene er nede</p></li></ul><p></p>
     */
    @Override
    public FerdigstillOppgaveBolkResponse ferdigstillOppgaveBolk(FerdigstillOppgaveBolkRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
