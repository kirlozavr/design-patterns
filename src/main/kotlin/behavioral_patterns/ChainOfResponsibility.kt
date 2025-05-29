package behavioral_patterns

internal fun chainOfResponsibility() {
    println("\nChainOfResponsibility Pattern")

    val first = FirstChain()
    val second = SecondChain(first)
    val third = ThirdChain(second)
    val result = third.addMessage("The beginning of the chain: ")
    println(result)
}

internal interface Chain {
    fun addMessage(inputMessage: String): String
}

internal class FirstChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("First ")
    }
}

internal class SecondChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("Second ")
    }
}

internal class ThirdChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("Third ")
    }
}