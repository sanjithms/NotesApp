package com.example.notesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class note_details_activity extends AppCompatActivity {

    EditText notetitle, notecontent;
    ImageButton notedone;
    TextView pagetitile;
    String title, content, doctid;
    boolean iseditmode = false;
    TextView deletenotebutton;

    private static final long CLICK_DELAY_MS = 1000; // Delay in milliseconds
    private long lastClickTime = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        notetitle = findViewById(R.id.notes_title);
        notecontent = findViewById(R.id.notes_content);
        notedone = findViewById(R.id.save_note_button);
        pagetitile = findViewById(R.id.pagetitle);
        deletenotebutton = findViewById(R.id.delete_note);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        doctid = getIntent().getStringExtra("docid");

        if (doctid != null && !doctid.isEmpty()) {
            iseditmode = true;
        }

        notetitle.setText(title);
        notecontent.setText(content);

        if (iseditmode) {
            pagetitile.setText("Edit Your Notes");
            deletenotebutton.setVisibility(View.VISIBLE);
        }

        notedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < CLICK_DELAY_MS) {
                    return; // Ignore clicks that happen within the delay period
                }
                lastClickTime = currentTime;
                v.setEnabled(false); // Disable the button
                savenote();
            }
        });

        deletenotebutton.setOnClickListener((v) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < CLICK_DELAY_MS) {
                return; // Ignore clicks that happen within the delay period
            }
            lastClickTime = currentTime;
            deletefromfirebase();
        });
    }

    private void savenote() {
        String title = notetitle.getText().toString();
        String content = notecontent.getText().toString();
        if (title == null || title.isEmpty()) {
            notetitle.setError("Title is Required");
            notedone.setEnabled(true); // Re-enable the button
            return;
        }
        notemodel nOtemodel = new notemodel();
        nOtemodel.setTitle(title);
        nOtemodel.setContent(content);
        nOtemodel.setTimestamp(Timestamp.now());
        savenotetofirebase(nOtemodel);
    }

    private void savenotetofirebase(notemodel nOtemodel) {
        DocumentReference documentReference;
        if (iseditmode) {
            documentReference = utility.getcollectionrefferencefornotes().document(doctid);
        } else {
            documentReference = utility.getcollectionrefferencefornotes().document();
        }

        documentReference.set(nOtemodel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                notedone.setEnabled(true); // Re-enable the button after operation
                if (task.isSuccessful()) {
                    utility.showtoast(note_details_activity.this, "Notes Added Successfully");
                    finish();
                } else {
                    utility.showtoast(note_details_activity.this, "Failed to add the notes");
                }
            }
        });
    }

    private void deletefromfirebase() {
        DocumentReference documentReference = utility.getcollectionrefferencefornotes().document(doctid);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deletenotebutton.setEnabled(true); // Re-enable the button after operation
                if (task.isSuccessful()) {
                    utility.showtoast(note_details_activity.this, "Notes Deleted Successfully");
                    finish();
                } else {
                    utility.showtoast(note_details_activity.this, "Failed to Delete the notes");
                }
            }
        });
    }
}
