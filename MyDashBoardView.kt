package com.thinkwithu.www.gre.ui.widget
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.thinkwithu.www.gre.R
import kotlin.math.cos


/**
 * author: Gary
 * e-mail: 2509908478@qq.com
 * createTime: 2021/06/17 10:40
 * desc: 自定义仪表盘控件
 */
class MyDashBoardView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    var radius = SizeUtils.dp2px(100f).toFloat()
    private var progress = 0f

    //进度条宽度
    var progressStorkPx = SizeUtils.dp2px(14f).toFloat()

    //中间文本颜色
    var centerTextColor = ContextCompat.getColor(context, R.color.dash_board_progress)

    //中部文本大小
    var centerTextSizeSp = 50f

    //底部文本颜色
    var bottomTextColor = ContextCompat.getColor(context, R.color.font_dark_light)

    //底部文本大小
    var bottomTextSizeSp = 20f

    //底部显示文本，为空字符串则不显示
    var bottomText = ""

    //进度条最大值
    var max = 0f
    var rect = RectF()
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var animator = ObjectAnimator()

    init {
        paint.textSize = SizeUtils.dp2px(40f).toFloat()
        paint.textAlign = Paint.Align.CENTER
    }

    /**
     * 调用这个方法来设置动态的进度条动画
     * @param progress 0-max之间
     */
    fun setAnimaion(progress: Float) {
        animator = ObjectAnimator.ofFloat(this, "progress", 0f, progress)
        animator.duration = 1000
        animator.interpolator = FastOutLinearInInterpolator()
        animator.start()
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.end()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //设置warp_content的默认宽高
        val width = 300f
        val height = 200f
        //AT_MOST对应wrap_content；EXACTLY对应match_parent
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            //都设置的wrap_content的话
            setMeasuredDimension(SizeUtils.dp2px(width), SizeUtils.dp2px(height))
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            //都设置的match_parent话
            setMeasuredDimension(widthSize, heightSize)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //宽度为自适应
            setMeasuredDimension(SizeUtils.dp2px(width), heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //高度为自适应
            setMeasuredDimension(widthSize, SizeUtils.dp2px(height))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //圆弧半径大小根据view所获得的高宽来进行自适应判定
        val centerX = width / 2.toFloat()
        val centerY: Float =
                if (width >= height) {
                    var t = height / (1 + cos(Math.PI / 3).toFloat())
                    t = if (t >= centerX) centerX else t
                    radius = t - (progressStorkPx * 2)
                    radius + progressStorkPx
                } else {
                    radius = centerX - progressStorkPx
                    radius + progressStorkPx
                }
        //画进度条下部扇形阴影区域（特殊需求）
        //色彩渐变效果
//        val colorsNormal = intArrayOf(ContextCompat.getColor(context, R.color.transparent), ContextCompat.getColor(context, R.color.live_bg_grey), ContextCompat.getColor(context, R.color.transparent))
//        val shaderNormal: Shader = LinearGradient(centerX - radius + progressStorkPx, centerY + radius - progressStorkPx, centerX + radius - progressStorkPx, centerY + radius - progressStorkPx, colorsNormal, null, Shader.TileMode.CLAMP)
//        paint.shader = shaderNormal
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = progressStorkPx
//        rect.set(centerX - radius + progressStorkPx, centerY - radius + progressStorkPx, centerX + radius - progressStorkPx, centerY + radius - progressStorkPx)
//        canvas.drawArc(rect, 150f, 240f, false, paint)

        //原自定义扇形阴影和虚线都取消替换成图片
        var x=progressStorkPx/2
        rect.set(centerX - radius + x, centerY - radius + x, centerX + radius - x, centerY + radius - (progressStorkPx*2))
        canvas.drawBitmap(BitmapFactory.decodeResource(resources,R.mipmap.result_bj),null,rect,paint)

        //画底部视图
        paint.shader = null
        paint.color = ContextCompat.getColor(context, R.color.dash_board_grey_shallow)
        rect.set((centerX - radius), (centerY - radius), (centerX + radius), (centerY + radius))
        canvas.drawArc(rect, 150f, 240f, false, paint)

        //画覆盖区域
        paint.color = ContextCompat.getColor(context, R.color.dash_board_progress)
        canvas.drawArc(rect, 150f, (progress / max) * 240f, false, paint)

        //画进度条下部扇形阴影内虚线
        //色彩渐变效果
//        val colorsDotLine = intArrayOf(ContextCompat.getColor(context, R.color.transparent), ContextCompat.getColor(context, R.color.dash_board_grey_shallow), ContextCompat.getColor(context, R.color.transparent))
//        val shaderDotLine: Shader = LinearGradient(centerX - radius + progressStorkPx, centerY + radius - progressStorkPx, centerX + radius - progressStorkPx, centerY + radius - progressStorkPx, colorsDotLine, null, Shader.TileMode.CLAMP)
//        paint.shader = shaderDotLine
//        paint.strokeWidth = 0f
//        paint.pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
//        rect.set(centerX - radius + progressStorkPx, centerY - radius + progressStorkPx, centerX + radius - progressStorkPx, centerY + radius - progressStorkPx)
//        canvas.drawArc(rect, 150f, 240f, false, paint)

        //画中部文本
        paint.shader = null
        paint.style = Paint.Style.FILL
        paint.color = centerTextColor
        paint.textSize = SizeUtils.sp2px(centerTextSizeSp).toFloat()
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(progress.toInt().toString(), centerX, centerY, paint)
        //画底部文本
        paint.color = bottomTextColor
        paint.textSize = SizeUtils.sp2px(bottomTextSizeSp).toFloat()
        paint.typeface = Typeface.DEFAULT
        canvas.drawText(bottomText, centerX, centerY + (radius * cos(Math.PI / 3).toFloat()), paint)
    }
}
