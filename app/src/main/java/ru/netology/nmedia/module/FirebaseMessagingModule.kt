package ru.netology.nmedia.module

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FirebaseMessagingModule {

    @Singleton
    @Provides
    fun providesFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()
}