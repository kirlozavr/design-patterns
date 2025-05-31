package behavioral_patterns

internal fun mediator() {
    println("\nMediator Pattern")

    val mediator = ChatMediator()
    val bob = ChatUser(mediator, "bob")
    mediator.addUser(bob)
    val martin = ChatUser(mediator, "martin")
    mediator.addUser(martin)
    val steve = ChatUser(mediator, "steve")
    mediator.addUser(steve)
    mediator.removeUser(steve)

    bob.sendMessage("Hello everyone!")
}

internal interface User {
    fun sendMessage(message: String)
    fun onReceivedMessage(message: String)
}

internal class ChatUser constructor(
    private val mediator: Mediator,
    private val name: String
): User {

    override fun sendMessage(message: String) {
        mediator.sendMessage(message = message)
    }

    override fun onReceivedMessage(message: String) {
        println("$name received the message=$message")
    }
}

internal interface Mediator {
    fun sendMessage(message: String)
    fun addUser(user: User)
    fun removeUser(user: User)
}

internal class ChatMediator: Mediator {
    private val users = mutableListOf<User>()

    override fun sendMessage(message: String) {
        users.forEach { it.onReceivedMessage(message = message) }
    }

    override fun addUser(user: User) {
        if (users.contains(user)) return
        users.add(user)
    }

    override fun removeUser(user: User) {
        users.remove(user)
    }
}