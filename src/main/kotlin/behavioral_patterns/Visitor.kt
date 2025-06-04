package behavioral_patterns

internal fun visitor() {
    println("\nVisitor Pattern")

    val receipt = Receipt()
    receipt.add(Milk(price = 150f, weight = 1000f))
    receipt.add(Tea(price = 100f, weight = 50f))
    receipt.add(Tea(price = 100f, weight = 50f))
    receipt.remove(Tea(price = 100f, weight = 50f))
    receipt.add(Chocolate(price = 200f, weight = 80f))

    val priceCalculator = PriceCalculator()
    val weightCalculator = WeightCalculator()

    receipt.accept(priceCalculator)
    receipt.accept(weightCalculator)

    println("Total price is ${priceCalculator.result} and total weight is ${weightCalculator.result}")
}

internal interface Visitable {
    fun <T: Visitable> accept(visitor: Visitor<T>)
}

internal interface PriceValue: Visitable {
    val price: Float
}

internal interface WeightValue: Visitable {
    val weight: Float
}

internal data class Milk(
    override val price: Float,
    override val weight: Float
): PriceValue, WeightValue {
    override fun <T: Visitable> accept(visitor: Visitor<T>) {
        visitor.visit(this as T)
    }
}

internal data class Tea(
    override val price: Float,
    override val weight: Float
): PriceValue, WeightValue {
    override fun <T: Visitable> accept(visitor: Visitor<T>) {
        visitor.visit(this as T)
    }
}

internal data class Chocolate(
    override val price: Float,
    override val weight: Float
): PriceValue, WeightValue {
    override fun <T: Visitable> accept(visitor: Visitor<T>) {
        visitor.visit(this as T)
    }
}

internal interface Visitor <T: Visitable> {
    val result: Float
    fun visit(item: T)
}

internal class PriceCalculator : Visitor<PriceValue> {
    private var _result: Float = 0f
    override val result get() = _result

    override fun visit(item: PriceValue) {
        _result += item.price
    }
}

internal class WeightCalculator : Visitor<WeightValue> {
    private var _result: Float = 0f
    override val result get() = _result

    override fun visit(item: WeightValue) {
        _result += item.weight
    }
}

internal class Receipt: Visitable {
    private val items = mutableListOf<Visitable>()

    internal fun add(item: Visitable) {
        items.add(item)
    }

    internal fun remove(item: Visitable) {
        items.remove(item)
    }

    override fun <T : Visitable> accept(visitor: Visitor<T>) {
        items.forEach { visitor.visit(it as T) }
    }
}