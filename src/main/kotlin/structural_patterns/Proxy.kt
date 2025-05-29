package structural_patterns

internal fun proxy() {
    println("\nProxy Pattern")

    val realSubject = RealSubject()
    val proxySubject = ProxySubject(realSubject)
    val result = proxySubject.operation()
    println(result)
}

internal interface Subject {
    fun operation(): String
}

internal class RealSubject: Subject {
    override fun operation(): String {
        return "This is real Subject"
    }
}

internal class ProxySubject constructor(
    private val subject: Subject
): Subject {

    override fun operation(): String {
        return "${subject.operation()} with proxy Subject"
    }
}