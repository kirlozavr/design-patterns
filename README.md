# Обзор архитектурных и design паттернов

## Архитектурные паттерны

## MVVM

### Как устроен?

Архитектурный паттерн `MVVM` состоит из:
1) `Model` - слой данных и бизнес логики;
2) `View` - слоя представления данных;
3) `ViewModel` - связывает View и Model. Тут подготавливаются данные для отображения на слое представления (View).

Из особенностей можно выделить то, что ViewModel ничего не знает о View, а View, в свою очередь, знает о ViewModel.
View слой не должен иметь никакой логики, кроме той, что отвечает за обновление UI, всю подготовку и обработку данных выполняет ViewModel или Model слой.

Уведомление ViewModel о событиях происходит через методы `viewModel.onSaveButtonClicked()` и т.д., которые дергает View слой.
Чтобы получать обновления UI, View слой подписывается на изменение соответствующих данных у ViewModel, например, `viewModel.onProfileImageEvent()`.
Для этого можно использовать LiveData, Flow или любой другой инструмент поддерживающий Observable паттерн.
Так же можно подписываться на изменения в самом XML.

Схема взаимодействия между слоями в MVVM представлена ниже:

![img.png](images/img1.png)

## MVI

### Как устроен?

Архитектурный паттерн `MVI` состоит из:
1) `Model` - слой данных и бизнес логики;
2) `View` - слоя представления данных;
3) `Intent` - намерения пользователей (действия).

Главная идея `MVI` заключается в управлении состоянием экрана через единый источник правды, еще может встречаться следующий термин: однонаправленный поток данных (Unidirectional Data Flow, сокр. UDF).
Т.е. UI наделяется некоторым состоянием `State`, которое и описывает текущий интерфейс.
Обновление State происходит в результате определенных действий пользователя.

Я предпочитаю оперировать следующими 2 терминами: `State` - непосредственно состояние UI и `Event` - оно же намерение (Intent) пользователя, например, нажатие кнопки сохранить.
Так же я предпочитаю в MVI выделять слой, который по сути будет напоминать ViewModel слой из MVVM и будет связывать логику View и Model слоев.
View уведомляет ViewModel о событиях через `viewModel.onEvent()`, а события назад приходят через подписку на изменение State, например, `viewModel.viewState`.
Дополнительно, хочется напомнить, что ViewModel ничего не знает о View, а вот View уже знает о ViewModel.

![img.png](images/img2.png)

## Паттерны проектирования

Паттерны проектирования я бы описал как набор практик для решения типовых задач и/или проблем.
Базово они делятся на 3 категории:
1) `Порождающие паттерны` или `creational patterns` - которые решают проблему создания объектов;
2) `Структурные паттерны` или `structural patterns` - которые упрощают организацию классов;
3) `Поведенческие паттерны` или `behavioral patterns` - которые решают проблему взаимодействия между объектами.

К **порождающим паттернам** относятся:
1) Builder;
2) Singleton;
3) Factory Method;
4) Abstract Factory.

К **структурным паттернам** относятся:
1) Decorator;
2) Adapter;
3) Facade;
4) Composite;
5) Proxy;
6) Service Locator (Отдельно от остальных).

К **поведенческим паттернам** относятся:
1) Command;
2) Observer;
3) Strategy;
4) State;
5) Chain Of Responsibility;
6) Mediator.

## Creational patterns

`Creational patterns` решают проблему создания объектов.

## Builder

Паттерн `Строитель` используется для гибкого создания объектов. 

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
val person = Builder(name = "Bob")
        .setAge(18)
        .setHeight(190)
        .build()
```

**Вывод:**

```txt
Person(name=Bob, age=18, height=190)
```

## Singleton

`Singleton` или же паттерн `Одиночка` гарантирует, что существует только один экземпляр объекта в момент работы программы.

**Реализация:**

```kotlin
internal object SingletonA {

    internal fun printHello() {
        println("Hello from SingletonA")
    }
}
```

```kotlin
internal class SingletonB private constructor() {

    internal fun printHello() {
        println("Hello from SingletonB")
    }

    companion object {
        @Volatile
        private var instance: SingletonB? = null

        internal fun getInstance(): SingletonB {
            val current = instance
            if (current != null) return current
            return synchronized(this) {
                SingletonB().also { instance = it }
            }
        }
    }
}
```

**Применение:**

```kotlin
SingletonA.printHello()

val singletonB = SingletonB.getInstance()
singletonB.printHello()
```

**Вывод:**

```txt
Hello from SingletonA
Hello from SingletonB
```

## Factory Method

`Фабричный метод` абстрагирует создание объектов, так что тип создаваемого экземпляра может быть определен в runtime.

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
val currency = CurrencyFactory.currencyByCountry("USA")
println(currency.code())
```

**Вывод:**

```txt
USD
```

## Abstract Factory

`Абстрактная фабрика` абстрагирует создание целой группы объектов и делегирует эту работу своим подклассам. Конкретная группа может быть определена как в runtime, так и во время компиляции.

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
val uiFactory = AbstractFactory.createFactory("android")
val button = uiFactory.createButton()
button.click()
val dialog = uiFactory.createDialog()
dialog.show()
```

**Вывод:**

```txt
Android Button clicked
Android Dialog showed
```

## Structural patterns

`Structural patterns` упрощают организацию классов.

## Decorator

Паттерн `Decorator` позволяет расширять и/или изменять функциональность объектов.
Обеспечивается это путем заворачивания объекта в класс-обертку. 

**Реализация:**

```kotlin
internal class ListPrinter <T> constructor(
    private val list: MutableList<T>
): MutableList<T> by list {

    override fun add(element: T): Boolean {
        println(element)
        return list.add(element)
    }
}
```

**Применение:**

```kotlin
val listPrinter = ListPrinter(
        list = mutableListOf(124, 142)
    )
