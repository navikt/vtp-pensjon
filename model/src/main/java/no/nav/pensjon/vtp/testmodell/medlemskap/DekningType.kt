package no.nav.pensjon.vtp.testmodell.medlemskap

data class DekningType(val kode: String) {
    companion object {
        private var VALID_KODER = setOf(
            "IT_DUMMY_EOS",
            "IHT_Avtale",
            "FTL_2-7_3_ledd_a",
            "FTL_2-7_3_ledd_b",
            "Full",
            "FTL_2-9_1_ledd_a",
            "FTL_2-9_a",
            "FTL_2-9_1_ledd_c",
            "FTL_2-9_1_ledd_b",
            "FTL_2-7_bok_a",
            "FTL_2-7_bok_b",
            "FTL_2-9_b",
            "FTL_2-9_c",
            "PENDEL",
            "FTL_2-9_2_ld_jfr_1a",
            "Unntatt",
            "FTL_2-9_2_ld_jfr_1c",
            "Opphor",
            "IKKEPENDEL",
            "IT_DUMMY",
            "FTL_2-9_2_ledd",
            "IHT_Avtale_Forord",
            "FTL_2-6"
        )

        val IHT_AVTALE = DekningType("IHT_Avtale")
    }

    init {
        require(VALID_KODER.contains(kode)) { "Kode er ikke gyldig Medl2 trygdedekning type: $kode" }
    }
}
