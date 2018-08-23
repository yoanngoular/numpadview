package com.ygoular.numpadviewsample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_numpad.*

/**
 * Project : NumPadActivity
 * @file com.ygoular.numpadviewsample.NumPadActivity
 * @brief A really basic sample of the custom view's usage.
 * Remember that any programmatic changes to the attributes of the view imply
 * a call of the apply() method. On the other hand, the method restoreDefaults() already
 * implicitly call it.
 * @date 2018-08-16
 * @author yoann
 */
class NumPadActivity : AppCompatActivity(){

    private var currentValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_numpad)

        num_pad_view.setOnInteractionListener(

                onLeftIconClick = { text_value_number.text = handleLeftValue() },
                onRightIconClick = { handleRightValue() },
                onNewValue = { text_value_number.text = handleNewValue(it) }
        )
    }

    /**
     * Display a SnackBar
     */
    private fun handleRightValue() { layout_numpad.snack(getString(R.string.thanks)) }

    /**
     * Trunk last char of the String if possible, else, return 0
     * @return the value after a back input
     */
    private fun handleLeftValue(): String {
        currentValue = if (currentValue.length > 1) { currentValue.shorten() } else { "0" }
        return currentValue
    }

    /**
     * Concat current value with input [value] if current value is not 0
     * @param value input
     */
    private fun handleNewValue(value: String): String {
        currentValue = "${if (currentValue != "0") currentValue else ""}$value"
        return currentValue
    }

    private fun String.shorten() = substring(0, lastIndex)
    private fun View.snack(text: String) { Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show() }
}
