package com.mobily.bugit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobily.bugit.database.entity.BugEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BugDao {
    //this function is used for insert bug
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBug(bugEntity: BugEntity)

    //this function is for getting all bugs
    @Query("SELECT * FROM bug ORDER BY id DESC")
    fun getAllBugs(): Flow<List<BugEntity>>
}