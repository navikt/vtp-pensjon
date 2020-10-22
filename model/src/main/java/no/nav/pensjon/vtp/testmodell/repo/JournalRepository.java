package no.nav.pensjon.vtp.testmodell.repo;

import java.util.List;
import java.util.Optional;

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;

public interface JournalRepository {
    Optional<DokumentModell> finnDokumentMedDokumentId(String dokumentId);

    List<JournalpostModell> finnJournalposterMedFnr(String fnr);

    List<JournalpostModell> finnJournalposterMedSakId(String sakId);

    Optional<JournalpostModell> finnJournalpostMedJournalpostId(String journalpostId);

    String leggTilJournalpost(JournalpostModell journalpostModell);
}
