package lukianol.tictactoe.in2d.drawing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Paint;
import lukianol.tictactoe.StrokeKind;

final class StrokeTileFactory extends StrokeTileFactoryBase<StrokeKind> {
		
	@SuppressWarnings("serial")
	public StrokeTileFactory(Paint paint, float hMarginPercent, float vMarginPercent) {
		
		super(paint, hMarginPercent, vMarginPercent);
		
		_strokeTiles = Collections.unmodifiableMap(new HashMap<StrokeKind, IStrokeTile>(2){
			{
				put(StrokeKind.X, new XStrokeTile(get_paint(), get_hMarginPercent(), get_vMarginPercent()));
				put(StrokeKind.O, new OStrokeTile(get_paint(), get_hMarginPercent(), get_vMarginPercent()));
			}
		});
		
	}

	public IStrokeTile get(StrokeKind strokeKind){
		
		IStrokeTile tile = _strokeTiles.get(strokeKind);
		
		if (tile == null)
			throw new IllegalArgumentException();
		
		return tile;
	}

	private final Map<StrokeKind, IStrokeTile> _strokeTiles;	
}
