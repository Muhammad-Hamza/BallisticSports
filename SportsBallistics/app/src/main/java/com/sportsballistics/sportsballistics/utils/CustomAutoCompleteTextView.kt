package com.sportsballistics.sportsballistics.utils

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AutoCompleteTextView


class CustomAutoCompleteTextView(context: Context?, attrs: AttributeSet?) :
    AutoCompleteTextView(context, attrs) {
    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(
        focused: Boolean, direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) {
            performFiltering(text, 0)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        showDropDown()
        return super.onTouchEvent(event)
    }
}