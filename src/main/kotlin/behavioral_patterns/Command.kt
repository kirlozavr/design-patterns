package behavioral_patterns

internal fun command() {
    println("\nCommand Pattern")

    val calculator = Calculator().apply {
        addCommand(AddCommand(10f))
        addCommand(MultiplyCommand(10f))
        addCommand(SubtractCommand(10f))
        addCommand(DivideCommand(9f))
        addCommand(DivideCommand(10f))
        undo()
    }
    val result = calculator.calculate()
    println("The result of the calculation is $result")
}

internal interface Command {
    fun execute(currentValue: Float): Float
}

internal class AddCommand constructor(
    private val operand: Float
): Command {
    override fun execute(currentValue: Float): Float {
        return currentValue + operand
    }
}

internal class SubtractCommand constructor(
    private val operand: Float
): Command {
    override fun execute(currentValue: Float): Float {
        return currentValue - operand
    }
}

internal class DivideCommand constructor(
    private val operand: Float
): Command {
    override fun execute(currentValue: Float): Float {
        return currentValue / operand
    }
}

internal class MultiplyCommand constructor(
    private val operand: Float
): Command {
    override fun execute(currentValue: Float): Float {
        return currentValue * operand
    }
}

internal class Calculator {
    private val operations = mutableListOf<Command>()

    internal fun addCommand(command: Command) {
        operations.add(command)
    }

    internal fun undo() {
        operations.removeLastOrNull()
    }

    internal fun calculate(initialValue: Float = 0f): Float {
        var result = initialValue
        operations.forEach {
            result = it.execute(result)
        }
        return result
    }
}