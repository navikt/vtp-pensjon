package no.nav.pensjon.vtp.testmodell.enheter

class EnheterIndeks(private val byDiskresjonskode: MutableMap<String, Norg2Modell> = HashMap()) {
    fun saveAll(enheter: Collection<Norg2Modell>) {
        enheter.forEach { m: Norg2Modell -> byDiskresjonskode.putIfAbsent(m.diskresjonskode, m) }
    }

    fun finnByDiskresjonskode(diskresjonskode: String): Norg2Modell? {
        return byDiskresjonskode[diskresjonskode]
    }

    val alleEnheter: Collection<Norg2Modell>
        get() = byDiskresjonskode.values

    fun findByEnhetIdIn(enhetIds: Collection<Long>): Set<Norg2Modell> {
        return enhetIds
            .mapNotNull { finnByEnhetId(it) }
            .toSet()
    }

    fun finnByEnhetId(enhetId: Long): Norg2Modell? {
        return byDiskresjonskode.values.firstOrNull { enhetId == it.enhetId }
    }
}
