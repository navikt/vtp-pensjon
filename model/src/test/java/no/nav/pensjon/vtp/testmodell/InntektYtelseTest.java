package no.nav.pensjon.vtp.testmodell;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.RelatertYtelseTema;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.ArenaMeldekort;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.ArenaSak;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.ArenaVedtak;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.SakStatus;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.VedtakStatus;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTilTemplateMapper;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;

public class InntektYtelseTest {
    private static final JsonMapper jsonMapper =  new JsonMapper();

    @Test
    public void skal_skrive_scenario_til_inntektytelse_json() throws Exception {
        IdenterIndeks identerIndeks = new IdenterIndeks();
        TestscenarioTilTemplateMapper mapper = new TestscenarioTilTemplateMapper();

        InntektYtelseModell inntektYtelse = new InntektYtelseModell();
        initArenaModell(inntektYtelse);
        initInntektskomponentModell(inntektYtelse);

        Testscenario scenario = new Testscenario("test3", null, identerIndeks.getIdenter("abc"));
        scenario.setSøkerInntektYtelse(inntektYtelse);

        String json = skrivInntektYtelse(scenario, mapper);

        System.out.println(json);
    }

    private void initInntektskomponentModell(InntektYtelseModell inntektYtelse){
        InntektskomponentModell inntektskomponentModell = inntektYtelse.getInntektskomponentModell();
        List<Inntektsperiode> inntektsperioder = lagInntektsperioder();
        inntektskomponentModell.setInntektsperioder(inntektsperioder);
    }

    private List<Inntektsperiode> lagInntektsperioder() {
        List<Inntektsperiode> resultat = new ArrayList<>();
        resultat.add(lagInntektsperiode());
        return resultat;
    }

    private Inntektsperiode lagInntektsperiode() {
        LocalDate now = LocalDate.now();
        Inntektsperiode inntektsperiode = new Inntektsperiode();
        inntektsperiode.setBeløp(5000);
        inntektsperiode.setFom(now.minusMonths(10));
        inntektsperiode.setTom(now.minusMonths(1));
        return inntektsperiode;
    }

    private void initArenaModell(InntektYtelseModell inntektYtelse) {
        ArenaSak arenaSak = lagArenaSak();
        inntektYtelse.getArenaModell().leggTil(arenaSak);
    }

    private ArenaSak lagArenaSak() {
        ArenaSak arenaSak = new ArenaSak();
        arenaSak.setSaksnummer("999");
        arenaSak.setStatus(SakStatus.AKTIV);
        arenaSak.setTema(RelatertYtelseTema.AAP);

        ArenaVedtak arenaVedtak = new ArenaVedtak();
        arenaVedtak.setStatus(VedtakStatus.IVERK);
        ArenaMeldekort meldekort = new ArenaMeldekort();
        meldekort.setBeløp(BigDecimal.TEN);
        meldekort.setDagsats(BigDecimal.valueOf(10000L));
        meldekort.setUtbetalingsgrad(BigDecimal.ONE);

        arenaVedtak.leggTil(meldekort);
        arenaSak.leggTil(arenaVedtak);
        return arenaSak;
    }

    private String skrivInntektYtelse(Testscenario scenario, TestscenarioTilTemplateMapper mapper) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream buf = new BufferedOutputStream(baos);
        mapper.skrivInntektYtelse(jsonMapper.canonicalMapper(), buf, scenario, scenario.getSøkerInntektYtelse());
        buf.flush();
        return baos.toString(UTF_8);
    }
}
