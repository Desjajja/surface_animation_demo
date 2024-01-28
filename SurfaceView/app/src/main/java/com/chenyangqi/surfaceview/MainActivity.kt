package com.chenyangqi.surfaceview

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val animWrapper = findViewById<LinearLayout>(R.id.anim_wrapper)
        val surfaceWrapper = LayoutInflater.from(this).inflate(R.layout.surface_wrapper, animWrapper, false)


        findViewById<Button>(R.id.btnStart).setOnClickListener {
//            frameImgView.visibility = View.VISIBLE
            animWrapper.addView(surfaceWrapper)
        }
        findViewById<Button>(R.id.btnEnd).setOnClickListener {
//            frameImgView.visibility = View.INVISIBLE
            animWrapper.removeView(surfaceWrapper)
        }
//            val duration = 60
//            val animationDrawable = AnimationDrawable()
//            val intArray = Util.getSourceId()
//            for (sourceId in intArray) {
//                ContextCompat.getDrawable(this, sourceId)?.let {
//                    animationDrawable.addFrame(it, duration)
//                }
//            }
//
//            frameImgView.setImageDrawable(animationDrawable)
//            animationDrawable.start()
//        }
    }
}