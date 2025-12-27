package com.ayub.khosa.chatapplication.di



import android.content.Context
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.data.repository.AuthRepositoryImpl
import com.ayub.khosa.chatapplication.data.repository.HomeRepositoryImpl
import com.ayub.khosa.chatapplication.domain.repository.HomeRepository
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.AuthUseCases
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.IsUserAuthenticatedInFirebase
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SignIn
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.HomeUseCase
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.IsUserSignOutInFirebase

import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.SignOut

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }



    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorageInstance() = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseDatabaseInstance() = FirebaseDatabase.getInstance()


    @Provides
    fun providesAuthRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): AuthRepository = AuthRepositoryImpl(
        auth,
        database = database
    )

    @Provides
    fun providesAuthUseCases(authRepository: AuthRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticatedInFirebase(authRepository),
        signIn = SignIn(authRepository),
        setUserStatusToFirebase =  com.ayub.khosa.chatapplication.domain.usecase.authScreen.SetUserStatusToFirebase(authRepository)
    )

    @Provides
    fun providesHomeRepository(
        auth: FirebaseAuth,
        database: FirebaseDatabase
    ): HomeRepository = HomeRepositoryImpl(
        auth,
        database = database
    )




    @Provides
    fun provideHomeUseCase(homeRepository: HomeRepository) = HomeUseCase(
        isUserAuthenticated = IsUserSignOutInFirebase(homeRepository),
        signOut = SignOut(homeRepository),
        setUserStatusToFirebase = com.ayub.khosa.chatapplication.domain.usecase.homeScreen.SetUserStatusToFirebase(homeRepository)
    )


}