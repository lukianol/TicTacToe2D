package lukianol.tictactoe.in2d.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class TextStrokeTile extends StrokeTileBase
{
	private String _text;
	
	protected TextStrokeTile(String text, Paint paint, float hStrokeMarginPercent,
			float vStrokeMarginPercent) {
		super(paint, hStrokeMarginPercent, vStrokeMarginPercent);	
		_text = text;
	}

	@Override
	public void Draw(Canvas canvas, RectF rect) {
		canvas.drawText(_text, rect.centerX(), rect.centerY() + this.getVStrokeMargin(rect), getPaint());
	}
	
	
}