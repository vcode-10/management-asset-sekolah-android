package com.example.scanasetsekolah;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Button buttonScanAset, buttonPerbaikiAset,buttonPindahAset;
    TextView textViewHasilScan,txtResult,textViewError;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonScanAset = findViewById(R.id.scan_aset);
        buttonPerbaikiAset = findViewById(R.id.perbaiki_aset);
        buttonPindahAset = findViewById(R.id.pindah_aset);
        textViewError = findViewById(R.id.error);
        textViewHasilScan = findViewById(R.id.textViewHasilScan);

        sharedPreferences = getSharedPreferences("myAppName", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


        buttonScanAset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                startActivity(intent);
                // kirim hasil scan sebagai extra
//                intent.putExtra("hasil_scan", hasilScan);
            }
        });
        buttonPerbaikiAset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PerbaikiActivity.class);
                startActivity(intent);
            }
        });
        buttonPindahAset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PindahActivity.class);
                startActivity(intent);
            }
        });
    }
}