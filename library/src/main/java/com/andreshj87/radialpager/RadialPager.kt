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

  var middleItem: RadialPagerItem<T>? = null
  val items: ArrayList<RadialPagerItem<T>> = ArrayList()

  var vertialCoordinate = 0
  var previousVerticalCoordinate: Float = -1f

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

    itemRadius.add(resources.getDimension(R.dimen.radialpager_center_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_first_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_second_level_radius))
    itemRadius.add(resources.getDimension(R.dimen.radialpager_third_level_radius))

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
    Log.i(javaClass.simpleName, event?.toString())

    if (event?.action == MotionEvent.ACTION_UP) {
      snapViews()
      vertialCoordinate = 0
      previousVerticalCoordinate = -1f
    } else if (event?.action == MotionEvent.ACTION_DOWN) {
      vertialCoordinate = 0
      previousVerticalCoordinate = event.y
    } else if (event?.action == MotionEvent.ACTION_MOVE) {
      if (previousVerticalCoordinate < 0) {
        previousVerticalCoordinate = event.y
      } else {
        if (event.y > previousVerticalCoordinate) {
          vertialCoordinate++
        } else {
          vertialCoordinate--
        }
      }
      Log.i(javaClass.simpleName, "Moving! " + vertialCoordinate)
    }

    return true
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
      if (index >= MAX_ITEMS) {
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
    constraintLayout!!.addView(item)
  }

  fun snapViews() {
    Log.i(javaClass.simpleName, "Snap items!")
  }

  fun Int.isEven(): Boolean {
    return this % 2 == 0
  }
}