package it.unibo.almamap.extensions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

inline fun <reified V: Any> Settings.mutableStateOf(key: String, defaultValue: V): MutableState<V> {
    val state = mutableStateOf(get<V>(key) ?: defaultValue)
    return object : MutableState<V> by state {
        override var value: V
            get() = state.value
            set(value) {
                state.value = value
                set(key, value)
            }
    }
}