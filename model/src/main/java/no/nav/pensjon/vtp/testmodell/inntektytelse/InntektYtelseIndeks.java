package no.nav.pensjon.vtp.testmodell.inntektytelse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class InntektYtelseIndeks {

    private final Map<String, InntektYtelseModell> byIdent = new ConcurrentHashMap<>();

    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return Optional.ofNullable(byIdent.get(ident));
    }

    public Stream<Map.Entry<String, InntektYtelseModell>> entries() {
        return byIdent.entrySet().stream();
    }

    public void leggTil(String ident, InntektYtelseModell iy) {
        if(ident != null && iy != null){
            byIdent.put(ident, iy);
        }
    }

    public Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId) {
        return getInntektYtelseModell(aktørId.substring(aktørId.length() - 11));
    }
}
