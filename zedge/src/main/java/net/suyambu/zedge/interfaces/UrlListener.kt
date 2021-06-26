package net.suyambu.zedge.interfaces

import sh.fearless.hiper.data.HiperResponse
import java.lang.Exception

interface UrlListener {
    fun onReject(response: HiperResponse) {}
    fun onError(error: Exception) {}
    fun onResolve(url: String?) {}
}