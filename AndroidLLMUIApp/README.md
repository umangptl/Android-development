# Android LLM UI App
An Android application that allows users to interact with large language models (LLMs) like Gemini and Groq. The app sends user prompts to the selected model and displays the response in real-time. This project utilizes Android’s background threading mechanisms to call APIs.

Features
- LLM Model Selection: Users can choose between Gemini and Groq LLMs using radio buttons.
- Send & Cancel: Users can input a prompt, send it to the LLM, or cancel the operation.
- API Calls: Uses Gemini and Groq APIs to fetch responses in real-time.
- Clean UI: Displays the response in a neatly formatted text box, with scroll support for longer content.
- Error Handling: Gracefully handles errors like API failures and unexpected responses.


Usage
- Choose a model (Gemini or Groq) by selecting the radio button.
- Enter a prompt in the text box.
- Press the Send button to get a response from the LLM.
- The response will appear in the response box, formatted and scrollable for easy reading.
- If needed, cancel the operation using the Cancel button.

<div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px;">
  <img src="https://github.com/umangptl/Android-development/blob/main/AndroidLLMUIApp/Images/Main.png" width="25%" alt="Main-Page">
  <img src="https://github.com/umangptl/Android-development/blob/main/AndroidLLMUIApp/Images/Gemini-hi.png" width="25%" alt="Page">
  <img src="https://github.com/umangptl/Android-development/blob/main/AndroidLLMUIApp/Images/Gemini-id.png" width="25%" alt="Page">
  <img src="https://github.com/umangptl/Android-development/blob/main/AndroidLLMUIApp/Images/Groq-hi.png" width="25%" alt="Page">
  <img src="https://github.com/umangptl/Android-development/blob/main/AndroidLLMUIApp/Images/Groq-id.png" width="25%" alt="Page">
</div>
