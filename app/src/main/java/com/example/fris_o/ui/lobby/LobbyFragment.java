package com.example.fris_o.ui.lobby;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fris_o.BuildConfig;
import com.example.fris_o.R;
import com.example.fris_o.tools.LobbyVar;

import java.util.ArrayList;

public class LobbyFragment extends Fragment {

    public ArrayList<LobbyVar> lobbyList = new ArrayList<>();
    private int lobbyTotalAmount = 2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lobby, container, false);
        TextView textView;
        Button buttonView;
        ImageView imageView;
        for (int i = 0; i < lobbyTotalAmount; i++)
        {
            String textA = "lobbyText" + (1);
            String buttonA = "lobbyButton" + (1);
            String imageA = "lobbyBar" + (1);
            textView = root.findViewById(getResources().getIdentifier(textA, "id", BuildConfig.APPLICATION_ID+""));
            buttonView = root.findViewById(getResources().getIdentifier(buttonA, "id", BuildConfig.APPLICATION_ID+""));
            imageView = root.findViewById(getResources().getIdentifier(imageA, "id", BuildConfig.APPLICATION_ID+""));
            LobbyVar lobbyVar = new LobbyVar(textView, buttonView, imageView);
            lobbyList.add(lobbyVar);
        }
        //textView.setText(lobbyTotalAmount);
        lobbyList.get(0).SetLobbyName("xDDDDD");
        lobbyList.get(0).GetTextView().setText(lobbyList.get(0).GetLobbyName());
        return root;
    }
}