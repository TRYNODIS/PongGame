package hu.nagyi.ponggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PongView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //region VARIABLES

    private val paintBg: Paint = Paint()
    private val paintLine: Paint = Paint()
    private val paintWhiteFill: Paint = Paint()
    private val paintText: Paint = Paint()

    private val PLAYER_WIDTH: Int = 180;
    private val PLAYER_HEIGHT: Int = 60;
    private var playerX: Float = 0f
    private var playerY: Float = 0f
    private var touchX: Float = 0f

    private var GAME_ENABLED = false
    private var circleX = 200f
    private var circleY = 200f
    private var CIRCLE_RAD = 45f
    private var dX = 10
    private var dY = 10
    private var point = 0

    //region INIT

    init {
        this.paintBg.color = Color.BLACK
        this.paintBg.style = Paint.Style.FILL

        this.paintLine.color = Color.WHITE
        this.paintLine.style = Paint.Style.STROKE
        this.paintLine.strokeWidth = 7f

        this.paintWhiteFill.color = Color.WHITE
        this.paintWhiteFill.style = Paint.Style.FILL

        this.paintText.color = Color.WHITE
        this.paintText.style = Paint.Style.STROKE
        this.paintText.textSize = 100f
    }

    //endregion

    //endregion

    //region METHODS

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.playerX = (this.width / 2 - this.PLAYER_WIDTH / 2).toFloat();

        this.paintText.textSize = this.height / 20f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)


        canvas?.drawRect(
            this.playerX,
            (this.height - this.PLAYER_HEIGHT).toFloat(),
            this.playerX + this.PLAYER_WIDTH,
            this.height.toFloat(), this.paintWhiteFill
        )

        canvas?.drawCircle(this.circleX, this.circleY, this.CIRCLE_RAD, this.paintWhiteFill)

        canvas?.drawText(this.point.toString(), 20f, this.height / 20 - 10f, this.paintText)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> this.touchX = event.x
            MotionEvent.ACTION_MOVE -> {
                handleMove(event)
            }
        }

        return true
    }

    private fun handleMove(event: MotionEvent) {
        playerX = playerX - (this.touchX - event.x)
        this.touchX = event.x
        this.playerX = if (this.playerX < 0) 0.toFloat()
        else if (this.playerX > this.width - this.PLAYER_WIDTH) {
            (this.width - this.PLAYER_WIDTH).toFloat()
        } else {
            this.playerX
        }
        this.invalidate()
    }

    fun startGame() {
        this.GAME_ENABLED = true
        GameThread().start()
    }

    fun stopGame() {
        this.GAME_ENABLED = false
        this.resetGame()
        this.invalidate()
    }

    private fun resetGame() {
        this.circleX = 200f
        this.circleY = 200f
        this.dX = 10
        this.dY = 10

        this.point = 0
    }

    //endregion

    //region INNER CLASS

    inner class GameThread : Thread() {
        override fun run() {
            while (this@PongView.GAME_ENABLED) {
                this@PongView.circleX += this@PongView.dX
                this@PongView.circleY += this@PongView.dY


                if (this@PongView.circleX > this@PongView.width - this@PongView.CIRCLE_RAD) {
                    this@PongView.circleX = this@PongView.width - this@PongView.CIRCLE_RAD
                    this@PongView.dX *= -1
                } else if (this@PongView.circleX < this@PongView.CIRCLE_RAD) {
                    this@PongView.circleX = this@PongView.CIRCLE_RAD
                    this@PongView.dX *= -1
                }

                if (this@PongView.circleY >= this@PongView.height - this@PongView.CIRCLE_RAD - this@PongView.PLAYER_HEIGHT) {
                    if (this@PongView.circleX in this@PongView.playerX..playerX + this@PongView.PLAYER_WIDTH) {
                        this@PongView.circleY =
                            this@PongView.height - this@PongView.CIRCLE_RAD - this@PongView.PLAYER_HEIGHT
                        this@PongView.dY *= -1
                        this@PongView.point++
                    } else {
                        this@PongView.resetGame()
                    }
                } else if (this@PongView.circleY < this@PongView.CIRCLE_RAD) {
                    this@PongView.circleY = this@PongView.CIRCLE_RAD
                    this@PongView.dY *= -1
                }

                this@PongView.postInvalidate()

                sleep(10)
            }
        }
    }

    //endregion

}