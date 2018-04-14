package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater

class RadialPager<T> : ConstraintLayout, RadialPagerScrollManager.ScrollListener {

  private val radialPagerItemManager = RadialPagerItemManager<T>()
  private val radialPagerRenderer = RadialPagerViewRenderer<T>()
  private val scrollManager = RadialPagerScrollManager(this)

  constructor(context: Context?) : super(context) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  fun init() {
    val baseView = LayoutInflater.from(context).inflate(R.layout.radial_pager, this, true)
    radialPagerRenderer.init(context, baseView)
    setOnTouchListener(scrollManager)
  }

  override fun snap() {
    radialPagerRenderer.snap()
  }

  override fun moveForward(movementPercentage: Int) {
    radialPagerRenderer.moveForward(movementPercentage)
  }

  override fun moveBackwards(movementPercentage: Int) {
    radialPagerRenderer.moveBackwards(movementPercentage)
  }

  fun setCenterItem(centerItem: RadialPagerItem<T>) {
    radialPagerRenderer.renderCenterView(centerItem)
  }

  fun setItems(items: ArrayList<RadialPagerItem<T>>) {
    radialPagerItemManager.items = items
    val initialItems = radialPagerItemManager.getInitialItems()
    radialPagerRenderer.renderInitialItems(initialItems)
  }
}