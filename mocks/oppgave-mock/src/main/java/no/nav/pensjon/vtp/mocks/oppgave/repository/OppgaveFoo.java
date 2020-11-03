package no.nav.pensjon.vtp.mocks.oppgave.repository;

import static java.util.UUID.randomUUID;

import java.time.LocalDate;

public class OppgaveFoo {
    private final String id;
    private final int version;
    private final Sporing opprettetSporing;
    private final Sporing endretSporing;

    protected String brukerId;
    protected String brukertypeKode;
    protected String oppgavetypeKode;
    protected String fagomradeKode;
    protected String underkategoriKode;
    protected String prioritetKode;
    protected String beskrivelse;
    protected String oppfolging;
    protected LocalDate aktivFra;
    protected LocalDate aktivTil;
    protected String ansvarligEnhetId;
    protected String ansvarligId;
    protected String dokumentId;
    protected LocalDate mottattDato;
    protected LocalDate normDato;
    protected String saksnummer;
    protected LocalDate skannetDato;
    protected String soknadsId;
    protected String henvendelseId;
    protected String kravId;
    protected boolean lest;
    protected String mappeId;
    protected String revurderingstype;


    public OppgaveFoo(String id, int version, Sporing opprettetSporing, Sporing endretSporing) {
        this.id = id;
        this.version = version;
        this.opprettetSporing = opprettetSporing;
        this.endretSporing = endretSporing;
    }

    public OppgaveFoo(Sporing opprettetSporing) {
        this.opprettetSporing = opprettetSporing;
        this.endretSporing = opprettetSporing;
        this.id = randomUUID().toString();
        this.version = 1;
    }

    public String getOppgaveId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public OppgaveFoo withIncrementedVersion() {
        return new OppgaveFoo(id, version + 1, opprettetSporing, endretSporing);
    }

    public String getOpprettetEnhetId() {
        return opprettetSporing.getEnhetId();
    }

    public String getOpprettetEnhetNavn() {
        return opprettetSporing.getEnhetNavn();
    }

    public String getOpprettetAnsattIdent() {
        return opprettetSporing.getAnsattIdent();
    }

    public String getEndretAnsattIdent() {
        return endretSporing.getAnsattIdent();
    }

    public String getEndretEnhetId() {
        return endretSporing.getEnhetId();
    }

    public String getEndretEnhetNavn() {
        return endretSporing.getEnhetNavn();
    }

    public String getId() {
        return id;
    }

    public Sporing getOpprettetSporing() {
        return opprettetSporing;
    }

    public Sporing getEndretSporing() {
        return endretSporing;
    }

    public String getBrukerId() {
        return brukerId;
    }

    public void setBrukerId(String brukerId) {
        this.brukerId = brukerId;
    }

    public String getBrukertypeKode() {
        return brukertypeKode;
    }

    public void setBrukertypeKode(String brukertypeKode) {
        this.brukertypeKode = brukertypeKode;
    }

    public String getOppgavetypeKode() {
        return oppgavetypeKode;
    }

    public void setOppgavetypeKode(String oppgavetypeKode) {
        this.oppgavetypeKode = oppgavetypeKode;
    }

    public String getFagomradeKode() {
        return fagomradeKode;
    }

    public void setFagomradeKode(String fagomradeKode) {
        this.fagomradeKode = fagomradeKode;
    }

    public String getUnderkategoriKode() {
        return underkategoriKode;
    }

    public void setUnderkategoriKode(String underkategoriKode) {
        this.underkategoriKode = underkategoriKode;
    }

    public String getPrioritetKode() {
        return prioritetKode;
    }

    public void setPrioritetKode(String prioritetKode) {
        this.prioritetKode = prioritetKode;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getOppfolging() {
        return oppfolging;
    }

    public void setOppfolging(String oppfolging) {
        this.oppfolging = oppfolging;
    }

    public LocalDate getAktivFra() {
        return aktivFra;
    }

    public void setAktivFra(LocalDate aktivFra) {
        this.aktivFra = aktivFra;
    }

    public LocalDate getAktivTil() {
        return aktivTil;
    }

    public void setAktivTil(LocalDate aktivTil) {
        this.aktivTil = aktivTil;
    }

    public String getAnsvarligEnhetId() {
        return ansvarligEnhetId;
    }

    public void setAnsvarligEnhetId(String ansvarligEnhetId) {
        this.ansvarligEnhetId = ansvarligEnhetId;
    }

    public String getAnsvarligId() {
        return ansvarligId;
    }

    public void setAnsvarligId(String ansvarligId) {
        this.ansvarligId = ansvarligId;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(String dokumentId) {
        this.dokumentId = dokumentId;
    }

    public LocalDate getMottattDato() {
        return mottattDato;
    }

    public void setMottattDato(LocalDate mottattDato) {
        this.mottattDato = mottattDato;
    }

    public LocalDate getNormDato() {
        return normDato;
    }

    public void setNormDato(LocalDate normDato) {
        this.normDato = normDato;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    public void setSaksnummer(String saksnummer) {
        this.saksnummer = saksnummer;
    }

    public LocalDate getSkannetDato() {
        return skannetDato;
    }

    public void setSkannetDato(LocalDate skannetDato) {
        this.skannetDato = skannetDato;
    }

    public String getSoknadsId() {
        return soknadsId;
    }

    public void setSoknadsId(String soknadsId) {
        this.soknadsId = soknadsId;
    }

    public String getHenvendelseId() {
        return henvendelseId;
    }

    public void setHenvendelseId(String henvendelseId) {
        this.henvendelseId = henvendelseId;
    }

    public String getKravId() {
        return kravId;
    }

    public void setKravId(String kravId) {
        this.kravId = kravId;
    }

    public boolean isLest() {
        return lest;
    }

    public void setLest(boolean lest) {
        this.lest = lest;
    }

    public String getMappeId() {
        return mappeId;
    }

    public void setMappeId(String mappeId) {
        this.mappeId = mappeId;
    }

    public String getRevurderingstype() {
        return revurderingstype;
    }

    public void setRevurderingstype(String revurderingstype) {
        this.revurderingstype = revurderingstype;
    }
}
