package com.andreshj87.radialpager.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.andreshj87.radialpager.RadialPager

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val radialPager = findViewById<RadialPager>(R.id.radialpager)
    radialPager.setText("Awesome!")
  }
}
