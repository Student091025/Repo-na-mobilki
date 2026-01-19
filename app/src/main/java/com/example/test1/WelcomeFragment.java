package com.example.test1;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_welcomeFragment_to_quizFragment)
        );

        return view;
    }
}
