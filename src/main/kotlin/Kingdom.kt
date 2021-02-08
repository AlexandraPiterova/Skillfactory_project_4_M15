fun main() {
    val kingdom = Kingdom()

    println("Ruler's name: ${kingdom.ruler.name}, intellect: ${kingdom.ruler.intellect}, power: ${kingdom.ruler.power}")
    kingdom.heirs.forEach() {
        println("Heir's name: ${it.name}, intellect: ${it.intellect}, power: ${it.power}")
    }
    println(kingdom.archers)
    println(kingdom.warriors)

    Ruler.geroldGreetings()

    val workerTaxCollector = object: TaxCollector() {
        override fun collect() {
            val taxGroup = kingdom.peasants.filter {
                it.occupation == Occupation.WORKER
            }
            kingdom.treasury += taxCalculation(taxGroup.size, 1)
        }
    }

    val builderTaxCollector = object: TaxCollector() {
        override fun collect() {
            val taxGroup = kingdom.peasants.filter {
                it.occupation == Occupation.BUILDER
            }
            kingdom.treasury += taxCalculation(taxGroup.size, 2)
        }
    }

    val farmerTaxCollector = object: TaxCollector() {
        override fun collect() {
            val taxGroup = kingdom.peasants.filter {
                it.occupation == Occupation.FARMER
            }
            kingdom.treasury += taxCalculation(taxGroup.size, 3)
        }
    }

    workerTaxCollector.collect()
    builderTaxCollector.collect()
    farmerTaxCollector.collect()

    println("Gold: " + kingdom.treasury)

    "Ваша популярность падает!".yourHighness()
    "На нас напали!".yourHighness()
    "Нужно больше золота!".yourHighness()

    kingdom.upgradeYourArmy(kingdom.archers) {
        println(it)
    }

    kingdom.upgradeYourWarriors(kingdom.warriors) {
        println(it)
    }

    kingdom.giveFunToPeasants(kingdom.peasants)

}

class Kingdom {
    val ruler = Ruler("Alexandra")
    val heirs = mutableListOf<Heir>()

    val archers = mutableListOf<Archer>()
    val warriors = mutableListOf<Warrior>()
    val peasants = mutableListOf<Peasant>()

    var treasury: Int = 0

    private val wheelOfFortune = WheelOfFortune()

    private fun addHeirs(amount: Int) {
        for (i in 0 until amount) {
            heirs.add(Heir("Heir $i", wheelOfFortune))
        }
    }
    private fun addArchers(amount: Int) {
        for (i in 0 until amount) {
            if (i % 2 == 0) {
                archers.add(Archer("Dagger"))
            } else {
                archers.add(Archer("None"))
            }
        }
    }
    private fun addWarriors(amount: Int) {
        for (i in 0 until amount) {
            if (i % 2 == 0) {
                warriors.add(Warrior("Sword"))
            } else {
                warriors.add(Warrior("Axe"))
            }
        }
    }
    private fun addPeasants(amount: Int) {
        for (i in 0 until amount) {
            when {
                i % 3 == 0 -> peasants.add(Peasant(Occupation.FARMER))
                i % 2 == 0 -> peasants.add(Peasant(Occupation.BUILDER))
                else -> peasants.add(Peasant(Occupation.WORKER))
            }
        }
    }

    fun upgradeYourArmy(list: List<Archer>, operation: (List<Archer>) -> Unit) {
        list.forEach {
            it.bow = "Composite bow"
        }
        operation(list)
    }

    fun upgradeYourWarriors(list: List<Warrior>, operation: (List<Warrior>) -> Unit) {
        list.forEach {
            it.weapon = "Frostmorn"
        }
        operation(list)
    }

    fun giveFunToPeasants(list: List<Peasant>) {
        list.forEach { peasant1 ->
            list.forEach { peasant2 ->
                if (peasant1.occupation == peasant2.occupation) println("Привет, коллега!")
                else println("Вижу мы с вами занимаемся разным")
            }
        }
    }

    init {
        addHeirs(2)
        addArchers(5)
        addWarriors(5)
        addPeasants(5)
    }
}

open class Ruler(val name: String) {
    var power = 10f
    var intellect = 10f

    companion object {
        fun geroldGreetings() {
            println("Король в здании!")
        }
    }
}

class Heir(name: String, wheelOfFortune: WheelOfFortune) : Ruler(name) {
    init {
        power *= wheelOfFortune.coefficient()
        intellect *= wheelOfFortune.coefficient()
    }
}

data class Archer(var bow: String = "Longbow", val dagger: String) {
    constructor(dagger: String): this("Longbow", dagger)
}

data class Warrior(var weapon: String)

class WheelOfFortune {
    fun coefficient(): Float = (0..200).random() / 100f
}

data class Peasant(val occupation: Occupation)

enum class Occupation {
    WORKER,
    BUILDER,
    FARMER
}

abstract class TaxCollector : CollectTaxes

interface CollectTaxes {
    fun collect()
    fun taxCalculation(groupSize: Int, multiplier: Int) = groupSize * multiplier
}

fun String.yourHighness() {
    println("Ваше Высочество! $this")
}