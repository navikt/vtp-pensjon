package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.time.LocalDate;

public abstract class VoksenModell extends PersonModell {

    public VoksenModell() {
    }

    public VoksenModell(String lokalIdent, String navn, LocalDate fødselsdato, Kjønn kjønn) {
        super(lokalIdent, navn, fødselsdato);
        setKjønn(kjønn);
    }

    @Override
    public String getIdent() {
        return getIdenter() == null
            ? null
            : getFødselsdatoFraVars(getLokalIdent())
                .map(dato -> getIdenter().getVoksenIdentForLokalIdent(getLokalIdent(), getKjønn(), dato))
                .orElse(getIdenter().getVoksenIdentForLokalIdent(getLokalIdent(), getKjønn()));
    }
}
