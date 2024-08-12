package com.example.notesapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class utility {
    static  void  showtoast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    static CollectionReference getcollectionrefferencefornotes(){
        FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("Notes").document(firebaseUser.getUid()).collection("My_notes");

    }
    static String timetotimestam(Timestamp timestamp){
         return new SimpleDateFormat("dd/MM/yyyy  HH:mm").format(timestamp.toDate());
    }
}
