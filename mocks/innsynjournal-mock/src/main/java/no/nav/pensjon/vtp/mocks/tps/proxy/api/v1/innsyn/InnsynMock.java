package no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn;

import io.swagger.annotations.Api;
import no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn.dto.Personinfo;
import no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn.dto.Relasjon;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/innsyn", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"innsyn-controller"})
public class InnsynMock {
    @GetMapping("/person")
    public Personinfo hentPersoninfoForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                             @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                             @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                             @NotNull @HeaderParam("Nav-Personident") String personident) {

        return Personinfo.builder().build();
    }

    @GetMapping("/relasjon")
    public List<Relasjon> hentRelasjonsinfoForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                                    @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                                    @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @HeaderParam("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }

    @GetMapping("/barn")
    public List<Relasjon> hentBarneListeForIdent(@NotNull @HeaderParam("Authorization") String authToken,
                                                    @NotNull @HeaderParam("Nav-Call-Id") String callId,
                                                    @NotNull @HeaderParam("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @HeaderParam("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }
}
