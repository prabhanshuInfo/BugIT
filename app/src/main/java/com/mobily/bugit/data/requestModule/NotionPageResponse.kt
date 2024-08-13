package com.mobily.bugit.data.requestModule

import com.mobily.bugit.data.model.Parent

data class NotionPageResponse(val obj: String,
                              val id: String,
                              val created_time: String,
                              val last_edited_time: String,
                              val parent: Parent)
