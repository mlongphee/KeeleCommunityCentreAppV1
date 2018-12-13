package com.keele_v11.switchers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
public class LostnFoundFragment extends Fragment {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;

    private static Animator mCurrentAnimator;
    private static int mShortAnimationDuration;

    private static ImageView expandedImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lostn_found, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("LostnFound");
        mDatabase.keepSynced(true);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mBlogList = (RecyclerView) v.findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this.getActivity(), 3);
        //glm.setOrientation(LinearLayoutManager.VERTICAL);
        //glm.setReverseLayout(true);
        //glm.setStackFromEnd(true);
        mBlogList.setLayoutManager(glm);

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        expandedImageView = (ImageView) v.findViewById(R.id.expanded_Image);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(MainActivity.filter.equals("All")) {

            FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_WhatsUp, BlogViewHolder>(
                    Blog_WhatsUp.class, R.layout.blog_row_lnf, BlogViewHolder.class, mDatabase) {
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder,final Blog_WhatsUp model, final int position) {

                    viewHolder.setTitle(model.getTitle());
                    //viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity(), model.getImage());
                    viewHolder.setNewImage(getActivity(), model.getNewicon());

                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                if (users.Admin) {
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
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
                                } else {
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            viewHolder.zoomImageFromThumb(getActivity(),model.getImage());
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
                    Blog_WhatsUp.class, R.layout.blog_row_lnf, BlogViewHolder.class, mDatabase.orderByChild("title").equalTo(MainActivity.filter)) {
                @Override
                protected void populateViewHolder(final BlogViewHolder viewHolder,final Blog_WhatsUp model, final int position) {

                    viewHolder.setTitle(model.getTitle());
                    //viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getActivity(), model.getImage());
                    viewHolder.setNewImage(getActivity(), model.getNewicon());

                    if (MainActivity.user_id != null) {

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Users users = dataSnapshot.child(MainActivity.user_id).getValue(Users.class);

                                if (users.Admin) {
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
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
                                } else{
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            viewHolder.zoomImageFromThumb(getActivity(),model.getImage());
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

       /*public void setDesc(String desc)
        {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }*/

        public void setImage(final Context cxt, final String image){

            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            //Picasso.with(cxt).load(image).centerCrop().resize(250,250).into(post_image);
            Picasso.with(cxt).load(image).networkPolicy(NetworkPolicy.OFFLINE).centerCrop().resize(250,250).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(cxt).load(image).centerCrop().resize(250,250).into(post_image);
                }
            });
        }

        public void setNewImage(Context cxt, String image){
            ImageView newIcon = (ImageView) mView.findViewById(R.id.newIcon);
            Picasso.with(cxt).load(image).centerCrop().resize(60,35).into(newIcon);
            if(image == null){
                newIcon.setVisibility(View.GONE);
            } else {
                newIcon.setVisibility(View.VISIBLE);
            }

        }

        public void zoomImageFromThumb(Context cxt, String image) {
            // If there's an animation in progress, cancel it
            // immediately and proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }
            final View thumbView = mView.findViewById(R.id.post_image);

            // Load the high-resolution "zoomed-in" image.
            Picasso.with(cxt).load(image).centerCrop().resize(300,250).into(expandedImageView);

            // Calculate the starting and ending bounds for the zoomed-in image.
            // This step involves lots of math. Yay, math.
            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            // The start bounds are the global visible rectangle of the thumbnail,
            // and the final bounds are the global visible rectangle of the container
            // view. Also set the container view's offset as the origin for the
            // bounds, since that's the origin for the positioning animation
            // properties (X, Y).
            thumbView.getGlobalVisibleRect(startBounds);
            mView.findViewById(R.id.LinearLayout)
                    .getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            // Adjust the start bounds to be the same aspect ratio as the final
            // bounds using the "center crop" technique. This prevents undesirable
            // stretching during the animation. Also calculate the start scaling
            // factor (the end scaling factor is always 1.0).
            float startScale;
            if ((float) finalBounds.width() / finalBounds.height()
                    > (float) startBounds.width() / startBounds.height()) {
                // Extend start bounds horizontally
                startScale = (float) startBounds.height() / finalBounds.height();
                float startWidth = startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;
                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            } else {
                // Extend start bounds vertically
                startScale = (float) startBounds.width() / finalBounds.width();
                float startHeight = startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) / 2;
                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins, it will position the zoomed-in view in the place of the
            // thumbnail.
            thumbView.setAlpha(0f);
            expandedImageView.setVisibility(View.VISIBLE);

            // Set the pivot point for SCALE_X and SCALE_Y transformations
            // to the top-left corner of the zoomed-in view (the default
            // is the center of the view).
            expandedImageView.setPivotX(0f);
            expandedImageView.setPivotY(0f);

            // Construct and run the parallel animation of the four translation and
            // scale properties (X, Y, SCALE_X, and SCALE_Y).
            AnimatorSet set = new AnimatorSet();
            set
                    .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                            startBounds.left, finalBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                            startBounds.top, finalBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                    View.SCALE_Y, startScale, 1f));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;

            // Upon clicking the zoomed-in image, it should zoom back down
            // to the original bounds and show the thumbnail instead of
            // the expanded image.
            final float startScaleFinal = startScale;
            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentAnimator != null) {
                        mCurrentAnimator.cancel();
                    }

                    // Animate the four positioning/sizing properties in parallel,
                    // back to their original values.
                    AnimatorSet set = new AnimatorSet();
                    set.play(ObjectAnimator
                            .ofFloat(expandedImageView, View.X, startBounds.left))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.Y,startBounds.top))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.SCALE_Y, startScaleFinal));
                    set.setDuration(mShortAnimationDuration);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }
                    });
                    set.start();
                    mCurrentAnimator = set;
                }
            });
        }
    }


}

