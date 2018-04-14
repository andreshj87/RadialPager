package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class RadialPagerViewRenderer<T>: RadialPagerRenderer {

  private val MAX_LAYERS = 3
  private val MAX_ITEMS_PER_LAYER = 6
  private val MAX_ITEMS = MAX_LAYERS * MAX_ITEMS_PER_LAYER

  private val itemSizes: ArrayList<Float> = ArrayList(MAX_LAYERS)
  private val itemRadius: ArrayList<Float> = ArrayList(MAX_LAYERS)
  private val itemOddAngles: ArrayList<Int> = ArrayList(MAX_ITEMS_PER_LAYER)
  private val itemEvenAngles: ArrayList<Int> = ArrayList(MAX_ITEMS_PER_LAYER)

  private var context: Context? = null
  private var baseView: View? = null
  private var constraintLayout: ConstraintLayout? = null
  private var centerView: ImageView? = null
  private var itemViews = ArrayList<View>()

  fun init(context: Context, baseView: View) {
    this.context = context
    this.baseView = baseView
    constraintLayout = baseView!!.findViewById(R.id.constraint_layout)
    centerView = baseView?.findViewById(R.id.center_item)

    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_center_item_size))
    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_first_level_item_size))
    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_second_level_item_size))
    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_third_level_item_size))
    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_fourth_level_item_size))
    itemSizes.add(context.resources.getDimension(R.dimen.radialpager_fifth_level_item_size))

    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_center_level_radius))
    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_first_level_radius))
    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_second_level_radius))
    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_third_level_radius))
    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_fourth_level_radius))
    itemRadius.add(context.resources.getDimension(R.dimen.radialpager_fifth_level_radius))

    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_first_item_angle))
    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_second_item_angle))
    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_third_item_angle))
    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_fourth_item_angle))
    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_fifth_item_angle))
    itemOddAngles.add(context.resources.getInteger(R.integer.radialpager_odd_level_sixth_item_angle))

    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_first_item_angle))
    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_second_item_angle))
    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_third_item_angle))
    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_fourth_item_angle))
    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_fifth_item_angle))
    itemEvenAngles.add(context.resources.getInteger(R.integer.radialpager_even_level_sixth_item_angle))
  }

  override fun snap() {
    Log.i(javaClass.simpleName, "Snap items!")
  }

  override fun moveForward(movementPercentage: Int) {
    Log.d(javaClass.simpleName, "[Moving forward] $movementPercentage")
    if (movementPercentage > 100) return

    var layer: Int = 1
    var itemPerLayer: Int = 0
    itemViews.forEach {
      val layoutParams: ConstraintLayout.LayoutParams = it.layoutParams as ConstraintLayout.LayoutParams

      if (itemPerLayer >= MAX_ITEMS_PER_LAYER) {
        layer++
        itemPerLayer = 0
      }

      val totalSize = itemSizes.get(layer - 1) - itemSizes.get(layer)
      if (layer == 1 && itemPerLayer == 1) {
        Log.i(javaClass.simpleName, "totalSize between layer 1 and 0 = " + totalSize)
      }
      val totalRadius = itemRadius.get(layer) - itemRadius.get(layer - 1)
      val totalEvenAngle = (itemEvenAngles.get(itemPerLayer) - itemOddAngles.get( (itemPerLayer-1).module(6) )).module(360)
      val totalOddAngle = (itemOddAngles.get(itemPerLayer) - itemEvenAngles.get( (itemPerLayer-1).module(6) )).module(360)
      val totalAngle = if (layer.isEven()) totalEvenAngle else totalOddAngle

      val dSize = (movementPercentage * totalSize) / 100
      val dRadius = (movementPercentage * totalRadius) / 100
      val dAngle = (movementPercentage * totalAngle) / 100

      val currentSize = itemSizes.get(layer) + dSize
      if (layer == 1 && itemPerLayer == 1) {
        Log.i(javaClass.simpleName, "Layer[0].size = " + itemSizes.get(layer-1))
        Log.i(javaClass.simpleName, "Actual CurrentLayerSize = " + layoutParams.width)

        Log.i(javaClass.simpleName, "Percentage " + movementPercentage)
        Log.i(javaClass.simpleName, "dSize = " + dSize)
        Log.i(javaClass.simpleName, "Calculated currentSize = " + currentSize)
      }
      val currentRadius = itemRadius.get(layer) - dRadius
      val currentAngle = if (layer.isEven()) itemEvenAngles.get(itemPerLayer) - dAngle else itemOddAngles.get(itemPerLayer) - dAngle

      layoutParams.width = currentSize.toInt()
      layoutParams.height = currentSize.toInt()
      layoutParams.circleRadius = currentRadius.toInt()
      layoutParams.circleAngle = currentAngle.toFloat()
      it.layoutParams = layoutParams

      if (layer == 1) {
        it.alpha = ((100f - movementPercentage.toFloat()) / 100f)
      }

      if (layer > MAX_LAYERS) {
        it.alpha = movementPercentage.toFloat() / 100f
      }

      if (layoutParams.width > itemSizes.get(layer-1)) {
        Log.i(javaClass.simpleName, "[BLOCKING THE FUCK OUT OF IT] layoutParams.width " + layoutParams.width + " itemSizes.get(layer-1) " + itemSizes.get(layer-1) )
      }

      itemPerLayer++
    }
  }

  override fun moveBackwards(movementPercentage: Int) {
    Log.d(javaClass.simpleName, "[Moving backwards] $movementPercentage")
    if (movementPercentage > 100) return

    var layer: Int = 1
    var itemPerLayer: Int = 0
    itemViews.forEach {
      val layoutParams: ConstraintLayout.LayoutParams = it.layoutParams as ConstraintLayout.LayoutParams

      if (itemPerLayer >= MAX_ITEMS_PER_LAYER) {
        layer++
        itemPerLayer = 0
      }

      val totalSize = itemSizes.get(layer) - itemSizes.get(layer + 1)
      val totalRadius = itemRadius.get(layer + 1) - itemRadius.get(layer)
      val totalEvenAngle = (itemEvenAngles.get( (itemPerLayer+1).module(6) ) - itemOddAngles.get(itemPerLayer)).module(360)
      val totalOddAngle = (itemOddAngles.get( (itemPerLayer+1).module(6) ) - itemEvenAngles.get(itemPerLayer)).module(360)
      val totalAngle = if (layer.isEven()) totalEvenAngle else totalOddAngle

      val dSize = (movementPercentage * totalSize) / 100
      val dRadius = (movementPercentage * totalRadius) / 100
      val dAngle = (movementPercentage * totalAngle) / 100

      val currentSize = itemSizes.get(layer) - dSize
      val currentRadius = itemRadius.get(layer) + dRadius
      val currentAngle = if (layer.isEven()) itemEvenAngles.get(itemPerLayer) + dAngle else itemOddAngles.get(itemPerLayer) + dAngle

      layoutParams.width = currentSize.toInt()
      layoutParams.height = currentSize.toInt()
      layoutParams.circleRadius = currentRadius.toInt()
      layoutParams.circleAngle = currentAngle.toFloat()
      it.layoutParams = layoutParams

      if (layer == MAX_LAYERS) {
        it.alpha = ((100f - movementPercentage.toFloat()) / 100f)
      }

      if (layoutParams.width < itemSizes.get(layer + 1)) {
        return
      }

      itemPerLayer++
    }
  }

  fun renderCenterView(centerItem: RadialPagerItem<T>) {
    centerView?.setImageResource(centerItem.imageResource!!)
  }

  fun renderInitialItems(item: ArrayList<RadialPagerItem<T>>) {
    var currentLayer: Int = 1
    var currentItemPerLayer: Int = 0
    item.forEachIndexed { index, radialPagerItem ->
      if (index >= MAX_ITEMS + MAX_ITEMS_PER_LAYER) {
        return
      }

      if (radialPagerItem.imageResource == null && radialPagerItem.imageUrl == null) {
        renderImagelessItem(radialPagerItem, index.toString(), currentLayer, currentItemPerLayer)
      } else {
        renderItem(radialPagerItem, currentLayer, currentItemPerLayer)
      }

      currentItemPerLayer++
      if (currentItemPerLayer >= MAX_ITEMS_PER_LAYER) {
        currentLayer++
        currentItemPerLayer = 0
      }
    }
  }

  fun renderItem(radialPagerItem: RadialPagerItem<T>, layer: Int, itemPerLayer: Int) {
    val item = CircleImageView(context)
    item.setImageResource(radialPagerItem.imageResource!!)
    val itemSize: Float = itemSizes.get(layer)
    val layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(itemSize.toInt(), itemSize.toInt())
    layoutParams.circleConstraint = centerView!!.id
    layoutParams.circleRadius = itemRadius.get(layer).toInt()
    layoutParams.circleAngle = if (layer.isEven()) itemEvenAngles.get(itemPerLayer).toFloat() else itemOddAngles.get(itemPerLayer).toFloat()
    item.layoutParams = layoutParams
    if (layer > MAX_LAYERS) {
      item.alpha = 0f
    }
    itemViews.add(item)
    constraintLayout!!.addView(item)
  }

  @Deprecated("Remove this eventually")
  fun renderImagelessItem(radialPagerItem: RadialPagerItem<T>, text: String, layer: Int, itemPerLayer: Int) {
    val item = TextView(context)
    item.text = text
    item.setTextColor(context?.resources!!.getColor(android.R.color.white))
    item.setBackgroundResource(R.drawable.radial_pager_item_text_background)
    item.gravity = Gravity.CENTER
    val itemSize: Float = itemSizes.get(layer)
    val layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(itemSize.toInt(), itemSize.toInt())
    layoutParams.circleConstraint = centerView!!.id
    layoutParams.circleRadius = itemRadius.get(layer).toInt()
    layoutParams.circleAngle = if (layer.isEven()) itemEvenAngles.get(itemPerLayer).toFloat() else itemOddAngles.get(itemPerLayer).toFloat()
    item.layoutParams = layoutParams
    if (layer > MAX_LAYERS) {
      item.alpha = 0f
    }
    itemViews.add(item)
    constraintLayout!!.addView(item)
  }

  fun Int.isEven(): Boolean {
    return this % 2 == 0
  }

  fun Int.module(mod: Int): Int {
    var r = this.rem(mod)
    if (r < 0) r += mod

    return r
  }

  fun Int.isFirstLayer(): Boolean {
    return this == 1
  }
}