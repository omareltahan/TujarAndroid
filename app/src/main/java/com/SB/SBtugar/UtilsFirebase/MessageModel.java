package com.SB.SBtugar.UtilsFirebase;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public class MessageModel implements Serializable {
    String message_id = "message_id",sender_id = "sender_id",message_time = "",message_text="";
    boolean isImage = false;
    public String getMessage_id() {
        return message_id;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public MessageModel(DataSnapshot dataSnapshot){
        message_text = dataSnapshot.child("Message_Text").getValue().toString();
        message_time = dataSnapshot.child("Message_Time").getValue().toString();
        if (dataSnapshot.hasChild("Sender_Id"))
            sender_id = dataSnapshot.child("Sender_Id").getValue().toString();
        else {
            UserData main_user = ModelController.getInstance().getModel().getUser();

            sender_id = String.valueOf(main_user.getId());
        }
        if (dataSnapshot.hasChild("isImage"))
            isImage = Boolean.parseBoolean(dataSnapshot.child("isImage").getValue().toString());
        else
            isImage = false;
        message_id = dataSnapshot.getKey();
    }
    public MessageModel(){}
}
