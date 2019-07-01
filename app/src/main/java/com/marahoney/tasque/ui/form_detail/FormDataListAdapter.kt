package com.marahoney.tasque.ui.form_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemFormDetailArticleBinding
import com.marahoney.tasque.databinding.ItemFormDetailImageBinding
import com.marahoney.tasque.ui.base.BaseViewHolder

class FormDataListAdapter(private val viewModel: FormDetailViewModel) : ListAdapter<FormData, RecyclerView.ViewHolder>(FormData.diffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FormData.Image -> VIEW_TYPE_IMAGE
            is FormData.Article -> VIEW_TYPE_ARTICLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_IMAGE -> ImageViewHolder(inflater.inflate(R.layout.item_form_detail_image, parent, false))
            VIEW_TYPE_ARTICLE -> ArticleViewHolder(inflater.inflate(R.layout.item_form_detail_article, parent, false))
            else -> ArticleViewHolder(inflater.inflate(R.layout.item_form_detail_article, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> onBindImageViewHolder(holder, position)
            is ArticleViewHolder -> onBindArticleViewHolder(holder, position)
        }
    }

    private fun onBindImageViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.item = getItem(position) as FormData.Image?
        holder.binding.vm = viewModel

        Glide.with(holder.binding.image)
                .load(holder.binding.item?.image)
                .into(holder.binding.image)
    }

    private fun onBindArticleViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.binding.item = getItem(position) as FormData.Article?
        holder.binding.vm = viewModel
    }

    class ImageViewHolder(view: View) : BaseViewHolder<ItemFormDetailImageBinding>(view)
    class ArticleViewHolder(view: View) : BaseViewHolder<ItemFormDetailArticleBinding>(view)

    companion object {
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_ARTICLE = 2
    }
}