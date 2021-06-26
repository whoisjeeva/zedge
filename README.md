# Zedge - Android API library for Zedge website

<p align="center">
    <a href="https://jitpack.io/#okbash/zedge"><img src="https://img.shields.io/jitpack/v/github/okbash/zedge?style=for-the-badge" alt="Release"></a>
    <a href="https://travis-ci.com/okbash/zedge"><img src="https://img.shields.io/travis/com/okbash/zedge/master?style=for-the-badge" alt="Build Status"></a>
    <a href="https://github.com/okbash/zedge/blob/master/LICENSE"><img src="https://img.shields.io/github/license/okbash/zedge.svg?style=for-the-badge" alt="License"></a>
<!--     <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/okbash/zedge?logo=GitHub&style=for-the-badge"> -->
    <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/okbash/zedge?logo=GitHub&style=for-the-badge">
    <a href="https://github.com/okbash/zedge/issues"><img alt="GitHub open issues" src="https://img.shields.io/github/issues/okbash/zedge?style=for-the-badge"></a>
</p>


### Getting Started

Add it in your root build.gradle at the end of repositories

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```gradle
implementation "com.github.okbash.zedge:$zedge_version"
```

### Using Zedge wallpaper API

Create a Zedge wallpaper instance

```kotlin
val zedge = ZedgeWallpaper()
```

Get all trending wallpapers

```kotlin
zedge.trending(1, object : WallpaperListener {
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
```

Get HD image URL for a ZedgeImage

```kotlin
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
```

Search Zedge database

```kotlin
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
```