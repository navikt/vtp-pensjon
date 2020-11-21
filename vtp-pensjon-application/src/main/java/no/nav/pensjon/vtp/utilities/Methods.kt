package no.nav.pensjon.vtp.utilities

import javassist.ClassPool
import javassist.CtClass
import javassist.LoaderClassPath
import java.lang.reflect.Method

/**
 * Creates a string representation of the method call that the
 * IntelliJ console identifies and creates into a link.
 */
fun description(method: Method): String {
    val classpool = ClassPool.getDefault();
    classpool.appendClassPath(LoaderClassPath(Thread.currentThread().contextClassLoader));

    val cc: CtClass = classpool.get(method.declaringClass.canonicalName)

    val javassistMethod = cc.getDeclaredMethod(method.name)
    val linenumber = javassistMethod.methodInfo.getLineNumber(0)
    return "at ${method.declaringClass.canonicalName}.${method.name}(${javassistMethod.declaringClass.classFile.sourceFile}:$linenumber)"
}
