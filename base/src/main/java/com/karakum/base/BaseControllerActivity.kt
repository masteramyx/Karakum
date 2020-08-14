package com.karakum.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_controller.*


/**
 * Base Activity class designed to be used with [com.bluelinelabs.conductor.Controller],
 * quick setup for base controller via [getRootController] implementation
 *
 * Example:
 *
 * override fun getRootController() = TestController.newInstance()
 */
abstract class BaseControllerActivity : AppCompatActivity() {

    protected lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        router = Conductor.attachRouter(this, container, savedInstanceState)

        router.setRoot(RouterTransaction.with(getRootController()))

    }

    abstract fun getRootController(): Controller
}