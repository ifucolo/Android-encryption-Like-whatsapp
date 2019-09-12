package com.ifucolo.androidencryption.encrypt

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

fun hexStringToByteArray(hexString: String): ByteArray {
    val len = hexString.length / 2
    val result = ByteArray(len)
    for (i in 0 until len)
        result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
    return result
}