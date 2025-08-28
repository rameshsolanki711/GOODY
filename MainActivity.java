package com.example.technosupertts;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private Button convertButton;
    private ProgressBar progressBar;
    
    // Your Eleven Labs API details
    private static final String ELEVEN_LABS_API_KEY = "sk_17e87f71bab1eef11f99db1704512f8d1389d7ef726df669";
    private static final String VOICE_ID = "1qEiC6qsybMkmnNdVMbK";
    private static final String ELEVEN_LABS_URL = "https://api.elevenlabs.io/v1/text-to-speech/" + VOICE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        convertButton = findViewById(R.id.convertButton);
        progressBar = findViewById(R.id.progressBar);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputText.getText().toString().trim();
                if (!text.isEmpty()) {
                    convertTextToSpeech(text);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter some text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void convertTextToSpeech(String text) {
        progressBar.setVisibility(View.VISIBLE);
        convertButton.setEnabled(false);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"text\": \"" + text + "\", \"voice_settings\": {\"stability\": 0.5, \"similarity_boost\": 0.5}}";
        
        Request request = new Request.Builder()
                .url(ELEVEN_LABS_URL)
                .post(RequestBody.create(mediaType, requestBody))
                .addHeader("xi-api-key", ELEVEN_LABS_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        convertButton.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        convertButton.setEnabled(true);
                    }
                });

                if (response.isSuccessful()) {
                    // Handle the audio response here
                    // You would need to implement audio playback
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Text converted successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Conversion failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}