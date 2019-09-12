package com.ifucolo.androidencryption.encrypt

import java.math.BigInteger
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.DHParameterSpec
import javax.crypto.spec.DHPublicKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Aes encryption
 * @param publicKeyUser the public key of the user that I would like to create a secret and has a conversation
 * @param privateKey my private key saved on my device(in real scenario)
 *
 * if you need to use string just change the publicKey parameter to String like in the comments bellow
 * @see class AES(publicKeyUser: String, privateKey: PrivateKey)
 */
class AES(publicKeyUser: PublicKey, privateKey: PrivateKey) {

    private var ivBytes: ByteArray
    private var keyBytes: ByteArray

    companion object {
        const val TRANSFORMATION_SYMMETRIC = "AES/CBC/PKCS7Padding"
        const val SECRET_ALGORITHM = "AES"
        const val SHARED_KEY_ALGORITHM = "DH"
        const val SHA_1 = "SHA-1"
        const val MD5 = "MD5"
    }

    /**
     *
     * To use all stuff in this encryption class we need to start creating the common secret key between user:
     * @see generateCommonSecretKeyByKey method
     *
     * In that case we need 2 messages diggest with SHA-1 nad MD5 Algorithm
     * to combine with the secret key
     *
     * @see MessageDigest [https://www.techopedia.com/definition/4024/message-digest]
     *
     * @see keyBytes the key material of the secret key. The contents of
     * the array are copied to protect against subsequent modification. @see SecretKeySpec
     *
     * @see ivBytes the buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification. @see IvParameterSpec
     */
    init {
        val sharedKey = generateCommonSecretKeyByKey(publicKeyUser, privateKey)
        val shared= sharedKey.toHexString()

        println("android-encrypt: shared key = $shared")

        try {
            val sha = MessageDigest.getInstance(SHA_1)
            val md5 = MessageDigest.getInstance(MD5)

            val key = sha.digest(shared.toByteArray())
            keyBytes = key.copyOf(16)

            val iv = md5.digest(shared.toByteArray())
            ivBytes = iv.copyOf(16)

        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("Error to start encrypting: ${e.message}")

        }
    }

    /**
     * This function to generate a common secret between to users, this secret is used for encrypt and decrypt a message
     * @param publicKeyUser the public key for a external user
     * @param privateKey my private key on my device
     */
    private fun generateCommonSecretKeyByKey(publicKeyUser: PublicKey, privateKey: PrivateKey): ByteArray {
        val p = GenerateMyKeys.pKey
        val g = GenerateMyKeys.gKey

        val pBig = BigInteger(p ,16)
        val gBig = g.toBigInteger()

        val keyAgreement = KeyAgreement.getInstance(SHARED_KEY_ALGORITHM)

        keyAgreement.init(privateKey, DHParameterSpec(pBig, gBig, 2048))
        keyAgreement.doPhase(publicKeyUser, true)

        val sharedKey = keyAgreement.generateSecret(SHARED_KEY_ALGORITHM)
        return sharedKey.encoded
    }

    /**
     * In a real scenario wht public key will came from the backend side and probably came like a String
     * and for that we need to convert this string in a real PublicKey object and in this method
     * before create the secret between the users we generate the PublicKey using the same P and G that was
     * used to generate for all users key independent of platform in the  [getPublicKey] method,
     * and then we can create the sharedKey
     * created based on the publicKey of the user that I would like send or receive encrypt messages and
     * on my PrivateKey
     * @param publicKeyUser = the string key came from server
     * @param privateKey = my private key saved on my device(because just my device need to have it saved)
     */
    private fun generateCommonSecretKeyByKey(publicKeyUser: String, privateKey: PrivateKey): ByteArray {
        val p = GenerateMyKeys.pKey
        val g = GenerateMyKeys.gKey

        val pBig = BigInteger(p ,16)
        val gBig = g.toBigInteger()

        val pbKey = BigInteger(publicKeyUser, 16)

        val keyAgreement = KeyAgreement.getInstance(SHARED_KEY_ALGORITHM)
        val publicKey = getPublicKey(pbKey, pBig, gBig)

        keyAgreement.init(privateKey, DHParameterSpec(pBig, gBig, 2048))
        keyAgreement.doPhase(publicKey, true)

        val sharedKey = keyAgreement.generateSecret(SHARED_KEY_ALGORITHM)
        return sharedKey.encoded
    }

    private fun getPublicKey(publicKey: BigInteger, p: BigInteger, g: BigInteger): PublicKey {
        val dhSpec = DHPublicKeySpec(publicKey, p, g)
        val keyFact = KeyFactory.getInstance(SHARED_KEY_ALGORITHM)

        return keyFact.generatePublic(dhSpec)
    }

    /**
     * To encrypt wee need 3 things:
     * @see cipher transformation  [https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html]
     * @see ivSpec is the buffer create from [ivBytes] [https://docs.oracle.com/javase/7/docs/api/javax/crypto/spec/IvParameterSpec.html]
     * @see secreKey is created with 2 parameters [keyBytes] and the algorithm (in this case AES)
     * Then the cipher init need to be called with ENCRYPT_MODE passing the [secretKey] and [ivSpec]
     * And to get finally get the message encrypted,
     * we need to call the method doFinal from cipher passing the message in byteArray
     * and for that we need transforming the message in byteArray in hex format.
     */
    fun encrypt(message: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION_SYMMETRIC)
            val ivSpec = IvParameterSpec(ivBytes)
            val secretKey = SecretKeySpec(
                keyBytes,
                SECRET_ALGORITHM
            )

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val encryptMessage = cipher.doFinal(message.toByteArray()).toHexString()

            encryptMessage
        } catch (e: Exception) {
            /**
             * To avoid this error just remove the exception and return a empty string
             */
            throw IllegalArgumentException("Error while encrypting: ${e.message}")
        }
    }

    /**
     * To decrypt wee need 3 things:
     * @see cipher transformation  [https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html]
     * @see ivSpec is the buffer create from [ivBytes] [https://docs.oracle.com/javase/7/docs/api/javax/crypto/spec/IvParameterSpec.html]
     * @see secreKey is created with 2 parameters [keyBytes] and the algorithm (in this case AES)
     * Then the cipher init need to be called with DECRYPT_MODE passing the [secretKey] and [ivSpec]
     * And to get finally get the message encrypted,
     * we need to call the method doFinal from cipher passing the message in byteArray with
     * and the and transalating the message to a normal word with UTF-8
     */

    fun decrypt(message: String): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION_SYMMETRIC)
            val ivSpec = IvParameterSpec(ivBytes)
            val secretKey = SecretKeySpec(
                keyBytes,
                SECRET_ALGORITHM
            )

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val decryptMessage = cipher.doFinal(hexStringToByteArray(message)).toString(Charsets.UTF_8)

            decryptMessage
        } catch (e: Exception) {
            /**
             * To avoid this error just remove the exception and return a empty string
             */
            throw IllegalArgumentException("Error while decrypting: ${e.message}")
        }
    }
}
