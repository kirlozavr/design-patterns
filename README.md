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

Уведомление ViewModel о событиях происходит через методы `viewModel.onSaveButtonClicked()` и т.д., которые вызывает View слой.
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
6) Mediator;
7) Iterator;
8) Interpreter;
9) Memento;
10) Visitor;
11) Template.

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

`Adapter` позволяет связывать 2 несовместимых объекта.

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

## Behavioral patterns

`Behavioral patterns` решают проблему взаимодействия между объектами.

## Command

Паттерн `Command` превращает запросы в объекты, которые можно передавать как аргументы при вызове методов, ставить их в очередь, отменять и т.д.

**Реализация:**

```kotlin
internal interface Command {
    fun execute(currentValue: Float): Float
}

internal class AddCommand constructor(
    private val operand: Float
): Command {

    override fun execute(currentValue: Float): Float {
        return currentValue + operand
    }
}

internal class SubtractCommand constructor(
    private val operand: Float
): Command {

    override fun execute(currentValue: Float): Float {
        return currentValue - operand
    }
}

internal class DivideCommand constructor(
    private val operand: Float
): Command {

    override fun execute(currentValue: Float): Float {
        return currentValue / operand
    }
}

internal class MultiplyCommand constructor(
    private val operand: Float
): Command {

    override fun execute(currentValue: Float): Float {
        return currentValue * operand
    }
}

internal class Calculator {
    private val operations = mutableListOf<Command>()

    internal fun addCommand(command: Command) {
        operations.add(command)
    }

    internal fun undo() {
        operations.removeLastOrNull()
    }

    internal fun calculate(initialValue: Float = 0f): Float {
        var result = initialValue
        operations.forEach {
            result = it.execute(result)
        }
        return result
    }
}
```

**Применение:**

```kotlin
val calculator = Calculator().apply {
    addCommand(AddCommand(10f))
    addCommand(MultiplyCommand(10f))
    addCommand(SubtractCommand(10f))
    addCommand(DivideCommand(9f))
    addCommand(DivideCommand(10f))
    undo()
}
val result = calculator.calculate()
println("The result of the calculation is $result")
```

**Вывод:**

```txt
The result of the calculation is 10.0
```

## Observer

Паттерн `Observer` позволяет одному объекту публиковать информацию об изменениях его состояния.
Другие объекты в свою очередь могут подписаться на прослушивание этих изменений.

**Реализация:**

```kotlin
internal fun interface Observer {
    fun onAction(action: String)
}

internal class ActionsUpdater {
    private val observers = mutableListOf<Observer>()

    internal fun addObserver(observer: Observer) {
        if (observers.contains(observer)) return
        observers.add(observer)
    }

    internal fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    internal fun update(newAction: String) {
        observers.forEach { it.onAction(action = newAction) }
    }
}
```

**Применение:**

```kotlin
val actionsUpdater = ActionsUpdater()
val observerA = Observer { action ->
    println("Observer A received \"$action\" action")
}
val observerB = Observer { action ->
    println("Observer B received \"$action\" action")
}
val observerC = Observer { action ->
    println("Observer C received \"$action\" action")
}
actionsUpdater.addObserver(observerA)
actionsUpdater.addObserver(observerB)
actionsUpdater.addObserver(observerB)
actionsUpdater.addObserver(observerC)
actionsUpdater.addObserver(observerC)
actionsUpdater.removeObserver(observerC)
actionsUpdater.update("some action")
```

**Вывод:**

```txt
Observer A received "some action" action
Observer B received "some action" action
```

## Strategy

`Strategy` позволяет определять семейства алгоритмов, делать их взаимозаменяемыми и затем в runtime иметь возможность менять реализацию по необходимости.

**Реализация:**

```kotlin
internal interface Transport {
    fun travel()
}

internal class Bus: Transport {
    override fun travel() {
        println("Travel by Bus")
    }
}

internal class Plane: Transport {
    override fun travel() {
        println("Travel by Plane")
    }
}

internal class Ship: Transport {
    override fun travel() {
        println("Travel by Ship")
    }
}

internal class TravellingClient constructor(
    private var transport: Transport
) {
    internal fun travel() {
        transport.travel()
    }

    internal fun change(transport: Transport) {
        this@TravellingClient.transport = transport
    }
}
```

**Применение:**

```kotlin
val travellingClient = TravellingClient(Bus())
travellingClient.travel()
travellingClient.change(Plane())
travellingClient.travel()
travellingClient.change(Ship())
travellingClient.travel()
```

**Вывод:**

