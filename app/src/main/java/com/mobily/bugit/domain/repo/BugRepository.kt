package com.mobily.bugit.domain.repo

import com.mobily.bugit.database.entity.BugEntity
import kotlinx.coroutines.flow.Flow

interface BugRepository {
    suspend fun insertBug(bugEntity: BugEntity)
    fun getAllBugs(): Flow<List<BugEntity>>
}