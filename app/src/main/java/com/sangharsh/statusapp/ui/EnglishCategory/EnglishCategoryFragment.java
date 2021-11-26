package com.sangharsh.statusapp.ui.EnglishCategory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sangharsh.statusapp.Adapters.EnglishCategoryAdapter;
import com.sangharsh.statusapp.Adapters.HindiCategoryAdapter;
import com.sangharsh.statusapp.Model.EnglishCategoryModel;
import com.sangharsh.statusapp.Model.HindiCategoryModel;
import com.sangharsh.statusapp.Model.QuoteModel;
import com.sangharsh.statusapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sangharsh.statusapp.api.GET_ENGLISH_CATEGORY;
import static com.sangharsh.statusapp.api.GET_HINDI_CATEGORY;

public class EnglishCategoryFragment extends Fragment {
    EnglishCategoryAdapter adapter;
    EnglishCategoryModel english_category_model;
    List<EnglishCategoryModel> english_category_list;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
   public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_notifications, container, false);
       super.onAttach(root.getContext());

       recyclerView = root.findViewById(R.id.recyclerView_English);
       progressBar = root.findViewById(R.id.progressBar);
       swipeRefreshLayout = root.findViewById(R.id.swipe_for_refresh);
       progressBar.setVisibility(View.VISIBLE);

           // loadEnglishCategory();

           LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
           recyclerView.setLayoutManager(gridLayoutManager);

            english_category_list = new ArrayList<>();
           JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, GET_ENGLISH_CATEGORY, null, new Response.Listener<JSONArray>() {
               @Override
               public void onResponse(JSONArray response) {
                   for (int i = 0; i < response.length(); i++) {
                       try {
                           JSONObject object = response.getJSONObject(i);
                           english_category_model = new EnglishCategoryModel();
                           english_category_model.setTextsms(object.getString("category_name"));
                           english_category_list.add(english_category_model);
                       } catch (JSONException e) {
                           e.printStackTrace();
//                        progressBar.setVisibility(View.INVISIBLE);
                       }

                       Collections.reverse(english_category_list);
                       adapter = new EnglishCategoryAdapter(english_category_list);
                       recyclerView.setAdapter(adapter);
                       //swipeRefreshLayout.setRefreshing(false);
                       progressBar.setVisibility(View.GONE);
                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   //   Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
               }
           });
           RequestQueue queue = Volley.newRequestQueue(requireContext());
           queue.add(request);
           swipeRefreshLayout.setRefreshing(false);

      // loadEnglishCategory();
//       if(english_category_list != null){
//           swipeRefreshLayout.setRefreshing(false);
//       }



       return root;
    }

    @Override
    public void onDestroyView() {
        english_category_list.clear();
       super.onDestroyView();
    }

    @Override
    public void onDetach() {
       english_category_list.clear();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        english_category_list.clear();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
            public void onRefresh() {

                 swipeRefreshLayout.setRefreshing(true);
//                if(english_category_list == null) {
//                    loadEnglishCategory();
//                }
                adapter  = new EnglishCategoryAdapter(english_category_list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onStart() {

       //swipeRefreshLayout.setRefreshing(true);
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
       super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
    }

    private void loadEnglishCategory() {
      // swipeRefreshLayout.setRefreshing(true);
        //progressBar.setVisibility(View.VISIBLE);
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,  GET_ENGLISH_CATEGORY, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject object = response.getJSONObject(i);
//                        english_category_model = new EnglishCategoryModel();
//                        english_category_model.setTextsms(object.getString("category_name"));
//                        english_category_list.add(english_category_model);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
////                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//
//                    Collections.reverse(english_category_list);
//                    adapter = new EnglishCategoryAdapter(english_category_list);
//                    recyclerView.setAdapter(adapter);
//        //            swipeRefreshLayout.setRefreshing(false);
//                    //progressBar.setVisibility(View.GONE);
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//             //   Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestQueue queue = Volley.newRequestQueue(requireContext());
//        queue.add(request);
    }
    }
