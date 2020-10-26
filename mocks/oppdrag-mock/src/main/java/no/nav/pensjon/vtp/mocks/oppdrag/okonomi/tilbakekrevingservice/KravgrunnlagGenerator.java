package no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.common.collect.Lists;

import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagBelopDto;
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagDto;
import no.nav.tilbakekreving.kravgrunnlag.detalj.v1.DetaljertKravgrunnlagPeriodeDto;
import no.nav.tilbakekreving.kravgrunnlag.hentliste.v1.ReturnertKravgrunnlagDto;
import no.nav.tilbakekreving.typer.v1.JaNeiDto;
import no.nav.tilbakekreving.typer.v1.PeriodeDto;
import no.nav.tilbakekreving.typer.v1.TypeGjelderDto;
import no.nav.tilbakekreving.typer.v1.TypeKlasseDto;

class KravgrunnlagGenerator {

    private static final String ENHET = "4815";

    public static DetaljertKravgrunnlagDto hentGrunnlag() {
        DetaljertKravgrunnlagDto detaljertKravgrunnlag = new DetaljertKravgrunnlagDto();
        detaljertKravgrunnlag.setVedtakId(BigInteger.valueOf(207406));
        detaljertKravgrunnlag.setKravgrunnlagId(BigInteger.valueOf(152806));
        detaljertKravgrunnlag.setDatoVedtakFagsystem(konvertDato(LocalDate.of(2019, 3, 14)));
        detaljertKravgrunnlag.setEnhetAnsvarlig(ENHET);
        detaljertKravgrunnlag.setFagsystemId("10000000000000000");
        detaljertKravgrunnlag.setKodeFagomraade("PENAP");
        detaljertKravgrunnlag.setKodeHjemmel("22-15-1-1");
        detaljertKravgrunnlag.setKontrollfelt("42354353453454");
        detaljertKravgrunnlag.setReferanse("1");
        detaljertKravgrunnlag.setRenterBeregnes(JaNeiDto.N);
        detaljertKravgrunnlag.setSaksbehId("Z991136");
        detaljertKravgrunnlag.setUtbetalesTilId("10127435540"); //mock verdi
        detaljertKravgrunnlag.setEnhetBehandl(ENHET);
        detaljertKravgrunnlag.setEnhetBosted(ENHET);
        detaljertKravgrunnlag.setKodeStatusKrav("BEHA");
        detaljertKravgrunnlag.setTypeGjelderId(TypeGjelderDto.PERSON);
        detaljertKravgrunnlag.setTypeUtbetId(TypeGjelderDto.PERSON);
        detaljertKravgrunnlag.setVedtakGjelderId("10127435540"); //mock verdi
        detaljertKravgrunnlag.setVedtakIdOmgjort(BigInteger.valueOf(207407));
        detaljertKravgrunnlag.getTilbakekrevingsPeriode().addAll(hentPerioder());


        return detaljertKravgrunnlag;
    }

