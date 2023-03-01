package com.cerenb.flickrfinder.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.cerenb.flickrfinder.databinding.LayoutErrorOverlayBinding

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val viewBinding: LayoutErrorOverlayBinding = LayoutErrorOverlayBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        orientation = VERTICAL
    }

    fun showError(errorMessage: String, retry: (() -> Unit)? = null) {
        viewBinding.root.isVisible = true
        viewBinding.errorMessage.text = errorMessage
        viewBinding.retryButton.apply {
            retry?.let {
                isVisible = true
                setOnClickListener { retry.invoke() }
            }
        }
    }
}