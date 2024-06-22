package it.unibo.almamap.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

/**
 * Creates a [MutableState] that reads and writes to the settings with the given [key].
 * The value is initialized with the value stored in the settings, or [defaultValue] if no value is stored.
 * The value is stored in the settings when it is changed.
 * @param T The type of the value to store in the settings.
 * @param key The key to use to store the value in the settings.
 * @param defaultValue The value to use if no value is stored in the settings.
 * @return A [MutableState] that reads and writes to the settings with the given [key].
 * @see Settings.get
 * @see Settings.set
 */
inline fun <reified T : Any> Settings.mutableStateOf(key: String, defaultValue: T): MutableState<T> {
    val state = mutableStateOf(get<T>(key) ?: defaultValue)
    return object : MutableState<T> by state {
        override var value: T
            get() = state.value
            set(value) {
                state.value = value
                set(key, value)
            }
    }
}

/**
 * Creates a [MutableState] that reads and writes to the settings with the given [key].
 * The value is initialized with the value stored in the settings, or [defaultValue] if no value is stored.
 * The value is stored in the settings when it is changed.
 * The state is updated when the value in the settings changes.
 * @param T The type of the value to store in the settings.
 * @param key The key to use to store the value in the settings.
 * @param defaultValue The value to use if no value is stored in the settings.
 * @return A [MutableState] that reads and writes to the settings with the given [key].
 * @see Settings.get
 * @see Settings.set
 */
inline fun <reified T : Any> ObservableSettings.mutableStateOf(key: String, defaultValue: T): MutableState<T> {
    val state = mutableStateOf(get<T>(key) ?: defaultValue)

    // Add listener to update the state when the value in the settings changes for supported types
    when (T::class) {
        Boolean::class -> addBooleanListener(key, defaultValue as Boolean) { state.value = it as T }
        Int::class -> addIntListener(key, defaultValue as Int) { state.value = it as T }
        Long::class -> addLongListener(key, defaultValue as Long) { state.value = it as T }
        Float::class -> addFloatListener(key, defaultValue as Float) { state.value = it as T }
        Double::class -> addDoubleListener(key, defaultValue as Double) { state.value = it as T }
        String::class -> addStringListener(key, defaultValue as String) { state.value = it as T }
    }

    return object : MutableState<T> by state {
        override var value: T
            get() = state.value
            set(value) {
                state.value = value
                set(key, value)
            }
    }
}