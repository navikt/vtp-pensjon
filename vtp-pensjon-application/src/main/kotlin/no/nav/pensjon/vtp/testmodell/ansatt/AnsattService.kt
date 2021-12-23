package no.nav.pensjon.vtp.testmodell.ansatt

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.random.Random.Default.nextInt

@Service
class AnsattService(
    private var ansatteIndeks: AnsatteIndeks,
    private var eventPublisher: ApplicationEventPublisher
) {
    fun addAnnsatt(
        cn: String = randomString(10),
        givenname: String = randomString(5),
        sn: String = randomString(5),
        displayName: String = randomString(10),
        email: String = randomString(10),
        groups: List<String>,
        enheter: List<Long>,
        generated: Boolean
    ) = ansatteIndeks.save(
        NAVAnsatt(
            cn = cn,
            givenname = givenname,
            sn = sn,
            displayName = displayName,
            email = email,
            groups = groups,
            enheter = enheter,
            generated = generated,
        )
    ).also {
        eventPublisher.publishEvent(NewAnsattEvent(it))
    }

    fun findAll(includeGenerated: Boolean) = ansatteIndeks.findAll()
        .filter { includeGenerated || !it.generated }

    fun findByCn(ident: String) = ansatteIndeks.findByCn(ident)

    companion object {
        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        private fun randomString(length: Int) = (1..length)
            .map { nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
