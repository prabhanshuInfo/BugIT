package com.mobily.bugit.data

import com.mobily.bugit.data.api.ApiHelper
import com.mobily.bugit.data.model.NotionPage
import com.mobily.bugit.utils.AppConstants
import javax.inject.Inject

class NotionRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun addPageToDatabase(notionPage: NotionPage) =  apiHelper.addDataOnNotionPage(
        token = AppConstants.NOTION_TOKEN,
        version = AppConstants.NOTION_VERSION,
        page = notionPage
    )
}