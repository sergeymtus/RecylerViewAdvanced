package ru.netology.nmedia.dto

sealed interface FeedItem {
    val id: Long
}


data class Post(
    override val id: Long,
    val author: String,
    val authorAvatar: String,
    val authorId: Long,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val share: Boolean = false,
    val sharesCount: Int = 0,
    var attachment: Attachment? = null,
    var viewed: Boolean = false,
    val video: String = "",
    val ownedByMe: Boolean = false
) : FeedItem

data class Ad(
    override val id: Long,
    val name: String
) : FeedItem

data class Attachment(
    val url: String,
    val description: String = "",
    val type: AttachmentType,
)

enum class AttachmentType {
    IMAGE
}