package com.mobily.bugit.domain.useCases

import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.repo.BugRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchBugUseCase @Inject constructor(private val repository: BugRepository) {
    operator fun invoke(): Flow<List<BugEntity>> {
        return repository.getAllBugs()
    }
}