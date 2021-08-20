package net.suyambu.zedge

import net.suyambu.zedge.data.*
import org.json.JSONObject
import sh.fearless.hiper.Hiper
import sh.fearless.hiper.controllers.Caller
import java.net.URLEncoder


class Zedge {
    companion object {
        const val API_URL = "https://api-gateway.zedge.net/"
        @Volatile private var wallInstance: Wallpaper? = null
        @Volatile private var ringInstance: Ringtone? = null

        fun getWallpaperInstance() = wallInstance ?: synchronized(this) {
            wallInstance ?: Zedge().Wallpaper()
        }

        fun getRingtoneInstance() = ringInstance ?: synchronized(this) {
            ringInstance ?: Zedge().Ringtone()
        }
    }

    private val hiper = Hiper.getInstance().async()
    private val trendingQuery = "\n    query browse(\$input: BrowseAsUgcInput!) {\n      browseAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "
    private val directUrlQuery = "\n    query contentDownloadUrl(\$itemId: ID!) {\n      contentDownloadUrlAsUgc(itemId: \$itemId)\n    }\n  "
    private val searchQuery = "\n    query search(\$input: SearchAsUgcInput!) {\n      searchAsUgc(input: \$input) {\n        ...browseContentItemsResource\n      }\n    }\n    \n  fragment browseContentItemsResource on BrowseContentItems {\n    page\n    total\n    items {\n      ... on BrowseWallpaper {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n      }\n\n      ... on BrowseRingtone {\n        id\n        contentType\n        title\n        tags\n        imageUrl\n        placeholderUrl\n        licensed\n        meta {\n          durationMs\n          previewUrl\n          gradientStart\n          gradientEnd\n        }\n      }\n    }\n  }\n\n  "


