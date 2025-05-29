package creational_patterns

internal fun singleton() {
    println("\nSingleton Pattern")

    val singletonA = SingletonA
    singletonA.printHello()

    val singletonB = SingletonB.getInstance()
    singletonB.printHello()
}

internal interface Singleton {
    fun printHello()
}

internal object SingletonA: Singleton {

    override fun printHello() {
        println("Hello from SingletonA")
    }
}

internal class SingletonB private constructor(): Singleton {

    override fun printHello() {
        println("Hello from SingletonB")
    }

    companion object {
        @Volatile
        private var instance: SingletonB? = null

        internal fun getInstance(): SingletonB {
            val current = instance
            if (current != null) return current
            return synchronized(this) {
                SingletonB().also { instance = it }
            }
        }
    }
}