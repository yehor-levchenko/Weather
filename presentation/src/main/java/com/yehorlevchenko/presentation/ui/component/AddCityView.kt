package com.yehorlevchenko.presentation.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import com.yehorlevchenko.presentation.databinding.ViewAddCityBinding
import com.yehorlevchenko.presentation.utils.makeGone
import com.yehorlevchenko.presentation.utils.makeVisible

class AddCityView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = ViewAddCityBinding.inflate(LayoutInflater.from(context), this, true)
    private var onConfirmClick: (String) -> Unit = {}

    init { setView() }

    fun doOnConfirmClick(onConfirmClick: (String) -> Unit) {
        this.onConfirmClick = onConfirmClick
    }

    fun getEditText(): EditText {
        return binding.etCity
    }

    fun clear() {
        binding.etCity.text.clear()
    }

    private fun setView() = with (binding) {
        ivConfirm.setOnClickListener { onConfirmClick(etCity.text?.toString() ?: "") }
        etCity.doAfterTextChanged { editable ->
            editable?.let {
                if (it.isNotEmpty() && it.isNotBlank()) ivConfirm.makeVisible()
                else ivConfirm.makeGone()
            }
        }
    }
}