package lukianol.tictactoe.in2d;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lukianol.tictactoe.Field;
import lukianol.tictactoe.Game;
import lukianol.tictactoe.GameBase;
import lukianol.tictactoe.GameEventListener;
import lukianol.tictactoe.GameState;
import lukianol.tictactoe.Position;
import lukianol.tictactoe.StrokeKind;
import lukianol.tictactoe.TicTacToeException;
import lukianol.tictactoe.in2d.drawing.DrawView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TicTacToe2DActivity extends Activity 
	implements DrawView.OnPositionTouchEventListener
	, GameEventListener {
    
	public static final int playgroundSize = Game.DefaultPlaygroundSize;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        initializeActivity();
        
    }
    
	public Boolean OnPositionTouch(View view, Position position) {
		
		if (!_game.isPlaying())
			this.openOptionsMenu();
		else
		{
			try 
			{
				_game.Stroke(position);
			} 
			catch (TicTacToeException e) 
			{
	    		Log.e(_className, "(!)", e);
	    	}
		}
		
		return true;		
		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus && !_windowHasFocusedFirstTime)
		{
			this.openOptionsMenu();		
			_windowHasFocusedFirstTime = true;
		}
	}
	
	public void onFieldStroked(GameBase game, Field field) {
		_drawView.addStroke(field.getPosition(), field.getStroke());		
	}
	
	public void onGameStateChanged(GameBase game, GameState gameState) {
		
		switch(gameState) {		
		case Playing:
			_drawView.initSurface();
			break;	
		case Won:
			_drawView.setWonStrokePositions(game.getWonPositions());
			this.openOptionsMenu();
			break;
		case Drawn:
			this.openOptionsMenu();
			break;			
		}
		
	}

	public void onCurrentStrokeChanged(GameBase game, StrokeKind stroke) {
		_currentStrokeDisplay.setText(String.format(getString(R.string.strokeLabel), stroke.toString().toUpperCase()));
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.options, menu);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{			
		ICommandFactory factory = _commands.get(item.getItemId());
		
		if (factory != null){
			factory.CreateCommand().Execute();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}			
	}

	private void initializeActivity(){
    	
    	setContentView(R.layout.main);
        _currentStrokeDisplay = (TextView)findViewById(R.id.stroke);
    	_drawView = new DrawView(this, playgroundSize);         
        _drawView.setOnPositionTouchEventListener(this);  	    	
    	ViewGroup layout = (ViewGroup)findViewById(R.id.root);
        layout.addView(_drawView, 0);        
    }
	
	public GameBase _game = GameBase.Null;	
    private DrawView _drawView;	
	private TextView _currentStrokeDisplay;
	private static final String _className = TicTacToe2DActivity.class.getName();
	private boolean _windowHasFocusedFirstTime = false;
	
	@SuppressWarnings("serial")
	private Map<Integer, ICommandFactory> _commands = Collections.unmodifiableMap(new HashMap<Integer, ICommandFactory>(){
				{
					put(R.id.newGame, new NewGameCommand(TicTacToe2DActivity.this));
					put(R.id.aboutGame, new AboutCommand(TicTacToe2DActivity.this));
					put(R.id.exitGame, new ExitCommand(TicTacToe2DActivity.this));
				}
			});
	
}