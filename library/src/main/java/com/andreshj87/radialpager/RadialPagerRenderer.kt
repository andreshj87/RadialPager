package com.andreshj87.radialpager

interface RadialPagerRenderer {
  fun snap()
  fun moveForward(movementPercentage: Int)
  fun moveBackwards(movementPercentage: Int)
}