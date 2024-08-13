package com.mobily.bugit.data.api

import com.mobily.bugit.data.model.NotionPage
import com.mobily.bugit.data.requestModule.NotionPageResponse
import retrofit2.Response

interface ApiHelper {
    suspend fun addDataOnNotionPage(token: String, version: String, page: NotionPage): Response<NotionPageResponse>
}