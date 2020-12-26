package com.SB.SBtugar.UtilsFirebase;

public class AllUrls {


    public static String GetChatAllMessagesUrlById(String chat_id){
        return  "All_Massage_Of_Chat/"+chat_id;
    }
    public static String ListenToChatLastMessage(String chat_id){
        return  "AllUsers/"+chat_id+"/CurrentChats/LastText";
    }
    public static String ListenToVendorChatLastMessage(String vendor_id, String chat_id){
        return  "AllVendors/"+vendor_id+"/"+chat_id;
    }
    public static String ListenToCurrentChat(String user_id){
        return  "AllUsers/"+user_id+"/CurrentChats";
    }
    public static String ListenToPreviousChat(String user_id){
        return  "AllUsers/"+user_id+"/Chats";
    }

    public static String GetMessageUrlFromMessageId(String message_id){
        return  "All_Messages/" + message_id;
    }

}
