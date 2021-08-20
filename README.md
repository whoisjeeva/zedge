<h1 align="center">Zedge API for Android</h1>

<p align="center">
    <a href="https://jitpack.io/#okbash/zedge"><img src="https://img.shields.io/jitpack/v/github/okbash/zedge?style=for-the-badge" alt="Release"></a>
    <a href="https://travis-ci.com/okbash/zedge"><img src="https://img.shields.io/travis/com/okbash/zedge/master?style=for-the-badge" alt="Build Status"></a>
    <a href="https://github.com/okbash/zedge/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/okbash/zedge.svg?style=for-the-badge" alt="License"></a>
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
val zedge = Zedge.getWallpaperInstance()
```

Get all trending wallpapers

```kotlin
zedge.trending(page = 1) {
    if (isSuccessful) {
        debug(images)
    } else {
        debug(statusCode)
    }
}
```

Get HD image URL for a ZedgeImage

```kotlin
zedge.directUrl(itemId = "ae7ce087-0cf8-3caf-8c7f-f7a957108865") {
    if (isSuccessful) {
        debug(url)
    } else {
        debug(statusCode)
    }
}
```

Search Zedge database

```kotlin
zedge.search(query = "iron man", page = 1) {
    if (isSuccessful) {
        debug(images)
    } else {
        debug(statusCode)
    }
}
```
