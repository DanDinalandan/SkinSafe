package com.example.skinsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceInputHelper {

    private static final String TAG = "VoiceInputHelper";

    private final Context context;
    private SpeechRecognizer speechRecognizer;
    private VoiceCallback callback;
    private final ArrayList<String> accumulatedSegments = new ArrayList<>();

    public interface VoiceCallback {
        /**
         * @param spokenText
         * @param fullText
         */
        void onResult(String spokenText, String fullText);
        void onError(String errorMessage);
        void onReadyForSpeech();
        void onEndOfSpeech();
    }

    public VoiceInputHelper(Context context) {
        this.context = context;
    }

    public static boolean isAvailable(Context context) {
        return SpeechRecognizer.isRecognitionAvailable(context);
    }

    public void startListening(VoiceCallback callback) {
        this.callback = callback;

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            callback.onError("Speech recognition is not available on this device.");
            return;
        }

        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "Ready for speech");
                if (VoiceInputHelper.this.callback != null)
                    VoiceInputHelper.this.callback.onReadyForSpeech();
            }

            @Override public void onBeginningOfSpeech() { Log.d(TAG, "Speech begun"); }

            @Override public void onRmsChanged(float rmsdB) { }

            @Override public void onBufferReceived(byte[] buffer) { }

            @Override public void onEndOfSpeech() {
                Log.d(TAG, "End of speech");
                if (VoiceInputHelper.this.callback != null)
                    VoiceInputHelper.this.callback.onEndOfSpeech();
            }

            @Override public void onError(int error) {
                String msg = speechErrorToMessage(error);
                Log.e(TAG, "Speech error: " + msg + " (code " + error + ")");
                if (VoiceInputHelper.this.callback != null)
                    VoiceInputHelper.this.callback.onError(msg);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches == null || matches.isEmpty()) {
                    if (VoiceInputHelper.this.callback != null)
                        VoiceInputHelper.this.callback.onError("Could not understand speech. Please try again.");
                    return;
                }

                String best = matches.get(0);
                Log.d(TAG, "Voice result: " + best);

                String normalised = best
                        .replaceAll("(?i)\\band\\b", ",")
                        .replaceAll("\\s{2,}", " ")
                        .trim();

                accumulatedSegments.add(normalised);

                String fullText = String.join(", ", accumulatedSegments);
                Log.d(TAG, "Accumulated text: " + fullText);

                if (VoiceInputHelper.this.callback != null)
                    VoiceInputHelper.this.callback.onResult(normalised, fullText);
            }

            @Override public void onPartialResults(Bundle partialResults) { }

            @Override public void onEvent(int eventType, Bundle params) { }
        });

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say ingredient names. Pause between each name.");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        // Give the user 3s of silence before cutting off
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000L);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.startListening(intent);
    }

    public void stopListening() {
        if (speechRecognizer != null) speechRecognizer.stopListening();
    }
    public String getAccumulatedText() {
        return String.join(", ", accumulatedSegments);
    }
    public void clearAccumulated() {
        accumulatedSegments.clear();
        Log.d(TAG, "Accumulated voice segments cleared.");
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        callback = null;
    }

    private String speechErrorToMessage(int error) {
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:                  return "Audio recording error. Check microphone.";
            case SpeechRecognizer.ERROR_CLIENT:                 return "Client side error.";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: return "Microphone permission not granted.";
            case SpeechRecognizer.ERROR_NETWORK:                return "Network error. Check your connection.";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:        return "Network timeout.";
            case SpeechRecognizer.ERROR_NO_MATCH:               return "No speech match. Try speaking more clearly.";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:        return "Recognizer is busy. Try again.";
            case SpeechRecognizer.ERROR_SERVER:                 return "Server error.";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:         return "No speech detected. Tap mic and try again.";
            default:                                            return "Speech error (code " + error + ").";
        }
    }
}