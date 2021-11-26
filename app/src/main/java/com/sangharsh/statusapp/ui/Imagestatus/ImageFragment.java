package com.sangharsh.statusapp.ui.Imagestatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sangharsh.statusapp.Adapters.EnglishCategoryAdapter;
import com.sangharsh.statusapp.Adapters.ImageAdapter;
import com.sangharsh.statusapp.Adapters.ImagecategoryAdapter;
import com.sangharsh.statusapp.Model.EnglishCategoryModel;
import com.sangharsh.statusapp.Model.ImageCategoryModel;
import com.sangharsh.statusapp.Model.Image_Model;
import com.sangharsh.statusapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sangharsh.statusapp.api.GET_ENGLISH_CATEGORY;
import static com.sangharsh.statusapp.api.GET_IMAGE_CATEGORY;

public class ImageFragment extends Fragment {

    List<ImageCategoryModel> image_category_list ;
    RecyclerView recyclerView;
    ImagecategoryAdapter adapter;
    ImageCategoryModel imageCategoryModel;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        recyclerView = root.findViewById(R.id.recyclerView_image_category);
        swipeRefreshLayout = root.findViewById(R.id.swipe_for_refresh);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);



        //final ImagecategoryAdapter adapter = {new ImagecategoryAdapter(image_category_list)};




        return root;
    }

    @Override
    public void onStart() {

        loadImageCategory();
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(image_category_list == null){
//                    loadImageCategory();
//                }

                adapter = new ImagecategoryAdapter(image_category_list,getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onDestroyView() {
        image_category_list.clear();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        image_category_list.clear();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        image_category_list.clear();
        super.onDestroy();
    }
    private void loadImageCategory() {
        progressBar.setVisibility(View.VISIBLE);
        image_category_list = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  GET_IMAGE_CATEGORY, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        imageCategoryModel= new ImageCategoryModel();
                        imageCategoryModel.setCategory_name(object.getString("category_name"));
                        imageCategoryModel.setCategory_image(object.getString("image"));
                        image_category_list.add(imageCategoryModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.reverse(image_category_list);
                adapter = new ImagecategoryAdapter(image_category_list,getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
    }
