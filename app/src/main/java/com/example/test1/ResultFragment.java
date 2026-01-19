package com.example.test1;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);

        int score = getArguments().getInt("score");
        int total = getArguments().getInt("total");

        TextView tvResult = view.findViewById(R.id.tvResult);
        tvResult.setText("Twój wynik: " + score + " / " + total);

        Button btnRestart = view.findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_resultFragment_to_welcomeFragment)
        );

        return view;
    }
}
