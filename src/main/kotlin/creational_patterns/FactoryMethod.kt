package creational_patterns

internal fun factoryMethod() {
    println("\nFactoryMethod Pattern")

    val currency = CurrencyFactory.currencyByCountry("USA")
    println(currency.code())
}

internal interface Currency {
    fun code(): String
}

internal class Rubble: Currency {
    override fun code(): String = "RUB"
}

internal class Dollar: Currency {
    override fun code(): String = "USD"
}

internal class Euro: Currency {
    override fun code(): String = "EUR"
}

internal interface Factory {
    fun currencyByCountry(country: String): Currency
}

internal object CurrencyFactory: Factory {
    override fun currencyByCountry(country: String): Currency {
        return when(country) {
            "Russia" -> Rubble()
            "USA" -> Dollar()
            "Europe" -> Euro()
            else -> throw IllegalStateException("The country must only be [Russia, USA, Europe]")
        }
    }
}