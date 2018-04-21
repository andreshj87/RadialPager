package com.andreshj87.radialpager

import android.util.Log

class RadialPagerItemManager<T> {

  private val LAYER_HIDDEN_INNER = 0
  private val LAYER_HIDDEN_OUTER = 4

  private val LAYERS_VISIBLE = 3
  private val LAYERS_TOTAL = 5

  private val ITEMS_PER_LAYER = 6
  private val ITEMS_VISIBLE = LAYERS_VISIBLE * ITEMS_PER_LAYER

  /*
  private val MAX_VISIBLE_LAYERS = 3
  private val MAX_INITIAL_LAYERS = 4
  private val MAX_TOTAL_LAYERS = 5

  private val MAX_ITEMS_PER_LAYER = 6
  private val MAX_INITIAL_ITEMS = MAX_INITIAL_LAYERS * MAX_ITEMS_PER_LAYER*/

  var items: ArrayList<RadialPagerItem<T>>? = null
  set(value) {
    field = value
    maxLayers = kotlin.math.ceil((value!!.size / ITEMS_PER_LAYER).toDouble()).toInt()
  }
  var maxLayers: Int = 0
  var lowerBound = 0
  var upperBound = 0

  fun getInitialItems(): ArrayList<RadialPagerItem<T>> {
    lowerBound = 0
    upperBound = 0

    val initialItems = ArrayList<RadialPagerItem<T>>()
    for (i in 0 until (LAYERS_VISIBLE+1)) {
      val layerItems = getLayer(i)
      if (layerItems.isNotEmpty()) {
        upperBound++
        initialItems.addAll(layerItems)
      }
    }

    return initialItems
  }

  fun getNextLayer(): ArrayList<RadialPagerItem<T>> {
    val layerItems = getLayer(upperBound + 1)
    if (layerItems.isNotEmpty()) {
      lowerBound++
      upperBound++
    }

    return layerItems
  }

  fun getPreviousLayer(): ArrayList<RadialPagerItem<T>> {
    val layerItems = getLayer(lowerBound - 1)
    if (layerItems.isNotEmpty()) {
      lowerBound--
      upperBound--
    }

    return layerItems
  }

  fun canMoveForward(): Boolean {
    return upperBound <= maxLayers
  }

  fun canMoveBackwards(): Boolean {
    return lowerBound > 0
  }

  private fun getLayer(layer: Int): ArrayList<RadialPagerItem<T>> {
    val layerItems = ArrayList<RadialPagerItem<T>>()
    for (i in layer*ITEMS_PER_LAYER until layer*ITEMS_PER_LAYER+ITEMS_PER_LAYER) {
      if (i>= 0 && i < items!!.size) {
        layerItems.add(items!!.get(i))
      }
    }

    return layerItems
  }
}