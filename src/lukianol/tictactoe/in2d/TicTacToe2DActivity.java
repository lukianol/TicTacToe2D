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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TicTacToe2DActivity extends Activity implements DrawView.OnPositionTouchEventListener,
GameEventListener {
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        init();
    }
    
    private void init(){
    	
    	setContentView(R.layout.main);        
                
        _stroke = (TextView)findViewById(R.id.stroke);
        
        Button newGame = (Button)findViewById(R.id.newGame); 
        final TicTacToe2DActivity _this = this;
    	newGame.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (_game != null)
					_game.removeGameEventListener(_this);
				_game = new Game(_this, playGroundSize);
				drawView.clearSurface();
			}
		});
    	
    	_game = new Game(this, playGroundSize);
        drawView = new DrawView(this, playGroundSize, false); 
        drawView.setOnPositionTouchEventListener(this);      
        LinearLayout layout = (LinearLayout)findViewById(R.id.root);
        layout.addView(drawView);
    }
    
    public Boolean OnPositionTouch(View view, Position position) {
		try {
			
			StrokeKind stroke = _game.getCurrentStroke();
			_game.Stroke(position);
			drawView.addStroke(position, stroke);
			return true;
			
		} catch (TicTacToeException e) {
    	
    		e.printStackTrace();
    		return false;
    	}
		
	}
	
	public void GameStateChanged(IGame game, GameState gameState) {
		
		switch(gameState){
			case Won:
				Field[] fields = game.getWonFields();
				Position[] positions = new Position[fields.length];
				int index = 0;
				for(Field field : game.getWonFields()){
					positions[index++] = field.getPosition();
				}
				drawView.setWonStrokePositions(positions);
				break;
		}
		
	}

	public void CurrentStrokeChanged(IGame game, StrokeKind stroke) {
		_stroke.setText(stroke.toString().toUpperCase());
	}	

	private DrawView drawView;
	private Game _game;
	private final int playGroundSize = 3;
	private TextView _stroke;
}