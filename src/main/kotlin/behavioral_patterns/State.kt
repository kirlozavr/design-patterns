package behavioral_patterns

internal fun state() {
    println("\nState Pattern")

    val stateMachine = StateMachine()
    println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
    stateMachine.setName("new name")
    println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
    stateMachine.shutdown()
    println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
}

internal sealed interface State {
    data object Uninitialized: State
    data class Initialized(
        val name: String
    ): State
}

internal class StateMachine {
    private var state: State = State.Uninitialized

    internal fun isInitialized(): Boolean {
        return state is State.Initialized
    }

    internal fun setName(name: String) {
        state = State.Initialized(name = name)
    }

    internal fun getName(): String {
        return when(val current = state) {
            is State.Initialized -> current.name
            is State.Uninitialized -> "Unknown"
        }
    }

    internal fun shutdown() {
        state = State.Uninitialized
    }
}
