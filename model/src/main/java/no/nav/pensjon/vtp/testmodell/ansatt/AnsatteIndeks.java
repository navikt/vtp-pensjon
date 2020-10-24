package no.nav.pensjon.vtp.testmodell.ansatt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class AnsatteIndeks {
    private List<NAVAnsatt> ansatte = new ArrayList<>();

    public Stream<NAVAnsatt> hentAlleAnsatte() {
        return ansatte.stream();
    }

    public void leggTil(List<NAVAnsatt> ansatte) throws RuntimeException {
        this.ansatte.addAll(ansatte);
    }

    public Optional<NAVAnsatt> hentNAVAnsatt(String ident) {
        return ansatte.stream().filter(ansatt -> ansatt.cn.equals(ident)).findFirst();
    }
}
