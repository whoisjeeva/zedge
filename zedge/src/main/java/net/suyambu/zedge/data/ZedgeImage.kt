package net.suyambu.zedge.data

import sh.fearless.hiper.data.Headers

data class ZedgeImage(
    val page: Int,
    val pageCount: Int,
    val images: List<Image>,
    val statusMessage: String,
    val statusCode: Int,
    val isSuccessful: Boolean,
    val isRedirect: Boolean,
    val headers: Headers,
    val contentText: String?
)