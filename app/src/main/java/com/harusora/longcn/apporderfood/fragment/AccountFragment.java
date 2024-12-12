package com.harusora.longcn.apporderfood.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.harusora.longcn.apporderfood.R;

public class AccountFragment extends Fragment {

    private TextView tvName, tvGender, tvAddress, tvPhoneNumber;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        tvGender = view.findViewById(R.id.tvGender);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPhoneNumber = view.findViewById(R.id.btnLogout);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> onClickLogout());
    }

    private void onClickLogout() {
    }


}