package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentMyPage extends Fragment {
    private TextView tvMyAddr;
    private Button btnSetMyAddr;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        tvMyAddr = view.findViewById(R.id.tvMyAddr);
        btnSetMyAddr = view.findViewById(R.id.btnSetMyAddr);

        btnSetMyAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetMyAddrActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("config", 0);
        String addr = pref.getString("areaName", null);
        if (addr != null) {
            tvMyAddr.setText(addr);
        }
    }
}