package creational_patterns

internal fun builder() {
    println("\nBuilder Pattern")

    val person = Builder(name = "Bob")
        .setAge(18)
        .setHeight(190)
        .build()
    println(person)
}

internal data class Person(
    val name: String,
    val age: Int?,
    val height: Int?
)

internal class Builder private constructor() {

    private lateinit var name: String
    private var age: Int? = null
    private var height: Int? = null

    internal constructor(name: String) : this() {
        this@Builder.name = name
    }

    internal fun setAge(age: Int): Builder = also {
        it.age = age
    }

    internal fun setHeight(height: Int): Builder = also {
        it.height = height
    }

    internal fun build(): Person {
        return Person(
            name = name,
            age = age,
            height = height
        )
    }
}