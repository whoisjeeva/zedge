package net.suyambu.zedge.data

data class ZedgeImage(
    val page: Int,
    val total: Int,
    val images: List<Image>
)