package com.mobily.bugit.presentation.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobily.bugit.data.NotionRepository
import com.mobily.bugit.data.model.NotionPage
import com.mobily.bugit.data.model.NotionProperty
import com.mobily.bugit.data.model.NotionText
import com.mobily.bugit.data.model.Parent
import com.mobily.bugit.data.model.Text
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.useCases.InsertBugUseCase
import com.mobily.bugit.domain.useCases.UploadImageOnFirebaseUseCase
import com.mobily.bugit.utils.AppConstants
import com.mobily.bugit.utils.NetworkHelper
import com.mobily.bugit.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBugViewModel @Inject constructor(
    private val insertBugUseCase: InsertBugUseCase,
    private val notionRepository: NotionRepository,
    private val networkHelper: NetworkHelper,
    private val uploadImageOnFirebaseUseCase: UploadImageOnFirebaseUseCase
) : ViewModel() {

    private var tag = AddBugViewModel::class.java.name

    private val _resultState = MutableStateFlow<Result<Any>>(Result.Loading)
    val resultState : StateFlow<Result<Any>> = _resultState


    private suspend fun insertBug(bugEntity: BugEntity) {
        viewModelScope.launch {
            insertBugUseCase.invoke(bugEntity)
        }
    }

    private suspend fun addValueToNotionPage(imageUrl: String, description: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val notionPage = NotionPage(
                        parent = Parent(AppConstants.NOTION_DATABASE_ID),
                        properties = mapOf(
                            "Name" to NotionProperty(title = listOf(NotionText(Text("BugIt")))),
                            "ImageUrl" to NotionProperty(url = imageUrl),
                            "Description" to NotionProperty(
                                rich_text = listOf(
                                    NotionText(
                                        Text(
                                            description
                                        )
                                    )
                                )
                            )
                        )
                    )
                    notionRepository.addPageToDatabase(notionPage).let {
                        when {
                            it.isSuccessful -> {
                                Log.d(tag, "Successfully added ${it.message()}")
                            }
                            else -> {
                                Log.d(tag, "Not able to added ${it.message()}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun uploadImageOnFirebase(uri: Uri, description: String) {
        viewModelScope.launch {
            uploadImageOnFirebaseUseCase.invoke(uri).collectLatest {  result ->
                    when (result) {
                        is Result.Loading -> {
                            _resultState.value = result
                        }
                        is Result.Failed -> {
                            _resultState.value = result
                        }
                        is Result.Success -> {
                            addValueToNotionPage("${result.data}", description)
                            val bugEntity = BugEntity(0, "${result.data}", description)
                            insertBug(bugEntity)
                            _resultState.value = result
                        }
                    }
            }
        }
    }
}