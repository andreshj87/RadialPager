package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView

class RadialPager : ConstraintLayout {

  constructor(context: Context?) : super(context)

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

  fun setText(text: String) {
    val textView = TextView(context)
    textView.text = text
    addView(textView)
  }
}