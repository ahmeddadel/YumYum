package com.dolla.yumyum.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dolla.yumyum.R
import com.dolla.yumyum.databinding.ActivitySplashScreenBinding
import com.dolla.yumyum.ui.activites.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object instance using the layout file name
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        // Set the content view to the binding object's root
        setContentView(binding.root)

        // hide status bar and action bar and make the activity full screen
        window.decorView.systemUiVisibility = (
                // Hide the status bar.
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Hide the action bar
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )

        val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake)
        binding.splashScreenText.startAnimation(shakeAnimation)

        // delay the transition to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            // start the next activity
            startActivity(Intent(this, MainActivity::class.java))
            // transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            // finish the current activity
            finish()
        }, 2000)
    }
}