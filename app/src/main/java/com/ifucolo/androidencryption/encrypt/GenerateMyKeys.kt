package com.ifucolo.androidencryption.encrypt

import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.interfaces.DHPrivateKey
import javax.crypto.interfaces.DHPublicKey
import javax.crypto.spec.DHParameterSpec

object GenerateMyKeys {

    /***
     * Change it to use yours, it is a example and its no recommend keep this in the code like this
     */
    const val pKey = "f43633e3212e9016ca32e09e9eeb774c08942c99cfe470172b3efa0120b6d473b2895304f6262bb728872e2ab9ae3018ef2be44da8c6751ce59a62c8775503c415a581125e2089ec067c967a3a13d72196446b08ae275c1e5ddba40e9f52912e2dc7a116d972ed3795844331258cdeec8f803a4c98866c6cc8bbc2cf3b2057b4c3624e2c7cfbfc6d45d94495bb70b87cae869c7c5203ef260fa741f97c42fc8cb4d94ff67889854788de8ac689075c9a9f2eefe2b79d9353af19f553d78412265888c93e74d36f11bb05896654dbb28f281051ea1c6fca8a3650bf099d9a1a6d142a220661d689d5913e9a7dbc41e5002eb12961770cbdee29f6452c9b708ba3"
    const val gKey = "02"
    const val ALGORITHM = "DH"

    /***
     * This method generate private key and public key unsind Diff-Helman algorithm
     * To use that we need 2 parameters P and G
     * @param pKey is a prime number
     * @param gKey is a number
     */
    fun generateKeys(): Keys {
        val keyGen = KeyPairGenerator.getInstance(ALGORITHM)
        val pBig = BigInteger(pKey, 16)
        val gBig = gKey.toBigInteger()

        keyGen.initialize(DHParameterSpec(pBig, gBig, 2048))
        val keyPair = keyGen.generateKeyPair()

        val private = (keyPair.private as DHPrivateKey).x.toString(16)
        val public = (keyPair.public as DHPublicKey).y.toString(16)

        println(">>>> private: $private\n\n")
        println(">>>> publick: $public")

        return Keys(keyPair.private, keyPair.public)
    }
}

data class Keys(
    val privateKey: PrivateKey,
    val publicKey: PublicKey
)