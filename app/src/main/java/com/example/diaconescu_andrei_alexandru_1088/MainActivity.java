package com.example.diaconescu_andrei_alexandru_1088;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// hello boys
public class MainActivity extends AppCompatActivity {

    public static final String ADD_LOCATIE = "addLocatie";
    public static final String MODIFY_LOCATIE = "modifyLocatie";
    public static final String INDEX_LOCATIE = "indexLocatie";
    public static List<Locatie> listaLocatii = new ArrayList<>();
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.veziLocatii) {
            Intent intent = new Intent(MainActivity.this, VeziLocatii.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Optiune neimplementata!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize DB and load data
        DatabaseController db = DatabaseController.getInstance(getApplicationContext());
        listaLocatii.clear();
        listaLocatii.addAll(db.getAllLocatii());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        FloatingActionButton faButton = findViewById(R.id.floatingActionButton);
        faButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AdaugaForm.class);
            startActivity(intent);
        });

        Button btnPreiaDate = findViewById(R.id.btnNetwork);
        ListView listView = findViewById(R.id.listView);

        Button btnClearDB = findViewById(R.id.btnClearDB);
        btnClearDB.setOnClickListener(view -> {
            DatabaseController db = DatabaseController.getInstance(MainActivity.this);
            db.deleteAllLocatii();
            MainActivity.listaLocatii.clear();
            CustomAdapter customAdapter = new CustomAdapter(
                    MainActivity.this,
                    android.R.layout.simple_spinner_item,
                    getLayoutInflater(),
                    MainActivity.listaLocatii
            );
            listView.setAdapter(customAdapter);
            Toast.makeText(MainActivity.this, "Database Cleared!", Toast.LENGTH_SHORT).show();
        });

        btnPreiaDate.setOnClickListener(view -> {
            @SuppressLint("StaticFieldLeak") LocatieXML extractXML = new LocatieXML() {
                @Override
                protected void onPostExecute(InputStream inputStream) {
                    super.onPostExecute(inputStream);

                    MainActivity.listaLocatii.clear();
                    MainActivity.listaLocatii.addAll(this.locatieList);

                    DatabaseController db = DatabaseController.getInstance(MainActivity.this);
                    db.deleteAllLocatii();
                    for (Locatie l : this.locatieList) {
                        db.addLocatie(l);
                    }

                    CustomAdapter customAdapter = new CustomAdapter(
                            MainActivity.this,
                            android.R.layout.simple_spinner_item,
                            getLayoutInflater(),
                            MainActivity.listaLocatii
                    );
                    listView.setAdapter(customAdapter);

                    Toast.makeText(MainActivity.this, "Data imported into Database!", Toast.LENGTH_SHORT).show();
                }
            };

            try {
                extractXML.execute(new URL("https://pastebin.com/raw/fkQLUzh0"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "URL invalid!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}