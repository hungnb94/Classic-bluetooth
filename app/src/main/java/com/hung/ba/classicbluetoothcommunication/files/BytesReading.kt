package com.hung.ba.classicbluetoothcommunication.files

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.*

object BytesReading {

    fun read(input: InputStream): List<Byte> {
        return read(InputStreamReader(input))
    }

    fun read(reader: Reader): List<Byte> {
        val result = LinkedList<Byte>()
        val bufferedReader = BufferedReader(reader)
        var line: String? = bufferedReader.readLine()
        while (line != null) {
            val tokenizer = StringTokenizer(line)
            while (tokenizer.hasMoreTokens()) {
                result.add(hexToByte(tokenizer.nextToken()))
            }
            line = bufferedReader.readLine()
        }
        return result
    }

    fun hexToByte(hex: String): Byte {
        return hex.toInt(16).toByte()
    }
}