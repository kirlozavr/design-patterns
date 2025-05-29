package behavioral_patterns

internal fun strategy() {
    println("\nStrategy Pattern")

    val travellingClient = TravellingClient(Bus())
    travellingClient.travel()
    travellingClient.change(Plane())
    travellingClient.travel()
    travellingClient.change(Ship())
    travellingClient.travel()
}

internal interface Transport {
    fun travel()
}

internal class Bus: Transport {
    override fun travel() {
        println("Travel by Bus")
    }
}

internal class Plane: Transport {
    override fun travel() {
        println("Travel by Plane")
    }
}

internal class Ship: Transport {
    override fun travel() {
        println("Travel by Ship")
    }
}

internal class TravellingClient constructor(
    private var transport: Transport
) {

    internal fun travel() {
        transport.travel()
    }

    internal fun change(transport: Transport) {
        this@TravellingClient.transport = transport
    }
}