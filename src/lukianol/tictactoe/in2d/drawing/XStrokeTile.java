package lukianol.tictactoe.in2d.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class XStrokeTile extends StrokeTileBase
{
	public XStrokeTile(Paint paint, float hStrokeMarginPercent, float vStrokeMarginPercent) {
		super(paint, hStrokeMarginPercent, vStrokeMarginPercent);			
	}

	@Override
	public void Draw(Canvas canvas, RectF rect) {
		
		float hMargin = getHStrokeMargin(rect);
		float vMargin = getVStrokeMargin(rect);
		
		drawMarginedLine(canvas, rect.left, rect.top, rect.right, rect.bottom, hMargin, vMargin);
		drawMarginedLine(canvas, rect.right, rect.top, rect.left, rect.bottom, -hMargin, vMargin);
	}
	
	private void drawMarginedLine(Canvas canvas, float startX, float startY, float stopX, float stopY, float hMargin, float vMargin){
		
		canvas.drawLine(startX + hMargin
				, startY  + vMargin
				, stopX - hMargin
				, stopY - vMargin
				, getPaint());		
	}
}