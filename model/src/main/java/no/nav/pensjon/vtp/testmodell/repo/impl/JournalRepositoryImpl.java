package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;

@Repository
public class JournalRepositoryImpl implements JournalRepository {

    private final HashMap<String, JournalpostModell> journalposter;
    private final HashMap<String, DokumentModell> dokumenter;

    private final AtomicInteger journalpostId;
    private final AtomicInteger dokumentId;

    public JournalRepositoryImpl() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Mdkm");
        journalposter = new HashMap<>();
        dokumenter = new HashMap<>();
        journalpostId = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
        dokumentId = new AtomicInteger(Integer.parseInt(LocalDateTime.now().format(formatter)) * 100);
    }

    @Override
    public Optional<DokumentModell> finnDokumentMedDokumentId(String dokumentId) {
        return ofNullable(dokumenter.get(dokumentId));
    }

    @Override
    public List<JournalpostModell> finnJournalposterMedFnr(String fnr){
        return journalposter.values().stream()
                .filter(e -> (e.getAvsenderFnr() != null && e.getAvsenderFnr().equalsIgnoreCase(fnr)))
                .collect(Collectors.toList());
    }

    @Override
    public List<JournalpostModell> finnJournalposterMedSakId(String sakId){
        return journalposter.values().stream()
                .filter(e -> (e.getSakId() != null && e.getSakId().equalsIgnoreCase(sakId)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JournalpostModell> finnJournalpostMedJournalpostId(String journalpostId){
        return ofNullable(journalposter.get(journalpostId));
    }

    @Override
    public String leggTilJournalpost(JournalpostModell journalpostModell){
        String journalpostId;
        if(journalpostModell.getJournalpostId() != null && !journalpostModell.getJournalpostId().isEmpty()){
            journalpostId = journalpostModell.getJournalpostId();
        } else {
            journalpostId = genererJournalpostId();
            journalpostModell.setJournalpostId(journalpostId);
        }

        for(DokumentModell dokumentModell : journalpostModell.getDokumentModellList()){
            String dokumentId;
            if(dokumentModell.getDokumentId() != null && !journalpostModell.getJournalpostId().isEmpty()){
                dokumentId = dokumentModell.getDokumentId();
            } else {
                dokumentId = genererDokumentId();
                dokumentModell.setDokumentId(dokumentId);
            }
            if(dokumenter.containsKey(dokumentId)){
                throw new IllegalStateException("Forsøker å opprette dokument allerede eksisterende dokumentId");
            } else {
                dokumenter.put(dokumentId, dokumentModell);
            }
        }

        if(journalposter.containsKey(journalpostId)){
            throw new IllegalStateException("Forsøker å opprette journalpost allerede eksisterende journalpostId");
        } else {
            journalposter.put(journalpostId, journalpostModell);
            return journalpostId;
        }
    }


    public String genererJournalpostId(){
        return Integer.toString(journalpostId.incrementAndGet());
    }

    public String genererDokumentId(){
        return Integer.toString(dokumentId.incrementAndGet());
    }




}
