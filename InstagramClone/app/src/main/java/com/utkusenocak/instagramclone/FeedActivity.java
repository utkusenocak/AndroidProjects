package com.utkusenocak.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {
    ListView listView;
    PostClass adaptor;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userCommentFromFB;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post){
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listView = findViewById(R.id.listView);

        userEmailFromFB = new ArrayList<>();
        userCommentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();

        adaptor = new PostClass(userEmailFromFB, userCommentFromFB, userImageFromFB, this);
        listView.setAdapter(adaptor);
        getDataFromFireBase();
    }

    public void getDataFromFireBase(){
        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("Posts");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    userEmailFromFB.add(hashMap.get("userEmail"));
                    userCommentFromFB.add(hashMap.get("userComment"));
                    userImageFromFB.add(hashMap.get("downloadUrl"));
                    adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
