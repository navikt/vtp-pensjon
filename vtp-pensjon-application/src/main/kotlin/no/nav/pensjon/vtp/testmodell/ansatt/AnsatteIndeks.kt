package no.nav.pensjon.vtp.testmodell.ansatt

class AnsatteIndeks {
    private val ansatte: MutableList<NAVAnsatt> = ArrayList()

    fun findAll(): List<NAVAnsatt> {
        return ansatte
    }

    fun save(ansatt: NAVAnsatt) = ansatt.also(ansatte::add)

    fun saveAll(ansatte: List<NAVAnsatt>) {
        this.ansatte.addAll(ansatte)
    }

    fun findByCn(ident: String): NAVAnsatt? {
        return ansatte.firstOrNull { it.cn == ident }
    }

    fun findByEmail(email: String): NAVAnsatt? {
        return ansatte.firstOrNull { it.email == email }
    }

    fun findBySnIgnoreCase(sn: String): NAVAnsatt? {
        return ansatte.firstOrNull { sn.equals(it.sn, ignoreCase = true) }
    }

    fun findByEnhetsId(enhetsId: Long): List<NAVAnsatt> {
        return ansatte.filter { it.enheter.contains(enhetsId) }
    }
}
