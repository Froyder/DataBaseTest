package com.example.databasetest.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.databasetest.database.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun addUser (user: User)

    @Insert
    fun insertAll(vararg users: User)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Delete
    fun delete(user: User)
}