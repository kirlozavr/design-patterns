package structural_patterns

internal fun decorator() {
    println("\nDecorator Pattern")

    val listPrinter = ListPrinter(
        list = mutableListOf(124, 142)
    )
    listPrinter.add(966)
}

internal class ListPrinter <T> constructor(
    private val list: MutableList<T>
): MutableList<T> by list {

    override fun add(element: T): Boolean {
        println(element)
        return list.add(element)
    }
}