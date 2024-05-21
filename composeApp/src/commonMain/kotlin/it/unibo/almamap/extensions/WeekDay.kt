package it.unibo.almamap.extensions

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.weekday__friday
import almamap.composeapp.generated.resources.weekday__monday
import almamap.composeapp.generated.resources.weekday__saturday
import almamap.composeapp.generated.resources.weekday__sunday
import almamap.composeapp.generated.resources.weekday__thursday
import almamap.composeapp.generated.resources.weekday__tuesday
import almamap.composeapp.generated.resources.weekday__wednesday
import io.ktor.util.date.WeekDay
import org.jetbrains.compose.resources.StringResource

fun WeekDay.label(): StringResource {
    return when (this) {
        WeekDay.SUNDAY -> Res.string.weekday__sunday
        WeekDay.MONDAY -> Res.string.weekday__monday
        WeekDay.TUESDAY -> Res.string.weekday__tuesday
        WeekDay.WEDNESDAY -> Res.string.weekday__wednesday
        WeekDay.THURSDAY -> Res.string.weekday__thursday
        WeekDay.FRIDAY -> Res.string.weekday__friday
        WeekDay.SATURDAY -> Res.string.weekday__saturday
    }
}