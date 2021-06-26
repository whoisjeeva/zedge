package net.suyambu.zedge

import net.suyambu.zedge.data.Image
import net.suyambu.zedge.data.ZedgeImage
import net.suyambu.zedge.interfaces.UrlListener
import net.suyambu.zedge.interfaces.WallpaperListener
import org.json.JSONArray
import org.json.JSONObject
import sh.fearless.hiper.Hiper
import sh.fearless.hiper.Queue
import sh.fearless.hiper.controllers.Caller
import sh.fearless.hiper.data.HiperResponse
import sh.fearless.util.debug
import java.lang.Exception
import java.net.URLEncoder

class ZedgeWallpaper {
    companion object {
        const val API_URL = "https://api-gateway.zedge.net/"
    }

    private val hiper = Hiper.getInstance().async()
    private val trendingQuery = "\n    query browse(\$input: BrowseAsUgcInput!) {\n      browseAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "
    private val directUrlQuery = "\n    query contentDownloadUrl(\$itemId: ID!) {\n      contentDownloadUrlAsUgc(itemId: \$itemId)\n    }\n  "
    private val searchQuery = "\n    query search(\$input: SearchAsUgcInput!) {\n      searchAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "


    fun search(q: String, pageNum: Int, listener: WallpaperListener? = null): Caller {
        val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","keyword":"${URLEncoder.encode(q.replace("\"", ""), "UTF-8")}","page":$pageNum,"size":24}}}
        """.trimIndent())
        json.put("query", searchQuery)
        val queue = hiper.post(API_URL, json = json)
        queue.resolve {
            var obj = JSONObject(it.text!!)
            if (obj.has("errors")) {
                val out = ZedgeImage(page = pageNum, total = 0, images = listOf())
                listener?.onResolve(out)
            } else {
                obj = obj.getJSONObject("data").getJSONObject("searchAsUgc")
                val images = ArrayList<Image>()
                val items = obj.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    images.add(Image(
                        id = item.getString("id"),
                        imageUrl = item.getString("imageUrl"),
                        licensed = item.getBoolean("licensed"),
                        title = item.getString("title")
                    ))
                }
                val out = ZedgeImage(page = pageNum, total = obj.getInt("total"), images = images)
                listener?.onResolve(out)
            }
        }
        queue.reject { listener?.onReject(it) }
        queue.catch { listener?.onError(it) }
        return queue.execute()
    }

    fun directUrl(itemId: String, listener: UrlListener? = null): Caller {
        val json = JSONObject("""
            {"variables":{"itemId":"$itemId"}}
        """.trimIndent())
        json.put("query", directUrlQuery)
        val queue = hiper.post(API_URL, json = json)
        queue.resolve {
            val obj = JSONObject(it.text!!)
            if (obj.has("errors")) {
                listener?.onResolve(null)
            } else {
                val url = obj.getJSONObject("data").getString("contentDownloadUrlAsUgc")
                listener?.onResolve(url)
            }
        }
        queue.reject { listener?.onReject(it) }
        queue.catch { listener?.onError(it) }
        return queue.execute()
    }

    fun trending(pageNum: Int, listener: WallpaperListener? = null): Caller {
        val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","page":$pageNum,"size":24}}}
        """.trimIndent())
        json.put("query", trendingQuery)
        val queue = hiper.post(API_URL, json=json)
        queue.resolve {
            var obj = JSONObject(it.text!!)
            if (obj.has("errors")) {
                val out = ZedgeImage(page = pageNum, total = 0, images = listOf())
                listener?.onResolve(out)
            } else {
                obj = obj.getJSONObject("data").getJSONObject("browseAsUgc")
                val images = ArrayList<Image>()
                val items = obj.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    images.add(Image(
                        id = item.getString("id"),
                        imageUrl = item.getString("imageUrl"),
                        licensed = item.getBoolean("licensed"),
                        title = item.getString("title")
                    ))
                }
                val out = ZedgeImage(page = pageNum, total = obj.getInt("total"), images = images)
                listener?.onResolve(out)
            }
        }
        queue.reject {
            listener?.onReject(it)
        }
        queue.catch {
            listener?.onError(it)
        }
        return queue.execute()
    }
}