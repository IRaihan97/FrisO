package com.example.fris_o.tools;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

// Custom Variable to hold the lobby sessions

public class LobbyVar {
    private String lobbyName = "null";
    private TextView lobbyNameText;
    private int currentPlayers = 0;
    private int maxPlayers = 2;
    private Button lobbyButton;
    private ImageView lobbyImage;
    private Boolean beingUsed = false;
    private Boolean onGoing = false;

    // Constructor
    public LobbyVar(TextView nameText, Button Button, ImageView Image){
        lobbyNameText = nameText;
        lobbyButton = Button;
        lobbyImage = Image;
    }

    public void SetLobbyName (String name){
        lobbyName = name;
    }
    public String GetLobbyName (){
        return  lobbyName;
    }

    public void SetCurrentPlayers (int num){
        currentPlayers = num;
    }
    public int GetCurrentPlayers (){
        return currentPlayers;
    }

    public void SetMaxPlayers (int num){
        maxPlayers = num;
    }
    public int GetMaxPlayers (){
        return maxPlayers;
    }

    public void SetBool (boolean isIt){
        beingUsed = isIt;
    }
    public boolean GetBool (){
        return beingUsed;
    }

    public void SetPlaying (boolean isIt){
        onGoing = isIt;
    }
    public boolean GetPlaying (){
        return onGoing;
    }

    public TextView GetTextView (){
        return lobbyNameText;
    }
    public Button GetButton (){
        return lobbyButton;
    }
    public ImageView GetImageView (){
        return lobbyImage;
    }
}
