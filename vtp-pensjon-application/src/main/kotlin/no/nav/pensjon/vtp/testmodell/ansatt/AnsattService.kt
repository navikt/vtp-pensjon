package no.nav.pensjon.vtp.testmodell.ansatt

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class AnsattService(
    private var ansatteIndeks: AnsatteIndeks,
    private var eventPublisher: ApplicationEventPublisher
) {
    fun addAnnsatt(
        cn: String = UUID.randomUUID().toString(),
        givenname: String = UUID.randomUUID().toString(),
        sn: String = UUID.randomUUID().toString(),
        displayName: String = UUID.randomUUID().toString(),
        email: String = UUID.randomUUID().toString(),
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
}
