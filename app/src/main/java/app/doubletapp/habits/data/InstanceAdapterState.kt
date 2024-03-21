package app.doubletapp.habits.data

import android.os.Bundle
import app.doubletapp.habits.recyclerview.HabitAdapter
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class InstanceAdapterState {

    fun saveInstanceState(outState: Bundle, adapter: HabitAdapter) {
        val adapterBytes = adapterToBytes(adapter)
        outState.putByteArray(KEY_ADAPTER_STATE, adapterBytes)
    }

    fun restoreInstanceState(savedInstanceState: Bundle?, adapter: HabitAdapter): HabitAdapter {
        val adapterBytes = savedInstanceState?.getByteArray(KEY_ADAPTER_STATE)
        return bytesToAdapter(adapterBytes, adapter)
    }

    private fun adapterToBytes(adapter: HabitAdapter): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(adapter)
        objectOutputStream.close()
        return byteArrayOutputStream.toByteArray()
    }

    private fun bytesToAdapter(bytes: ByteArray?, defaultAdapter: HabitAdapter): HabitAdapter {
        val objectInputStream = bytes?.inputStream()?.buffered()?.let { ObjectInputStream(it) }
        val adapter = objectInputStream?.readObject() as? HabitAdapter
        objectInputStream?.close()
        return adapter ?: defaultAdapter
    }

    companion object {
        private const val KEY_ADAPTER_STATE = "adapter_state"
    }
}