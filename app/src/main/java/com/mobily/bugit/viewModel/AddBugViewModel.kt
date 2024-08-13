package com.mobily.bugit.viewModel

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
import com.mobily.bugit.utils.AppConstants
import com.mobily.bugit.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBugViewModel @Inject constructor(
    private val insertBugUseCase: InsertBugUseCase,
    private val notionRepository: NotionRepository,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private var tag = AddBugViewModel::class.java.name

    fun insertBug(bugEntity: BugEntity) {
        viewModelScope.launch {
            insertBugUseCase.invoke(bugEntity)
        }
    }

    fun addValueToNotionPage(imageUrl: String, description: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkAvailable()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val notionPage = NotionPage(
                        parent = Parent(AppConstants.NOTION_DATABASE_ID),
                        properties = mapOf(
                            "Name" to NotionProperty(title = listOf(NotionText(Text("New Page Title")))),
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
}