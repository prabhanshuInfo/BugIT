package com.mobily.bugit.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobily.bugit.database.entity.BugEntity
import com.mobily.bugit.domain.useCases.FetchBugUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(private val fetchBugUseCase: FetchBugUseCase): ViewModel() {

    private val _bugList = MutableStateFlow<List<BugEntity>>(emptyList())
    val bugList: StateFlow<List<BugEntity>> = _bugList

    init {
        fetchAllBugs()
    }

    private fun fetchAllBugs() {
        viewModelScope.launch {
            val fetchBugs = fetchBugUseCase()

            fetchBugs.collectLatest {
                _bugList.value = it
            }
        }

    }
}