package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.Utils
import ru.netology.nmedia.view.load

interface PostCallback {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun remove(post: Post)
    fun edit(post: Post)
    fun onVideo(post: Post)
    fun onPost(post: Post)
    fun onImage(post: Post)
}

class PostsAdapter(private val postCallback: PostCallback) :
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostsDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when(getItem(position)){
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            null -> error("Unknown item type")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
         when(viewType){
            R.layout.card_post -> {
                val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, postCallback)
            }
            R.layout.card_ad -> {
                val binding = CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdViewHolder(binding)
            }
            else -> error("Unknown view type $viewType")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)){
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> error("Unknown view type")
        }
    }

}

class AdViewHolder(
    private val binding: CardAdBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad){
        binding.imageAd.load("http://10.0.2.2:9999/media/${ad.name}")
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val postCallback: PostCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            like.text = Utils.reductionInNumbers(post.likes)
            share.text = Utils.reductionInNumbers(post.sharesCount)
            like.isChecked = post.likedByMe

            menu.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE


            Glide.with(avatar)
                .load("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .timeout(10_000)
                .error(R.drawable.ic_error)
                .into(avatar)


            when (post.attachment?.type) {
                AttachmentType.IMAGE -> {
                    Glide.with(viewForImage)
                        .load("http://10.0.2.2:9999/media/${post.attachment?.url}")
                        .timeout(10_000)
                        .into(viewForImage)
                }
            }
            viewForImage.isVisible = post.attachment?.type == AttachmentType.IMAGE



            if (!post.video.isNullOrBlank()) {
                group.visibility = View.VISIBLE
            }

            like.setOnClickListener {
                postCallback.onLike(post)
            }

            share.setOnClickListener {
                postCallback.onShare(post)
            }

            play.setOnClickListener {
                postCallback.onVideo(post)
            }

            viewForVideo.setOnClickListener {
                postCallback.onVideo(post)
            }

            content.setOnClickListener {
                postCallback.onPost(post)
            }

            viewForImage.setOnClickListener {
                postCallback.onImage(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.post_remove -> {
                                postCallback.remove(post)
                                true
                            }
                            R.id.post_edit -> {
                                postCallback.edit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }


        }
    }
}


class PostsDiffCallback : DiffUtil.ItemCallback<FeedItem>() {

    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) return false
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

}