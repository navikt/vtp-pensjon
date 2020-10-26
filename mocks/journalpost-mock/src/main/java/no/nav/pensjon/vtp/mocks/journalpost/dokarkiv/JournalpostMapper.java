package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv;

import no.nav.dokarkiv.generated.model.Bruker;
import no.nav.dokarkiv.generated.model.Dokument;
import no.nav.dokarkiv.generated.model.DokumentVariant;
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest;
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostBruker;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Dokumentkategori;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Variantformat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JournalpostMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMapper.class);


    //TODO: utvid med nødvendig mapping
    public JournalpostModell tilModell(OpprettJournalpostRequest journalpostRequest){
        JournalpostModell modell = new JournalpostModell();
        modell.setJournalposttype(mapJournalposttype(journalpostRequest.getJournalpostType()));
        modell.setArkivtema(mapArkivtema(journalpostRequest.getTema()));
        modell.setBruker(mapAvsenderFraBruker(journalpostRequest.getBruker()));
        modell.setSakId(journalpostRequest.getSak().getArkivsaksnummer());
        modell.setMottattDato(Optional.ofNullable(journalpostRequest.getDatoMottatt()).map(OffsetDateTime::toLocalDateTime).orElse(LocalDateTime.now()));

        List<DokumentModell> dokumentModeller = new ArrayList<>();
        if(!journalpostRequest.getDokumenter().isEmpty()) {
            dokumentModeller.add(mapDokument(journalpostRequest.getDokumenter().remove(0), DokumentTilknyttetJournalpost.HOVEDDOKUMENT));
            dokumentModeller.addAll(
                    journalpostRequest
                            .getDokumenter().stream()
                            .map(it -> mapDokument(it, DokumentTilknyttetJournalpost.VEDLEGG))
                            .collect(Collectors.toList()));
        }
        modell.setDokumentModellList(dokumentModeller);



        //TODO: Hvordan håndteres denne (getAvsenderMottaker) sammenlignet med bruker? & Map felter videre
        journalpostRequest.getAvsenderMottaker();
        journalpostRequest.getBehandlingstema();
        journalpostRequest.getTittel();
        journalpostRequest.getTilleggsopplysninger();
        journalpostRequest.getEksternReferanseId();
        journalpostRequest.getKanal();

        return modell;

    }


    public JournalpostBruker mapAvsenderFraBruker(Bruker bruker){
        switch (bruker.getIdType()){
            case FNR:
                return new JournalpostBruker(bruker.getId(), BrukerType.FNR);
            case AKTOERID:
                return new JournalpostBruker(bruker.getId(),BrukerType.AKTOERID);
            case ORGNR:
                return new JournalpostBruker(bruker.getId(),BrukerType.ORGNR);
            default:
                LOG.warn("Ikke støtte for annen brukertype enn person i journalpostmodell");
                throw new UnsupportedOperationException("Kan ikke opprette journalpost for brukertype");
        }
    }

    private DokumentModell mapDokument(Dokument dokument, DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost){
        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentkategori(new Dokumentkategori(dokument.getDokumentKategori()));
        dokumentModell.setTittel(dokument.getTittel());

        List<DokumentVariantInnhold> dokumentVariantInnholds =
                dokument.getDokumentvarianter().stream()
                .map( it -> mapDokumentVariant(it))
                .collect(Collectors.toList());

        dokumentModell.setDokumentVariantInnholdListe(dokumentVariantInnholds);
        dokumentModell.setDokumentTilknyttetJournalpost(dokumentTilknyttetJournalpost);

        //TODO: Map videre felter
        dokument.getDokumentKategori();
        dokument.getBrevkode();
        dokument.getDokumentKategori();
        dokument.getDokumentvarianter();

        return dokumentModell;
    }

    private DokumentVariantInnhold mapDokumentVariant(DokumentVariant dokumentVariant){
        DokumentVariantInnhold dokumentVariantInnhold = new DokumentVariantInnhold(
                new Arkivfiltype(dokumentVariant.getFiltype()),
                new Variantformat(dokumentVariant.getVariantformat()),
                dokumentVariant.getFysiskDokument()
        );
        return dokumentVariantInnhold;
    }



    private Arkivtema mapArkivtema(String tema){
        return new Arkivtema(tema);
    }

    private Journalposttyper mapJournalposttype(OpprettJournalpostRequest.JournalpostTypeEnum type){
        if(type.value().equalsIgnoreCase("INNGAAENDE")){
            return Journalposttyper.INNGAAENDE_DOKUMENT;
        } else if (type.value().equalsIgnoreCase("UTGAAENDE")){
            return Journalposttyper.UTGAAENDE_DOKUMENT;
        } else if(type.value().equalsIgnoreCase("NOTAT"))          {
            return Journalposttyper.NOTAT;
        } else {
            throw new IllegalArgumentException("Verdi journalposttype ikke støttet");
        }
    }
}