package no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn;

import io.swagger.annotations.Api;
import no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn.dto.Personinfo;
import no.nav.pensjon.vtp.mocks.tps.proxy.api.v1.innsyn.dto.Relasjon;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/innsyn", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"innsyn-controller"})
public class InnsynMock {
    @GetMapping("/person")
    public Personinfo hentPersoninfoForIdent(@NotNull @RequestHeader("Authorization") String authToken,
                                             @NotNull @RequestHeader("Nav-Call-Id") String callId,
                                             @NotNull @RequestHeader("Nav-Consumer-Id") String consumerId,
                                             @NotNull @RequestHeader("Nav-Personident") String personident) {

        return Personinfo.builder().build();
    }

    @GetMapping("/relasjon")
    public List<Relasjon> hentRelasjonsinfoForIdent(@NotNull @RequestHeader("Authorization") String authToken,
                                                    @NotNull @RequestHeader("Nav-Call-Id") String callId,
                                                    @NotNull @RequestHeader("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @RequestHeader("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }

    @GetMapping("/barn")
    public List<Relasjon> hentBarneListeForIdent(@NotNull @RequestHeader("Authorization") String authToken,
                                                    @NotNull @RequestHeader("Nav-Call-Id") String callId,
                                                    @NotNull @RequestHeader("Nav-Consumer-Id") String consumerId,
                                                    @NotNull @RequestHeader("Nav-Personident") String personident) {

        return List.of(Relasjon.builder().build());
    }
}
