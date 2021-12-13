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
import kotlinx.coroutines.launch
import java.io.InputStream

class TransferDataViewModel : ViewModel() {
    private val _readingFile = MutableLiveData<Boolean>()
    val readingFile: LiveData<Boolean> = _readingFile

    private val _percentTransfer = MutableLiveData(0f)
    val percentTransfer: LiveData<Float> = _percentTransfer

    private lateinit var bytes: List<Byte>
    private val bluetoothService: BluetoothChatService = BluetoothChat.mChatService

    companion object {
        private const val TAG = "TransferDataViewModel"
    }

    fun readData(inputStream: InputStream) {
        _readingFile.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            bytes = BytesReading.read(inputStream)
            _readingFile.postValue(false)
        }
    }

    fun startTransfer() {
        viewModelScope.launch(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            Log.i(TAG, "startTransfer: $startTime")
            bytes.forEach {
                bluetoothService.write(byteArrayOf(it))
            }
            val finishTime = System.currentTimeMillis()
            Log.i(TAG, "finishTransfer: $finishTime, total time: ${finishTime - startTime}")
        }
    }

}