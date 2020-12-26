package com.SB.SBtugar.UtilsFirebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MessagesRecycler extends RecyclerView.Adapter<MessagesRecycler.ViewHolder> {
     ArrayList<MessageModel> ListData;
     Context context;
    public MessagesRecycler(Context context, ArrayList<MessageModel> ListData){
        this.ListData=ListData;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        UserData main_user = ModelController.getInstance().getModel().getUser();
        if (ListData.get(position).isImage()) {

            if (ListData.get(position).getSender_id().equals(String.valueOf(main_user.getId()))) {
                return R.layout.senderchatimage;
            }
            else {
                return R.layout.receiverchatimage;
            }
        }
        else {
            if (ListData.get(position).getSender_id().equals(String.valueOf(main_user.getId()))) {
                return R.layout.senderchat;
            }
            else {
                return R.layout.receiverchat;
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.messageTime.setText(ListData.get(position).getMessage_time());
        if (ListData.get(position).getMessage_time().equals(""))
            holder.messageTime.setVisibility(View.GONE);
        Log.e("GGG",ListData.get(position).getMessage_text());
        if (ListData.get(position).isImage()){
            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

            RequestOptions options =
                    new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child(AllUrls.GetMessageUrlFromMessageId(ListData.get(position).getMessage_id())+".jpg");
            Log.e("value_of_it",storageRef.toString());

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.e("URI", uri.toString());
                    Glide.with(context).load(uri).into(holder.image);


                }
            });


        }
        else
            holder.messageText.setText(ListData.get(position).getMessage_text());
    }

    @Override
    public int getItemCount() {
        return ListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userImage,image;
        TextView messageText,messageTime;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime=itemView.findViewById(R.id.messageTime);
            image = itemView.findViewById(R.id.image_chat);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
