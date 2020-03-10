package com.example.fris_o.ui.lobby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.fris_o.R;

public class LobbyFragment extends Fragment {

    private LobbyViewModel lobbyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        lobbyViewModel =
                ViewModelProviders.of(this).get(LobbyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lobby, container, false);
        final TextView textView = root.findViewById(R.id.text_lobby);
        lobbyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}