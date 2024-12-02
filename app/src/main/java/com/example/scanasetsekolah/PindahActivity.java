package com.example.scanasetsekolah;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.StringRequest;
import com.example.scanasetsekolah.RuanganSpinnerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PindahActivity extends AppCompatActivity {

    Spinner spinnerRuangan;
    ArrayList<Ruangan> ruanganList;
    private int selectedRuanganId;
    private String scanId;

    TextView textViewError,textViewRuangan;
    Button buttonSubmit;
    ProgressBar progressBar;
    ImageView imageView;
    TextInputEditText textInputEditTextResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindah);
        textViewError = findViewById(R.id.error);
        textInputEditTextResult = findViewById(R.id.txtResult);
        buttonSubmit = findViewById(R.id.buttonScan);
        progressBar = findViewById(R.id.loading);
        imageView = findViewById(R.id.image);


        textViewRuangan = findViewById(R.id.textViewRuangan);
        spinnerRuangan = findViewById(R.id.spinner_ruangan);
        ruanganList = new ArrayList<>();

        String url = Constant.LOKASI;//"http://192.168.112.61/api/lokasi";

// Create a new JsonObjectRequest to make a GET request to the API endpoint
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "status" field from the response JSON object
                            String status = response.getString("status");

                            // Check if the request was successful
                            if(status.equals("success")) {
                                // Get the "data" array from the response JSON object
                                JSONArray dataArray = response.getJSONArray("data");

                                // Loop through the "data" array and add each "Ruangan" item to the "ruanganList"
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObj = dataArray.getJSONObject(i);
                                    int id = dataObj.getInt("id");
                                    String nama = dataObj.getString("nama");
                                    ruanganList.add(new Ruangan(id, nama));
                                }

                                // Set the adapter for the spinner with the "ruanganList"
                                RuanganSpinnerAdapter adapter = new RuanganSpinnerAdapter(PindahActivity.this, R.layout.spinner_item, ruanganList);
                                spinnerRuangan.setAdapter(adapter);

                                // Display a toast message indicating success
//                                Toast.makeText(PindahActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                // Display a toast message indicating failure
                                Toast.makeText(PindahActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Display a toast message indicating an error occurred while getting the data
                        Toast.makeText(PindahActivity.this, "Error getting data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

// Add the JsonObjectRequest to the RequestQueue to initiate the request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);




        spinnerRuangan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan item yang dipilih dari Spinner
                Ruangan selectedRuangan = (Ruangan) parent.getItemAtPosition(position);
                selectedRuanganId = selectedRuangan.getId();
                // Melakukan sesuatu dengan item yang dipilih
//                Toast.makeText(PindahActivity.this, "Anda memilih ruangan " + selectedRuangan.getNama(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ketika tidak ada item yang dipilih
            }
        });



        imageView.setOnClickListener(v->{
            scanCode();
        });



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject object = new JSONObject();
                try {
                    //input your API parameters
                    object.put("lokasi_id", selectedRuanganId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = Constant.QR + "/pindah/" + scanId;
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String pesan = jsonObject.getString("message");
                                if (status.equals("success")) {

                                    Toast.makeText(PindahActivity.this, "Lokasi Aset Berhasil dipindahkan!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PindahActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else if (status.equals("failed")) {
                                    progressBar.setVisibility(View.GONE);
                                    textViewError.setVisibility(View.VISIBLE);
                                    textViewError.setText(pesan.toString());
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    textViewError.setVisibility(View.VISIBLE);
                                    textViewError.setText(pesan.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                                textInputEditTextResult.setText("String Response : "+ response.toString());
                        },
                        error -> {
                            textInputEditTextResult.setText("Error getting response");
                        }
                ) {
                    @Override
                    public byte[] getBody() {
                        return object.toString().getBytes(StandardCharsets.UTF_8);
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                requestQueue.add(stringRequest);
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
           scanId =result.getContents().replaceAll("/", "");

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