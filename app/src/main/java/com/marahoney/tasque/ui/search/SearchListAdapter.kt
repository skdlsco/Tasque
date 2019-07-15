package com.marahoney.tasque.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemSearchCategoryBinding
import com.marahoney.tasque.databinding.ItemSearchFormBinding
import com.marahoney.tasque.ui.base.BaseViewHolder
import org.jetbrains.anko.sdk27.coroutines.onClick

class SearchListAdapter(private val viewModel: SearchViewModel) : ListAdapter<Any, RecyclerView.ViewHolder>(diffCallback) {

    override fun getItemViewType(position: Int): Int =
            when (getItem(position)) {
                is Form -> VIEW_TYPE_FORM
                is Category -> VIEW_TYPE_CATEGORY
                else -> VIEW_TYPE_FORM
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                VIEW_TYPE_FORM -> FormViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_form, parent, false))
                VIEW_TYPE_CATEGORY -> CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_category, parent, false))
                else -> FormViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_form, parent, false))
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FormViewHolder -> onBindFormViewHolder(holder, position)
            is CategoryViewHolder -> onBindCategoryViewHolder(holder, position)
        }
    }

    private fun onBindCategoryViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position) as Category
        holder.binding.item = item
        holder.binding.vm = viewModel

        val imageArray = viewModel.getThumbNails(item.token)

        holder.binding.run {
            when (imageArray.size) {
                in 0..1 -> {
                    image2.visibility = View.GONE
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                2 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                3 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.VISIBLE
                    image4.visibility = View.GONE
                    image3.layoutParams = (image3.layoutParams as ConstraintLayout.LayoutParams).apply {
                        this.endToStart = image2.id
                    }
                }
                4 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.VISIBLE
                    image4.visibility = View.VISIBLE
                    image3.layoutParams = (image3.layoutParams as ConstraintLayout.LayoutParams).apply {
                        this.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                }
            }

            imageArray.forEachIndexed { index, url ->
                val view = when (index) {
                    0 -> image1
                    1 -> image2
                    2 -> image3
                    3 -> image4
                    else -> image1
                }
                Glide.with(view)
                        .load(url)
                        .into(view)
            }
        }

        holder.itemView.onClick {
            viewModel.onClickForm(item, position)
        }
    }

    private fun onBindFormViewHolder(holder: FormViewHolder, position: Int) {
        val item = getItem(position) as Form
        holder.binding.item = item
        holder.binding.vm = viewModel

        val imageCount = item.data?.count { it.mode == "image" } ?: 0
        holder.binding.run {
            when (imageCount) {
                in 0..1 -> {
                    image2.visibility = View.GONE
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                2 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                3 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.VISIBLE
                    image4.visibility = View.GONE
                    image3.layoutParams = (image3.layoutParams as ConstraintLayout.LayoutParams).apply {
                        this.endToStart = image2.id
                    }
                }
                4 -> {
                    image2.visibility = View.VISIBLE
                    image3.visibility = View.VISIBLE
                    image4.visibility = View.VISIBLE
                    image3.layoutParams = (image3.layoutParams as ConstraintLayout.LayoutParams).apply {
                        this.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                }
            }
            if (imageCount == 0) {
                Glide.with(image1)
                        .load(item.screenshot)
                        .into(image1)
            } else {
                item.data?.filter { it.mode == "image" }?.forEachIndexed { index, formData ->
                    val url = (formData as FormData.Image).image
                    val view = when (index) {
                        0 -> image1
                        1 -> image2
                        2 -> image3
                        3 -> image4
                        else -> image1
                    }
                    Glide.with(view)
                            .load(url)
                            .into(view)
                }
            }
        }

        holder.itemView.onClick {
            viewModel.onClickForm(item, position)
        }
    }

    class FormViewHolder(view: View) : BaseViewHolder<ItemSearchFormBinding>(view)
    class CategoryViewHolder(view: View) : BaseViewHolder<ItemSearchCategoryBinding>(view)

    companion object {

        val VIEW_TYPE_FORM = 1
        val VIEW_TYPE_CATEGORY = 2

        val diffCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
        }
    }
}