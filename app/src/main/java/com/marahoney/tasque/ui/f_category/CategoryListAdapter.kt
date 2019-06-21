package com.marahoney.tasque.ui.f_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Category
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemCategoryBinding
import com.marahoney.tasque.ui.base.BaseViewHolder

class CategoryListAdapter(private val viewModel: CategoryFragmentViewModel) : ListAdapter<Category, CategoryListAdapter.ViewHolder>(Category.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.vm = viewModel

//        val imageArray = ArrayList<String>()
//        (0..item.forms.size).forEach {
//            it
//        }
    }

    fun getImage(pos: Int, imgNum: Int): String? {
        if (getItem(pos).forms.size < imgNum)
            return null
        val formData = getItem(pos).forms[imgNum].data.firstOrNull { it is FormData.Image }
                ?: return null
        return (formData as FormData.Image).image
    }

    class ViewHolder(view: View) : BaseViewHolder<ItemCategoryBinding>(view)
}