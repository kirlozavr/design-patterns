package structural_patterns

import kotlin.random.Random

internal fun adapter() {
    println("\nAdapter Pattern")

    val mediaAdapter = MediaAdapter("mp3")
    mediaAdapter.play("file name")

    val thermometer = Thermometer()
    val thermometerAdapter = ThermometerAdapter(thermometer)
    val temperature = thermometerAdapter.measureTemperature("Fahrenheit")
    println(temperature)
}

internal interface Player {
    fun play(fileName: String)
}

internal class Mp3Player: Player {
    override fun play(fileName: String) {
        println("Mp3Player is playing")
    }
}

internal class Mp4Player: Player {
    override fun play(fileName: String) {
        println("Mp4Player is playing")
    }
}

internal class MediaAdapter constructor(
    private val audioType: String
) {

    private val player: Player = when(audioType) {
        "mp3" -> Mp3Player()
        "mp4" -> Mp4Player()
        else -> throw IllegalStateException("Audio type must only be mp3 or mp4")
    }

    internal fun play(fileName: String) {
        player.play(fileName = fileName)
    }
}

internal class Thermometer {
    internal fun measureTemperature(): Float {
        return Random.nextFloat().coerceIn(-50f, 100f)
    }
}

internal class ThermometerAdapter constructor(
    private val thermometer: Thermometer
) {

    internal fun measureTemperature(temperatureType: String): Float {
        return when(temperatureType) {
            "Celsius" -> thermometer.measureTemperature()
            "Fahrenheit" -> {
                val temperatureInFahrenheit = thermometer.measureTemperature()
                return (temperatureInFahrenheit - 32) * 5 / 9
            }
            else -> throw IllegalStateException("The temperatureType must only be Celsius or Fahrenheit")
        }
    }
}