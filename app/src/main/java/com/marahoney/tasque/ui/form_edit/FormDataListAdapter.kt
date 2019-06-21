package com.marahoney.tasque.ui.form_edit

import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemFormArticleBinding
import com.marahoney.tasque.databinding.ItemFormImageBinding
import com.marahoney.tasque.ui.base.BaseViewHolder
import org.jetbrains.anko.sdk27.coroutines.onTouch

class FormDataListAdapter(private val viewModel: FormEditViewModel,
                          private val itemTouchHelper: ItemTouchHelper) : ListAdapter<FormData, RecyclerView.ViewHolder>(FormData.diffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FormData.Image -> VIEW_TYPE_IMAGE
            is FormData.Article -> VIEW_TYPE_ARTICLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_IMAGE -> ImageViewHolder(inflater.inflate(R.layout.item_form_image, parent, false))
            VIEW_TYPE_ARTICLE -> ArticleViewHolder(inflater.inflate(R.layout.item_form_article, parent, false))
            else -> ArticleViewHolder(inflater.inflate(R.layout.item_form_article, parent, false))
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

        holder.binding.button.onTouch { v, event ->
            if (event.action == ACTION_DOWN)
                itemTouchHelper.startDrag(holder)
        }
    }

    private fun onBindArticleViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.binding.item = getItem(position) as FormData.Article?
        holder.binding.vm = viewModel

        holder.binding.button.onTouch { v, event ->
            if (event.action == ACTION_DOWN)
                itemTouchHelper.startDrag(holder)
        }
    }

    class ImageViewHolder(view: View) : BaseViewHolder<ItemFormImageBinding>(view)
    class ArticleViewHolder(view: View) : BaseViewHolder<ItemFormArticleBinding>(view)

    companion object {
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_ARTICLE = 2
    }
}