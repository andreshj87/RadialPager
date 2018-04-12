package com.andreshj87.radialpager

class RadialPagerItem<T> private constructor(builder: Builder<*>) {

  val data: T
  var imageResource: Int? = null
  var imageUrl: String? = null

  init {
    this.data = builder.data as T
    if (builder.imageResource != null) this.imageResource = builder.imageResource
    if (builder.imageUrl != null) this.imageUrl = builder.imageUrl
  }

  class Builder<T> {
    internal var data: T? = null
    internal var imageResource: Int? = null
    internal var imageUrl: String? = null

    fun data(data: T): Builder<T> {
      this.data = data
      return this
    }

    fun imageResource(imageResource: Int?): Builder<T> {
      this.imageResource = imageResource
      return this
    }

    fun imageUrl(imageUrl: String): Builder<T> {
      this.imageUrl = imageUrl
      return this
    }

    fun build(): RadialPagerItem<T> {
      if (imageResource == null && imageUrl == null) {
        // TODO remove this eventually
        //throw IllegalArgumentException("You have to provide an image")
      }

      return RadialPagerItem(this)
    }
  }

}
