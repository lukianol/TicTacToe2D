package lukianol.tictactoe.in2d.drawing;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Paint;

public class TextStrokeTileFactory extends StrokeTileFactoryBase<String> {

	public TextStrokeTileFactory(Paint paint, float hMarginPercent,
			float vMarginPercent) {
		super(paint, hMarginPercent, vMarginPercent);
		_strokeTiles = new HashMap<String, IStrokeTile>();
	}

	@Override
	public IStrokeTile get(String value) {
		if (!_strokeTiles.containsKey(value)){
			IStrokeTile textTile = new TextStrokeTile(value, this.get_paint(), this.get_hMarginPercent(), this.get_vMarginPercent());
			_strokeTiles.put(value, textTile);
		}
		
		return _strokeTiles.get(value);
	}
	
	private final Map<String, IStrokeTile> _strokeTiles;

}
