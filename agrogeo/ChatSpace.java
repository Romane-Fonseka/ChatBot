package com.example.agrogeo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatSpace extends AppCompatActivity {

    private RecyclerView ChatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private ArrayList<ChatsModel>chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;

    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_space);
        ChatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEditMsg);
        sendMsgFAB = findViewById(R.id.idFMsend);
        chatsModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ChatsRV.setLayoutManager(manager);
        ChatsRV.setAdapter(chatRVAdapter);


        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ChatSpace.this,"Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
               getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });
    }
    private void getResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=170509&key=EsArWJr8ie2ZsTKx&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/g";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModel> call = retrofitAPI.getMessage(url);
        call.equence(new Callback<MsgModel>(){
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response){
                if (response.isSuccessful()){
                    MsgModel model = response.body();
                    chatsModelArrayList.add(new ChatsModel(model.getCnt().BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t){
                chatsModelArrayList.add(new ChatsModel("PLEASE REVERT YOUR QUESTION",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }
}