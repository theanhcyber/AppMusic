package com.example.appmusic.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import android.util.Log;

import androidx.appcompat.widget.SearchView; // Ensure this is the correct import
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmusic.R;

public class Fragment_Trang_Chu extends Fragment {
    private View view;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frapment_trang_chu, container, false);

        searchView = view.findViewById(R.id.searchView);

        if (searchView != null) {
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Chuyển đổi sang Fragment_Tim_Kiem khi nhấn vào ô tìm kiếm
                    Fragment_Tim_Kiem fragmentTimKiem = new Fragment_Tim_Kiem();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragmentTimKiem);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        } else {
            Log.e("Fragment_Trang_Chu", "SearchView is null");
        }

        return view;
    }
}