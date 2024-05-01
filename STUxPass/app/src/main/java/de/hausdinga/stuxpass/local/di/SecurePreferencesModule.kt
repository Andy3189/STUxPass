package de.hausdinga.stuxpass.local.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.hausdinga.stuxpass.local.pref.SecurePreferences

@Module
@InstallIn(SingletonComponent::class)
class SecurePreferencesModule {
    @Provides
    fun provideSecurePreferences(@ApplicationContext context: Context): SecurePreferences {
        return SecurePreferences(context)
    }
}
