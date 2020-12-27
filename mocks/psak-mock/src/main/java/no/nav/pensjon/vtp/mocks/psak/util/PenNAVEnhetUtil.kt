package no.nav.pensjon.vtp.mocks.psak.util

import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet

fun asboPenNAVEnhet(): ASBOPenNAVEnhet {
    return ASBOPenNAVEnhet().apply {
        enhetsId = "4407"
        enhetsNavn = "NAV Arbeid og ytelser Tønsberg"
        orgNivaKode = "GR"
        orgEnhetsId = "7000"
    }
}
