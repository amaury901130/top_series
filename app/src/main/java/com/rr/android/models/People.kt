package com.rr.android.models

data class People(
    val _links: Links?,
    val id: Int?,
    val image: Image?,
    val name: String?,
    val url: String?,
    var shows: List<Show> = emptyList()
)

data class CrewCredits(
    val _embedded: Embedded,
    val _links: LinksX,
    val type: String
)

data class Embedded(
    val show: Show
)

data class LinksX(
    val show: ShowX
)

data class ShowX(
    val href: String
)
