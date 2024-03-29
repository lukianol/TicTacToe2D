package lukianol.tictactoe.in2d;

import lukianol.tictactoe.Game;
import lukianol.tictactoe.GameBase;

public final class NewGameCommand extends ActivityCommand<TicTacToe2DActivity> {

	public NewGameCommand(TicTacToe2DActivity activity) {
		super(activity);
	}

	@Override
	public void Execute() {
		TicTacToe2DActivity activity = this.getActivity();
		GameBase game = activity._game;
		game.dispose();
		activity._game = new Game(activity, TicTacToe2DActivity.playgroundSize);		
	}

}
