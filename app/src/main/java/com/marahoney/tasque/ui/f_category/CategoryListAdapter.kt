package com.marahoney.tasque.ui.f_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.databinding.ItemCategoryBinding
import com.marahoney.tasque.ui.base.BaseViewHolder

class CategoryListAdapter(private val viewModel: CategoryFragmentViewModel) : ListAdapter<Category, CategoryListAdapter.ViewHolder>(Category.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.vm = viewModel

        val imageArray = viewModel.getThumbNails(item.token)

        holder.binding.run { }
        holder.binding.run {
            when (imageArray.size) {
                in 0..1 -> {
                    image2.visibility = View.GONE
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                2 -> {
                    image3.visibility = View.GONE
                    image4.visibility = View.GONE
                }
                3 -> {
                    image4.visibility = View.GONE
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
    }

    class ViewHolder(view: View) : BaseViewHolder<ItemCategoryBinding>(view)
}