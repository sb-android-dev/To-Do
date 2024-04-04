package com.sbdev.project.todo.di

import com.sbdev.project.todo.data.AuthRepositoryImpl
import com.sbdev.project.todo.data.AuthSource
import com.sbdev.project.todo.data.FirestoreRepositoryImpl
import com.sbdev.project.todo.data.FirestoreSource
import com.sbdev.project.todo.domain.AuthRepository
import com.sbdev.project.todo.domain.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(AuthSource())

    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository =
        FirestoreRepositoryImpl(FirestoreSource())

}