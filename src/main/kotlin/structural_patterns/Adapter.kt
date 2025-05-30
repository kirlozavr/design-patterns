package structural_patterns

internal fun adapter() {
    println("\nAdapter Pattern")

    val oldPrinter = OldPrinter()
    val newPrinter = NewPrinterAdapter(printer = oldPrinter)
    newPrinter.print("Hello")

    val adapter = object : UiAdapter {
        override fun createText(): String {
            return "Some message from the UiAdapter"
        }
    }
    val uiSystem = UiSystem()
    uiSystem.drawUi(adapter)
}

internal interface Printer {
    fun print(message: String)
}

internal interface NewPrinter {
    fun print(message: String)
}

internal class OldPrinter: Printer {
    override fun print(message: String) {
        println("OldPrinter printed the \"$message\"")
    }
}

internal class NewPrinterAdapter constructor(
    private val printer: Printer
): NewPrinter {

    override fun print(message: String) {
        printer.print("($message) message")
    }
}

internal interface UiAdapter {

    fun createText(): String
}

internal interface UiCanvas {

    fun drawText(message: String)
}

internal class UiCanvasImpl: UiCanvas {

    override fun drawText(message: String) {
        println("The Canvas drew \"$message\"")
    }
}

internal class UiSystem {

    private val canvas: UiCanvas = UiCanvasImpl()

    internal fun drawUi(adapter: UiAdapter) {
        val text = adapter.createText()
        canvas.drawText(text)
    }
}