    inner class Ringtone {

        fun trending(page: Int, callback: ZedgeAudio.() -> Unit): Caller {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"RINGTONE","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", trendingQuery)
            val queue = hiper.post(API_URL, json=json)
            queue.resolve { resp ->
                var obj = JSONObject(resp.text!!)
                if (obj.has("errors")) {
                    val out = ZedgeAudio(
                        page = page,
                        pageCount = 0,
                        audios = listOf(),
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback.invoke(out)
                } else {
                    obj = obj.getJSONObject("data").getJSONObject("browseAsUgc")
                    val audios = ArrayList<Audio>()
                    val items = obj.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        audios.add(Audio(
                            id = item.getString("id"),
                            imageUrl = item.getString("imageUrl"),
                            licensed = item.getBoolean("licensed"),
                            title = item.getString("title"),
                            audioUrl = item.getJSONObject("meta").getString("previewUrl"),
                            gradientStart = item.getJSONObject("meta").getString("gradientStart"),
                            gradientEnd = item.getJSONObject("meta").getString("gradientEnd")
                        ))
                    }
                    val out = ZedgeAudio(
                        page = page,
                        pageCount = obj.getInt("total"),
                        audios = audios,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback.invoke(out)
                }
            }
            queue.reject { resp ->
                val out = ZedgeAudio(
                    page = page,
                    pageCount = 0,
                    audios = listOf(),
                    statusMessage = resp.message,
                    statusCode = resp.statusCode,
                    isSuccessful = resp.isSuccessful,
                    isRedirect = resp.isRedirect,
                    headers = resp.headers,
                    contentText = resp.text
                )
                callback.invoke(out)
            }
            queue.catch {
                throw it
            }
            return queue.execute()
        }

        fun search(query: String, page: Int, callback: ZedgeAudio.() -> Unit): Caller {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"RINGTONE","keyword":"${URLEncoder.encode(query.replace("\"", ""), "UTF-8")}","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", searchQuery)
            val queue = hiper.post(API_URL, json = json)
            queue.resolve { resp ->
                var obj = JSONObject(resp.text!!)
                if (obj.has("errors")) {
                    val out = ZedgeAudio(
                        page = page,
                        pageCount = 0,
                        audios = listOf(),
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
                } else {
                    obj = obj.getJSONObject("data").getJSONObject("searchAsUgc")
                    val audios = ArrayList<Audio>()
                    val items = obj.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        audios.add(Audio(
                            id = item.getString("id"),
                            imageUrl = item.getString("imageUrl"),
                            licensed = item.getBoolean("licensed"),
                            title = item.getString("title"),
                            audioUrl = item.getJSONObject("meta").getString("previewUrl"),
                            gradientStart = item.getJSONObject("meta").getString("gradientStart"),
                            gradientEnd = item.getJSONObject("meta").getString("gradientEnd")
                        ))
                    }
                    val out = ZedgeAudio(
                        page = page,
                        pageCount = obj.getInt("total"),
                        audios = audios,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
                }
            }
            queue.reject { resp ->
                val out = ZedgeAudio(
                    page = page,
                    pageCount = 0,
                    audios = listOf(),
                    statusMessage = resp.message,
                    statusCode = resp.statusCode,
                    isSuccessful = resp.isSuccessful,
                    isRedirect = resp.isRedirect,
                    headers = resp.headers,
                    contentText = resp.text
                )
                callback(out)
            }
            queue.catch { throw it }
            return queue.execute()
        }

    }

    inner class Wallpaper {
        fun search(query: String, page: Int, callback: ZedgeImage.() -> Unit): Caller {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","keyword":"${URLEncoder.encode(query.replace("\"", ""), "UTF-8")}","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", searchQuery)
            val queue = hiper.post(API_URL, json = json)
            queue.resolve { resp ->
                var obj = JSONObject(resp.text!!)
                if (obj.has("errors")) {
                    val out = ZedgeImage(
                        page = page,
                        pageCount = 0,
                        images = listOf(),
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
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
                    val out = ZedgeImage(
                        page = page,
                        pageCount = obj.getInt("total"),
                        images = images,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
                }
            }
            queue.reject { resp ->
                val out = ZedgeImage(
                    page = page,
                    pageCount = 0,
                    images = listOf(),
                    statusMessage = resp.message,
                    statusCode = resp.statusCode,
                    isSuccessful = resp.isSuccessful,
                    isRedirect = resp.isRedirect,
                    headers = resp.headers,
                    contentText = resp.text
                )
                callback(out)
            }
            queue.catch { throw it }
            return queue.execute()
        }

        fun directUrl(itemId: String, callback: ZedgeUrl.() -> Unit): Caller {
            val json = JSONObject("""
            {"variables":{"itemId":"$itemId"}}
        """.trimIndent())
            json.put("query", directUrlQuery)
            val queue = hiper.post(API_URL, json = json)
            queue.resolve { resp ->
                val obj = JSONObject(resp.text!!)
                if (obj.has("errors")) {
                    callback(ZedgeUrl(
                        url = null,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    ))
                } else {
                    val url = obj.getJSONObject("data").getString("contentDownloadUrlAsUgc")
                    callback(ZedgeUrl(
                        url = url,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    ))
                }
            }
            queue.reject { resp ->
                callback(ZedgeUrl(
                    url = null,
                    statusMessage = resp.message,
                    statusCode = resp.statusCode,
                    isSuccessful = resp.isSuccessful,
                    isRedirect = resp.isRedirect,
                    headers = resp.headers,
                    contentText = resp.text
                ))
            }
            queue.catch { throw it }
            return queue.execute()
        }

        fun trending(page: Int, callback: ZedgeImage.() -> Unit): Caller {
            val json = JSONObject("""
            {"variables":{"input":{"contentType":"WALLPAPER","page":$page,"size":24}}}
        """.trimIndent())
            json.put("query", trendingQuery)
            val queue = hiper.post(API_URL, json=json)
            queue.resolve { resp ->
                var obj = JSONObject(resp.text!!)
                if (obj.has("errors")) {
                    val out = ZedgeImage(
                        page = page,
                        pageCount = 0,
                        images = listOf(),
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
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
                    val out = ZedgeImage(
                        page = page,
                        pageCount = obj.getInt("total"),
                        images = images,
                        statusMessage = resp.message,
                        statusCode = resp.statusCode,
                        isSuccessful = resp.isSuccessful,
                        isRedirect = resp.isRedirect,
                        headers = resp.headers,
                        contentText = resp.text
                    )
                    callback(out)
                }
            }
            queue.reject { resp ->
                val out = ZedgeImage(
                    page = page,
                    pageCount = 0,
                    images = listOf(),
                    statusMessage = resp.message,
                    statusCode = resp.statusCode,
                    isSuccessful = resp.isSuccessful,
                    isRedirect = resp.isRedirect,
                    headers = resp.headers,
                    contentText = resp.text
                )
                callback(out)
            }
            queue.catch { throw it }
            return queue.execute()
        }
    }
}