package com.mobily.bugit.data.api

import com.mobily.bugit.data.model.NotionPage
import com.mobily.bugit.data.requestModule.NotionPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("v1/pages")
    suspend fun addDataOnNotionPage(
        @Header("Authorization") token: String,
        @Header("Notion-Version") version: String,
        @Body page: NotionPage
    ): Response<NotionPageResponse>
}