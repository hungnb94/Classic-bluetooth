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

    private var packetLength = 1
    private var connectionInterval = 0

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

    fun onPacketLengthChange(text: CharSequence?, start: Int, before: Int, count: Int) {
        packetLength = Integer.parseInt(text?.toString() ?: "0")
    }

    fun onConnectionIntervalChange(text: CharSequence?, start: Int, before: Int, count: Int) {
        connectionInterval = Integer.parseInt(text?.toString() ?: "0")
    }

    fun startTransfer() {
        viewModelScope.launch(Dispatchers.IO) {
            transferBytes(packetLength, connectionInterval.toLong())
        }
    }

    private suspend fun transferBytes(packetLength: Int, connectionInterval: Long) {
        val startTime = System.currentTimeMillis()
        Log.i(TAG, "startTransfer: $startTime")
        val length = bytes.size.toFloat()
        var position = 0
        while (position < length) {
            delay(connectionInterval)
            val start = position
            val end = start + packetLength
            position = end
            if (position > length) position = length.toInt()
            val array = bytes.subList(start, end).toByteArray()
            bluetoothService?.write(array)
            val percent = position / length
            _percentTransfer.postValue(percent)
            _transferDuration.postValue(System.currentTimeMillis() - startTime)
            Log.i(TAG, "startTransfer: percent upload $percent")
        }
        val finishTime = System.currentTimeMillis()
        Log.i(TAG, "finishTransfer: $finishTime, total time: ${finishTime - startTime}")
    }

}