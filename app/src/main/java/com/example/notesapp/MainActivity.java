package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addonbtn;
    RecyclerView recyclerView;
    ImageButton menubtn;
    noteAdapter nOteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
                // User is logged in but email is not verified
                Toast.makeText(MainActivity.this, "Please verify your email to continue.", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        } else {
            // No user is logged in, redirect to LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        addonbtn=findViewById(R.id.add_note_button);
        menubtn=findViewById(R.id.menu_note_button);
        recyclerView=findViewById(R.id.recyler_view);
        addonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,note_details_activity.class));
            }
        });

        menubtn.setOnClickListener((v)->showmenu());
        setrecylerview();
    }

    private void setrecylerview() {
        Query query=utility.getcollectionrefferencefornotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<notemodel> options=new FirestoreRecyclerOptions.Builder<notemodel>()
                .setQuery(query,notemodel.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nOteAdapter=new noteAdapter(options,this);
        recyclerView.setAdapter(nOteAdapter);
    }

    private void showmenu() {
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menubtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        nOteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        nOteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nOteAdapter.notifyDataSetChanged();
    }
}