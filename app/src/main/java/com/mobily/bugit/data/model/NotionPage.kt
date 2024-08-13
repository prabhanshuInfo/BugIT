package com.mobily.bugit.data.model

data class NotionPage(val parent: Parent,
                      val properties: Map<String, NotionProperty>)
