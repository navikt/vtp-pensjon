package no.nav.pensjon.vtp.mocks.oppgave.rest

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
class OppgaveIntegrationTest @Autowired constructor() {
    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    fun `Gitt en opprettet oppgave når hentoppgave blir kallt så returner oppgaven`() {

        val opprettOppgaveRequest = """
            {
              "tildeltEnhetsnr" : "99768",
              "opprettetAvEnhetsnr" : "9999",
              "journalpostId" : "511105704",
              "beskrivelse" : "Inngående P9000 - Svar på forespørsel om informasjon / Rina saksnr: 432594",
              "tema" : "PEN",
              "oppgavetype" : "JFR",
              "prioritet" : "NORM",
              "fristFerdigstillelse" : "2021-05-12",
              "aktivDato" : "2021-05-11"
            }
        """

        this.mockMvc!!.perform(
            MockMvcRequestBuilders.post("/rest/oppgave/api/v1/oppgaver")
                .content(opprettOppgaveRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        this.mockMvc.perform(
            MockMvcRequestBuilders.post("/rest/oppgave/api/v1/oppgaver")
                .content(opprettOppgaveRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

        this.mockMvc.perform(
            MockMvcRequestBuilders.get("/rest/oppgave/api/v1/oppgaver/2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("P9000")))
    }
}
