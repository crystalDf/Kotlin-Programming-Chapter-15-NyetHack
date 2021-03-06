package com.star.nyethack

import java.lang.IllegalStateException

fun main(args: Array<String>) {

    Game.play()
}

object Game {

    private val player = Player("Star")
    private var currentRoom: Room = TownSquare()

    private val statusFormatString = "(HP)(A) -> H"

    private var worldMap = listOf(
        listOf(currentRoom, Room("Tavern"), Room("Back Room")),
        listOf(Room("Long Corridor"), Room("Generic Room"))
    )

    init {

        println("Welcome, adventurer.")

        val inebriationStatus = player.castFireball(5)

        println("Inebriation Status: $inebriationStatus")
    }

    fun play() {

        while (true) {
            println(currentRoom.description())
            println(currentRoom.load())

            printPlayerStatus(player)
            printFormattedPlayerStatus(statusFormatString, player)

            print("> Enter your command: ")

            val message = GameInput(readLine()).processCommand()

            println(message)

            if (message == "See you.") {
                break
            }
        }
    }

    private fun printPlayerStatus(player: Player) {

        println(
            "(Aura: ${player.formatAuraColor()}) " +
                    "(Blessed: ${if (player.isBlessed) "YES" else "NO"})"
        )

        println("${player.name} ${player.formatHealthStatus()}")
    }

    private fun printFormattedPlayerStatus(statusFormatString: String, player: Player) {

        val result: StringBuilder = StringBuilder("")

        for (i in statusFormatString.indices) {

            val replacement = when (statusFormatString[i]) {
                'B' -> "Blessed: ${if (player.isBlessed) "YES" else "NO"}"
                'A' -> "Aura: ${player.formatAuraColor()}"
                'H' -> if (((i + 1) < statusFormatString.length) && (statusFormatString[i + 1] == 'P')) {
                    "HP: ${player.healthPoints}"
                } else {
                    "${player.name} ${player.formatHealthStatus()}"
                }
                'P' -> ""
                else -> {
                    statusFormatString[i].toString()
                }
            }

            result.append(replacement)
        }

        println(result)
    }

    private class GameInput(arg: String?) {

        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() = when (command.toLowerCase()) {
            "move" -> move(argument)
            "map" -> showMagicMap()
            "ring" -> ringBellInTownSquare()
            in listOf("quit", "exit") -> sayFarewell()
            else -> commandNotFound()
        }

        private fun move(directionInput: String) =
            try {
                val direction = Direction.valueOf(directionInput.toUpperCase())
                val newPosition = direction.updateCoordinate(player.currentPosition)

                if (!newPosition.isInBounds) {
                    throw IllegalStateException("$direction is out of bounds.")
                }

                val newRoom = worldMap[newPosition.y][newPosition.x]
                player.currentPosition = newPosition
                currentRoom = newRoom

                "OK, you move $direction to the ${newRoom.name}.\n${newRoom.load()}"

            } catch (e: Exception) {
                "Invalid direction: $directionInput."
            }

        private fun showMagicMap(): String {

            var magicMap = ""

            for (i in worldMap.indices) {
                for (j in worldMap[i].indices) {
                    magicMap += if (Coordinate(j, i) == player.currentPosition) "X " else "O "
                }
                magicMap += "\n"
            }

            return magicMap
        }

        private fun ringBellInTownSquare() = if (currentRoom is TownSquare)
            (currentRoom as TownSquare).ringBell() else "You are not in the Town Square."

        private fun sayFarewell() = "See you."

        private fun commandNotFound() = "I'm not quite sure what you're trying to do!"
    }
}
