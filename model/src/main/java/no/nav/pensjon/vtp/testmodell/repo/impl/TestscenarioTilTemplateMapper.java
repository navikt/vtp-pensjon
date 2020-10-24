package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;

public class TestscenarioTilTemplateMapper {
    public void skrivInntektYtelse(ObjectMapper objectMapper, OutputStream out, TestscenarioImpl scenario, InntektYtelseModell inntektYtelse) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, inntektYtelse);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }

    public void skrivPersonopplysninger(ObjectMapper objectMapper, OutputStream out, TestscenarioImpl scenario) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, scenario.getPersonopplysninger());
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke skrive json for scenario: " + scenario, e);
        }
    }

}
