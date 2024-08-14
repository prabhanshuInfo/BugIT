package com.mobily.bugit.domain.repo

import android.net.Uri
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.utils.Result
import kotlinx.coroutines.flow.Flow

interface BugRepository {
    suspend fun insertBug(bugEntity: BugEntity)
    fun getAllBugs(): Flow<List<BugEntity>>
    fun uploadImageOnFirebase(uri: Uri): Flow<Result<Any>>
}