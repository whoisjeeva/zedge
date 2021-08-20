package net.suyambu.zedge.data

import sh.fearless.hiper.data.Headers

data class ZedgeAudio(
    val currentPageNumber: Int,
    val pageCount: Int,
    val audios: List<Audio>,
    val message: String,
    val statusCode: Int,
    val isSuccessful: Boolean,
    val isRedirect: Boolean,
    val headers: Headers,
    val contentText: String?
)
