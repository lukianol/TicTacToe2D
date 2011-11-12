package lukianol.tictactoe.in2d;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lukianol.tictactoe.Position;
import lukianol.tictactoe.StrokeKind;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public final class DrawView extends View {

	public DrawView(Context context, int playgroundSize) {
		this(context, playgroundSize, false);
	}
	
	@SuppressWarnings("serial")
	public DrawView(Context context, int playgroundSize, Boolean debugMode) {
		super(context);	
		
		_debugMode = debugMode;
		
		_strokesPaint = _linesPaint = new Paint();
		_linesPaint.setColor(Color.WHITE);
		_linesPaint.setStrokeWidth(10);
		_linesPaint.setStrokeCap(Cap.ROUND);
		_linesPaint.setStyle(Style.STROKE);
		_linesPaint.setAntiAlias(true);
		
		_wonPaint = new Paint();
		_wonPaint.setARGB(225, 255, 0, 0);
		_wonPaint.setStrokeWidth(20);
		_wonPaint.setStrokeCap(Cap.ROUND);
		_wonPaint.setStyle(Style.STROKE);
		_wonPaint.setAntiAlias(true);
		
		_backgroundPaint = new Paint();
		_backgroundPaint.setColor(Color.BLACK);
		_backgroundPaint.setStyle(Style.FILL);
		
		
		if (_debugMode) {
			_debugBorderPaint = new Paint();
			_debugBorderPaint.setColor(Color.RED);
			_debugBorderPaint.setStrokeWidth(2);
			_debugBorderPaint.setStyle(Style.STROKE);
			_debugBorderPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 1));
		}
		else 
			_debugBorderPaint = null;
				
		_size = playgroundSize;
		
		_horizontalAreaMarginPercent = _verticalAreaMarginPercent = 0.05f;
		
		_strokeTiles = Collections.unmodifiableMap(new HashMap<StrokeKind, IStrokeTile>(2){
			{
				put(StrokeKind.X, new XStrokeTile(_strokesPaint, 0.2f, 0.2f));
				put(StrokeKind.O, new OStrokeTile(_strokesPaint, 0.2f, 0.2f));
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (this._isInited && !this.hasWonPositions())
			switch(event.getAction()){
			
			case MotionEvent.ACTION_DOWN:
				Position position = getPosition(event);			
				if (position != null)
					return invokeOnPositionTouchEventListener(position);
				break;
				
			default:
				return super.onTouchEvent(event);			
			}
		
		return super.onTouchEvent(event);		
	}

	public void setOnPositionTouchEventListener(OnPositionTouchEventListener listener){
		_onPositionTouchListener = listener;
	}
	
	public void addStroke(Position position, StrokeKind strokeKind){
		_positionToStrokeKindMap.put(position, strokeKind);
		this.invalidate();
	}

	public void setWonStrokePositions(Position[] positions){
		_wonPositions = positions;
		invalidate();
	}
	
	public void initSurface(){
		_positionToStrokeKindMap.clear();
		_wonPositions = null;	
		_isInited = true;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {		
		
		if (!this._isInited)	
			return;
		
		initSurfaceBox();	
		drawBackground(canvas);
		debugDrawView(canvas);
		debugDrawSurfaceBox(canvas);
		drawVerticalLines(canvas);		
		drawHorizontalLines(canvas);
		drawStrokes(canvas);
		drawWonPositions(canvas);
	}
	
	private void debugDrawView(Canvas canvas){
		if (this._debugMode)
			canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), this._debugBorderPaint);
	}
	
	private void drawWonPositions(Canvas canvas){
		
		if (hasWonPositions()){

			RectF start = _positionToFieldMap.get(_wonPositions[0]);
			RectF end = _positionToFieldMap.get(_wonPositions[_wonPositions.length - 1]);
			
			RectF finalRect = new RectF(start);
			finalRect.union(end);
			float finalHeight = finalRect.height();
			float finalWidth = finalRect.width();
			float startX, startY, stopX, stopY;
			 
			if ( finalHeight == finalWidth ) 							
				if (start.centerX() < end.centerX()){
					startX = finalRect.left;
					startY = finalRect.top;
					stopX = finalRect.right;
					stopY = finalRect.bottom;
				} else {
					startX = finalRect.right;
					startY = finalRect.top;
					stopX = finalRect.left;
					stopY = finalRect.bottom;
				}
			else if (finalHeight > finalWidth){
				startX = finalRect.centerX();
				startY = finalRect.top;
				stopX = startX;
				stopY = finalRect.bottom;
			}
			else {
				startX = finalRect.left;
				startY = finalRect.centerY();
				stopX = finalRect.right;
				stopY = startY;
			}
			
			canvas.drawLine(startX, startY, stopX, stopY, this._wonPaint);
			
		}
		
	}
	
	private void drawStrokes(Canvas canvas) {
		
		for(Map.Entry<Position, StrokeKind> item : _positionToStrokeKindMap.entrySet()){
			getStrokeTile(item.getValue()).Draw(canvas, _positionToFieldMap.get(item.getKey()));
		}
		
	}

	private Boolean invokeOnPositionTouchEventListener(Position position){
		if (_onPositionTouchListener != null)
		{
			return  _onPositionTouchListener.OnPositionTouch(this, position);	
			
		}
			
		return false;
	}
	
	private Position getPosition(MotionEvent event) {
		
		if (_positionToFieldMap != null)
			for(Map.Entry<Position, RectF> field : _positionToFieldMap.entrySet())			
				if (field.getValue().contains(event.getX(), event.getY()))
					return field.getKey();
		
		return null;		
	}
		
	private IStrokeTile getStrokeTile(StrokeKind stroke) {
		return _strokeTiles.get(stroke);
	}
	
	private void drawBackground(Canvas canvas){		
		canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), this._backgroundPaint);
	}
	
	private void drawVerticalLines(Canvas canvas) {
		
		float widthInterval = getFieldWidth();
		float startY = _surfaceBox.top, stopY = _surfaceBox.bottom;
		
		for(int i = 1; i < _size; i++){
			float startX, stopX;
			startX = stopX = widthInterval * i + _surfaceBox.left;						
			canvas.drawLine(startX, startY, stopX, stopY, _linesPaint);			
		}
		
	}
	
	private void drawHorizontalLines(Canvas canvas) {
			
		float heightInterval = getFieldHeight();
		float startX = _surfaceBox.left, stopX = _surfaceBox.right;
		
		for(int i = 1; i < _size; i++){
			float startY, stopY;
			startY = stopY = heightInterval * i + _surfaceBox.top;
						
			canvas.drawLine(startX, startY, stopX, stopY, _linesPaint);			
		}
		
	}

	private void debugDrawSurfaceBox(Canvas canvas){
		if (_debugMode)
			canvas.drawRect(_surfaceBox, this._debugBorderPaint);
	}
	
	private float getHorizontalAreaMargin(){
		int width = this.getWidth();
		int height = this.getHeight();
		int squarizer = ((width > height) ? width - height : 0); 
		return (Math.min(height, width) * _horizontalAreaMarginPercent) + (squarizer / 2);
	}
	
	private float getVerticalAreaMargin(){
		int width = this.getWidth();
		int height = this.getHeight();
		int squarizer = ((height > width) ? height - width : 0);
		return (Math.min(height, width) * _verticalAreaMarginPercent) + (squarizer / 2);
	}
	
	private float getFieldWidth(){
		return this._surfaceBox.width() / this._size;
	}
	
	private float getFieldHeight(){
		return this._surfaceBox.height() / this._size;
	}
	
	private void initSurfaceBox(){
		
		float hMargin = getHorizontalAreaMargin();
		float vMargin = getVerticalAreaMargin();
		
		_surfaceBox = new RectF();
		_surfaceBox.left = hMargin;
		_surfaceBox.right =  (this.getWidth() - hMargin);
		_surfaceBox.top =  vMargin;
		_surfaceBox.bottom = (this.getHeight() - vMargin);
				
		float fieldHeight = getFieldHeight();
		float fieldWidth = getFieldWidth();
		_positionToFieldMap = new HashMap<Position, RectF>();
		
		for(int c = 0; c < _size; c++)
			for(int r = 0; r < _size; r++){
				Position position = Position.Create(c, r);
				RectF field = new RectF();
				field.left = _surfaceBox.left + fieldWidth * c;
				field.right = field.left + fieldWidth;
				field.top = _surfaceBox.top + fieldHeight * r;
				field.bottom = field.top + fieldHeight;
				_positionToFieldMap.put(position, field);
			}
	}
	
	private Boolean hasWonPositions(){
		return _wonPositions != null;
	}
	
	private final Paint _debugBorderPaint;
	private final Paint _backgroundPaint;
	private final Paint _linesPaint;
	private final Paint _strokesPaint;
	private final Paint _wonPaint;
	private final int _size;
	private final float _horizontalAreaMarginPercent;
	private final float _verticalAreaMarginPercent;
	private RectF _surfaceBox;
	private Map<Position, RectF> _positionToFieldMap;
	private final Boolean _debugMode;
	private OnPositionTouchEventListener _onPositionTouchListener;
	private final Map<Position, StrokeKind> _positionToStrokeKindMap = new HashMap<Position, StrokeKind>();
	private Position[] _wonPositions;
	private boolean _isInited = false;
	private final Map<StrokeKind, IStrokeTile> _strokeTiles;
	
	public interface OnPositionTouchEventListener {
		public abstract Boolean OnPositionTouch(View view, Position position);
	}

	interface IStrokeTile
	{
		public abstract void Draw(Canvas canvas, RectF rect);
	}
	
	abstract class StrokeTileBase implements IStrokeTile
	{
		private float _hStrokeMarginPercent;
		private float _vStrokeMarginPercent;
		protected Paint _paint;
		
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
	}
	
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
					, this._paint);		
		}
	}
	
	class OStrokeTile extends StrokeTileBase
	{
		protected OStrokeTile(Paint paint, float hStrokeMarginPercent,
				float vStrokeMarginPercent) {
			super(paint, hStrokeMarginPercent, vStrokeMarginPercent);			
		}

		@Override
		public void Draw(Canvas canvas, RectF rect){
			float radius = (Math.min(rect.width(), rect.height()) / 2) - Math.max(this.getHStrokeMargin(rect), this.getVStrokeMargin(rect));
			canvas.drawCircle(rect.centerX(), rect.centerY(), radius, this._paint);
		}
	}
	
}
