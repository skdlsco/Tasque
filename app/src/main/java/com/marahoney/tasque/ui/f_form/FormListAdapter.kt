package com.marahoney.tasque.ui.f_form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.marahoney.tasque.R
import com.marahoney.tasque.data.model.Form
import com.marahoney.tasque.data.model.FormData
import com.marahoney.tasque.databinding.ItemFormBinding
import com.marahoney.tasque.ui.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class FormListAdapter(private val viewModel: FormFragmentViewModel) : ListAdapter<Form, FormListAdapter.ViewHolder>(Form.diffCallback) {

    private val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd(E)에 생성됨.", Locale.KOREAN)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_form, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.vm = viewModel

        holder.binding.info.text = makeInfo(item)
        holder.binding.date.text = simpleDateFormat.format(item.createAt)

        if (item.data?.count { it.mode == "image" } == 0) {
            holder.binding.image2.visibility = View.GONE
            holder.binding.image3.visibility = View.GONE
            holder.binding.image4.visibility = View.GONE

            Glide.with(holder.binding.image1)
                    .load(item.screenshot)
                    .into(holder.binding.image1)
        }
    }

    private fun makeInfo(item: Form): String {
        val result = StringBuilder()
        val pictureSize = item.data?.filter { it is FormData.Image }?.size ?: 0

        if (pictureSize == 1)
            result.append("사진 1장")
        else if (pictureSize > 1)
            result.append("${pictureSize}장의 사진")

        val letterCount = item.data?.filter { it is FormData.Article }?.sumBy { (it as FormData.Article).article.length }
                ?: 0
        if (result.isNotBlank())
            result.append(", ")

        result.append("글자 수 ${letterCount}자")
        return result.toString()
    }

    class ViewHolder(view: View) : BaseViewHolder<ItemFormBinding>(view)
}