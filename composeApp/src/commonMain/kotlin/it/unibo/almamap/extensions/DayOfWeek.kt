package it.unibo.almamap.extensions

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.weekday__friday
import almamap.composeapp.generated.resources.weekday__monday
import almamap.composeapp.generated.resources.weekday__saturday
import almamap.composeapp.generated.resources.weekday__sunday
import almamap.composeapp.generated.resources.weekday__thursday
import almamap.composeapp.generated.resources.weekday__tuesday
import almamap.composeapp.generated.resources.weekday__wednesday
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.StringResource

fun DayOfWeek.label(): StringResource {
    return when (this) {
        DayOfWeek.SUNDAY -> Res.string.weekday__sunday
        DayOfWeek.MONDAY -> Res.string.weekday__monday
        DayOfWeek.TUESDAY -> Res.string.weekday__tuesday
        DayOfWeek.WEDNESDAY -> Res.string.weekday__wednesday
        DayOfWeek.THURSDAY -> Res.string.weekday__thursday
        DayOfWeek.FRIDAY -> Res.string.weekday__friday
        DayOfWeek.SATURDAY -> Res.string.weekday__saturday
        else -> Res.string.weekday__sunday
    }
}

fun DayOfWeek.previous(): DayOfWeek = DayOfWeek.entries[(ordinal + 6) % 7]
fun DayOfWeek.next(): DayOfWeek = DayOfWeek.entries[(ordinal + 1) % 7]