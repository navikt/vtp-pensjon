package no.nav.pensjon.vtp.mocks.virksomhet.person.v3;

import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;

public class FinnPerson {
    private final PersonIndeks personIndeks;

    public FinnPerson(PersonIndeks personIndeks) {
        this.personIndeks = personIndeks;
    }

    public PersonModell finnPerson(Aktoer aktoer) throws HentPersonPersonIkkeFunnet {

        BrukerModell bruker;
        String ident;
        if (aktoer instanceof PersonIdent) {
            PersonIdent personIdent = (PersonIdent) aktoer;
            ident = personIdent.getIdent().getIdent();
            bruker = personIndeks.finnByIdent(ident);
        } else {
            AktoerId aktoerId = (AktoerId) aktoer;
            ident = aktoerId.getAktoerId();
            bruker = personIndeks.finnByAktørIdent(ident);
        }

        if (bruker == null) {
            throw new HentPersonPersonIkkeFunnet("BrukerModell ikke funnet:" + ident, new PersonIkkeFunnet());
        } else if (!(bruker instanceof PersonModell)) {
            throw new IllegalStateException("Fant ikke bruker av type PersonModell for ident:" + ident + ", fikk:" + bruker);
        }
        return (PersonModell)bruker;
    }

}
