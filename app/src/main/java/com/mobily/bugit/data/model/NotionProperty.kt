package com.mobily.bugit.data.model

data class NotionProperty( val title: List<NotionText>? = null,
                           val rich_text: List<NotionText>? = null,
                           val url: String? = null)
