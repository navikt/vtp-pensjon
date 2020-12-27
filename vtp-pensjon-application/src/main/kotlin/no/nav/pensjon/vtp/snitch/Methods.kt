package no.nav.pensjon.vtp.utilities

import javassist.ClassPool
import javassist.CtClass
import javassist.LoaderClassPath
import java.lang.Thread.currentThread
import java.lang.reflect.Method
import javax.servlet.http.HttpServletResponse

private const val X_VTP_HANDLER = "x-vtp-handler"
private const val X_VTP_CLASS = "x-vtp-class"
private const val X_VTP_METHOD = "x-vtp-method"


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

fun addVtpHandlerMetaData(response: HttpServletResponse, string: String) {
    response.addHeader(X_VTP_HANDLER, string)
}

fun addVtpHandlerMetaData(response: HttpServletResponse, method: Method) {
    response.addHeader(X_VTP_HANDLER, description(method))
    response.addHeader(X_VTP_CLASS, method.declaringClass.simpleName)
    response.addHeader(X_VTP_METHOD, method.name)
}

fun findHandler(response: HttpServletResponse): String? {
    return findHeader(response, X_VTP_HANDLER)
}

fun findClass(response: HttpServletResponse): String? {
    return findHeader(response, X_VTP_CLASS)
}

fun findMethod(response: HttpServletResponse): String? {
    return findHeader(response, X_VTP_METHOD)
}

private fun findHeader(response: HttpServletResponse, name: String): String? {
    val headers = response.getHeaders(name)
    return if (headers.isNotEmpty()) {
        headers.iterator().next()
    } else {
        null
    }
}
