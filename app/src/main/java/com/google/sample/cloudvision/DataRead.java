package com.google.sample.cloudvision;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.DatabaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRead extends AppCompatActivity {

    DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "MainActivity";
    ListView listView1;
    String result;
    List<String> stores = new ArrayList<>();
    List<String> itemType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        result = intent.getStringExtra("key");
        setContentView(R.layout.activity_data_read);
        listView1 = findViewById(R.id.listView1);
        fillStores();

        readfromDatabse(result);

    }


    public void fillStores() {
        stores.add("Aldi's");
        stores.add("Walmart");
        stores.add("Target");
        //stores.add("Costco");
    }


    public void readfromDatabse(final String typeOfItem) {


            mDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean present = false;
                    for (String s : stores) {
                        if(dataSnapshot.child(s).hasChild(typeOfItem)) {
                            itemType.add(s + "\n Category: " + typeOfItem + "");
                            itemType.addAll((List<String>) dataSnapshot.child(s).child(typeOfItem).getValue());
                            present = true;
                        }
                    }
                    if(present == false)
                        itemType.add("Item category not available");
                    System.out.println(itemType);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DataRead.this,
                           android.R.layout.simple_list_item_1, itemType);
                    listView1.setAdapter(adapter);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }

            });


    }
}
