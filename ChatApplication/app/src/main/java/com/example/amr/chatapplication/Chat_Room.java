package com.example.amr.chatapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Amr on 11/2/2016.
 */
public class Chat_Room extends AppCompatActivity {
    Button b;
    EditText editText;
    TextView textView;
    String user_name,room_name,key,chat_msg,chat_user_name;
    DatabaseReference root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        b=(Button)findViewById(R.id.btn_send);
        editText=(EditText)findViewById(R.id.editText2);
        textView=(TextView)findViewById(R.id.textView);
        user_name=getIntent().getExtras().get("user name").toString();
        room_name=getIntent().getExtras().get("room name").toString();
        setTitle("Room :"+room_name);
        root= FirebaseDatabase.getInstance().getReference().child(room_name);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map =new HashMap<String, Object>();
                key=root.push().getKey();
                root.updateChildren(map);
                DatabaseReference message_root=root.child(key);
                Map<String,Object> map2 =new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",editText.getText().toString());
                message_root.updateChildren(map2);

            }
        });
      root.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           append_chat_conversation(dataSnapshot);
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              append_chat_conversation(dataSnapshot);
          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
        chat_msg= (String) ((DataSnapshot)i.next()).getValue();
        chat_user_name= (String) ((DataSnapshot)i.next()).getValue();
         textView.append(chat_user_name+":"+chat_msg+" \n");

        }
    }
}