    private static List<DetaljertKravgrunnlagPeriodeDto> hentPerioder() {
        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode1 = new DetaljertKravgrunnlagPeriodeDto();
        PeriodeDto periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2016, 3, 16)));
        periode.setTom(konvertDato(LocalDate.of(2016, 3, 25)));
        kravgrunnlagPeriode1.setPeriode(periode);
        kravgrunnlagPeriode1.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.valueOf(9000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TypeKlasseDto.FEIL, "KL_KODE_FEIL_PEN", BigDecimal.valueOf(0)));
        kravgrunnlagPeriode1.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(6000.00), BigDecimal.valueOf(6000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, "PENAPGP", BigDecimal.valueOf(30)));
        kravgrunnlagPeriode1.setBelopSkattMnd(new BigDecimal(2700));

        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode2 = new DetaljertKravgrunnlagPeriodeDto();
        periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2016, 3, 26)));
        periode.setTom(konvertDato(LocalDate.of(2016, 3, 31)));
        kravgrunnlagPeriode2.setPeriode(periode);
        kravgrunnlagPeriode2.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(3000.00), BigDecimal.valueOf(3000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, "PENAPGP", BigDecimal.valueOf(30)));
        kravgrunnlagPeriode2.setBelopSkattMnd(new BigDecimal(2700));

        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode3 = new DetaljertKravgrunnlagPeriodeDto();
        periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2016, 4, 1)));
        periode.setTom(konvertDato(LocalDate.of(2016, 4, 30)));
        kravgrunnlagPeriode3.setPeriode(periode);
        kravgrunnlagPeriode3.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.valueOf(21000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TypeKlasseDto.FEIL, "KL_KODE_FEIL_PEN", BigDecimal.valueOf(0)));
        kravgrunnlagPeriode3.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(21000.00), BigDecimal.valueOf(21000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, "PENAPGA", BigDecimal.valueOf(25.456)));
        kravgrunnlagPeriode3.setBelopSkattMnd(new BigDecimal(5346));


        DetaljertKravgrunnlagPeriodeDto kravgrunnlagPeriode4 = new DetaljertKravgrunnlagPeriodeDto();
        periode = new PeriodeDto();
        periode.setFom(konvertDato(LocalDate.of(2016, 5, 1)));
        periode.setTom(konvertDato(LocalDate.of(2016, 5, 26)));
        kravgrunnlagPeriode4.setPeriode(periode);
        kravgrunnlagPeriode4.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.valueOf(9000.00), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, TypeKlasseDto.FEIL, "KL_KODE_FEIL_PEN", BigDecimal.valueOf(0)));
        kravgrunnlagPeriode4.getTilbakekrevingsBelop().add(hentBeløp(BigDecimal.ZERO, BigDecimal.valueOf(9000.00), BigDecimal.valueOf(9000.00), BigDecimal.ZERO, TypeKlasseDto.YTEL, "PENAPGA", BigDecimal.valueOf(31.123)));
        kravgrunnlagPeriode4.setBelopSkattMnd(new BigDecimal(2802));

        return Lists.newArrayList(kravgrunnlagPeriode1, kravgrunnlagPeriode2, kravgrunnlagPeriode3, kravgrunnlagPeriode4);
    }

    private static DetaljertKravgrunnlagBelopDto hentBeløp(BigDecimal nyBeløp,
                                                           BigDecimal tilbakekrevesBeløp,
                                                           BigDecimal opprUtbetBeløp,
                                                           BigDecimal uInnkrevdBeløp,
                                                           TypeKlasseDto typeKlasse,
                                                           String kodeKlasse,
                                                           BigDecimal skatteprosent) {
        DetaljertKravgrunnlagBelopDto detaljertKravgrunnlagBelop = new DetaljertKravgrunnlagBelopDto();
        detaljertKravgrunnlagBelop.setTypeKlasse(typeKlasse);
        detaljertKravgrunnlagBelop.setBelopNy(nyBeløp);
        detaljertKravgrunnlagBelop.setBelopOpprUtbet(opprUtbetBeløp);
        detaljertKravgrunnlagBelop.setBelopTilbakekreves(tilbakekrevesBeløp);
        detaljertKravgrunnlagBelop.setBelopUinnkrevd(uInnkrevdBeløp);
        detaljertKravgrunnlagBelop.setKodeKlasse(kodeKlasse);
        detaljertKravgrunnlagBelop.setSkattProsent(skatteprosent);

        return detaljertKravgrunnlagBelop;
    }

    public static ReturnertKravgrunnlagDto createReturnertKravgrunnlagDto() {
        ReturnertKravgrunnlagDto kravgrunnlag = new ReturnertKravgrunnlagDto();
        kravgrunnlag.setKravgrunnlagId(BigInteger.valueOf(152806));
        kravgrunnlag.setKodeStatusKrav("NY");
        kravgrunnlag.setGjelderId("10127435540"); // mock verdi
        kravgrunnlag.setTypeGjelderId(TypeGjelderDto.PERSON);
        kravgrunnlag.setUtbetalesTilId("10127435540"); // mock verdi
        kravgrunnlag.setTypeUtbetId(TypeGjelderDto.PERSON);
        kravgrunnlag.setKodeFagomraade("PENAP");
        kravgrunnlag.setFagsystemId("10000000000000000");
        kravgrunnlag.setDatoVedtakFagsystem(konvertDato(LocalDate.of(2019, 3, 14)));
        kravgrunnlag.setBelopSumFeilutbetalt(BigDecimal.valueOf(39000));
        kravgrunnlag.setEnhetBosted(ENHET);
        kravgrunnlag.setEnhetAnsvarlig(ENHET);
        kravgrunnlag.setDatoKravDannet(konvertDato(LocalDate.of(2016, 6, 1)));
        PeriodeDto periodeDto = new PeriodeDto();
        periodeDto.setFom(konvertDato(LocalDate.of(2016, 3, 1)));
        periodeDto.setTom(konvertDato(LocalDate.of(2016, 5, 26)));
        kravgrunnlag.setPeriode(periodeDto);
        return kravgrunnlag;
    }

    private static XMLGregorianCalendar konvertDato(LocalDate localDate) {
        return DatatypeFactory.newDefaultInstance().newXMLGregorianCalendar(localDate.toString());
    }
}