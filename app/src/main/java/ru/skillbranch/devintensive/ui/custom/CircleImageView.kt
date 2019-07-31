package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min


class CircleImageView : ImageView {
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
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = Utils.dpToPx(context, DEFAULT_BORDER_WIDTH)

    private var bitmapShader: Shader? = null
    private val shaderMatrix = Matrix()

    private val bitmapDrawBounds = RectF()
    private val borderBounds = RectF()

    private var bitmap: Bitmap? = null

    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var initialized: Boolean = false

    private fun init() {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, borderWidth)
            a.recycle()
        }

        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth.toFloat()

        initialized = true
        setupBitmap()
    }

    @Dimension
    fun getBorderWidth(): Int = Utils.pxToDp(context, borderWidth)

    fun setBorderWidth(@Dimension dp: Int) {
        val newWidth = Utils.dpToPx(context, dp)
        if (newWidth != borderWidth) {
            borderWidth = newWidth
            invalidate()
        }
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(bitmapDrawBounds, bitmapPaint)

        if (borderPaint.strokeWidth > 0f) {
            canvas.drawOval(borderBounds, borderPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = borderPaint.strokeWidth / 2f
        updateCircleDrawBounds(bitmapDrawBounds)
        borderBounds.set(bitmapDrawBounds)
        borderBounds.inset(halfStrokeWidth, halfStrokeWidth)

        updateBitmapSize()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        if (!initialized) {
            return
        }

        bitmap = getBitmapFromDrawable(drawable)
        if (bitmap == null) {
            return
        }

        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.shader = bitmapShader

        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        if (bitmap == null) {
            return
        }

        val dx: Float
        val dy: Float
        val scale: Float

        // Scale up/down with respect to this view size and maintain aspect ratio
        // Translate bitmap position with dx/dy to the center of the image
        if (bitmap!!.width < bitmap!!.height) {
            scale = bitmapDrawBounds.width() / bitmap!!.width.toFloat()
            dx = bitmapDrawBounds.left
            dy = bitmapDrawBounds.top - (bitmap!!.height * scale / 2f) + (bitmapDrawBounds.width() / 2f)
        } else {
            scale = bitmapDrawBounds.height() / bitmap!!.height.toFloat()
            dx = bitmapDrawBounds.left - (bitmap!!.width * scale / 2f) + (bitmapDrawBounds.width() / 2f)
            dy = bitmapDrawBounds.top
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx, dy)
        bitmapShader?.setLocalMatrix(shaderMatrix)
    }
}