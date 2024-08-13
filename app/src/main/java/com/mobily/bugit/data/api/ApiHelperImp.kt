package com.mobily.bugit.data.api

import com.mobily.bugit.data.model.NotionPage
import javax.inject.Inject

class ApiHelperImp @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun addDataOnNotionPage(
        token: String,
        version: String,
        page: NotionPage
    ) = apiService.addDataOnNotionPage(token, version, page)
}