package com.marahoney.tasque.ui.category_detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemCategoryDetailFormBinding
import com.marahoney.tasque.ui.base.BaseViewHolder
import org.jetbrains.anko.sdk27.coroutines.onClick

class FormListAdapter(private val viewModel: CategoryDetailViewModel) : ListAdapter<Form, FormListAdapter.ViewHolder>(Form.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category_detail_form, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
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

    class ViewHolder(view: View) : BaseViewHolder<ItemCategoryDetailFormBinding>(view)
}