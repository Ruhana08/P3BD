package com.example.googlebooks.data

data class BooksResponse(
    val totalItems: Int = 0,
    val items: List<BookItem>? = null
)
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)