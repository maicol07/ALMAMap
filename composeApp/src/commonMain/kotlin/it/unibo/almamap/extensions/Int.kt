package it.unibo.almamap.extensions

infix fun Int.floorMod(other: Int) =
    ((this % other) + other) % other

infix fun Int.floorMod(other: Double) =
    ((this % other) + other) % other