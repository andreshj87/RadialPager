package com.andreshj87.radialpager

import android.util.Log
import android.view.MotionEvent
import android.view.View

class RadialPagerScrollManager(private val movementListener: RadialPagerMovementListener) : View.OnTouchListener {

  private val movementMultiplier = 4

  private var currentVerticalCoordinate = 0
  private var previousVerticalCoordinate: Float? = null
  private var blockMovement = false
  private var canGoBackwards = false
  private var canGoFoward = false

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    Log.i(javaClass.simpleName, "MotionEvent.y = " + event?.y)

    when {
      event?.action == MotionEvent.ACTION_DOWN -> onTouchDown(event)
      event?.action == MotionEvent.ACTION_MOVE -> onTouchMove(event)
      event?.action == MotionEvent.ACTION_UP -> onTouchUp()
    }

    return true
  }

  private fun onTouchDown(event: MotionEvent?) {
    Log.i(javaClass.simpleName, "[ACTION_DOWN]")

    currentVerticalCoordinate = 0
    previousVerticalCoordinate = event!!.y
    blockMovement = false
  }

  private fun onTouchMove(event: MotionEvent?) {
    Log.i(javaClass.simpleName, "[ACTION_MOVE]")

    if (previousVerticalCoordinate == null) {
      Log.i(javaClass.simpleName, "[ACTION_MOVE] Initializing previousVerticalCoordinate")
      previousVerticalCoordinate = event!!.y
    }

    if (event!!.y > previousVerticalCoordinate!!) {
      currentVerticalCoordinate++
    } else {
      currentVerticalCoordinate--
    }

    var movementPercentage = currentVerticalCoordinate.toMovementPercentage()
    if (movementPercentage < -100) movementPercentage = -100
    if (movementPercentage > 100) movementPercentage = 100
    if (movementPercentage < 0) movementListener.moveBackwards(movementPercentage * -1)
    if (movementPercentage > 0) movementListener.moveForward(movementPercentage)

    previousVerticalCoordinate = event!!.y
  }

  private fun onTouchUp() {
    Log.i(javaClass.simpleName, "[ACTION_UP]")

    val movementPercentage = currentVerticalCoordinate.toMovementPercentage()
    if (movementPercentage < 0) movementListener.snapBackwards(movementPercentage * -1)
    if (movementPercentage > 0) movementListener.snapFoward(movementPercentage)
    currentVerticalCoordinate = 0
    previousVerticalCoordinate = null
    blockMovement = false
  }

  fun Int.toMovementPercentage(): Int {
    return this * movementMultiplier
  }
}