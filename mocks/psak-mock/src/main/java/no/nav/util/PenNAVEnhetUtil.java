package no.nav.util;

import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet;

public class PenNAVEnhetUtil {

    public static ASBOPenNAVEnhet getAsboPenNAVEnhet() {
        ASBOPenNAVEnhet enhet = new ASBOPenNAVEnhet();
        enhet.setEnhetsId("4407");
        enhet.setEnhetsNavn("NAV Arbeid og ytelser Tønsberg");
        enhet.setOrgNivaKode("GR");
        enhet.setOrgEnhetsId("7000");
        return enhet;
    }

}
