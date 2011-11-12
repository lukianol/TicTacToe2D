package lukianol.tictactoe.in2d.drawing;

import android.graphics.Canvas;
import android.graphics.RectF;

interface IStrokeTile
{
	public abstract void Draw(Canvas canvas, RectF rect);
}