
package com.shahryar.nextjstoolkit.icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object NextIcons {

    private fun load(name: String): Icon =
        IconLoader.getIcon(
            "/icons/$name.svg",
            NextIcons::class.java
        )

    val NEXT = load("next")

    val JS = load("js")
    val TS = load("ts")

    val COMPONENT = load("component")
    val LAYOUT = load("layout")
    val PAGE = load("page")
    val PROXY = load("proxy")
    val ERROR = load("error")
    val NOT_FOUND = load("not-found")
    val SERVER_ACTION = load("server-action")
    val API_ROUTE = load("api-route")
    val LOADING = load("loading")
}
