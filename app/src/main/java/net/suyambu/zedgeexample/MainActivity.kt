package net.suyambu.zedgeexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.suyambu.zedge.Zedge
import net.suyambu.zedge.data.ZedgeAudio
import net.suyambu.zedge.data.ZedgeImage
import net.suyambu.zedge.interfaces.RingtoneListener
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

        val zedge = Zedge.getRingtoneInstance()


        try {
            zedge.search(query = "iron man", page = 1) {
                if (isSuccessful) {
                    debug(audios)
                } else {
                    debug(contentText)
                }
            }
        } catch (e: Exception) {
            debug(e)
        }

    }
}