package no.nav.pensjon.vtp.testmodell.enheter

import java.util.*
import java.util.function.Consumer

class EnheterIndeks(private val byDiskresjonskode: MutableMap<String, Norg2Modell> = HashMap()) {
    fun saveAll(enheter: Collection<Norg2Modell>) {
        enheter.forEach(Consumer { m: Norg2Modell -> byDiskresjonskode.putIfAbsent(m.diskresjonskode, m) })
    }

    fun finnByDiskresjonskode(diskresjonskode: String): Norg2Modell? {
        return byDiskresjonskode[diskresjonskode]
    }

    val alleEnheter: Collection<Norg2Modell>
        get() = byDiskresjonskode.values

    fun findByEnhetIdIn(enhetIds: Collection<String>): Set<Norg2Modell> {
        return enhetIds
            .mapNotNull { finnByEnhetId(it) }
            .toSet()
    }

    fun finnByEnhetId(enhetId: String): Norg2Modell? {
        return byDiskresjonskode.values.firstOrNull { enhetId == it.enhetId }
    }
}
