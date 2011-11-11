package lukianol.tictactoe.in2d;

import android.app.AlertDialog;
import android.content.DialogInterface;

public final class AboutCommand extends ActivityCommand<TicTacToe2DActivity> {

	public AboutCommand(TicTacToe2DActivity activity) {
		super(activity);		
	}

	@Override
	public void Execute() {
		TicTacToe2DActivity activity = this.getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(R.string.aboutApp);
		builder.setMessage(String.format("%s was created by %s", activity.getString(R.string.app_name), activity.getString(R.string.author)));
		builder.setPositiveButton(":)", new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();						
			}
		});
		builder.create().show();
	}

}
