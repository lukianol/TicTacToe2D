package lukianol.tictactoe.in2d;

import android.app.Activity;

public abstract class ActivityCommand<T extends Activity> implements ICommand, ICommandFactory {

	private final T _activity;
	
	protected T getActivity(){
		return _activity;
	}
	
	protected  ActivityCommand(T activity){
		_activity = activity;
	}
	
	public final ICommand CreateCommand(){
		return this;
	}

	public abstract void Execute();
}
