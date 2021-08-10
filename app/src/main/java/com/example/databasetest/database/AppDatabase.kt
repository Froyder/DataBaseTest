package com.example.databasetest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.databasetest.database.User
import com.example.databasetest.database.UserDao

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}