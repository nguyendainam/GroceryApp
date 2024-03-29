package com.example.groceryfinal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryfinal.R;
import com.example.groceryfinal.adapters.HomeAdapter;
import com.example.groceryfinal.adapters.PopularAdapter;
import com.example.groceryfinal.adapters.RecommendedAdapter;

import com.example.groceryfinal.models.HomeCategory;
import com.example.groceryfinal.models.PopularModel;
import com.example.groceryfinal.models.RecommendedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;

    RecyclerView popularRec,homeCatRec,recommendedRec;
    FirebaseFirestore db;

    //popular item
    List<PopularModel> popularModelList;
    PopularAdapter popularAdapter;

    //Home Category
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;

    //recommended
    List<RecommendedModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_home,container,false);
        popularRec= root.findViewById(R.id.pop_rec);
        homeCatRec=root.findViewById(R.id.explore_rec);
        recommendedRec=root.findViewById(R.id.recommended_rec);

        scrollView=root.findViewById(R.id.scroll_view);
        progressBar=root.findViewById(R.id.progessbar);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        db=FirebaseFirestore.getInstance();
        //Popular Items
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        popularModelList=new ArrayList<>();
        popularAdapter=new PopularAdapter(getActivity(),popularModelList);
        popularRec.setAdapter(popularAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModel popularModel=document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(getActivity(), "loi113"+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //Home Category
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryList= new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(),categoryList);
        homeCatRec.setAdapter(homeAdapter);



        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory=document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                homeAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), "loi114"+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //recommended
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(),recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);

        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel= document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "loi115"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        return root;
    }


}