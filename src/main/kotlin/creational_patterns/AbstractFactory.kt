package creational_patterns

import java.lang.IllegalStateException

internal fun abstractFactory() {
    println("\nAbstractFactory Pattern")

    val uiFactory = AbstractFactory.createFactory("android")
    val button = uiFactory.createButton()
    button.click()
    val dialog = uiFactory.createDialog()
    dialog.show()
}

internal interface Button {
    fun click()
}

internal interface Dialog {
    fun show()
}

internal class AndroidButton: Button {
    override fun click() {
        println("Android Button clicked")
    }
}

internal class IosButton: Button {
    override fun click() {
        println("Ios Button clicked")
    }
}

internal class AndroidDialog: Dialog {
    override fun show() {
        println("Android Dialog showed")
    }
}

internal class IosDialog: Dialog {
    override fun show() {
        println("Ios Dialog showed")
    }
}

internal interface UiFactory {
    fun createButton(): Button
    fun createDialog(): Dialog
}

internal class AndroidUiFactory: UiFactory {
    override fun createButton(): Button {
        return AndroidButton()
    }

    override fun createDialog(): Dialog {
        return AndroidDialog()
    }
}

internal class IosUiFactory: UiFactory {
    override fun createButton(): Button {
        return IosButton()
    }

    override fun createDialog(): Dialog {
        return IosDialog()
    }
}

internal object AbstractFactory {
    internal fun createFactory(system: String): UiFactory {
        return when(system) {
            "android" -> AndroidUiFactory()
            "ios" -> IosUiFactory()
            else -> throw IllegalStateException("Must only be android or ios")
        }
    }
}