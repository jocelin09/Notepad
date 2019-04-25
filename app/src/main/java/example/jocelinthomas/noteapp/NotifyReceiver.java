package example.jocelinthomas.noteapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import example.jocelinthomas.noteapp.callback.NoteListener;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jocelinthomas on 13/04/19.
 */

public class NotifyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,NotesFragment.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle("Title")
                .setContentText("Body")
                .setSmallIcon(R.drawable.ic_action_edit)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setAutoCancel(true);

        notificationManager.notify(100,notification.build());

        Toast.makeText(context, "Receiver class", Toast.LENGTH_LONG).show();
    }


}
