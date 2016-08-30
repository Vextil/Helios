package io.vextil.launcher.activities

import android.Manifest
import android.app.KeyguardManager
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlin.properties.Delegates

open class AuthenticableActivity: AppCompatActivity() {

    private val KEY_NAME = "HeliosLauncherFTW!"
    private var keyguardManager: KeyguardManager by Delegates.notNull()
    private var fingerprintManager: FingerprintManager by Delegates.notNull()
    private var keyStore: KeyStore by Delegates.notNull()
    private var keyGenerator: KeyGenerator by Delegates.notNull()
    private var cipher: Cipher by Delegates.notNull()
    private var cryptoObject: FingerprintManager.CryptoObject by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        if (!keyguardManager.isKeyguardSecure) {
            Toast.makeText(this, "No lockscreen security.", Toast.LENGTH_LONG).show()
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Fingerprint permission not enabled.", Toast.LENGTH_LONG).show()
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "No Fingerprints found.", Toast.LENGTH_LONG).show()
        }

        generateKey()
        cipherInit()
        cryptoObject = FingerprintManager.CryptoObject(cipher)
    }

    protected fun generateKey() {
        keyStore.load(null)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT.and(KeyProperties.PURPOSE_DECRYPT))
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build())
        keyGenerator.generateKey()
    }

    protected fun cipherInit() {
        cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyStore.load(null)
        val key = keyStore.getKey(KEY_NAME, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, key)
    }
}