```txt
Travel by Bus
Travel by Plane
Travel by Ship
```

## State

Паттерн `State` позволяет объекту менять свою логику в зависимости от текущего состояния.

**Реализация:**

```kotlin
internal sealed interface State {
    data object Uninitialized: State
    data class Initialized(
        val name: String
    ): State
}

internal class StateMachine {
    private var state: State = State.Uninitialized

    internal fun isInitialized(): Boolean {
        return state is State.Initialized
    }

    internal fun setName(name: String) {
        state = State.Initialized(name = name)
    }

    internal fun getName(): String {
        return when(val current = state) {
            is State.Initialized -> current.name
            is State.Uninitialized -> "Unknown"
        }
    }

    internal fun shutdown() {
        state = State.Uninitialized
    }
}
```

**Применение:**

```kotlin
val stateMachine = StateMachine()
println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
stateMachine.setName("new name")
println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
stateMachine.shutdown()
println("State: name=${stateMachine.getName()} and isInitialized=${stateMachine.isInitialized()}")
```

**Вывод:**

```txt
State: name=Unknown and isInitialized=false
State: name=new name and isInitialized=true
State: name=Unknown and isInitialized=false
```

## Chain Of Responsibility

`Chain Of Responsibility` позволяет передавать запрос последовательно по цепочке обработчиков, те в свою очередь могут как либо модифицировать запрос, либо просто передавать запрос дальше по цепочке не трогая его.

**Реализация:**

```kotlin
internal interface Chain {
    fun addMessage(inputMessage: String): String
}

internal class FirstChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("First ")
    }
}

internal class SecondChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("Second ")
    }
}

internal class ThirdChain constructor(
    private val next: Chain? = null
): Chain {
    override fun addMessage(inputMessage: String): String {
        return inputMessage + next?.addMessage("Third ")
    }
}
```

**Применение:**

```kotlin
val first = FirstChain()
val second = SecondChain(first)
val third = ThirdChain(second)
val result = third.addMessage("The beginning of the chain: ")
println(result)
```

**Вывод:**

```txt
The beginning of the chain: Third Second null
```

## Mediator

`Mediator` предоставляет централизованное управление различными объектами в системе.

**Реализация:**

```kotlin
internal interface User {
    fun sendMessage(message: String)
    fun onReceivedMessage(message: String)
}

internal class ChatUser constructor(
    private val mediator: Mediator,
    private val name: String
): User {
    override fun sendMessage(message: String) {
        mediator.sendMessage(message = message)
    }

    override fun onReceivedMessage(message: String) {
        println("$name received the message=$message")
    }
}

internal interface Mediator {
    fun sendMessage(message: String)
    fun addUser(user: User)
    fun removeUser(user: User)
}

internal class ChatMediator: Mediator {
    private val users = mutableListOf<User>()

    override fun sendMessage(message: String) {
        users.forEach { it.onReceivedMessage(message = message) }
    }

    override fun addUser(user: User) {
        if (users.contains(user)) return
        users.add(user)
    }

    override fun removeUser(user: User) {
        users.remove(user)
    }
}
```

**Применение:**

```kotlin
val mediator = ChatMediator()
val bob = ChatUser(mediator, "bob")
mediator.addUser(bob)
val martin = ChatUser(mediator, "martin")
mediator.addUser(martin)
val steve = ChatUser(mediator, "steve")
mediator.addUser(steve)
mediator.removeUser(steve)

bob.sendMessage("Hello everyone!")
```

**Вывод:**

```txt
bob received the message=Hello everyone!
martin received the message=Hello everyone!
```

## Iterator

`Iterator` позволяет проходить по элементам коллекции не раскрывая реализации.

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
val listIterable = ListIterable<Int>()
listIterable.add(1)
listIterable.add(2)
listIterable.add(3)

val iterator = listIterable.iterator()
while (iterator.hasNext()) {
    val element = iterator.next()
    println(element)
}
```

**Вывод:**

```txt
1
2
3
```

## Interpreter

`Interpreter` определяет грамматику языка и интерпретирует его выражения. Иными словами, данный паттерн позволяет описывать операции над языковыми конструкциями и их обработку/выполнение.

**Реализация:**

```kotlin
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
```

**Применение:**

```kotlin
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
```

**Вывод:**

```txt
Multiplication Result=100.0
Addition Result=20.0
Last Expression Result=10.0
```

## Memento

Паттерн `Memento` позволяет сохранять и восстанавливать предыдущее состояние объекта. 

**Реализация:**

```kotlin
internal data class Message(
    val text: String
)

