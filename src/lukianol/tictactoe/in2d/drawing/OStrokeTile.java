package lukianol.tictactoe.in2d.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class OStrokeTile extends StrokeTileBase
{
	protected OStrokeTile(Paint paint, float hStrokeMarginPercent,
			float vStrokeMarginPercent) {
		super(paint, hStrokeMarginPercent, vStrokeMarginPercent);			
	}

	@Override
	public void Draw(Canvas canvas, RectF rect){
		float radius = (Math.min(rect.width(), rect.height()) / 2) - Math.max(this.getHStrokeMargin(rect), this.getVStrokeMargin(rect));
		canvas.drawCircle(rect.centerX(), rect.centerY(), radius, getPaint());
	}
}