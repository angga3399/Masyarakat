package unikom.skripsi.angga.masyarakat.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import unikom.skripsi.angga.masyarakat.activity.MainActivity;
import unikom.skripsi.angga.masyarakat.activity.ShowNotificationActivity;
import unikom.skripsi.angga.masyarakat.config.Config;
import unikom.skripsi.angga.masyarakat.util.NotificationUtils;

public class FirebaseMsgService extends FirebaseMessagingService {
    public FirebaseMsgService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null)
            return;
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            handleDataMessage(json);
        } catch (Exception e) {
            Toast.makeText(MainActivity.getAppContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("title", title);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("image", imageUrl);
                pushNotification.putExtra("timestamp", timestamp);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                Intent resultIntent = new Intent(getApplicationContext(), ShowNotificationActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("image", imageUrl);
                resultIntent.putExtra("timestamp", timestamp);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            }
        } catch (JSONException e) {
            Toast.makeText(MainActivity.getAppContext(), "Json Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.getAppContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}
