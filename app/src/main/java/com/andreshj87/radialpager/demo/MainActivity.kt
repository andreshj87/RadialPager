package com.andreshj87.radialpager.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.andreshj87.radialpager.RadialPager
import com.andreshj87.radialpager.RadialPagerItem

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    drawItems()
  }

  fun drawItems() {
    val radialPager = findViewById<RadialPager<Person>>(R.id.radialpager)

    val person = Person("Putin",
        "https://static.timesofisrael.com/www/uploads/2018/03/000_12537P-e1520673369804-640x400.jpg")

    val radialPagerItemCenter = RadialPagerItem.Builder<Person>()
        .data(person)
        .imageResource(R.drawable.putin)
        .build()

    radialPager.setCenterItem(radialPagerItemCenter)

    val radialPagerItem = RadialPagerItem.Builder<Person>()
        .data(person)
        .build()

    val items: ArrayList<RadialPagerItem<Person>> = ArrayList()
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)

    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)

    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)

    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)
    items.add(radialPagerItem)

    radialPager.setItems(items)
  }
}
