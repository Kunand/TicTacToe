package com.example.tictactoe.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.tictactoe.MainActivity
import com.example.tictactoe.model.TicTacToeModel

class TicTacToeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintBackground : Paint = Paint()
    private var paintLine : Paint = Paint()

    var gameEnabled = true


    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.YELLOW
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 5f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameArea(canvas)
        drawPlayers(canvas)


    }


    private fun drawGameArea(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)

        canvas?.drawLine(0f,(height / 3).toFloat(), width.toFloat(), (height / 3).toFloat(), paintLine)
        canvas?.drawLine(0f,( 2 * height / 3).toFloat(), width.toFloat(), ( 2 * height / 3).toFloat(), paintLine)

        canvas?.drawLine((width / 3).toFloat() ,0f, (width / 3).toFloat(), height.toFloat(), paintLine)
        canvas?.drawLine(( 2* width / 3).toFloat() ,0f, (2 * width / 3).toFloat(), height.toFloat(), paintLine)


    }

    private fun drawPlayers(canvas: Canvas){
        for(i in 0..2){
            for(j in 0..2){
                if (TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CIRCLE){
                    val centerx = (i * width / 3 + width / 6).toFloat()
                    val centery = (j * height / 3 + height / 6).toFloat()
                    val radius = height / 6 -2

                    canvas.drawCircle(centerx, centery, radius.toFloat(), paintLine)
                }else if(TicTacToeModel.getFieldContent(i, j) == TicTacToeModel.CROSS){
                    canvas.drawLine((i * width / 3).toFloat(), (j * height / 3).toFloat(),
                    ((i + 1) * width / 3).toFloat(),
                    ((j + 1) * height / 3).toFloat(), paintLine)

                    canvas.drawLine(((i + 1) * width / 3).toFloat(), (j * height / 3).toFloat(),
                    (i * width / 3).toFloat(), ((j + 1) * height / 3).toFloat(), paintLine)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && gameEnabled){
            val tx = event.x.toInt() / (width/3)
            val ty = event.y.toInt() / (width/3)

            if (tx < 3 && ty < 3 && TicTacToeModel.getFieldContent(tx, ty) == TicTacToeModel.EMPTY){
                TicTacToeModel.setFieldContent(tx, ty, TicTacToeModel.getNextPlayer())
                TicTacToeModel.changeNextPlayer()


                var next = "0"
                if (TicTacToeModel.getNextPlayer() == TicTacToeModel.CROSS){
                    next = "X"
                }
                if(TicTacToeModel.findWinner() == TicTacToeModel.NOT_FINISHED) {
                    (context as MainActivity).showNextPlayer("Next player is $next")
                }
                else if(TicTacToeModel.findWinner() == TicTacToeModel.CROSS){
                    (context as MainActivity).showNextPlayer("Winner is X")
                    gameEnabled = false
                }
                else if(TicTacToeModel.findWinner() == TicTacToeModel.CIRCLE){
                    (context as MainActivity).showNextPlayer("Winner is O")
                    gameEnabled = false
                }
                else{
                    (context as MainActivity).showNextPlayer("Tie")
                    gameEnabled = false
                }
                invalidate()


            }
        }
        return true
    }


    fun resetGame(){
        TicTacToeModel.resetModel()
        (context as MainActivity).showNextPlayer("Next player is 0")
        gameEnabled = true
        invalidate()
    }



}
