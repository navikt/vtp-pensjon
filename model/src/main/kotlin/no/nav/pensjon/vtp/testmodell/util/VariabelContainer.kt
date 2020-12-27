package no.nav.pensjon.vtp.testmodell.util

import java.util.*
import java.util.function.BiConsumer
import java.util.function.Function

class VariabelContainer {
    private val vars: NavigableMap<String, String?>

    constructor() {
        vars = TreeMap()
    }

    constructor(variable: Map<String, String>) {
        vars = TreeMap(variable)
    }

    fun getVar(key: String): String? {
        return vars[key]
    }

    fun getVars(): Map<String, String?> {
        return vars
    }

    fun putAll(vars2: VariabelContainer) {
        putAll(vars2.getVars())
    }

    fun putAll(vars2: Map<String, String?>) {
        vars2.forEach { (key: String, value: String?) -> cleanKey(key)?.let { vars[it] = value } }
    }

    fun forEach(action: BiConsumer<String, String?>) {
        vars.forEach(action)
    }

    fun computeIfAbsent(key: String, mappingFunction: Function<String, String?>): String? {
        return cleanKey(key)?.let {
            vars.computeIfAbsent(it, mappingFunction)
        }
    }

    companion object {
        private fun cleanKey(dirtyKey: String?): String? {
            if (dirtyKey == null) {
                return null
            }
            val matcher = FindTemplateVariables.templateVariablePattern.matcher(dirtyKey)
            return if (matcher.find()) matcher.group(1) else dirtyKey
        }
    }
}
