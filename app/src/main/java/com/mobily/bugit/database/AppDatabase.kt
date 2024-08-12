package com.mobily.bugit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobily.bugit.database.dao.BugDao
import com.mobily.bugit.database.entity.BugEntity

@Database(entities = [BugEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun bugDao(): BugDao
}