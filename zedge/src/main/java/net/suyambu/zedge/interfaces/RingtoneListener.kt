package net.suyambu.zedge.interfaces

import net.suyambu.zedge.data.ZedgeAudio
import sh.fearless.hiper.data.HiperResponse
import java.lang.Exception

interface RingtoneListener {
    fun onResolve(data: ZedgeAudio)
    fun onReject(response: HiperResponse)
    fun onError(error: Exception)
}