package net.suyambu.zedge.interfaces

import net.suyambu.zedge.data.ZedgeImage
import sh.fearless.hiper.data.HiperResponse
import java.lang.Exception

interface WallpaperListener {
    fun onReject(response: HiperResponse) {}
    fun onError(error: Exception) {}
    fun onResolve(data: ZedgeImage) {}
}