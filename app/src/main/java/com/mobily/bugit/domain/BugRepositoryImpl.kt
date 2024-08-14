package com.mobily.bugit.domain

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobily.bugit.database.dao.BugDao
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.repo.BugRepository
import com.mobily.bugit.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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

    override fun uploadImageOnFirebase(uri: Uri): Flow<Result<Any>> = flow{
        try {
            val parseUri = Uri.parse(uri.toString())
            val imageName = parseUri.lastPathSegment ?: "Unknown"
            val ref: StorageReference = FirebaseStorage.getInstance().getReference().child("images_$imageName")
            val uploadTask = ref.putFile(uri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            emit(Result.Success(downloadUrl))
        }catch (e: Exception){
            emit(Result.Failed(e.message!!))
        }
    }
}