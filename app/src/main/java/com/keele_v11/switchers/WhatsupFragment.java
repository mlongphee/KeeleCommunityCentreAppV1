package com.keele_v11.switchers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keele_v11.Blog_WhatsUp;
import com.keele_v11.MainActivity;
import com.keele_v11.R;
import com.keele_v11.Users;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by MJMJ2 on 22/01/17.
 */
public class WhatsupFragment extends Fragment {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_whatsup,container,false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Camp");
        mDatabase.keepSynced(true);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mBlogList = (RecyclerView) v.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        mBlogList.setLayoutManager(llm);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(MainActivity.filter.equals("All")) {
            FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder>(
                    Blog_WhatsUp.class, R.layout.blog_row, BlogViewHolder.class, mDatabase) {
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, Blog_WhatsUp model, final int position) {

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity(), model.getImage());
                    viewHolder.setCampName(model.getCamp_name());

                    viewHolder.setTime(model.getTimestamp());

                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                if (users.Admin) {
                                    viewHolder.mView.findViewById(R.id.delete_buttonWU).setVisibility(View.VISIBLE);
                                    viewHolder.mView.findViewById(R.id.delete_buttonWU).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (MainActivity.user_id != null) {

                                                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                                        if (users.Admin) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                            builder.setTitle("Delete Post");
                                                            builder.setMessage("Are you sure?");

                                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    getRef(position).removeValue();

                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            };

            mBlogList.setAdapter(firebaseRecyclerAdapter);
        } else {
            FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder> firebaseRecyclerAdapterFilter = new FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder>(
                    Blog_WhatsUp.class, R.layout.blog_row, BlogViewHolder.class, mDatabase.orderByChild("camp_name").equalTo(MainActivity.filter)){
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, Blog_WhatsUp model, final int position) {

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity(), model.getImage());
                    viewHolder.setCampName(model.getCamp_name());

                    viewHolder.setTime(model.getTimestamp());

                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                if (users.Admin) {
                                    viewHolder.mView.findViewById(R.id.delete_buttonWU).setVisibility(View.VISIBLE);
                                    viewHolder.mView.findViewById(R.id.delete_buttonWU).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (MainActivity.user_id != null) {

                                                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                                        if (users.Admin) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                                            builder.setTitle("Delete Post");
                                                            builder.setMessage("Are you sure?");

                                                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    getRef(position).removeValue();

                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            };

            mBlogList.setAdapter(firebaseRecyclerAdapterFilter);
        }
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title)
        {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc)
        {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(final Context cxt,final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            //Picasso.with(cxt).load(image).resize(1280,1280).into(post_image);
            Picasso.with(cxt).load(image).networkPolicy(NetworkPolicy.OFFLINE).resize(1280, 1280).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).resize(1280,1280).into(post_image);

                }
            });

        }
        public void setCampName(String name){
            TextView post_CampName = (TextView) mView.findViewById(R.id.campTitle_whatsup);
            post_CampName.setText(name);
        }

        public void setTime(String time){
            TextView post_time = (TextView) mView.findViewById(R.id.timeStamp_whatsup);
            post_time.setText(time);
        }
    }
}

