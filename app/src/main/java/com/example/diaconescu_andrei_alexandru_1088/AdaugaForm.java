package com.example.diaconescu_andrei_alexandru_1088;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class AdaugaForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adauga_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<String> tipuri = new ArrayList<>();

        tipuri.add("urban");
        tipuri.add("rural");

        EditText nume = findViewById(R.id.numeEditText);
        EditText rating = findViewById(R.id.ratingEditText);
        EditText puncte = findViewById(R.id.puncteEditText);
        EditText adresa = findViewById(R.id.adresaEditText);
        Spinner spinnerTip = findViewById(R.id.spinnerTip);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipuri);

        spinnerTip.setAdapter(adapter);

        Button btnAdauga = findViewById(R.id.btnAdauga);

        Intent modificaIntent = getIntent();
        Locatie modificaLocatie = (Locatie) modificaIntent.getSerializableExtra(MainActivity.MODIFY_LOCATIE);
        Integer indexLocatie = (Integer) modificaIntent.getSerializableExtra(MainActivity.INDEX_LOCATIE);


        if(modificaLocatie != null) {
            btnAdauga.setText("Modifica");
            nume.setText(modificaLocatie.getNume());
            rating.setText(String.valueOf(modificaLocatie.getRating()));
            puncte.setText(String.valueOf(modificaLocatie.getPuncte()));
            adresa.setText(modificaLocatie.getAdresa());
            if(modificaLocatie.getTip().equals("urban")) {
                spinnerTip.setSelection(0);
            }
            else {
                spinnerTip.setSelection(1);
            }
        }

        btnAdauga.setOnClickListener(view -> {


            boolean valid = true;

            if(nume.getText().toString().equals("")) {
                nume.setError("Introduceti un nume valid!");
                valid = false;
            }
            if(rating.getText().toString().equals("")) {
                rating.setError("Va rog introduceti un rating!");
                valid = false;
            }
            if(puncte.getText().toString().equals("")) {
                puncte.setError("Va rog introduceti un punctaj!");
                valid = false;
            }
            if(adresa.getText().toString().equals("")) {
                adresa.setError("Va rog introduceti o adresa valida!");
                valid = false;
            }

            if(valid)
            {
                if(modificaLocatie == null)
                {
                    try {
                        Locatie locatie = new Locatie(adresa.getText().toString(), spinnerTip.getSelectedItem().toString(), Integer.parseInt(puncte.getText().toString()), Float.parseFloat(rating.getText().toString()), nume.getText().toString());
                        
                        // Save to DB
                        DatabaseController db = DatabaseController.getInstance(getApplicationContext());
                        db.addLocatie(locatie);

                        Intent intent = new Intent(AdaugaForm.this, VeziLocatii.class);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Eroare introducere date!\n" + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e("AdaugaForm", "Eroare introducere date!");
                    }
                }
                else {
                    Locatie locatieModificata = MainActivity.listaLocatii.get(indexLocatie);
                    locatieModificata.setAdresa(adresa.getText().toString());
                    locatieModificata.setTip(spinnerTip.getSelectedItem().toString());
                    locatieModificata.setPuncte(Integer.parseInt(puncte.getText().toString()));
                    locatieModificata.setRating(Float.parseFloat(rating.getText().toString()));
                    locatieModificata.setNume(nume.getText().toString());

                    DatabaseController db = DatabaseController.getInstance(getApplicationContext());
                    db.updateLocatie(locatieModificata);

                    Intent intent = new Intent(AdaugaForm.this, VeziLocatii.class);
                    startActivity(intent);
                }
            }
        });
    }
}