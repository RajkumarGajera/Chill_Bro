package com.chillbro.onealldigital.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import com.chillbro.onealldigital.R;
import com.chillbro.onealldigital.activity.MainActivity;
import com.chillbro.onealldigital.helper.Constant;
import com.chillbro.onealldigital.helper.Session;
import com.chillbro.onealldigital.model.filterDate;

public class Filterdates extends Fragment {

    View root;
    Button start_btn,end_btn,apply;
    TextView start_txt=null,end_txt=null;
    int year;
    int month;
    int day;
    int Time=3;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Session session;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.filterdate, container, false);
        setHasOptionsMenu(true);

        activity=getActivity();

        start_btn=root.findViewById(R.id.startdate_btn);
        end_btn=root.findViewById(R.id.enddate_btn);
        start_txt=root.findViewById(R.id.startdate_txt);
        end_txt=root.findViewById(R.id.enddate_txt);
        apply=root.findViewById(R.id.apply);
        session = new Session(activity);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DATE);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int years, int months, int days) {

                        day=days;
                        year=years;
                        month=months+1;
                        start_txt.setText(day + "-" + month + "-" + year);
                        month=month-1;

                    }
                }, year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            }
        });
        month=month-1;
        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
//                year=calendar.get(Calendar.YEAR);
//                month=calendar.get(Calendar.MONTH);
//                day=calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onDateSet(DatePicker view, int years, int months, int days) {

                        if(years>=year){
                            if(months>=month){
                                if(days>day){
                                    end_txt.setText(days + "-" + (months+1) + "-" + years);
                                }
                                else {
                                    end_txt.setText(null);
                                    Toast.makeText(getContext(),"Enter Proper Date", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                end_txt.setText(null);
                                Toast.makeText(getContext(),"Enter Proper Date", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            end_txt.setText(null);
                            Toast.makeText(getContext(),"Enter Proper Date", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Start_date,End_date;
                System.out.println(start_txt.getText());
                System.out.println(end_txt.getText());
                Bundle bundle = new Bundle();
                bundle.putString("Start_date",start_txt.getText().toString());
                bundle.putString("End_date",end_txt.getText().toString());
                String User_name=session.getData(Constant.NAME);


                Start_date=start_txt.getText().toString();
                End_date=end_txt.getText().toString();
                filterDate filterDate= new filterDate(Start_date,End_date);
                myRef.child("Filterdate").child(User_name).child(User_name).setValue(filterDate);
                TrackOrderFragment trackOrderFragment=new TrackOrderFragment();
                trackOrderFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,trackOrderFragment).commit();

            }
        });

        return root;
    }

    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.toolbar_sort).setVisible(false);
        menu.findItem(R.id.toolbar_cart).setVisible(true);
        menu.findItem(R.id.toolbar_search).setVisible(false);
        menu.findItem(R.id.toolbar_notification).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.showHideSearchBar(false);
    }
}
