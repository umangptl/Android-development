package com.example.androidllmuiapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private EditText promptInput;
    private TextView responseBox;
    private ProgressBar progressBar;
    private String selectedModel = "Gemini";  // Default model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        promptInput = findViewById(R.id.prompt_input);
        responseBox = findViewById(R.id.response_box);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Set up Radio Button Listener for model selection
        RadioButton geminiRadio = findViewById(R.id.radio_gemini);
        RadioButton groqRadio = findViewById(R.id.radio_groq);

        geminiRadio.setOnClickListener(v -> selectedModel = "Gemini");
        groqRadio.setOnClickListener(v -> selectedModel = "Groq");

        // Button to send prompt
        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener(this::sendPrompt);
    }

    // Method for Send Button
    public void sendPrompt(View view) {
        String prompt = promptInput.getText().toString();
        Log.d("MainActivity", "Send button clicked. Prompt: " + prompt);

        // Execute the AsyncTask to make the API call
        new LLMTask().execute(prompt, selectedModel);
    }

    // Method to load the appropriate API key based on the selected model
    private String loadApiKey(String model) {
        String apiKey = "";
        try {
            Properties properties = new Properties();
            InputStream inputStream = getAssets().open("secrets.properties");
            properties.load(inputStream);

            // Load the correct API key based on the selected model
            if (model.equals("Gemini")) {
                apiKey = properties.getProperty("GEMINI_API_KEY");
            } else if (model.equals("Groq")) {
                apiKey = properties.getProperty("GROQ_API_KEY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    // Background task for API calls
    private class LLMTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            responseBox.setText("");  // Clear response box before making the request
        }

        @Override
        protected String doInBackground(String... params) {
            String prompt = params[0];
            String model = params[1];
            String apiKey = loadApiKey(model);
            String response = "";

            try {
                if (model.equals("Gemini")) {
                    response = callGeminiApi(prompt, apiKey);
                } else if (model.equals("Groq")) {
                    response = callGroqApi(prompt, apiKey);
                }
            } catch (Exception e) {
                Log.e("LLMTask", "Error in API call: " + e.getMessage());
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);

            // Log the raw response for debugging
            Log.d("LLMTask", "Raw API response: " + result);

            if (result.isEmpty()) {
                responseBox.setText("No response from server.");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    // Check if the response is from Gemini (candidates array present)
                    if (jsonObject.has("candidates")) {
                        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
                        StringBuilder formattedText = new StringBuilder();

                        for (int i = 0; i < candidatesArray.length(); i++) {
                            JSONObject candidate = candidatesArray.getJSONObject(i);

                            // Access the nested "content" field and its "parts"
                            JSONObject contentObject = candidate.getJSONObject("content");
                            JSONArray partsArray = contentObject.getJSONArray("parts");

                            for (int j = 0; j < partsArray.length(); j++) {
                                JSONObject part = partsArray.getJSONObject(j);
                                formattedText.append(part.getString("text")).append("\n\n");
                            }
                        }

                        // Display formatted text for Gemini response
                        responseBox.setText(formattedText.toString());

                        // Check if the response is from Groq (choices array present)
                    } else if (jsonObject.has("choices")) {
                        JSONArray choicesArray = jsonObject.getJSONArray("choices");

                        if (choicesArray.length() > 0) {
                            // Get the first choice (index 0)
                            JSONObject firstChoice = choicesArray.getJSONObject(0);

                            // Get the 'message' object inside the first choice
                            JSONObject messageObject = firstChoice.getJSONObject("message");

                            // Extract the 'content' which holds the assistant's response
                            String assistantResponse = messageObject.getString("content");

                            // Display the formatted response for Groq
                            responseBox.setText(assistantResponse.trim());
                            Log.d("LLMTask", "Response displayed: " + assistantResponse.trim());
                        } else {
                            responseBox.setText("No valid response received from the model.");
                            Log.e("LLMTask", "No choices available in the response.");
                        }

                    } else {
                        responseBox.setText("Unexpected response format.");
                    }

                } catch (Exception e) {
                    Log.e("LLMTask", "Error parsing response: " + e.getMessage());
                    responseBox.setText("Error parsing response.");
                    e.printStackTrace();
                }
            }
            Log.d("LLMTask", "Response displayed: " + result);
        }


        // Gemini API call
        private String callGeminiApi(String prompt, String apiKey) throws Exception {
            URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "utf-8"));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line.trim());
                }
                reader.close();
                return responseBuilder.toString();
            } else {
                Log.e("LLMTask", "Gemini API call failed with response code: " + conn.getResponseCode());
                return "{\"error\": \"Gemini API error: " + conn.getResponseMessage() + "\"}";
            }
        }

        // Method to call Groq API
        private String callGroqApi(String prompt, String apiKey) throws Exception {
            Log.d("LLMTask", "Sending request to Groq API");

            URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);  // Authorization header
            conn.setDoOutput(true);

            // Create the JSON payload for Groq (similar to OpenAI's chat model)
            JSONObject jsonPayload = new JSONObject();
            JSONArray messagesArray = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messagesArray.put(userMessage);
            jsonPayload.put("messages", messagesArray);
            jsonPayload.put("model", "llama3-8b-8192");

            Log.d("LLMTask", "Payload: " + jsonPayload.toString());

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                return content.toString();
            } else {
                Log.e("LLMTask", "Groq API call failed with response code: " + responseCode);
                return "{\"error\": \"Groq API error: Not Found\"}";
            }
        }
    }
}