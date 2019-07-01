package com.example.photovideoclockframe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        analogClock.setCurrentTime()
        compositeDisposable.add(
        Observable.interval(1, TimeUnit.SECONDS)
            .subscribe({ analogClock.setCurrentTime() }, {}))
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
