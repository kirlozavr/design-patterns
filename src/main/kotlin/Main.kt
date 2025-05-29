import behavioral_patterns.*
import behavioral_patterns.Calculator
import behavioral_patterns.MinusCommand
import behavioral_patterns.MultiplyCommand
import behavioral_patterns.PlusCommand
import creational_patterns.*
import creational_patterns.Builder
import creational_patterns.SingletonA
import creational_patterns.SingletonB
import structural_patterns.*
import structural_patterns.AudioPlayer
import structural_patterns.ListPrinter
import structural_patterns.MediaAdapter
import structural_patterns.Thermometer
import structural_patterns.ThermometerAdapter

internal fun main(args: Array<String>) {
    builder()
    singleton()
    factoryMethod()
    abstractFactory()
    serviceLocator()

    decorator()
    adapter()
    facade()
    composite()
    proxy()

    command()
    observer()
    strategy()
    state()
    chainOfResponsibility()
    mediator()
}