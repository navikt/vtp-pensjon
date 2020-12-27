package no.nav.pensjon.vtp.utilities

import javassist.ClassPool
import javassist.CtClass
import javassist.LoaderClassPath
import java.lang.Thread.currentThread
import java.lang.reflect.Method

/**
 * Creates a string representation of the method call that the
 * IntelliJ console identifies and creates into a link.
 */
fun description(method: Method): String {
    try {
        val classpool = ClassPool.getDefault()
        classpool.appendClassPath(LoaderClassPath(currentThread().contextClassLoader))

        val declaringClass = method.declaringClass

        val name: String = if (declaringClass.isMemberClass) {
            val index = declaringClass.canonicalName.lastIndexOf(".")
            declaringClass.canonicalName.replaceRange(index, index + 1, "$")
        } else {
            declaringClass.canonicalName
        }
        val cc: CtClass = classpool.get(name)

        val javassistMethod = cc.getDeclaredMethod(method.name)
        val linenumber = javassistMethod.methodInfo.getLineNumber(0)
        return "${declaringClass.canonicalName}.${method.name}(${javassistMethod.declaringClass.classFile.sourceFile}:$linenumber)"
    } catch (e: Exception) {
        return method.toString()
    }
}
