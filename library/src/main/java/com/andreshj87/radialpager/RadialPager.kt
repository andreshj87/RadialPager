package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater

class RadialPager<T> : ConstraintLayout, RadialPagerMovementListener {

  private val itemManager = RadialPagerItemManager<T>()
  private val viewRenderer = RadialPagerViewRenderer<T>()
  private val scrollManager = RadialPagerScrollManager(this)

  constructor(context: Context?) : super(context) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  fun init() {
    val baseView = LayoutInflater.from(context).inflate(R.layout.radial_pager, this, true)
    viewRenderer.init(context, baseView)
    setOnTouchListener(scrollManager)
  }

  override fun snapFoward(movementPercentage: Int) {
    viewRenderer.snapFoward(movementPercentage)
    /*if (movementPercentage > 50) {
      viewRenderer.clearInnerLayer()
      val nextLayer = itemManager.getNextLayer()
      viewRenderer.appendLayer(nextLayer)
    }*/
  }

  override fun snapBackwards(movementPercentage: Int) {
    viewRenderer.snapBackwards(movementPercentage)
    /*if (movementPercentage > 50) {
      viewRenderer.clearOuterLayer()
      val prevousLayer = itemManager.getPreviousLayer()
      viewRenderer.prependLayer(prevousLayer)
    }*/
  }

  override fun moveForward(movementPercentage: Int) {
    if (itemManager.canMoveForward()) {
      viewRenderer.moveForward(movementPercentage)
    }
  }

  override fun moveBackwards(movementPercentage: Int) {
    if (itemManager.canMoveBackwards()) {
      viewRenderer.moveBackwards(movementPercentage)
    }
  }

  fun setCenterItem(centerItem: RadialPagerItem<T>) {
    viewRenderer.renderCenterView(centerItem)
  }

  fun setItems(items: ArrayList<RadialPagerItem<T>>) {
    itemManager.items = items
    val initialItems = itemManager.getInitialItems()
    viewRenderer.renderInitialItems(initialItems)
  }
}