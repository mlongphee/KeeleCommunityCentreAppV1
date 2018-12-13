package com.keele_v11.switchers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keele_v11.MainActivity;
import com.keele_v11.PostEvents;
import com.keele_v11.R;
import com.keele_v11.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by MJMJ2 on 22/01/17.
 */
public class EventsFragment extends Fragment {

    private com.github.tibolte.agendacalendarview.AgendaCalendarView mCalendar;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;

    private CalendarPickerController calendarPickerController;

    private List<CalendarEvent> eventList;
    private com.github.tibolte.agendacalendarview.models.BaseCalendarEvent event1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_events, container, false);
        v.setBackgroundColor(getResources().getColor(android.R.color.white));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatabase.keepSynced(true);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mCalendar = (com.github.tibolte.agendacalendarview.AgendaCalendarView) v.findViewById(R.id.agenda);
        calendarPickerController = new CalendarPickerController() {
            @Override
            public void onDaySelected(DayItem dayItem) {

            }

            @Override
            public void onEventSelected(final CalendarEvent event) {

                if(MainActivity.user_id != null) {

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
                                        final DialogInterface dia = dialog;
                                        mDatabase.orderByChild("id").equalTo(event.getId()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    snapshot.getRef().removeValue();
                                                }

                                                dia.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

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

            @Override
            public void onScrollToDate(Calendar calendar) {

            }
        };

        final Calendar startTime = Calendar.getInstance();
        final Calendar endTime = Calendar.getInstance();
        //endTime.add(Calendar.MONTH, 1);

        eventList = new ArrayList<>();
        //mockList(eventList);

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.add(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        mCalendar.init(eventList, minDate, maxDate, Locale.getDefault(), calendarPickerController);

        if(MainActivity.filter.equals("All")) {

            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PostEvents newPost = dataSnapshot.getValue(PostEvents.class);
                    String LOG = "My Activity";
                    try {
                        if(newPost.camp_name.equals("Tiny Tots")) {
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.colorPrimary), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(newPost.camp_name.equals("Adventure Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.redColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(newPost.camp_name.equals("Youth Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.greenColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(!newPost.camp_name.equals("Tiny Tots") && !newPost.camp_name.equals("Adventure Camp") && !newPost.camp_name.equals("Youth Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.purpleColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        eventList.add(event1);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG, "Camp Name: " + newPost.getCamp_name());
                    Log.d(LOG, "Camp Name: " + newPost.time);
                    Log.d(LOG, "Camp Name: " + newPost.date);

                    Calendar minDate = Calendar.getInstance();
                    Calendar maxDate = Calendar.getInstance();

                    minDate.add(Calendar.MONTH, -2);
                    minDate.add(Calendar.DAY_OF_MONTH, 1);
                    maxDate.add(Calendar.YEAR, 1);

                    mCalendar.init(eventList, minDate, maxDate, Locale.getDefault(), calendarPickerController);
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
            });
        } else {
            mDatabase.orderByChild("camp_name").equalTo(MainActivity.filter).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    PostEvents newPost = dataSnapshot.getValue(PostEvents.class);
                    String LOG = "My Activity";
                    try {
                        if(newPost.camp_name.equals("Tiny Tots")) {
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.colorPrimary), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(newPost.camp_name.equals("Adventure Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.redColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(newPost.camp_name.equals("Youth Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.greenColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        if(!newPost.camp_name.equals("Tiny Tots") && !newPost.camp_name.equals("Adventure Camp") && !newPost.camp_name.equals("Youth Camp")){
                            event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent(newPost.camp_name + " - " + newPost.event_name + "\n" + newPost.time, "Bring Swim Stuff", newPost.location, ContextCompat.getColor(getActivity(), R.color.purpleColor), convertToCalendar(newPost.date), convertToCalendar(newPost.date), true);
                            event1.setId(newPost.id);
                        }
                        eventList.add(event1);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG, "Camp Name: " + newPost.getCamp_name());
                    Log.d(LOG, "Camp Name: " + newPost.time);
                    Log.d(LOG, "Camp Name: " + newPost.date);

                    Calendar minDate = Calendar.getInstance();
                    Calendar maxDate = Calendar.getInstance();

                    minDate.add(Calendar.MONTH, -2);
                    minDate.add(Calendar.DAY_OF_MONTH, 1);
                    maxDate.add(Calendar.YEAR, 1);

                    mCalendar.init(eventList, minDate, maxDate, Locale.getDefault(), calendarPickerController);
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
            });
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void mockList(List<CalendarEvent> eventList){
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MONTH, 1);
        com.github.tibolte.agendacalendarview.models.BaseCalendarEvent event1 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent("Swim Today\n12:00pm", "Bring Swim Stuff", "High Park",ContextCompat.getColor(getActivity(), R.color.colorPrimary),startTime,endTime,true);
        eventList.add(event1);

        com.github.tibolte.agendacalendarview.models.BaseCalendarEvent event2 = new com.github.tibolte.agendacalendarview.models.BaseCalendarEvent("Trip to Centre Island\nAll Day", "Bring Swim Stuff", "Centre Island",ContextCompat.getColor(getActivity(), R.color.colorAccent),startTime,endTime,true);
        eventList.add(event2);


    }

    private Calendar convertToCalendar(String date){
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM, dd, yyyy");
        Calendar cal = Calendar.getInstance();
        try{
            Date date1 = format.parse(date);
            cal.setTime(date1);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return cal;
    }

}

