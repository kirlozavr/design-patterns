package behavioral_patterns

internal fun chainOfResponsibility() {
    println("\nChainOfResponsibility Pattern")

    val first = FirstChain()
    val second = SecondChain(first)
    val third = ThirdChain(second)
    val result = third.handleMessage("Some message")
    println(result)
}

internal interface Chain {
    fun handleMessage(inputMessage: String): String
}

internal class FirstChain constructor(
    private val next: Chain? = null
): Chain {
    override fun handleMessage(inputMessage: String): String {
        val message = "The First handler received the message: [$inputMessage]"
        return next?.handleMessage(message) ?: message
    }
}

internal class SecondChain constructor(
    private val next: Chain? = null
): Chain {
    override fun handleMessage(inputMessage: String): String {
        val message = "The Second handler received the message: [$inputMessage]"
        return next?.handleMessage(message) ?: message
    }
}

internal class ThirdChain constructor(
    private val next: Chain? = null
): Chain {
    override fun handleMessage(inputMessage: String): String {
        val message = "The Third handler received the message: [$inputMessage]"
        return next?.handleMessage(message) ?: message
    }
}