package behavioral_patterns

internal fun interpreter() {
    println("\nInterpreter Pattern")

    val multiplicationExpression = MultiplicationExpression(
        left = NumberExpression(10f),
        right = NumberExpression(10f)
    )
    val additionExpression = AdditionExpression(
        left = NumberExpression(10f),
        right = NumberExpression(10f)
    )

    val expression = AdditionExpression(
        left = NumberExpression(2f),
        right = AdditionExpression(
            left = NumberExpression(2f),
            right = NumberExpression(6f)
        )
    )
    println("Multiplication Result=${multiplicationExpression.interpret()}")
    println("Addition Result=${additionExpression.interpret()}")
    println("Last Expression Result=${expression.interpret()}")
}

internal interface Expression {
    fun interpret(): Float
}

internal class NumberExpression constructor(
    private val value: Float
): Expression {
    override fun interpret(): Float {
        return value
    }
}

internal class AdditionExpression constructor(
    private val left: Expression,
    private val right: Expression
): Expression {

    override fun interpret(): Float {
        return left.interpret() + right.interpret()
    }
}

internal class SubtractionExpression constructor(
    private val left: Expression,
    private val right: Expression
): Expression {

    override fun interpret(): Float {
        return left.interpret() - right.interpret()
    }
}

internal class DivisionExpression constructor(
    private val left: Expression,
    private val right: Expression
): Expression {

    override fun interpret(): Float {
        val divisor = right.interpret()
        if (divisor == 0f) {
            throw ArithmeticException("Division by zero")
        }
        return left.interpret() / divisor
    }
}

internal class MultiplicationExpression constructor(
    private val left: Expression,
    private val right: Expression
): Expression {

    override fun interpret(): Float {
        return left.interpret() * right.interpret()
    }
}