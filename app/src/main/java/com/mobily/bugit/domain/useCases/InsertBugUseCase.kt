package com.mobily.bugit.domain.useCases

import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.repo.BugRepository
import javax.inject.Inject

class InsertBugUseCase @Inject constructor(private val repository: BugRepository) {
    suspend operator fun invoke(bugEntity: BugEntity){
        repository.insertBug(bugEntity)
    }
}