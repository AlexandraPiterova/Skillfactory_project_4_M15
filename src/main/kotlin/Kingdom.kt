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
            kingdom.treasury += taxGroup.size
        }
    }

    val builderTaxCollector = object: TaxCollector() {
        override fun collect() {
            val taxGroup = kingdom.peasants.filter {
                it.occupation == Occupation.BUILDER
            }
            kingdom.treasury += taxGroup.size * 2
        }
    }

    val farmerTaxCollector = object: TaxCollector() {
        override fun collect() {
            val taxGroup = kingdom.peasants.filter {
                it.occupation == Occupation.FARMER
            }
            kingdom.treasury += taxGroup.size * 3
        }
    }

    workerTaxCollector.collect()
    builderTaxCollector.collect()
    farmerTaxCollector.collect()

    println("Gold: " + kingdom.treasury)
}

class Kingdom {
    val ruler = Ruler("Alexandra")
    val heirs = mutableListOf<Heir>()

    val archers = mutableListOf<Archer>()
    val warriors = mutableListOf<Warrior>()
    val peasants = mutableListOf<Peasant>()

    var treasury: Int = 0

    private val wheelOfFortune = WheelOfFortune()

    init {
        for (i in 1..3) {
            heirs.add(Heir("Heir $i", wheelOfFortune))
        }
        for (i in 1..20) {
            if (i % 2 == 0) {
                archers.add(Archer("Dagger"))
            } else {
                archers.add(Archer("None"))
            }
        }
        for (i in 1..30) {
            if (i % 2 == 0) {
                warriors.add(Warrior("Sword"))
            } else {
                warriors.add(Warrior("Axe"))
            }
        }
        for (i in 1..100) {
            when {
                i % 3 == 0 -> peasants.add(Peasant(Occupation.FARMER))
                i % 2 == 0 -> peasants.add(Peasant(Occupation.BUILDER))
                else -> peasants.add(Peasant(Occupation.WORKER))
            }
        }
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

data class Warrior(val weapon: String)

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
}