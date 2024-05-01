package de.hausdinga.stuxpass.local.pref

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import de.hausdinga.stuxpass.util.Constants
import java.security.GeneralSecurityException

class SecurePreferences(context: Context) {
    private fun getMasterKey(context: Context): MasterKey {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()
        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
    }

    val preferences by lazy {
        context.let {
            try {
                EncryptedSharedPreferences.create(
                    it,
                    Constants.PREF_NAME,
                    getMasterKey(it),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (ex: GeneralSecurityException) {
                it.deleteSharedPreferences(Constants.PREF_NAME)
                EncryptedSharedPreferences.create(
                    it,
                    Constants.PREF_NAME,
                    getMasterKey(it),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }
        }
    }
}
