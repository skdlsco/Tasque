package com.marahoney.tasque.data.model

import java.util.*

data class ServerForm(val share: Boolean = false,
                 val title: String = "",
                 val content: List<ServerFormData> = listOf(),
                 val launchLink: String = "",
                 val pubDate: Date = Date(),
                 val id: String = "")