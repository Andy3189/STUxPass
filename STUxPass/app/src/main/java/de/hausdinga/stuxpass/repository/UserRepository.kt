package de.hausdinga.stuxpass.repository

import de.hausdinga.stuxpass.local.pref.SecurePreferences
import javax.inject.Inject

interface UserRepository {
    fun addUser(birthDate: String, immaNr: String)
    fun deleteUser(birthDate: String)
    fun getUsers(): List<Pair<String,String>>
}

class DefaultUserRepository @Inject constructor(
    private val securePreferences: SecurePreferences
) : UserRepository {

    override fun addUser(birthDate: String, immaNr: String) {
        securePreferences.preferences.edit().putString(birthDate, immaNr).apply()
    }

    override fun deleteUser(birthDate: String) {
        securePreferences.preferences.edit().remove(birthDate).apply()
    }

    override fun getUsers(): List<Pair<String, String>> {
        return securePreferences.preferences.all.entries.mapNotNull { entry ->
            (entry.value as? String)?.let {
                Pair(entry.key, it)
            }
        }
    }
}
