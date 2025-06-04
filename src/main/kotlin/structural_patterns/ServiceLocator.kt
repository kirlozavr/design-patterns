package structural_patterns

import kotlin.reflect.KClass

internal fun serviceLocator() {
    println("\nServiceLocator Pattern")

    val firstService = ServiceLocator.getService<FirstService>()
    val secondService = ServiceLocator.getService<SecondService>()

    println("The first service name is ${firstService.getName()} and the last one name is ${secondService.getName()}")
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

internal interface CacheManager {
    fun addService(service: Service)
    fun <T : Service> getService(serviceKClass: KClass<T>): T?
}

internal class CacheManagerImpl: CacheManager {
    private val services = mutableSetOf<Service>()

    override fun addService(service: Service) {
        services.add(service)
    }

    override fun <T : Service> getService(serviceKClass: KClass<T>): T? {
        return services.find { serviceKClass.isInstance(it) } as T?
    }
}

internal interface ServiceCreator {
    fun <T: Service> createService(serviceKClass: KClass<T>): Service
}

internal class ServiceCreatorImpl: ServiceCreator {
    override fun <T : Service> createService(serviceKClass: KClass<T>): Service {
        return when(serviceKClass) {
            FirstService::class -> FirstService()
            SecondService::class -> SecondService()
            else -> throw IllegalStateException("The service must only be FirstService or SecondService")
        }
    }
}

internal object ServiceLocator {
    private val cacheManager: CacheManager = CacheManagerImpl()
    private val serviceCreator: ServiceCreator = ServiceCreatorImpl()

    internal inline fun <reified T: Service> hasService(): Boolean {
        return cacheManager.getService(T::class) != null
    }

    internal inline fun <reified T: Service> getService(): Service {
        val service = cacheManager.getService(T::class)
        if (service != null) return service

        val newService = serviceCreator.createService(T::class) as T
        cacheManager.addService(newService)
        return newService
    }
}