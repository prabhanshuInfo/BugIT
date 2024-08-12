package com.mobily.bugit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.useCases.InsertBugUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBugViewModel @Inject constructor(
    private val insertBugUseCase: InsertBugUseCase
) : ViewModel() {


    fun insertBug(bugEntity: BugEntity) {
        viewModelScope.launch {
           insertBugUseCase.invoke(bugEntity)
        }
    }

}