package behavioral_patterns

internal fun observer() {
    println("\nObserver Pattern")

    val actionsUpdater = ActionsUpdater()
    val observerA = Observer { action ->
        println("Observer A received \"$action\" action")
    }
    val observerB = Observer { action ->
        println("Observer B received \"$action\" action")
    }
    val observerC = Observer { action ->
        println("Observer C received \"$action\" action")
    }
    actionsUpdater.addObserver(observerA)
    actionsUpdater.addObserver(observerB)
    actionsUpdater.addObserver(observerB)
    actionsUpdater.addObserver(observerC)
    actionsUpdater.addObserver(observerC)
    actionsUpdater.removeObserver(observerC)
    actionsUpdater.update("some action")
}


internal fun interface Observer {
    fun onAction(action: String)
}

internal class ActionsUpdater {
    private val observers = mutableListOf<Observer>()

    internal fun addObserver(observer: Observer) {
        if (observers.contains(observer)) return
        observers.add(observer)
    }

    internal fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    internal fun update(newAction: String) {
        observers.forEach { it.onAction(action = newAction) }
    }
}