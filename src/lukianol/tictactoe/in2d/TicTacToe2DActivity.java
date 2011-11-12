package lukianol.tictactoe.in2d;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lukianol.tictactoe.Field;
import lukianol.tictactoe.Game;
import lukianol.tictactoe.GameEventListener;
import lukianol.tictactoe.GameState;
import lukianol.tictactoe.IGame;
import lukianol.tictactoe.NullGame;
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
		
		try 
		{
			_game.Stroke(position);
		} 
		catch (TicTacToeException e) 
		{
    		Log.e(_className, "(!)", e);    		
    	}
		
		return true;		
		
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus && NullGame.equals(_game))
			this.openOptionsMenu();		
	}
	
	public void onFieldStroked(IGame game, Field field) {
		_drawView.addStroke(field.getPosition(), field.getStroke());		
	}
	
	public void onGameStateChanged(IGame game, GameState gameState) {
		
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

	public void onCurrentStrokeChanged(IGame game, StrokeKind stroke) {
		_currentStrokeDisplay.setText(stroke.toString().toUpperCase());
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
	
	public IGame _game = NullGame.Instance;	
    private DrawView _drawView;	
	private TextView _currentStrokeDisplay;
	private static final String _className = TicTacToe2DActivity.class.getName();
	
	@SuppressWarnings("serial")
	private Map<Integer, ICommandFactory> _commands = Collections.unmodifiableMap(new HashMap<Integer, ICommandFactory>(){
				{
					put(R.id.newGame, new NewGameCommand(TicTacToe2DActivity.this));
					put(R.id.aboutApp, new AboutCommand(TicTacToe2DActivity.this));
				}
			});
	
}