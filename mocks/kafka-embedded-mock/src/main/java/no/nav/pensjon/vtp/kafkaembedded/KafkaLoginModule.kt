package no.nav.pensjon.vtp.kafkaembedded

import org.slf4j.LoggerFactory
import javax.security.auth.Subject
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule

class KafkaLoginModule : LoginModule {
    private var subject: Subject? = null
    override fun initialize(
        subject: Subject,
        callbackHandler: CallbackHandler,
        sharedState: Map<String?, *>?,
        options: Map<String?, *>
    ) {
        this.subject = subject
        val username = options["username"].toString()
        val password = options["password"].toString()
        this.subject!!.publicCredentials.add(username)
        this.subject!!.privateCredentials.add(password)
        LOG.info("called: initialize subject: {}, callbackHandler: {}", subject, callbackHandler)
    }

    @Throws(LoginException::class)
    override fun login(): Boolean {
        LOG.info("called: login")
        return true
    }

    @Throws(LoginException::class)
    override fun commit(): Boolean {
        LOG.info("called: commit")
        return true
    }

    @Throws(LoginException::class)
    override fun abort(): Boolean {
        LOG.info("called: abort")
        return true
    }

    @Throws(LoginException::class)
    override fun logout(): Boolean {
        LOG.info("called: logout")
        return true
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaLoginModule::class.java)
    }
}
