package com.kuldeep.intelligent_farming.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kuldeep.intelligent_farming.MainActivity;
import com.kuldeep.intelligent_farming.Pojo_classes.mysensorsPojo;
import com.kuldeep.intelligent_farming.Project_classes.URLHelper;
import com.kuldeep.intelligent_farming.R;
import com.kuldeep.intelligent_farming.adapters.MySensorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class homefrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView atmtemp;
    private MySensorAdapter mySensorAdapter;

    GridView gridView;

    private ArrayList<mysensorsPojo> arrayList;


    private OnFragmentInteractionListener mListener;

    public homefrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homefrag.
     */
    // TODO: Rename and change types and number of parameters
    public static homefrag newInstance(String param1, String param2) {
        homefrag fragment = new homefrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homefrag, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList = new ArrayList<>();
        mySensorAdapter = new MySensorAdapter(getContext(), arrayList);
        gridView = (GridView) view.findViewById(R.id.gridviewsensor);
        gridView.setAdapter(mySensorAdapter);

        //arrayList.add(new mysensorsPojo("@drawable/soil_temperature","temperature","20 C"));


        SharedPreferences sharedPref = getContext().getSharedPreferences("farmingSharedPreference", MODE_PRIVATE);
        fatchsensorvalues(sharedPref.getString("email", "amaurya080197@gmail.com"));

    }

    private void fatchsensorvalues(final String email) {
        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.sensrDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", response);
                        //progressBar.setVisibility(View.INVISIBLE);
                        parseResponseString(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);
                return map;
            }
        };
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }

    private void parseResponseString(String response) {
        String SensorImageuri = "";
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String error = (String) jsonObject.get("errorString");

            JSONArray jsonArray2 = jsonObject.getJSONArray("sensorPojo");

            if (error.equals("false")) {
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray2.get(i);
                    switch (jsonObject2.get("sensorName").toString()) {
                        case "Temperature":
                            SensorImageuri = "@drawable/atmosp_temperature";
                            break;
                        case "Humidity":
                            SensorImageuri = "@drawable/soil_temperature";
                            break;
                        case "soil":
                            SensorImageuri = "@drawable/soil_humidity";
                            break;
                        case "Pump":
                            SensorImageuri = "@drawable/sprinkler";
                            break;
                        default:
                            SensorImageuri = "@drawable/soil_temperature";
                    }
                    arrayList.add(new mysensorsPojo(SensorImageuri, jsonObject2.get("sensorName").toString(), jsonObject2.get("value").toString()));
                    Toast.makeText(getContext(), "vallue added in array list", Toast.LENGTH_SHORT).show();
                    mySensorAdapter.notifyDataSetChanged();
                }

            } else {
                Toast.makeText(getContext(), "parsing error:" + error, Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            Toast.makeText(getContext(), "Exception error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
