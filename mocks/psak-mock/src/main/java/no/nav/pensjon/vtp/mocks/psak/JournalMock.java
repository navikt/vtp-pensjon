package no.nav.pensjon.vtp.mocks.psak;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.*;
import no.nav.virksomhet.tjenester.arkiv.journal.v2.Journal;
import no.nav.virksomhet.tjenester.arkiv.journal.v2.ObjectFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@SoapService(path = "/esb/nav-tjeneste-journal_v2Web/sca/JournalWSEXP")
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", name = "Journal")
@XmlSeeAlso({no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.felles.v1.ObjectFactory.class, ObjectFactory.class, no.nav.virksomhet.gjennomforing.arkiv.journal.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.arkiv.journal.feil.v2.ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class JournalMock implements Journal {
    @Override
    public HentJournalpostResponse hentJournalpost(HentJournalpostRequest request) {
        throw new NotImplementedException();
    }

    /**
     * Operasjon for å identifisere og hente brevgruppeKode for en gitt brevkodeId.
     */
    @Override
    public IdentifiserBrevgruppeResponse identifiserBrevgruppe(IdentifiserBrevgruppeRequest request) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2/Journal/finnJournalpostRequest")
    @RequestWrapper(localName = "finnJournalpost", targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpost")
    @ResponseWrapper(localName = "finnJournalpostResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpostResponse")
    @WebResult(name = "response")
    public no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.FinnJournalpostResponse finnJournalpost(
            @WebParam(name = "request")
                    no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.FinnJournalpostRequest request)
    {
        return new FinnJournalpostResponse();
    }

    @Override
    public HentDokumentURLResponse hentDokumentURL(HentDokumentURLRequest request) {
        throw new NotImplementedException();
    }

    @Override
    public HentDokumentResponse hentDokument(HentDokumentRequest request) {
        throw new NotImplementedException();
    }
}
