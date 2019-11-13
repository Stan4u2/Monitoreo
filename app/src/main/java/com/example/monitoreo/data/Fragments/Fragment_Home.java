package com.example.monitoreo.data.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.monitoreo.MainMenu;
import com.example.monitoreo.R;

public class Fragment_Home extends Fragment {
    LinearLayout UserLinearLayout, ReadingsLinearLayout, ElementsLinearLayout, AreasLinearLayout, SectionsLinearLayout, HistoryLinearLayout;
    Button UserButton, ReadingsButton, ElementsButton, AreasButton, SectionsButton, HistoryButton;
    ImageView UserImageView, ReadingsImageView, ElementsImageView, AreasImageView, SectionsImageView, HistoryImageView;
    TextView UserTextView, ReadingsTextView, ElementsTextView, AreasTextView, SectionsTextView, HistoryTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Button
        UserButton = view.findViewById(R.id.UserButton);
        ReadingsButton = view.findViewById(R.id.ReadingsButton);
        ElementsButton = view.findViewById(R.id.ElementsButton);
        AreasButton = view.findViewById(R.id.AreasButton);
        SectionsButton = view.findViewById(R.id.SectionsButton);
        HistoryButton = view.findViewById(R.id.HistoryButton);

        //ImageView
        UserImageView = view.findViewById(R.id.UserImageView);
        ReadingsImageView = view.findViewById(R.id.ReadingsImageView);
        ElementsImageView = view.findViewById(R.id.ElementsImageView);
        AreasImageView = view.findViewById(R.id.AreasImageView);
        SectionsImageView = view.findViewById(R.id.SectionsImageView);
        HistoryImageView = view.findViewById(R.id.HistoryImageView);

        //TextView
        UserTextView = view.findViewById(R.id.UserTextView);
        ReadingsTextView = view.findViewById(R.id.ReadingsTextView);
        ElementsTextView = view.findViewById(R.id.ElementsTextView);
        AreasTextView = view.findViewById(R.id.AreasTextView);
        SectionsTextView = view.findViewById(R.id.SectionsTextView);
        HistoryTextView = view.findViewById(R.id.HistoryTextView);

        //Linear Layour
        UserLinearLayout = view.findViewById(R.id.UserLinearLayout);
        ReadingsLinearLayout = view.findViewById(R.id.ReadingsLinearLayout);
        ElementsLinearLayout = view.findViewById(R.id.ElementsLinearLayout);
        AreasLinearLayout = view.findViewById(R.id.AreasLinearLayout);
        SectionsLinearLayout = view.findViewById(R.id.SectionsLinearLayout);
        HistoryLinearLayout = view.findViewById(R.id.HistoryLinearLayout);

        onTouchListeners();
        onClickListeners();
        return view;
    }

    public void onTouchListeners(){
        UserButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    UserLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    UserImageView.setColorFilter(Color.argb(100,0,134,119));
                    UserTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    UserTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    UserLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    UserImageView.setColorFilter(Color.WHITE);
                    UserTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    UserTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });

        ReadingsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ReadingsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    ReadingsImageView.setColorFilter(Color.argb(100,0,134,119));
                    ReadingsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    ReadingsTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    ReadingsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    ReadingsImageView.setColorFilter(Color.WHITE);
                    ReadingsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    ReadingsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });

        ElementsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ElementsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    ElementsImageView.setColorFilter(Color.argb(100,0,134,119));
                    ElementsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    ElementsTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    ElementsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    ElementsImageView.setColorFilter(Color.WHITE);
                    ElementsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    ElementsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });

        AreasButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AreasLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    AreasImageView.setColorFilter(Color.argb(100,0,134,119));
                    AreasTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    AreasTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    AreasLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    AreasImageView.setColorFilter(Color.WHITE);
                    AreasTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    AreasTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });

        SectionsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SectionsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    SectionsImageView.setColorFilter(Color.argb(100,0,134,119));
                    SectionsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    SectionsTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    SectionsLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    SectionsImageView.setColorFilter(Color.WHITE);
                    SectionsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    SectionsTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });

        HistoryButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    HistoryLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background1));
                    HistoryImageView.setColorFilter(Color.argb(100,0,134,119));
                    HistoryTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2_green));
                    HistoryTextView.setTextColor(Color.WHITE);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    HistoryLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background_green));
                    HistoryImageView.setColorFilter(Color.WHITE);
                    HistoryTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    HistoryTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_list_item_background2));
                }
                return false;
            }
        });
    }

    public void onClickListeners(){
        UserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).MainMenuButtons("users");
            }
        });

        ElementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).MainMenuButtons("elements");
            }
        });

        AreasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).MainMenuButtons("areas");
            }
        });

        SectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu)getActivity()).MainMenuButtons("sections");
            }
        });
    }
}