listPrinter.add(966)
```

**Вывод:**

```txt
966
```

## Adapter

`Adapter` позволяет связывать 2 несовместимых объекта

**Реализация:**

```kotlin
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
```

```kotlin
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
```

**Применение:**

```kotlin
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
```

**Вывод:**

```txt
OldPrinter printed the "(Hello) message"
The Canvas drew "Some message from the UiAdapter"
```

## Facade

`Facade` облегчает работу с более сложной системой путем предоставления упрощенного интерфейса.

**Реализация:**

```kotlin
internal interface Player {
    fun play(name: String)
}

internal class AudioPlayer: Player {
    override fun play(name: String) {
        println("AudioPlayer is playing the \"$name\"")
    }
}

internal class VideoPlayer: Player {
    override fun play(name: String) {
        println("VideoPlayer is playing the \"$name\"")
    }
}

internal interface VideoHosting {
    fun watchVideo(name: String)
}

internal class SomeMediaHosting constructor(
    private val audioPlayer: AudioPlayer,
    private val videoPlayer: VideoPlayer
): VideoHosting {

    override fun watchVideo(name: String) {
        audioPlayer.play(name = name)
        videoPlayer.play(name = name)
    }
}
```

**Применение:**

```kotlin
val audioPlayer = AudioPlayer()
    val videoPlayer = VideoPlayer()
    val mediaHosting = SomeMediaHosting(
        audioPlayer = audioPlayer,
        videoPlayer = videoPlayer
    )
    mediaHosting.watchVideo("The best video")
```

**Вывод:**

```txt
AudioPlayer is playing the "The best video"
VideoPlayer is playing the "The best video"
```

## Composite

`Composite` помогает группировать объекты в древовидную структуру для представления иерархии от частного к целому. Позволяет одинаково работать как с отдельными объектами, так и с группой.

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
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
```

**Вывод:**

```txt
Directory( root ):
    Directory( directoryB ):
        File( fileB1.txt )
        File( fileB2.txt )
        File( fileB3.txt )
    Directory( directoryC ):
        File( fileC1.txt )
        Directory( directoryA ):
            File( fileA1.txt )
            File( fileA2.txt )
```

## Proxy

`Proxy` предоставляет объект-заместитель, контролирующий доступ к другому объекту.

**Реализация:**

```kotlin
internal interface Subject {
    fun operation(): String
}

internal class RealSubject: Subject {
    override fun operation(): String {
        return "This is real Subject"
    }
}

internal class ProxySubject constructor(
    private val subject: Subject
): Subject {

    override fun operation(): String {
        return "${subject.operation()} with proxy Subject"
    }
}
```

**Применение:**

```kotlin
val realSubject = RealSubject()
val proxySubject = ProxySubject(realSubject)
val result = proxySubject.operation()
println(result)
```

**Вывод:**

```txt
This is real Subject with proxy Subject
```

## Service Locator

`Service Locator` необходим для создания, хранения и возврата экземпляра объекта (Сервиса) по требованию. 

**Реализация:**

```kotlin
internal interface Service {
    fun getName(): String
}

internal class FirstService: Service {
    override fun getName(): String = "FirstService"
}

internal class SecondService: Service {
    override fun getName(): String = "SecondService"
}

internal interface CacheManager {
    fun addService(service: Service)

    fun <T : Service> getService(serviceKClass: KClass<T>): T?
}

internal class CacheManagerImpl: CacheManager {
    private val services = mutableSetOf<Service>()

    override fun addService(service: Service) {
        services.add(service)
    }

    override fun <T : Service> getService(serviceKClass: KClass<T>): T? {
        return services.find { serviceKClass.isInstance(it) } as T?
    }
}

internal interface ServiceCreator {

    fun <T: Service> createService(serviceKClass: KClass<T>): Service
}

internal class ServiceCreatorImpl: ServiceCreator {
    override fun <T : Service> createService(serviceKClass: KClass<T>): Service {
        return when(serviceKClass) {
            FirstService::class -> FirstService()
            SecondService::class -> SecondService()
            else -> throw IllegalStateException("The service must only be FirstService or SecondService")
        }
    }
}

internal object ServiceLocator {

    private val cacheManager: CacheManager = CacheManagerImpl()
    private val serviceCreator: ServiceCreator = ServiceCreatorImpl()

    internal inline fun <reified T: Service> hasService(): Boolean {
        return cacheManager.getService(T::class) != null
    }

    internal inline fun <reified T: Service> getService(): Service {
        val service = cacheManager.getService(T::class)
        if (service != null) return service

        val newService = serviceCreator.createService(T::class) as T
        cacheManager.addService(newService)
        return newService
    }
}
```

**Применение:**

```kotlin
val firstService = ServiceLocator.getService<FirstService>()
val secondService = ServiceLocator.getService<SecondService>()

println("The first service name is ${firstService.getName()} and the last one name is ${secondService.getName()}")
```

**Вывод:**

```txt
The first service name is FirstService and the last one name is SecondService
```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

## {Название}



**Реализация:**

```kotlin

```

**Применение:**

```kotlin

```

**Вывод:**

```txt

```

