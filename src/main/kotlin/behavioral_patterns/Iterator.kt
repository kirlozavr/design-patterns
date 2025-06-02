package behavioral_patterns

internal fun iterator() {
    println("\nIterator Pattern")

    val listIterable = ListIterable<Int>()
    listIterable.add(1)
    listIterable.add(2)
    listIterable.add(3)

    val iterator = listIterable.iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        println(element)
    }
}

internal interface Iterator <out T> {
    fun hasNext(): Boolean

    fun next(): T
}

internal class ListIterator <out T> constructor(
    private val list: List<T>
): Iterator<T> {
    private var position: Int = 0

    override fun hasNext(): Boolean {
        return position != list.size
    }

    override fun next(): T {
        return list[position++]
    }
}

internal interface Iterable <out T> {
    fun iterator(): Iterator<T>
}

internal class ListIterable <T> : Iterable<T> {
    private val list = mutableListOf<T>()

    internal fun add(element: T) {
        list.add(element)
    }

    override fun iterator(): Iterator<T> {
        return ListIterator(list = list)
    }
}