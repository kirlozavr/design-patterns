package structural_patterns

internal fun serviceLocator() {
    println("\nServiceLocator Pattern")

    val firstService = ServiceLocator.getService<FirstService>()
    val secondService = ServiceLocator.getService<SecondService>()

    println("firstService name is ${firstService.getName()} and secondService name is ${secondService.getName()}")
}

internal interface Service {
    fun getName(): String
}

internal class FirstService: Service {
    override fun getName(): String = "FirstService"
}

internal class SecondService: Service {
    override fun getName(): String = "SecondService"
}

internal object ServiceLocator {
    private val services = mutableMapOf<String, Service>()

    internal inline fun <reified T: Service> getService(): Service {
        return services.getOrPut(T::class.java.simpleName) {
            T::class.java.getDeclaredConstructor().newInstance()
        }
    }
}