package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class RadialPager<T> : ConstraintLayout {

  var baseView: View? = null
  var centerView: ImageView? = null

  constructor(context: Context?) : super(context) {
    initView()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    initView()
  }

  fun initView() {
    baseView = LayoutInflater.from(context).inflate(R.layout.radial_pager, this, true)
    centerView = baseView?.findViewById(R.id.center_item)
  }

  fun setText(text: String) {
    val textView = TextView(context)
    textView.text = text
    addView(textView)
  }

  fun setCenterItem(centerItem: RadialPagerItem<T>) {
    centerView?.setImageResource(centerItem.imageResource!!)
  }
}