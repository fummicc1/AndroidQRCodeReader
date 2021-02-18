package dev.fummicc1.lit.androidqrcodereader

data class BookResponse(
    val items: List<Book>
)

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val pageCount: Int,
    val imageLinks: ImageLinks,
    val language: String
)

data class ImageLinks(
    val thumbnail: String
)