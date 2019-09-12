package com.ifucolo.androidencryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifucolo.androidencryption.encrypt.AES
import com.ifucolo.androidencryption.encrypt.GenerateMyKeys
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start()
    }

    private fun start() {
        val aliceKeys = GenerateMyKeys.generateKeys()
        val bobKeys = GenerateMyKeys.generateKeys()

        val aesAlice = AES(
            publicKeyUser = bobKeys.publicKey,
            privateKey = aliceKeys.privateKey
        )
        val aesBob = AES(
            publicKeyUser = aliceKeys.publicKey,
            privateKey = bobKeys.privateKey
        )

        val messageFromAlice = aesAlice.encrypt("Hi Bob")
        println("android-encrypt: message encrypted from Alice to Bob = $messageFromAlice")
        var messages = "message encrypted from Alice to Bob:\n$messageFromAlice"

        val messageDecryptedFromBob = aesBob.decrypt(messageFromAlice)
        println("android-encrypt: message from Alice decrypted by Bob = $messageDecryptedFromBob")
        messages+= "\n\nmessage from Alice decrypted by Bob:\n$messageDecryptedFromBob"

        val messageFromBob = aesBob.encrypt("Hi Alice")
        println("android-encrypt: message encrypted from Bob to Alice = $messageFromBob")
        messages += "\n\nmessage encrypted from Bob to Alice:\n$messageFromBob"

        val messageDecryptedFromAlice = aesAlice.decrypt(messageFromBob)
        println("android-encrypt: message decrypted from Bob to Alice = $messageDecryptedFromAlice")
        messages += "\n\nmessage from Bob decrypted by Alice:\n$messageDecryptedFromAlice"

        txtMessages.text = messages
    }
}
