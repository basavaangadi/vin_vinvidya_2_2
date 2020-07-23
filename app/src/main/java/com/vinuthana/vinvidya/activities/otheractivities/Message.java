package com.vinuthana.vinvidya.activities.otheractivities;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.vinuthana.vinvidya.R;
import com.vinuthana.vinvidya.adapters.ChatArrayAdapter;
import com.vinuthana.vinvidya.adapters.ChatMessage;

public class Message extends Activity {
    private static final String TAG = "ChatActivity";
    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    SwipeRefreshLayout SRLayout;
    private Button send;
    Intent intent;
    private boolean side = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_message);
        send = (Button) findViewById(R.id.btn);
        list = (ListView) findViewById(R.id.listview);
        SRLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.adapter_chat_array);
        list.setAdapter(adp);
        chatText = (EditText) findViewById(R.id.chat_text);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
                //recieveChatMessage();
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);
        adp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged(){
                super.onChanged();
                list.setSelection(adp.getCount() -1);
            }
        });
        SRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SRLayout.setRefreshing(true);
                recieveChatMessage();
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(list != null && list.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                SRLayout.setEnabled(enable);
            }});
    }
    private boolean sendChatMessage(){
        adp.add(new ChatMessage(side, chatText.getText().toString()));
        // chatText.setText("");
        // side = !side;
        return true;
    }

    private boolean recieveChatMessage(){
        adp.add(new ChatMessage(true, chatText.getText().toString()));
        chatText.setText("");
        // side = !side;
        return true;
    }
}