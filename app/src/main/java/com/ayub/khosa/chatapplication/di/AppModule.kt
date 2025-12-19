package com.ayub.khosa.chatapplication.di


import android.content.Context
import android.content.SharedPreferences
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AccountService
import com.ayub.khosa.chatapplication.utils.Constant
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constant.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirebaseMessagingInstance(): FirebaseMessaging = FirebaseMessaging.getInstance()


    @Provides
    @Singleton
    fun provideAccountService() = AccountService()


}