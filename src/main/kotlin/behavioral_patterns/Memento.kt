package behavioral_patterns

internal fun memento() {
    println("\nMemento Pattern")

    val originator = MessageOriginator(text = "Initial message")
    val caretaker = MessageCaretaker()
    caretaker.save(originator)

    originator.restore(Message(text = "First message"))
    originator.restore(Message(text = "Second message"))
    caretaker.save(originator)

    originator.restore(Message(text = "Third message"))
    caretaker.save(originator)
    caretaker.undo(originator)
}

internal data class Message(
    val text: String
)

internal interface Originator <T> {
    fun create(): T
    fun restore(memento: T)
}

internal class MessageOriginator constructor(
    private var text: String
): Originator<Message> {

    override fun create(): Message {
        return Message(text = text)
    }

    override fun restore(memento: Message) {
        this@MessageOriginator.text = memento.text
    }
}

internal interface Caretaker <T> {
    fun save(originator: Originator<T>)
    fun undo(originator: Originator<T>)
}

internal class MessageCaretaker: Caretaker<Message> {
    private val stack = mutableListOf<Message>()

    override fun save(originator: Originator<Message>) {
        val memento = originator.create()
        stack.add(memento)

        println("Save new $memento")
    }

    override fun undo(originator: Originator<Message>) {
        val previous = stack.removeLastOrNull() ?: return
        originator.restore(previous)

        println("Undo previous $previous")
        println("It is remaining $stack")
    }
}