package com.example.test1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.*;

import androidx.navigation.fragment.NavHostFragment;


public class QuizFragment extends Fragment {

    private TextView tvQuestion;
    private RadioGroup rgAnswers;
    private Button btnNext;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        rgAnswers = view.findViewById(R.id.rgAnswers);
        btnNext = view.findViewById(R.id.btnNext);

        loadQuiz();

        btnNext.setOnClickListener(v -> checkAnswerAndNext());

        return view;
    }

    private void loadQuiz() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/quiz/1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                Gson gson = new Gson();
                QuizResponse quiz = gson.fromJson(json, QuizResponse.class);

                requireActivity().runOnUiThread(() -> {
                    questions = quiz.questions;
                    showQuestion();
                });
            }
        });
    }

    private void showQuestion() {
        rgAnswers.removeAllViews();

        Question q = questions.get(currentIndex);
        tvQuestion.setText(q.questionText);

        for (Answer a : q.answers) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(a.text);
            rb.setTag(a.answerId);
            rgAnswers.addView(rb);
        }
    }

    private void checkAnswerAndNext() {
        int checkedId = rgAnswers.getCheckedRadioButtonId();
        if (checkedId == -1) return;

        RadioButton selected = rgAnswers.findViewById(checkedId);
        int answerId = (int) selected.getTag();

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        String json = gson.toJson(Collections.singletonMap("answerId", answerId));

        RequestBody body = RequestBody.create(
                json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://10.0.2.2:5000/api/check-answer")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject obj = gson.fromJson(response.body().string(), JsonObject.class);

                if (obj.get("correct").getAsBoolean()) {
                    score++;
                }

                requireActivity().runOnUiThread(() -> nextQuestion());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void nextQuestion() {
        currentIndex++;

        if (currentIndex < questions.size()) {
            showQuestion();
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("score", score);
            bundle.putInt("total", questions.size());

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_quizFragment_to_resultFragment, bundle);
        }
    }

}
