package net.suyambu.zedgeexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.suyambu.zedge.ZedgeWallpaper
import net.suyambu.zedge.data.ZedgeImage
import net.suyambu.zedge.interfaces.UrlListener
import net.suyambu.zedge.interfaces.WallpaperListener
import org.json.JSONObject
import sh.fearless.hiper.data.HiperResponse
import sh.fearless.util.debug
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zedge = ZedgeWallpaper()
        zedge.search("iron man", 1, object : WallpaperListener {
            override fun onReject(response: HiperResponse) {
                debug(response)
            }

            override fun onError(error: Exception) {
                debug(error)
            }

            override fun onResolve(data: ZedgeImage) {
                debug(data)
            }
        })

        zedge.directUrl("ae7ce087-0cf8-3caf-8c7f-f7a957108865", object : UrlListener {
            override fun onReject(response: HiperResponse) {
                debug(response)
            }

            override fun onError(error: Exception) {
                debug(error)
            }

            override fun onResolve(url: String?) {
                debug(url)
            }
        })
    }
}