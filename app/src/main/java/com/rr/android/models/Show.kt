package com.rr.android.models

import android.annotation.SuppressLint
import com.rr.android.util.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

data class Show(
    val _links: Links?,
    val averageRuntime: Int? = 0,
    val ended: String?,
    val externals: Externals?,
    val genres: List<String>?,
    val id: Int?,
    val image: Image?,
    val language: String?,
    val name: String?,
    val network: Network?,
    val officialSite: String?,
    val premiered: String?,
    val rating: Rating?,
    val runtime: Int? = 0,
    val schedule: Schedule?,
    val status: String?,
    val summary: String?,
    val type: String?,
    val updated: Int?,
    val url: String?,
    val webChannel: WebChannel?,
    val weight: Int?
)

data class Links(
    val previousepisode: Previousepisode?,
    val self: Self?
)

data class Externals(
    val imdb: String?,
    val thetvdb: Int?,
    val tvrage: Int?
)

data class Image(
    val medium: String?,
    val original: String?
)

data class Network(
    val country: Country?,
    val id: Int?,
    val name: String?
)

data class Rating(
    val average: Any?
)

data class Schedule(
    val days: List<String>?,
    val time: String?
)

data class WebChannel(
    val country: Country?,
    val id: Int?,
    val name: String?
)

data class Previousepisode(
    val href: String?
)

data class Self(
    val href: String?
)

data class Country(
    val code: String?,
    val name: String?,
    val timezone: String?
)

@SuppressLint("SimpleDateFormat")
fun Show.lifeTime(): Long {
    return premiered?.let {
        val format = SimpleDateFormat(DATE_FORMAT)
        val releaseDate: Date = format.parse(it)
        val endDate: Date = ended?.run { format.parse(this) } ?: Calendar.getInstance().time
        val diff: Long = endDate.time - releaseDate.time

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    } ?: 0L
}
