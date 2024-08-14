package com.mobily.bugit.domain.useCases

import android.net.Uri
import com.mobily.bugit.domain.repo.BugRepository
import com.mobily.bugit.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageOnFirebaseUseCase @Inject constructor(private val repository: BugRepository) {
    operator fun invoke(uri: Uri): Flow<Result<Any>> {
       return repository.uploadImageOnFirebase(uri)
    }
}