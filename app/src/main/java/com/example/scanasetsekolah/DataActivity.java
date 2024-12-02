package com.example.scanasetsekolah;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class DataActivity extends AppCompatActivity {
    TextView textViewError;
    Button buttonSubmit;
    ProgressBar progressBar;
    ImageView imageView;
    String toko,url;
    TextInputEditText textInputEditTextResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        textViewError = findViewById(R.id.error);
        textInputEditTextResult = findViewById(R.id.txtResult);
        buttonSubmit = findViewById(R.id.buttonScan);
        progressBar = findViewById(R.id.loading);
        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(v->{
            scanCode();
        });

    buttonSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = Constant.QR + "/" + textInputEditTextResult.getText().toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // baca data dari respons JSON
                                String status = response.getString("status");
                                if (status.equals("success")){
                                    JSONObject data = response.getJSONObject("data");
                                    String nama = data.getString("nama");
                                    String tipe = data.getString("tipe_aset_id");
                                    String lokasi = data.getString("lokasi");
                                    String kondisi = data.getString("kondisi_id");
                                    String statusAset = data.getString("status");
                                    String keterangan = data.getString("keterangan");

                                    // tampilkan data di TextView
                                    String resultText = "Nama: " + nama + "\n"
                                            + "Tipe Aset: " + tipe + "\n"
                                            + "Lokasi: " + lokasi + "\n"
                                            + "Kondisi: " + kondisi + "\n"
                                            + "Status Aset: " + statusAset + "\n"
                                            + "Keterangan: " + keterangan + "\n";
                                    Toast.makeText(getApplicationContext(),"Data Aset Ditemukan!",Toast.LENGTH_LONG).show();
                                    textInputEditTextResult.setText(resultText);
                                } else {
                                    String pesan = response.getString("message");
                                    textViewError.setVisibility(View.VISIBLE);
                                    textViewError.setText(pesan.toString());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Error getting response");
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    });

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Tombol atas hidup flash!");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CamptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents()!=null){
            textInputEditTextResult.setText(result.getContents().replaceAll("/", ""));

        }
    });
}