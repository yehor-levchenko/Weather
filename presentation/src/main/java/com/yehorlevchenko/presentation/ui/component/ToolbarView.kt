package com.yehorlevchenko.presentation.ui.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yehorlevchenko.presentation.R
import com.yehorlevchenko.presentation.databinding.ViewToolbarBinding
import com.yehorlevchenko.presentation.utils.makeGone
import com.yehorlevchenko.presentation.utils.makeVisible

class ToolbarView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = ViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    private var onBackClick: () -> Unit = {}

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ToolbarView)
        try { applyAttrs(attributes) } finally { attributes.recycle() }

        binding.ivArrowBack.setOnClickListener { onBackClick() }
    }

    fun doOnBackClick(onBackClick: () -> Unit) {
        this.onBackClick = onBackClick
    }

    fun setTitle(text: String?) {
        text?.let { binding.tvTitle.text = it }
    }

    private fun setArrowBackNeeded(backNavigationNeeded: Boolean) = with (binding.ivArrowBack) {
        if (backNavigationNeeded) makeVisible() else makeGone()
    }

    private fun applyAttrs(attrs: TypedArray) {
        setTitle(attrs.getString(R.styleable.ToolbarView_tv_title))
        setArrowBackNeeded(attrs.getBoolean(R.styleable.ToolbarView_tv_arrow_back_needed, true))
    }
}