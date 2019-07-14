package com.marahoney.tasque.ui.category_select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.databinding.ItemCategoryBinding
import com.marahoney.tasque.databinding.ItemCategorySelectBinding
import com.marahoney.tasque.ui.base.BaseViewHolder
import org.jetbrains.anko.sdk27.coroutines.onClick

class CategoryListAdapter(private val viewModel: CategorySelectViewModel) : ListAdapter<Category, CategoryListAdapter.ViewHolder>(Category.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category_select, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
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

    class ViewHolder(view: View) : BaseViewHolder<ItemCategorySelectBinding>(view)
}