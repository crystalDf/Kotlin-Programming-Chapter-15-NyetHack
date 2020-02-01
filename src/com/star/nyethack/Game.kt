package com.star.nyethack

fun main(args: Array<String>) {

    val player = Player("Star")

    val statusFormatString = "(HP)(A) -> H"

    printPlayerStatus(player)
    printFormattedPlayerStatus(statusFormatString, player)

    val inebriationStatus = player.castFireball(5)

    println("Inebriation Status: $inebriationStatus")

    var currentRoom = TownSquare()
    println(currentRoom.description())
    println(currentRoom.load())
}

private fun printFormattedPlayerStatus(
    statusFormatString: String, player: Player
) {
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

private fun printPlayerStatus(player: Player) {

    println(
        "(Aura: ${player.formatAuraColor()}) " +
                "(Blessed: ${if (player.isBlessed) "YES" else "NO"})"
    )

    println("${player.name} ${player.formatHealthStatus()}")
}
