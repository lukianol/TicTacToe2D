package lukianol.tictactoe.in2d.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

abstract class StrokeTileBase implements IStrokeTile
{
	protected StrokeTileBase(Paint paint, float hStrokeMarginPercent, float vStrokeMarginPercent){
		_hStrokeMarginPercent = hStrokeMarginPercent;
		_vStrokeMarginPercent = vStrokeMarginPercent;
		_paint = paint;
	}
	
	public abstract void Draw(Canvas canvas, RectF rect);

	protected float getHStrokeMargin(RectF rect){
		return rect.width()*_hStrokeMarginPercent;
	}
	
	protected float getVStrokeMargin(RectF rect){
		return rect.height()*_vStrokeMarginPercent;
	}
	
	protected Paint getPaint() {
		return _paint;
	}

	private final float _hStrokeMarginPercent;
	private final float _vStrokeMarginPercent;
	private final Paint _paint;
}