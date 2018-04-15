package com.andreshj87.radialpager

interface RadialPagerMovementListener {
  fun snapFoward(movementPercentage: Int)
  fun snapBackwards(movementPercentage: Int)
  fun moveForward(movementPercentage: Int)
  fun moveBackwards(movementPercentage: Int)
}