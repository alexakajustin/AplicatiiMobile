package com.example.diaconescu_andrei_alexandru_1088;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class VeziLocatii extends AppCompatActivity {

    private ListView listView;
    private static final String JSON_FILE = "locatii.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vezi_locatii);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Locatie locatie = (Locatie)intent.getSerializableExtra(MainActivity.ADD_LOCATIE);
        Button acasa = findViewById(R.id.btnAcasa);
        listView = findViewById(R.id.listView);

        Button btnSalveazaJSON = findViewById(R.id.btnSalveazaJSON);
        Button btnCitesteJSON = findViewById(R.id.btnCitesteJSON);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent modificaIntent = new Intent(VeziLocatii.this, AdaugaForm.class);
            Locatie modifyLocatie = MainActivity.listaLocatii.get(i);
            modificaIntent.putExtra(MainActivity.MODIFY_LOCATIE, modifyLocatie);
            modificaIntent.putExtra(MainActivity.INDEX_LOCATIE, i);
            startActivity(modificaIntent);
        });

        acasa.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(VeziLocatii.this)
                    .setTitle("Confirmare")
                    .setMessage("Sigur doriti sa va intoarceti la meniul ACASA?")
                    .setNegativeButton("NU", (dialogInterface, i) -> {
                    })
                    .setPositiveButton("DA", (dialogInterface, i) -> {
                        Intent intentAcasa = new Intent(VeziLocatii.this, MainActivity.class);
                        startActivity(intentAcasa);
                    }).create();
            dialog.show();
        });


        DatabaseController db = DatabaseController.getInstance(getApplicationContext());
        MainActivity.listaLocatii.clear();
        MainActivity.listaLocatii.addAll(db.getAllLocatii());


        updateListView();

        btnSalveazaJSON.setOnClickListener(v -> saveToJson());
        btnCitesteJSON.setOnClickListener(v -> loadFromJson());
    }

    private void updateListView() {
        if (MainActivity.listaLocatii.size() != 0) {
            CustomAdapter customAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getLayoutInflater(), MainActivity.listaLocatii);
            listView.setAdapter(customAdapter);
        }
    }

    private void saveToJson() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Locatie l : MainActivity.listaLocatii) {
                jsonArray.put(l.toJson());
            }

            File file = new File(getFilesDir(), JSON_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, "JSON salvat cu succes!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Eroare la salvarea JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromJson() {
        try {
            File file = new File(getFilesDir(), JSON_FILE);
            if (!file.exists()) {
                Toast.makeText(this, "Fisierul JSON nu exista!", Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();

            String jsonStr = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonStr);

            MainActivity.listaLocatii.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Locatie l = Locatie.fromJson(obj);
                MainActivity.listaLocatii.add(l);
            }

            updateListView();
            Toast.makeText(this, "JSON incarcat cu succes! " + jsonArray.length() + " locatii.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Eroare la incarcarea JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}