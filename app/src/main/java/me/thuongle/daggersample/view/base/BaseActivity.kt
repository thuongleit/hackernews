package me.thuongle.daggersample.view.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import me.thuongle.daggersample.App
import me.thuongle.daggersample.R
import me.thuongle.daggersample.di.ApplicationComponent
import me.thuongle.daggersample.util.DialogFactory

abstract class BaseActivity : AppCompatActivity() {

    val app: App
        get() = application as App

    val component: ApplicationComponent
        get() = app.applicationComponent

    protected val presenter: BasePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter?.subscribe()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unsubscribe()
    }

    fun reload() {
        overridePendingTransition(0, 0)
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }

    protected fun showNetworkErrorDialog() {
        DialogFactory
                .createSimpleOkErrorDialog(this, R.string.error_connection_lost_title, R.string.error_connection_lost_message)
                .show()
    }

    protected fun showInAppErrorDialog() {
        val alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.error_generic_error_title)
                .setMessage(R.string.error_generic_error_message)
                .setNeutralButton(getString(R.string.dialog_action_send_feedback), { dialog, _ -> dialog.dismiss() })
                .setPositiveButton(R.string.dialog_action_ok, null)

        alertDialog.create().show()
    }
}
