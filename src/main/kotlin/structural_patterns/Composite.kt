package structural_patterns

internal fun composite() {
    println("\nComposite Pattern")

    val directoryA = Directory("directoryA").apply {
        add(File("fileA1.txt"))
        add(File("fileA2.txt"))
    }
    val directoryB = Directory("directoryB").apply {
        add(File("fileB1.txt"))
        add(File("fileB2.txt"))
        add(File("fileB3.txt"))
    }
    val directoryC = Directory("directoryC").apply {
        add(File("fileC1.txt"))
        add(directoryA)
    }
    val root = Directory("root").apply {
        add(directoryB)
        add(directoryC)
    }
    root.show("")
}

internal interface FileSystem {
    fun show(indent: String)
}

internal class File constructor(
    private val name: String
): FileSystem {

    override fun show(indent: String) {
        println("${indent}File( $name )")
    }
}

internal class Directory constructor(
    private val name: String
): FileSystem {

    private val children = mutableListOf<FileSystem>()

    internal fun add(fileSystem: FileSystem) {
        children.add(fileSystem)
    }

    override fun show(indent: String) {
        println("${indent}Directory( $name ):")
        children.forEach { it.show("$indent    ") }
    }
}