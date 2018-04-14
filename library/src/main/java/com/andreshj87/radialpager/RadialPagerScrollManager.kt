package com.andreshj87.radialpager

import android.util.Log
import android.view.MotionEvent
import android.view.View

class RadialPagerScrollManager(private val scrollListener: ScrollListener) : View.OnTouchListener {

  private val movementMultiplier = 6

  private var verticalCoordinate = 0
  private var previousVerticalCoordinate: Float = -1f
  private var blockMovement = false

  interface ScrollListener {
    fun snap()
    fun moveForward(movementPercentage: Int)
    fun moveBackwards(movementPercentage: Int)
  }

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    Log.i(javaClass.simpleName, "MotionEvent.y = " + event?.y)

    if (event?.action == MotionEvent.ACTION_UP) {
      Log.i(javaClass.simpleName, "[ACTION_UP]")
      scrollListener.snap()
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
//          if (verticalCoordinate*movementMultiplier in 0..100 && !blockMovement) {
//            scrollListener.moveForward(verticalCoordinate*movementMultiplier)
//          }
        } else {
          Log.i(javaClass.simpleName, "[ACTION_MOVE] event.y IS NOT > previousVerticalCoordinate")
          verticalCoordinate--
//          if (verticalCoordinate*movementMultiplier in 0..100 && !blockMovement) {
//            scrollListener.moveBackwards(verticalCoordinate * movementMultiplier)
//          }
        }

        var temp = verticalCoordinate * movementMultiplier
        if (temp < 0) {
          scrollListener.moveBackwards(temp * -1)
        } else {
          scrollListener.moveForward(temp)
        }

        previousVerticalCoordinate = event.y
      }
      Log.i(javaClass.simpleName, "[ACTION_MOVE] Finishing... verticalCoordinate = " + verticalCoordinate)
    }

    return true
  }
}