package com.example.amr.chatapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button add_room;
    EditText room_name;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> lis_of_rooms=new ArrayList<String>();

    String name;
    //access database
    DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_room=(Button)findViewById(R.id.button);
        room_name=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lis_of_rooms);
        listView.setAdapter(arrayAdapter);
        request_user_name();
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save in key
                Map<String,Object> map =new HashMap<String, Object>();
                map.put(room_name.getText().toString(),"");
                root.updateChildren(map);

            }
        });
        //but room in listview
        root.addValueEventListener(new ValueEventListener() {
            //this method call when a child born
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<String>();
                //go throw database
                Iterator i=dataSnapshot.getChildren().iterator();
                //read line by line
                while (i.hasNext())
                {
                  set.add(((DataSnapshot)i.next()).getKey());
                }
                lis_of_rooms.clear();
                lis_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent=new Intent(getApplicationContext(),Chat_Room.class);
                intent.putExtra("room name",((TextView)view).getText().toString());
                intent.putExtra("user name",name);
                startActivity(intent);
            }
        });

    }
    public void request_user_name()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name :)");
        final EditText input=new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        name=input.getText().toString();}
                }
        );
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getBaseContext(),"please enter your name :)",Toast.LENGTH_LONG).show();
                dialogInterface.cancel();
                request_user_name();
            }
        });
        builder.show();
    }

}
