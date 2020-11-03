package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v2;

import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo;
import no.nav.pensjon.vtp.mocks.oppgave.repository.Sporing;
import no.nav.virksomhet.tjenester.oppgavebehandling.meldinger.v2.OpprettOppgave;

public class Mapper {
    public static OppgaveFoo asOppaveFoo(Sporing saksbeh, OpprettOppgave opprettOppgave) {
        return new OppgaveFoo(saksbeh, opprettOppgave);

    }
}
