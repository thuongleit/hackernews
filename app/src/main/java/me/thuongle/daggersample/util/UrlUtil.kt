package me.thuongle.daggersample.util

/**  Based on : http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/webkit/CookieManager.java#CookieManager.getBaseDomain%28java.lang.String%29
 * Get the base domain for a given host or url. E.g. mail.google.com will return google.com
 * @param url
 * *
 * @return
 */
fun getBaseDomain(url: String?): String {
    if (url == null || url.isEmpty())
        return ""

    val host = getHost(url)

    var startIndex = 0
    var nextIndex = host.indexOf('.')
    val lastIndex = host.lastIndexOf('.')
    while (nextIndex < lastIndex) {
        startIndex = nextIndex + 1
        nextIndex = host.indexOf('.', startIndex)
    }
    if (startIndex > 0) {
        return host.substring(startIndex)
    } else {
        return host
    }
}

/**
 * Will take a url such as http://www.stackoverflow.com and return www.stackoverflow.com

 * @param url
 * *
 * @return
 */
fun getHost(url: String?): String {
    if (url == null || url.isEmpty())
        return ""

    var doubleslash = url.indexOf("//")
    if (doubleslash == -1)
        doubleslash = 0
    else
        doubleslash += 2

    var end = url.indexOf('/', doubleslash)
    end = if (end >= 0) end else url.length

    val port = url.indexOf(':', doubleslash)
    end = if (port in 1..(end - 1)) port else end

    return url.substring(doubleslash, end)
}