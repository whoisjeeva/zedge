package net.suyambu.zedge.data

import sh.fearless.hiper.data.Headers

data class ZedgeImage(
    val currentPageNumber: Int,
    val pageCount: Int,
    val images: List<Image>,
    val message: String,
    val statusCode: Int,
    val isSuccessful: Boolean,
    val isRedirect: Boolean,
    val headers: Headers,
    val contentText: String?
)