internal interface Originator <T> {
    fun create(): T
    fun restore(memento: T)
}

internal class MessageOriginator constructor(
    private var text: String
): Originator<Message> {
    override fun create(): Message {
        return Message(text = text)
    }

    override fun restore(memento: Message) {
        text = memento.text
    }
}

internal interface Caretaker <T> {
    fun save(originator: Originator<T>)
    fun undo(originator: Originator<T>)
}

internal class MessageCaretaker: Caretaker<Message> {
    private val stack = mutableListOf<Message>()

    override fun save(originator: Originator<Message>) {
        val memento = originator.create()
        stack.add(memento)

        println("Save new $memento")
    }

    override fun undo(originator: Originator<Message>) {
        val previous = stack.removeLastOrNull() ?: return
        originator.restore(previous)

        println("Undo previous $previous")
        println("It is remaining $stack")
    }
}
```

**Применение:**

```kotlin
val originator = MessageOriginator(text = "Initial message")
val caretaker = MessageCaretaker()
caretaker.save(originator)

originator.restore(Message(text = "First message"))
originator.restore(Message(text = "Second message"))
caretaker.save(originator)

originator.restore(Message(text = "Third message"))
caretaker.save(originator)
caretaker.undo(originator)
```

**Вывод:**

```txt
Save new Message(text=Initial message)
Save new Message(text=Second message)
Save new Message(text=Third message)
Undo previous Message(text=Third message)
It is remaining [Message(text=Initial message), Message(text=Second message)]
```

## Visitor

`Visitor` позволяет выполнять различные операции над группой объектов и добавлять новые операции к уже существующим объектам без изменения их классов.

**Реализация:**

```kotlin
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
    var result: Float

    fun visit(item: T)
}

internal class PriceCalculator : Visitor<PriceValue> {
    override var result: Float = 0f

    override fun visit(item: PriceValue) {
        result += item.price
    }
}

internal class WeightCalculator : Visitor<WeightValue> {
    override var result: Float = 0f

    override fun visit(item: WeightValue) {
        result += item.weight
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
```

**Применение:**

```kotlin
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
```

**Вывод:**

```txt
Total price is 450.0 and total weight is 1130.0
```

## Template

`Template` паттерн помогает проектировать основу алгоритма, оставляя реализацию некоторых шагов подклассам.

**Реализация:**

```kotlin
internal data class Image(
    val photoPath: String,
    val description: String
)

internal abstract class ImageSaver {

    internal fun saveImage(image: Image) {
        val uploadLink = getUploadLink()
        uploadImage(localPath = image.photoPath, uploadLink = uploadLink)
        saveImageInfo(localPath = image.photoPath, description = image.description)
    }

    abstract fun getUploadLink(): String

    abstract fun uploadImage(localPath: String, uploadLink: String)

    abstract fun saveImageInfo(localPath: String, description: String)
}

internal class ProfileImageSaver: ImageSaver() {

    override fun getUploadLink(): String {
        return "profile upload link"
    }

    override fun uploadImage(localPath: String, uploadLink: String) {
        println("Profile Image with localPath=$localPath and uploadLink=$uploadLink have been successfully uploaded")
    }

    override fun saveImageInfo(localPath: String, description: String) {
        println("Profile Image Info with localPath=$localPath and description=$description have been successfully uploaded")
    }
}

internal class MessageImageSaver: ImageSaver() {

    override fun getUploadLink(): String {
        return "message upload link"
    }

    override fun uploadImage(localPath: String, uploadLink: String) {
        println("Message Image with localPath=$localPath and uploadLink=$uploadLink have been successfully uploaded")
    }

    override fun saveImageInfo(localPath: String, description: String) {
        println("Message Image Info with localPath=$localPath and description=$description have been successfully uploaded")
    }
}
```

**Применение:**

```kotlin
val profileImageSaver = ProfileImageSaver()
val profileImage = Image(photoPath = "Profile photo path", description = "Profile Image")
profileImageSaver.saveImage(profileImage)

val messageImageSaver = MessageImageSaver()
val messageImage = Image(photoPath = "Message photo path", description = "Message Image")
messageImageSaver.saveImage(messageImage)
```

**Вывод:**

```txt
Profile Image with localPath=Profile photo path and uploadLink=profile upload link have been successfully uploaded
Profile Image Info with localPath=Profile photo path and description=Profile Image have been successfully uploaded
Message Image with localPath=Message photo path and uploadLink=message upload link have been successfully uploaded
Message Image Info with localPath=Message photo path and description=Message Image have been successfully uploaded
```