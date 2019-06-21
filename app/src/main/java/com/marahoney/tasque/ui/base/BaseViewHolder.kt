package com.marahoney.tasque.ui.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {
    var binding: T = DataBindingUtil.bind(view)!!
}
