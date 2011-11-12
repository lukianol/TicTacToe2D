package lukianol.tictactoe.in2d.drawing;

import android.graphics.Paint;

abstract class StrokeTileFactoryBase<T> {
	
	public StrokeTileFactoryBase(Paint paint, float hMarginPercent, float vMarginPercent) {
		_paint = paint;
		_hMarginPercent = hMarginPercent;
		_vMarginPercent = vMarginPercent;
	}
	
	public abstract IStrokeTile get(T value);
	
	protected float get_hMarginPercent() {
		return _hMarginPercent;
	}

	protected float get_vMarginPercent() {
		return _vMarginPercent;
	}

	protected Paint get_paint() {
		return _paint;
	}

	private final float _hMarginPercent;
	private final float _vMarginPercent;
	private final Paint _paint;
}