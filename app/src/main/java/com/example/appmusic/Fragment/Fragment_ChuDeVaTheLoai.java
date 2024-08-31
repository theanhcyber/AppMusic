package com.example.appmusic.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.appmusic.Activity.DanhsachbaihatActivity;
import com.example.appmusic.Activity.DanhsachtatcachudeActivity;
import com.example.appmusic.Activity.DanhsachtheloaitheochudeActivity;
import com.example.appmusic.Model.ChuDeVaTheLoai;
import com.example.appmusic.Model.Theme;
import com.example.appmusic.Model.Type;
import com.example.appmusic.R;
import com.example.appmusic.Service.APIService;
import com.example.appmusic.Service.Dataservice;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ChuDeVaTheLoai extends Fragment {
    View view;
    HorizontalScrollView horizontalScrollView;
    TextView txtxemthemchudetheloai;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chudevatheloai,  container, false);
        horizontalScrollView = view.findViewById(R.id.horrizontalScrollview);
        txtxemthemchudetheloai = view.findViewById(R.id.textviewxemthemchude);
        txtxemthemchudetheloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhsachtatcachudeActivity.class);
                startActivity(intent);
            }
        });
        GetData();
        return view;
    }
    private void GetData() {
        Dataservice dataservice = APIService.getService();
        Call<ChuDeVaTheLoai> callback = dataservice.GetDataChuDeVaTheLoai();
        callback.enqueue(new Callback<ChuDeVaTheLoai>() {
            @Override
            public void onResponse(Call<ChuDeVaTheLoai> call, Response<ChuDeVaTheLoai> response) {
                ChuDeVaTheLoai chuDeVaTheLoai = response.body();

                final ArrayList<Theme> chudeArrayList = new ArrayList<>();
                chudeArrayList.addAll(chuDeVaTheLoai.getTheme());

                final ArrayList<Type> theloaiArrayList = new ArrayList<>();
                theloaiArrayList.addAll(chuDeVaTheLoai.getTypes());

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(900, 400);
                layout.setMargins(10,20,10,30);
                for (int i = 0; i < (chudeArrayList.size()); i++) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (chudeArrayList.get(i).getImageTheme() != null) {
                        Picasso.with(getActivity()).load(chudeArrayList.get(i).getImageTheme()).into(imageView);

                    }
                    cardView.setLayoutParams(layout);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                    int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DanhsachtheloaitheochudeActivity.class);
                            intent.putExtra("chude",chudeArrayList.get(finalI));
                            startActivity(intent);
                        }
                    });
                }
                for (int j = 0; j < (theloaiArrayList.size()); j++) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (theloaiArrayList.get(j).getImageType() != null) {
                        Picasso.with(getActivity()).load(theloaiArrayList.get(j).getImageType()).into(imageView);

                    }
                    cardView.setLayoutParams(layout);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                    int finalJ = j;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DanhsachbaihatActivity.class);
                            intent.putExtra("idtheloai",theloaiArrayList.get(finalJ));
                            Log.d("Fragment", "Starting DanhsachbaihatActivity with idtheloai: " + theloaiArrayList.get(finalJ).getIdType());
                            startActivity(intent);
                        }
                    });

                }
                horizontalScrollView.addView(linearLayout);

            }

            @Override
            public void onFailure(Call<ChuDeVaTheLoai> call, Throwable t) {
                Log.d("chudevatheloai",t.getMessage());

            }
        });
    }
}
