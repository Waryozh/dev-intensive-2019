package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import ru.skillbranch.devintensive.R

class AvatarImageView : CircleImageView {
    private var attrs: AttributeSet? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, _attrs: AttributeSet?) : super(context, _attrs) {
        this.attrs = _attrs
        init()
    }

    constructor(context: Context, _attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        _attrs,
        defStyleAttr
    ) {
        this.attrs = _attrs
        init()
    }

    companion object {
        private const val DEFAULT_TEXT = "??"
        private const val DEFAULT_TEXT_SIZE = 32f
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_BACKGROUND_COLOR = Color.DKGRAY
    }

    private var initials = DEFAULT_TEXT

    private val textBounds = Rect()
    private val backgroundBounds = RectF()

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun init() {
        var textSize = DEFAULT_TEXT_SIZE
        var textColor = DEFAULT_TEXT_COLOR
        var backgroundColor = DEFAULT_BACKGROUND_COLOR

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView)
            initials = a.getString(R.styleable.AvatarImageView_aiv_text) ?: DEFAULT_TEXT
            textSize = a.getDimension(R.styleable.AvatarImageView_aiv_textSize, DEFAULT_TEXT_SIZE)
            textColor = a.getColor(R.styleable.AvatarImageView_aiv_textColor, DEFAULT_TEXT_COLOR)
            backgroundColor = a.getColor(R.styleable.AvatarImageView_aiv_backgroundColor, DEFAULT_BACKGROUND_COLOR)
            a.recycle()
        }

        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.FILL

        textPaint.color = textColor
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = textSize
    }

    override fun onDraw(canvas: Canvas) {
        if (initials != DEFAULT_TEXT) {
            val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()
            canvas.drawOval(backgroundBounds, backgroundPaint)
            canvas.drawText(initials, backgroundBounds.centerX(), textBottom, textPaint)
            drawStroke(canvas)
        } else {
            super.onDraw(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateCircleDrawBounds(backgroundBounds)
    }

    fun setInitials(text: String) {
        initials = text
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)
        invalidate()
    }
}
