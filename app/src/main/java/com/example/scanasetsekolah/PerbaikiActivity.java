package com.example.scanasetsekolah;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerbaikiActivity extends AppCompatActivity {
    TextView textViewError;
    Button buttonSubmit;
    ProgressBar progressBar;
    ImageView imageView;
    TextInputEditText textInputEditTextResult, textInputEditTextTanggalMinta,textInputEditTextTanggalSelesai,textInputEditTextBiaya,textInputEditTextKeterangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perbaiki);
        textViewError = findViewById(R.id.error);
        textInputEditTextResult = findViewById(R.id.txtResult);
        textInputEditTextBiaya = findViewById(R.id.text_input_biaya);
        textInputEditTextKeterangan = findViewById(R.id.text_input_keterangan);
        textInputEditTextTanggalMinta = findViewById(R.id.text_input_tanggal_minta);
        textInputEditTextTanggalSelesai = findViewById(R.id.text_input_tanggal_selesai);
        buttonSubmit = findViewById(R.id.buttonScan);
        progressBar = findViewById(R.id.loading);
        imageView = findViewById(R.id.image);

        imageView.setOnClickListener(v->{
            scanCode();
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Constant.QR + "perbaiki";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equals("success")) {
                                        Toast.makeText(PerbaikiActivity.this, "Lokasi Aset Berhasil dipindahkan!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PerbaikiActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error handling
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("aset_id", textInputEditTextResult.getText().toString());
                        params.put("tanggal_selesai", textInputEditTextTanggalSelesai.getText().toString());
                        params.put("tanggal_minta", textInputEditTextTanggalMinta.getText().toString());
                        params.put("biaya", textInputEditTextBiaya.getText().toString());
                        params.put("keterangan", textInputEditTextKeterangan.getText().toString());
                        return params;
                    }
                };
                Volley.newRequestQueue(PerbaikiActivity.this).add(stringRequest);
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