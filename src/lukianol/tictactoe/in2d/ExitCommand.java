package lukianol.tictactoe.in2d;

public final class ExitCommand extends ActivityCommand<TicTacToe2DActivity> {

	public ExitCommand(TicTacToe2DActivity activity) {
		super(activity);		
	}

	@Override
	public void Execute() {
		this.getActivity().finish();		
	}

}
