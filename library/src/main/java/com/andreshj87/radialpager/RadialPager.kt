package com.andreshj87.radialpager

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class RadialPager<T> : ConstraintLayout, View.OnTouchListener {

  private val MAX_LAYERS = 3
  private val MAX_ITEMS_PER_LAYER = 6
  private val MAX_ITEMS = MAX_LAYERS * MAX_ITEMS_PER_LAYER

  private val itemSizes: ArrayList<Float> = ArrayList(MAX_LAYERS)
  private val itemRadius: ArrayList<Float> = ArrayList(MAX_LAYERS)
  private val itemOddAngles: ArrayList<Int> = ArrayList(MAX_ITEMS_PER_LAYER)
  private val itemEvenAngles: ArrayList<Int> = ArrayList(MAX_ITEMS_PER_LAYER)

  var baseView: View? = null
  var constraintLayout: ConstraintLayout? = null
  var centerView: ImageView? = null
  var itemViews = ArrayList<View>()

  var middleItem: RadialPagerItem<T>? = null
  val items: ArrayList<RadialPagerItem<T>> = ArrayList()

  var verticalCoordinate = 0
  var previousVerticalCoordinate: Float = -1f
  var blockMovement = false

  constructor(context: Context?) : super(context) {
    initView()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    initView()
  }

  fun initView() {
    setOnTouchListener(this)
    baseView = LayoutInflater.from(context).inflate(R.layout.radial_pager, this, true)
    constraintLayout = baseView!!.findViewById(R.id.constraint_layout)
    centerView = baseView?.findViewById(R.id.center_item)

    itemSizes.add(resources.getDimension(R.dimen.radialpager_center_item_size))
    itemSizes.add(resources.getDimension(R.dimen.radialpager_first_level_item_size))
    itemSizes.add(resources.getDimension(R.dimen.radialpager_second_level_item_size))
    itemSizes.add(resources.getDimension(R.dimen.radialpager_third_level_item_size))
    itemSizes.add(resources.getDimension(R.dimen.radialpager_fourth_level_item_size))

    itemRadius.add(resources.getDimension(R.dimen.radialpager_center_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_first_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_second_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_third_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_fourth_level_radius))

    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_first_item_angle))
    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_second_item_angle))
    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_third_item_angle))
    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_fourth_item_angle))
    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_fifth_item_angle))
    itemOddAngles.add(resources.getInteger(R.integer.radialpager_odd_level_sixth_item_angle))

    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_first_item_angle))
    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_second_item_angle))
    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_third_item_angle))
    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_fourth_item_angle))
    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_fifth_item_angle))
    itemEvenAngles.add(resources.getInteger(R.integer.radialpager_even_level_sixth_item_angle))
  }

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    Log.i(javaClass.simpleName, "MotionEvent.y = " + event?.y)

    if (event?.action == MotionEvent.ACTION_UP) {
      Log.i(javaClass.simpleName, "[ACTION_UP]")
      snapViews()
      verticalCoordinate = 0
      previousVerticalCoordinate = -1f
      blockMovement = false
    } else if (event?.action == MotionEvent.ACTION_DOWN) {
      Log.i(javaClass.simpleName, "[ACTION_DOWN]")
      verticalCoordinate = 0
      previousVerticalCoordinate = event.y
      blockMovement = false
    } else if (event?.action == MotionEvent.ACTION_MOVE) {
      Log.i(javaClass.simpleName, "[ACTION_MOVE]")
      if (previousVerticalCoordinate < 0) {
        Log.i(javaClass.simpleName, "[ACTION_MOVE] Initializing previousVerticalCoordinate")
        previousVerticalCoordinate = event.y
      } else {
        Log.i(javaClass.simpleName, "[ACTION_MOVE] Not initializing")
        if (event.y >= previousVerticalCoordinate) {
          Log.i(javaClass.simpleName, "[ACTION_MOVE] event.y > previousVerticalCoordinate")
          verticalCoordinate++
          if (verticalCoordinate*6 in 0..100 && !blockMovement) {
            moveDownItems(verticalCoordinate*6)
          }
        } else {
          Log.i(javaClass.simpleName, "[ACTION_MOVE] event.y IS NOT > previousVerticalCoordinate")
          //verticalCoordinate--
          //moveUpItems(verticalCoordinate / 8)
        }
        previousVerticalCoordinate = event.y
      }
      Log.i(javaClass.simpleName, "[ACTION_MOVE] Finishing... verticalCoordinate = " + verticalCoordinate)
    }

    return true
  }

  private fun moveDownItems(percentage: Int) {
    if (percentage > 100) {
      blockMovement = true
      return
    }

    var layer: Int = 1
    var itemPerLayer: Int = 0
    itemViews.forEach {
      val layoutParams: ConstraintLayout.LayoutParams = it.layoutParams as LayoutParams

      if (itemPerLayer >= MAX_ITEMS_PER_LAYER) {
        layer++
        itemPerLayer = 0
      }

      val totalSize = itemSizes.get(layer - 1) - itemSizes.get(layer)
      if (layer == 1 && itemPerLayer == 1) {
        Log.i(javaClass.simpleName, "totalSize between layer 1 and 0 = " + totalSize)
      }
      val totalRadius = itemRadius.get(layer) - itemRadius.get(layer - 1)
      val totalEvenAngle = (itemEvenAngles.get(itemPerLayer) - itemOddAngles.get( (itemPerLayer-1).rightMod(6) )).rightMod(360)
      val totalOddAngle = (itemOddAngles.get(itemPerLayer) - itemEvenAngles.get( (itemPerLayer-1).rightMod(6) )).rightMod(360)
      val totalAngle = if (layer.isEven()) totalEvenAngle else totalOddAngle

      val dSize = (percentage * totalSize) / 100
      val dRadius = (percentage * totalRadius) / 100
      val dAngle = (percentage * totalAngle) / 100

      val currentSize = itemSizes.get(layer) + dSize
      if (layer == 1 && itemPerLayer == 1) {
        Log.i(javaClass.simpleName, "Layer[0].size = " + itemSizes.get(layer-1))
        Log.i(javaClass.simpleName, "Actual CurrentLayerSize = " + layoutParams.width)

        Log.i(javaClass.simpleName, "Percentage " + percentage)
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
        it.alpha = ((100f - percentage.toFloat()) / 100f)
      }

      if (layer > MAX_LAYERS) {
        it.alpha = (percentage.toFloat()) / 100f
      }

      if (layoutParams.width > itemSizes.get(layer-1)) {
        Log.i(javaClass.simpleName, "[BLOCKING THE FUCK OUT OF IT] layoutParams.width " + layoutParams.width + " itemSizes.get(layer-1) " + itemSizes.get(layer-1) )
        blockMovement = true
      }

      itemPerLayer++
    }
  }

  fun moveUpItems(percentage: Int) {
    var layer: Int = 1
    var itemPerLayer: Int = 0
    itemViews.forEach {
      val layoutParams: ConstraintLayout.LayoutParams = it.layoutParams as LayoutParams

      if (itemPerLayer >= MAX_ITEMS_PER_LAYER) {
        layer++
        itemPerLayer = 0
      }

      val dSize = itemSizes.get(layer) - itemSizes.get(layer + 1)
      val dRadius = itemRadius.get(layer) - itemRadius.get(layer + 1)
      val dAngle = if (layer.isEven()) itemEvenAngles.get(itemPerLayer) - itemEvenAngles.get(  (itemPerLayer + 1).rightMod(6) )
      else itemOddAngles.get(itemPerLayer) - itemOddAngles.get( (itemPerLayer + 1).rightMod(6) )

      val currentSize = layoutParams.width - ((percentage * dSize) / 100)
      val currentRadius = layoutParams.circleRadius + ((percentage * dRadius) / 100)
      val currentAngle = layoutParams.circleAngle + ((percentage * dAngle) / 100)

      if (currentSize < itemSizes.get(layer + 1)) {
        return
      } else {
        layoutParams.width = currentSize.toInt()
        layoutParams.height = currentSize.toInt()
        layoutParams.circleRadius = currentRadius.toInt()
        layoutParams.circleAngle = currentAngle
        it.layoutParams = layoutParams

        itemPerLayer++
      }
    }
  }

  fun setText(text: String) {
    val textView = TextView(context)
    textView.text = text
    addView(textView)
  }

  fun setCenterItem(centerItem: RadialPagerItem<T>) {
    centerView?.setImageResource(centerItem.imageResource!!)
  }

  fun setItems(items: ArrayList<RadialPagerItem<T>>) {
    this.items.addAll(items)
    renderInitialItems(this.items)
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
    item.setTextColor(resources.getColor(android.R.color.white))
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

  fun snapViews() {
    Log.i(javaClass.simpleName, "Snap items!")
  }

  fun Int.isEven(): Boolean {
    return this % 2 == 0
  }

  fun Int.rightMod(mod: Int): Int {
    var r = this.rem(mod)
    if (r < 0) r += mod

    return r
  }
}