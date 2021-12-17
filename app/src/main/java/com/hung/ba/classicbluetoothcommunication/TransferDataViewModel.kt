package com.hung.ba.classicbluetoothcommunication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.BluetoothChat.BluetoothChat
import com.example.android.BluetoothChat.BluetoothChatService
import com.hung.ba.classicbluetoothcommunication.files.BytesReading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class TransferDataViewModel : ViewModel() {
    private val _readingFile = MutableLiveData(false)
    val readingFile: LiveData<Boolean> = _readingFile

    private val _percentTransfer = MutableLiveData(0f)
    val percentTransfer: LiveData<Float> = _percentTransfer

    private val _transferDuration = MutableLiveData(0L)
    val transferDuration: LiveData<Long> = _transferDuration

    private lateinit var bytes: List<Byte>
    private val bluetoothService: BluetoothChatService? = BluetoothChat.mChatService

    companion object {
        private const val TAG = "TransferDataViewModel"
    }

    fun readData(inputStream: InputStream) {
        _readingFile.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            Log.e(TAG, "readData: start read file")
            bytes = BytesReading.read(inputStream)
            _readingFile.postValue(false)
            Log.e(TAG, "readData: finish read file")
        }
    }

    fun startTransfer() {
        viewModelScope.launch(Dispatchers.IO) {
            transferBytesEverySecond()
        }
    }

    private fun transferSingleByteWithoutDelay() {
        val startTime = System.currentTimeMillis()
        Log.i(TAG, "startTransfer: $startTime")
        var count = 0
        val length = bytes.size.toFloat()
        bytes.forEach {
            bluetoothService?.write(byteArrayOf(it))
            ++count
            val percent = count / length
            _percentTransfer.postValue(percent)
            _transferDuration.postValue(System.currentTimeMillis() - startTime)
            Log.i(TAG, "startTransfer: percent upload $percent")
        }
        val finishTime = System.currentTimeMillis()
        Log.i(TAG, "finishTransfer: $finishTime, total time: ${finishTime - startTime}")
    }

    private suspend fun transferBytesEverySecond() {
        val startTime = System.currentTimeMillis()
        Log.i(TAG, "startTransfer: $startTime")
        var count: Int
        val length = bytes.size.toFloat()
        val duration = 5 * 60
        val speed = length / duration
        var position = 0
        for (i in 1..duration) {
            delay(1000)
            val start = position
            val end = (speed + start).toInt()
            count = end
            position = end
            val array = bytes.subList(start, end).toByteArray()
            bluetoothService?.write(array)
            val percent = count / length
            _percentTransfer.postValue(percent)
            _transferDuration.postValue(System.currentTimeMillis() - startTime)
            Log.i(TAG, "startTransfer: percent upload $percent")
        }
        val finishTime = System.currentTimeMillis()
        Log.i(TAG, "finishTransfer: $finishTime, total time: ${finishTime - startTime}")
    }

}