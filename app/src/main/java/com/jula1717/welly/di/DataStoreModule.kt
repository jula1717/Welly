package com.jula1717.welly.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val USER_PROFILE_FILE = "user_profile"
    private const val TAG = "DataStoreModule"

    @Provides
    @Singleton
    fun provideUserProfileDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler =
                ReplaceFileCorruptionHandler { error ->
                    Log.e(TAG, "Corrupt $USER_PROFILE_FILE DataStore, resetting to empty", error)
                    emptyPreferences()
                },
        ) {
            context.preferencesDataStoreFile(USER_PROFILE_FILE)
        }
}
