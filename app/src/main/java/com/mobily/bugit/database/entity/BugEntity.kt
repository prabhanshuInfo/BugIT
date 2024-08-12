package com.mobily.bugit.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Bug")
data class BugEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String,
    val bugDescription: String
): Serializable