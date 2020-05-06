package com.example.currentplacedetailsonmap;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.Nullable;

public class ServerFetchService extends IntentService
{
    public static final int SERVER_RESULT_FOUND = 0;
    public static final int SERVER_RESULT_NOTFOUND = 1;
    public static final String SERVER_RESULT = "SERVER_RESULT";
    private String username;
    public ServerFetchService() {
        super("com.example.currentplacedetailsonmap.ServerFetchService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // TODO implement server fetch logic
        username = intent.getStringExtra(StartingActivity.USERNAME);
        Intent broadcastIntent = new Intent("ServerFetchIntent");
        broadcastIntent.putExtra(SERVER_RESULT, SERVER_RESULT_FOUND);
        broadcastIntent.putExtra(StartingActivity.USERNAME, username);
        sendBroadcast(broadcastIntent);
    }
}
