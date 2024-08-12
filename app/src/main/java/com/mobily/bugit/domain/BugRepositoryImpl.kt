package com.mobily.bugit.domain

import com.mobily.bugit.database.dao.BugDao
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.repo.BugRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BugRepositoryImpl @Inject constructor(
    private val bugDao: BugDao
) : BugRepository {

    override suspend fun insertBug(bugEntity: BugEntity) {
       bugDao.insertBug(bugEntity)
    }

    override fun getAllBugs(): Flow<List<BugEntity>> {
        return bugDao.getAllBugs()
    }
}