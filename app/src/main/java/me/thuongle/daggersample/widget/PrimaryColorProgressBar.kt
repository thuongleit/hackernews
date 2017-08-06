package me.thuongle.daggersample.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar
import me.thuongle.daggersample.R

class PrimaryColorProgressBar : ProgressBar {

    constructor(context: Context) : super(context) {
        setupTintMode()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupTintMode()
    }

    private fun setupTintMode() {
        val color = context.resources.getColor(R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            indeterminateTintList = ColorStateList.valueOf(color)
        } else {
            indeterminateDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}