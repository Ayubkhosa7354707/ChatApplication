package com.ayub.khosa.chatapplication.di


import android.content.Context
import com.ayub.khosa.chatapplication.data.repository.AuthRepositoryImpl
import com.ayub.khosa.chatapplication.data.repository.ChatScreenRepositoryImpl
import com.ayub.khosa.chatapplication.data.repository.HomeRepositoryImpl
import com.ayub.khosa.chatapplication.data.repository.UserListScreenRepositoryImpl
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.domain.repository.ChatScreenRepository
import com.ayub.khosa.chatapplication.domain.repository.HomeRepository
import com.ayub.khosa.chatapplication.domain.repository.UserListScreenRepository
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.AuthUseCases
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.Getfcmtoken
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.IsUserAuthenticatedInFirebase
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.OnSignInWithGoogle
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SetUserStatusToFirebase
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SignIn
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SignUp
import com.ayub.khosa.chatapplication.domain.usecase.chatScreen.ChatScreenUseCases
import com.ayub.khosa.chatapplication.domain.usecase.chatScreen.InsertMessageToFirebase
import com.ayub.khosa.chatapplication.domain.usecase.chatScreen.LoadMessageFromFirebase
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.GetUserFirebase
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.HomeUseCase
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.IsUserSignOutInFirebase
import com.ayub.khosa.chatapplication.domain.usecase.homeScreen.SignOut
import com.ayub.khosa.chatapplication.domain.usecase.userslist.CheckChatRoomExistedFromFirebase
import com.ayub.khosa.chatapplication.domain.usecase.userslist.CreateChatRoomToFirebase
import com.ayub.khosa.chatapplication.domain.usecase.userslist.LoadFriendListFromFirebase
import com.ayub.khosa.chatapplication.domain.usecase.userslist.SearchUserFromFirebase
import com.ayub.khosa.chatapplication.domain.usecase.userslist.UserListScreenUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
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
    fun provideFirebaseMessagingInstance() = FirebaseMessaging.getInstance()


    @Provides
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
        fireMessage: FirebaseMessaging
    ): AuthRepository = AuthRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseDatabase = database,
        fireMessage = fireMessage
    )

    @Provides
    fun providesAuthUseCases(authRepository: AuthRepository) = AuthUseCases(
        isUserAuthenticated = IsUserAuthenticatedInFirebase(authRepository),
        signIn = SignIn(authRepository),
        setUserStatusToFirebase = SetUserStatusToFirebase(
            authRepository
        ),
        onSignInWithGoogle = OnSignInWithGoogle(authRepository),
        signUp = SignUp(authRepository),

        getfcmtoken = Getfcmtoken(authRepository)
    )

    @Provides
    fun providesHomeRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,

        ): HomeRepository = HomeRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseDatabase = database,
    )

    @Provides
    fun provideHomeUseCase(homeRepository: HomeRepository) = HomeUseCase(
        isUserAuthenticated = IsUserSignOutInFirebase(homeRepository),
        signOut = SignOut(homeRepository),
        setUserStatusToFirebase = com.ayub.khosa.chatapplication.domain.usecase.homeScreen.SetUserStatusToFirebase(
            homeRepository
        ),
        getUserFirebase = GetUserFirebase(homeRepository),
    )


    @Provides
    fun providesUserListScreenRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase
    ): UserListScreenRepository = UserListScreenRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseDatabase = database,
    )

    @Provides
    fun providesUserListScreenUseCases(
        userListScreenRepository: UserListScreenRepository
    ) = UserListScreenUseCases(
        searchUserFromFirebase = SearchUserFromFirebase(userListScreenRepository = userListScreenRepository),
        createChatRoomToFirebase = CreateChatRoomToFirebase(userListScreenRepository = userListScreenRepository),
        loadFriendListFromFirebase = LoadFriendListFromFirebase(userListScreenRepository = userListScreenRepository),
        checkChatRoomExistedFromFirebase = CheckChatRoomExistedFromFirebase(userListScreenRepository = userListScreenRepository),
    )


    @Provides
    fun providesChatScreenRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
    ): ChatScreenRepository = ChatScreenRepositoryImpl(
        firebaseAuth = firebaseAuth,
        firebaseDatabase = database,
    )

    @Provides
    fun providesChatScreenUseCases(chatScreenRepository: ChatScreenRepository) = ChatScreenUseCases(
        insertMessageToFirebase = InsertMessageToFirebase(chatScreenRepository = chatScreenRepository),
        loadMessageFromFirebase = LoadMessageFromFirebase(chatScreenRepository = chatScreenRepository),
    )


}