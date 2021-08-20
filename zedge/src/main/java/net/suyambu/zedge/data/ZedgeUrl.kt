package net.suyambu.zedge.data

import sh.fearless.hiper.data.Headers

data class ZedgeUrl(
    val url: String?,
    val message: String,
    val statusCode: Int,
    val isSuccessful: Boolean,
    val isRedirect: Boolean,
    val headers: Headers,
    val contentText: String?
)