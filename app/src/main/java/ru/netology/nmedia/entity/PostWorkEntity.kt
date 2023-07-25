package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.Post

@Entity
data class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val authorId: Long,
    val content: String,
    val published: String,
    val likesCount: Int = 0,
    val likedByMe: Boolean = false,
    val share: Boolean = false,
    val sharesCount: Int = 0,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    val viewed: Boolean = false,
    val video: String? = "",
    var uri: String? = null
)
{
    fun toDto() =
        Post(
            id,
            author,
            authorAvatar,
           authorId,
            content,
            published,
            likesCount,
            likedByMe,
            share,
            sharesCount,
            attachment?.toDto()
        )


    companion object {
        fun fromDto(post: Post) =
            PostWorkEntity(
                post.id,
                post.author,
                post.authorAvatar,
                post.authorId,
                post.content,
                post.published,
                post.likes,
                post.likedByMe,
                post.share,
                post.sharesCount,
                AttachmentEmbeddable.fromDto(post.attachment),
                post.viewed,
                post.video
            )
    }
}
//
//
//fun List<PostEntity>.toDto() = map(PostEntity::toDto)
//fun List<Post>.toEntity() = map(PostEntity::fromDto)

