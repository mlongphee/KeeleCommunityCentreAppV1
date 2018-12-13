package com.keele_v11.switchers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.squareup.picasso.Picasso;

/**
 * Created by MJMJ2 on 22/01/17.
 */
public class NewFeedFragment extends Fragment {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;

    public static FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder> firebaseRecyclerAdapterFilter;
    public static FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder> firebaseRecyclerAdapter;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_newsfeed, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewsFeed");
        mDatabase.keepSynced(true);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mBlogList = (RecyclerView) v.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        mBlogList.setLayoutManager(llm);

        notification = new NotificationCompat.Builder(getActivity());
        notification.setAutoCancel(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(MainActivity.user_id != null) {

            mDatabaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                    if(users.first_time){
                        new MaterialDialog.Builder(getActivity())
                                .title("Welcome to the Keele CC App")
                                .content("Keep track on what's going on around the camp all in one place"+"\n" +"\n"+
                                        "News Feed: Get reminders and updates from each camp" +"\n"+
                                        "Events: Keep track of events for each camp"+"\n"+
                                        "What's Up: See whats happening around each camp" +"\n"+
                                        "Lost and Found: Lost an item, well you know where to look")
                                .positiveText("Continue")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dataSnapshot.child(MainActivity.user_id).child("first_time").getRef().setValue(false);
                                    }
                                })
                                .iconRes(R.drawable.keele_icon)
                                .limitIconToDefaultSize().show();


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(MainActivity.filter.equals("All") ) {

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder>(
                    Blog_WhatsUp.class, R.layout.blog_row_news, BlogViewHolder.class, mDatabase) {
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog_WhatsUp model, final int position) {
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setCampName(model.getCamp_name());

                    viewHolder.setTime(model.getTimestamp());
                    //viewHolder.setImage(getActivity(), model.getImage());
                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                try {
                                    if (users.Admin) {
                                        viewHolder.mView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                                        viewHolder.mView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
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
                                }catch (NullPointerException e){
                                    e.printStackTrace();
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

            firebaseRecyclerAdapterFilter = new FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder>(
                    Blog_WhatsUp.class, R.layout.blog_row_news, BlogViewHolder.class, mDatabase.orderByChild("camp_name").equalTo(MainActivity.filter)
            ) {
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder, Blog_WhatsUp model, final int position) {
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setCampName(model.getCamp_name());

                    viewHolder.setTime(model.getTimestamp());


                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                if (users.Admin) {
                                    viewHolder.mView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                                    viewHolder.mView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
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

       /* mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Blog_WhatsUp post = dataSnapshot.getValue(Blog_WhatsUp.class);

                Long timeNow = System.currentTimeMillis();
                Long createdTime = post.getTime();
                Long timeElapsed = timeNow - createdTime;

                if (timeElapsed < 60000L) {
                    notification.setSmallIcon(R.mipmap.ic_launcher);
                    notification.setTicker("");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle(post.getCamp_name() + " posted in NewsFeed");
                    notification.setContentText(post.getDesc());

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setCampName(String name) {
            TextView post_CampName = (TextView) mView.findViewById(R.id.post_camp);
            post_CampName.setText(name);
        }

        public void setTime(String time) {
            TextView post_time = (TextView) mView.findViewById(R.id.time_news);
            post_time.setText(time);
        }

        public void setImage(Context cxt, String image) {

            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            //Picasso.with(cxt).load(image).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
            //Picasso.with(cxt).load(image).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
            Picasso.with(cxt).load(image).into(post_image);

        }
    }


}

