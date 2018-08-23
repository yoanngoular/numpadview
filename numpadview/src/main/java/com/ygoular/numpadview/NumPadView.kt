package com.ygoular.numpadview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.numpadview_layout.view.*

/**
 * Project : NumPadView
 * @file com.ygoular.numpadview.NumPadView
 * @brief A nice and customizable numeric keypad view.
 * @date 2018-08-16
 * @author yoann
 */
@Suppress("UNUSED")
class NumPadView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
        GridLayout(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : this(context, attrs, defStyleAttr, 0)

    companion object {
        private const val DEFAULT_FONT_FOLDER = "fonts/"
        private const val DEFAULT_FONT_NAME = "Noto_sans.ttf"
        const val DEFAULT_FONT_PATH = "$DEFAULT_FONT_FOLDER$DEFAULT_FONT_NAME"
        const val DEFAULT_LEFT_ICON_VALUE = "LEFT"
        const val DEFAULT_RIGHT_ICON_VALUE = "RIGHT"
        const val DEFAULT_COLOR_HEX = "#7097F7"
        const val DEFAULT_TEXT_SIZE_SP = 22F
        const val DEFAULT_TEXT_STYLE = Typeface.NORMAL
        const val DEFAULT_TEXT_RIPPLE_EFFECT = true
        const val DEFAULT_LEFT_ICON_RIPPLE_EFFECT = true
        const val DEFAULT_RIGHT_ICON_RIPPLE_EFFECT = true
        const val DEFAULT_DRAWABLE_LEFT = true
        const val DEFAULT_DRAWABLE_RIGHT = true
        const val DEFAULT_BACKGROUND_RESOURCE = android.R.color.transparent
        const val DEFAULT_BACKGROUND_TINT_NO_TINT = -1
    }

    private val mPadNumbers: MutableList<TextView> = mutableListOf()
    private val mDefaultColor by lazy { Color.parseColor(DEFAULT_COLOR_HEX) }
    private val mDefaultBackgroundNoRippleEffect by lazy { context.getDrawable(DEFAULT_BACKGROUND_RESOURCE) }
    private val mView by lazy { LayoutInflater.from(context).inflate(R.layout.numpadview_layout, this, false) }

    // Listener of the custom view
    private lateinit var mListener: NumPadView.OnNumPadInteractionListener

    // Variable properties of the custom view
    private var mTextSize: Float = 0F
    private var mTextColor: Int = 0
    private var mTextStyle: Int = 0
    private var mTextRippleEffect: Boolean = true
    private var mFontPath: String = ""
    private var mDrawableLeft: Boolean = true
    private var mDrawableRight: Boolean = true
    private lateinit var mLeftIcon: Drawable
    private lateinit var mRightIcon: Drawable
    private var mLeftIconTint: Int = 0
    private var mRightIconTint: Int = 0
    private var mLeftIconRippleEffect: Boolean = true
    private var mRightIconRippleEffect: Boolean = true
    private var mBackgroundDrawableResource: Int = 0

    init {

        context.theme.obtainStyledAttributes(attrs, R.styleable.NumPadView, 0, 0).apply {

            try {

                // Fetching all attributes
                mTextSize = getDimension(R.styleable.NumPadView_text_size, DEFAULT_TEXT_SIZE_SP)
                mTextColor = getColor(R.styleable.NumPadView_text_color, mDefaultColor)
                mTextStyle = getInt(R.styleable.NumPadView_text_style, DEFAULT_TEXT_STYLE)
                mTextRippleEffect = getBoolean(R.styleable.NumPadView_text_ripple_effect, DEFAULT_TEXT_RIPPLE_EFFECT)
                mFontPath = getString(R.styleable.NumPadView_font_path) ?: DEFAULT_FONT_PATH
                mDrawableLeft = getBoolean(R.styleable.NumPadView_drawable_left, DEFAULT_DRAWABLE_LEFT)
                mDrawableRight = getBoolean(R.styleable.NumPadView_drawable_right, DEFAULT_DRAWABLE_RIGHT)
                mLeftIconTint = getColor(R.styleable.NumPadView_left_icon_tint, mDefaultColor)
                mRightIconTint = getColor(R.styleable.NumPadView_right_icon_tint, DEFAULT_BACKGROUND_TINT_NO_TINT)
                mLeftIcon = getDrawable(R.styleable.NumPadView_left_icon) ?: resources.getDrawable(R.drawable.ic_back, context.theme)
                mRightIcon = getDrawable(R.styleable.NumPadView_right_icon) ?: resources.getDrawable(R.drawable.button_validate, context.theme)
                mLeftIconRippleEffect = getBoolean(R.styleable.NumPadView_left_icon_ripple_effect, DEFAULT_LEFT_ICON_RIPPLE_EFFECT)
                mRightIconRippleEffect = getBoolean(R.styleable.NumPadView_right_icon_ripple_effect, DEFAULT_RIGHT_ICON_RIPPLE_EFFECT)
                mBackgroundDrawableResource = getResourceId(R.styleable.NumPadView_background_resource, DEFAULT_BACKGROUND_RESOURCE)

            } finally { recycle() } // TypedArray objects are a shared resource and must be recycled after use.
        }

        // Adding TextView to a list in order to perform multiple operations on each.
        mPadNumbers.add(mView.pad_number_0)
        mPadNumbers.add(mView.pad_number_1)
        mPadNumbers.add(mView.pad_number_2)
        mPadNumbers.add(mView.pad_number_3)
        mPadNumbers.add(mView.pad_number_4)
        mPadNumbers.add(mView.pad_number_5)
        mPadNumbers.add(mView.pad_number_6)
        mPadNumbers.add(mView.pad_number_7)
        mPadNumbers.add(mView.pad_number_8)
        mPadNumbers.add(mView.pad_number_9)
        mPadNumbers.add(mView.pad_number_0)

        // Convert dimension to adapt screen size and resolution
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mTextSize, resources.displayMetrics)

        setup()

        addView(mView)
    }

    /**
     * Method that sets all attributes of the view
     */
    private fun setup() {

        mView.background = resources.getDrawable(mBackgroundDrawableResource, context.theme)

        // Text parameters

        mPadNumbers.forEach {
            it.textSize = mTextSize
            it.setTextColor(mTextColor)
            it.setTypeface(Typeface.createFromAsset(context.assets, mFontPath), mTextStyle)
            it.background =  if (mTextRippleEffect) { createRippleDrawableBackground() } else { mDefaultBackgroundNoRippleEffect }
            it.setOnClickListener(this) // Text mListener
        }

        // Drawable  parameters

        if(mDrawableLeft) {
            mLeftIcon.setTint(mLeftIconTint)
            mView.pad_number_left_icon.setImageDrawable(mLeftIcon)
            mView.pad_number_left_icon.background = if (mLeftIconRippleEffect) { createRippleDrawableBackground() }
                                                    else { mDefaultBackgroundNoRippleEffect }
            mView.pad_number_left_icon.setOnClickListener(this)
        } else {
            mView.pad_number_left_icon.background = mDefaultBackgroundNoRippleEffect
            mView.pad_number_left_icon.setImageResource(DEFAULT_BACKGROUND_RESOURCE)
            mView.pad_number_left_icon.setOnClickListener(null)
        }

        if(mDrawableRight) {
            if (mRightIconTint != DEFAULT_BACKGROUND_TINT_NO_TINT) mRightIcon.setTint(mRightIconTint)
            mView.pad_number_right_icon.setImageDrawable(mRightIcon)
            mView.pad_number_right_icon.background = if (mRightIconRippleEffect) { createRippleDrawableBackground() }
                                                     else { mDefaultBackgroundNoRippleEffect }
            mView.pad_number_right_icon.setOnClickListener(this)
        } else {
            mView.pad_number_right_icon.background = mDefaultBackgroundNoRippleEffect
            mView.pad_number_right_icon.setImageResource(DEFAULT_BACKGROUND_RESOURCE)
            mView.pad_number_right_icon.setOnClickListener(null)
        }
    }

    fun setTextSize(sp: Float): NumPadView {
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, sp, resources.displayMetrics)
        return this
    }

    fun setTextFont(fontPath: String): NumPadView {
        mFontPath = fontPath
        return this
    }

    fun setTextStyle(textStyle: Int): NumPadView {
        mTextStyle = textStyle
        return this
    }

    fun setTextColor(@ColorRes textColor: Int): NumPadView {
        mTextColor = resources.getColor(textColor, context.theme)
        return this
    }

    fun setTextColor(hex: String): NumPadView {
        mTextColor = Color.parseColor(hex)
        return this
    }

    fun setTextRippleEffect(rippleEffect: Boolean): NumPadView {
        mTextRippleEffect = rippleEffect
        return this
    }

    fun setDrawableLeft(exist: Boolean): NumPadView {
        mDrawableLeft = exist
        return this
    }

    fun setDrawableRight(exist: Boolean): NumPadView {
        mDrawableRight = exist
        return this
    }

    fun setLeftIcon(leftIcon: Drawable): NumPadView {
        mLeftIcon = leftIcon
        return this
    }

    fun setLeftIcon(@DrawableRes leftIcon: Int): NumPadView {
        mLeftIcon = resources.getDrawable(leftIcon, context.theme)
        return this
    }

    fun setLeftIconTint(@ColorRes leftIconTint: Int): NumPadView {
        mLeftIconTint = resources.getColor(leftIconTint, context.theme)
        return this
    }

    fun setLeftIconTint(r: Int, g: Int, b: Int): NumPadView {
        mLeftIconTint = Color.rgb(r,g,b)
        return this
    }

    fun setLeftIconTint(hex: String): NumPadView {
        mLeftIconTint = Color.parseColor(hex)
        return this
    }

    fun setLeftIconRippleEffect(rippleEffect: Boolean): NumPadView {
        mLeftIconRippleEffect = rippleEffect
        return this
    }

    fun setRightIcon(rightIcon: Drawable): NumPadView {
        mRightIcon = rightIcon
        return this
    }

    fun setRightIcon(@DrawableRes rightIcon: Int): NumPadView {
        mRightIcon = resources.getDrawable(rightIcon, context.theme)
        return this
    }

    fun setRightIconTint(@ColorRes rightIconTint: Int): NumPadView {
        mRightIconTint = resources.getColor(rightIconTint, context.theme)
        return this
    }

    fun setRightIconTint(r: Int, g: Int, b: Int): NumPadView {
        mRightIconTint = Color.rgb(r,g,b)
        return this
    }

    fun setRightIconTint(hex: String): NumPadView {
        mRightIconTint = Color.parseColor(hex)
        return this
    }

    fun setRightIconRippleEffect(rippleEffect: Boolean): NumPadView {
        mRightIconRippleEffect = rippleEffect
        return this
    }

    fun setBackgroundDrawableResource(@DrawableRes drawableId: Int): NumPadView {
        mBackgroundDrawableResource = drawableId
        return this
    }

    /**
     * Use this methods to set the view's attributes to its initial values.
     * Calling this method does not require to call [apply] method.
     */
    fun restoreDefaults() {
        setTextSize(DEFAULT_TEXT_SIZE_SP)
        setTextColor(DEFAULT_COLOR_HEX)
        setTextStyle(DEFAULT_TEXT_STYLE)
        setTextRippleEffect(DEFAULT_TEXT_RIPPLE_EFFECT)
        setTextFont(DEFAULT_FONT_PATH)
        setDrawableLeft(DEFAULT_DRAWABLE_LEFT)
        setDrawableRight(DEFAULT_DRAWABLE_RIGHT)
        setLeftIcon(R.drawable.ic_back)
        setLeftIconTint(DEFAULT_COLOR_HEX)
        setLeftIconRippleEffect(DEFAULT_LEFT_ICON_RIPPLE_EFFECT)
        setRightIcon(R.drawable.button_validate)
        setRightIconTint(android.R.color.background_light)
        setRightIconRippleEffect(DEFAULT_RIGHT_ICON_RIPPLE_EFFECT)
        setBackgroundDrawableResource(DEFAULT_BACKGROUND_RESOURCE)
        apply()
    }

    /**
     * This method must be called every time a change is made on the view's attributes.
     */
    fun apply() {
        invalidate()
        setup()
        requestLayout()
    }

    fun shake(context: Context) { startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake)) }

    private fun createRippleDrawableBackground(): Drawable? {
        return context.getDrawable(TypedValue().apply { context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true) }.resourceId)

    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.pad_number_left_icon -> { if(::mListener.isInitialized) mListener.onLeftIconClick() }
            R.id.pad_number_right_icon -> { if(::mListener.isInitialized) mListener.onRightIconClick() }
            else -> { if(::mListener.isInitialized) mListener.onNewValue(resources.getResourceEntryName(view.id).substringAfterLast("_"))  }
        }
    }

    /**
     * Method receiving lambda expressions to declare listeners of the different keypad buttons.
     * The numeric keypad is divided in 3 parts, the numbers, the left icon, and the right one.
     * By default, if [onLeftIconClick] and/or [onRightIconClick] lambdas are not passed in arguments
     * the function calls onNewValue method with specific values defining right and left icon clicks.
     */
    fun setOnInteractionListener(onLeftIconClick: () -> Unit = {},
                                 onRightIconClick: () -> Unit = {},
                                 onNewValue: (value: String) -> Unit = {}) {
        mListener = object : NumPadView.OnNumPadInteractionListener {
            override fun onLeftIconClick() { onLeftIconClick() }
            override fun onRightIconClick() { onRightIconClick() }
            override fun onNewValue(value: String) { onNewValue(value) }
        }
    }

    fun setOnInteractionListener(onNewValue: (value: String) -> Unit = {}) {
        mListener = object : NumPadView.OnNumPadInteractionListener {
            override fun onNewValue(value: String) { onNewValue(value) }
        }
    }

    fun setOnInteractionListener(listener: OnNumPadInteractionListener) { mListener = listener }

    interface OnNumPadInteractionListener {
        fun onLeftIconClick() = onNewValue(DEFAULT_LEFT_ICON_VALUE)
        fun onRightIconClick() = onNewValue(DEFAULT_RIGHT_ICON_VALUE)
        fun onNewValue(value: String)
    }
}