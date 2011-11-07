package lukianol.tictactoe.in2d;

import lukianol.tictactoe.Field;
import lukianol.tictactoe.Game;
import lukianol.tictactoe.GameEventListener;
import lukianol.tictactoe.GameState;
import lukianol.tictactoe.IGame;
import lukianol.tictactoe.Position;
import lukianol.tictactoe.StrokeKind;
import lukianol.tictactoe.TicTacToeException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TicTacToe2DActivity extends Activity 
	implements DrawView.OnPositionTouchEventListener
	, GameEventListener {
        
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

	public void onFieldStroked(IGame game, Field field) {
		_drawView.addStroke(field.getPosition(), field.getStroke());		
	}
	
	public void onGameStateChanged(IGame game, GameState gameState) {
		
		switch(gameState){
		
		case Playing:
			_drawView.clearSurface();
			break;
	
		case Won:
			_drawView.setWonStrokePositions(game.getWonPositions());
			break;
		}
		
	}

	public void onCurrentStrokeChanged(IGame game, StrokeKind stroke) {
		_currentStrokeDisplay.setText(stroke.toString().toUpperCase());
	}	
	
    private void initializeActivity(){
    	
    	setContentView(R.layout.main);
        _currentStrokeDisplay = (TextView)findViewById(R.id.stroke);
        Button newGame = (Button)findViewById(R.id.newGame); 
        
    	newGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				createNewGame();
			}
		});
    	
    	_drawView = new DrawView(this, playgroundSize);         
        _drawView.setOnPositionTouchEventListener(this);
    	
    	createNewGame();
    	    	
    	ViewGroup layout = (ViewGroup)findViewById(R.id.root);
        layout.addView(_drawView, 0);
    }
           
    private void createNewGame() {
    	
    	if (_game != null)
			_game.dispose();   
    	
		_game = new Game(this, playgroundSize);		
    }
    
    private DrawView _drawView;
	private Game _game;
	private TextView _currentStrokeDisplay;
	private static final String _className = TicTacToe2DActivity.class.getName();
	private static final int playgroundSize = Game.DefaultPlaygroundSize;
	
}