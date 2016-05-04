package startup.com.mediapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MainCategoryAdapter.ClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ProgressDialog pDialog;
    private RequestQueue mQueue;
    private final String fetch_url = "http://mediapp.netai.net/FetchMainCategoryItems.php";

    String[] category_img;
    String[] title;
    int len;

    LinearLayout ll_refresh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MainCategoryAdapter main_adapter;
    List<MainCategoryInfo> mainlist = new ArrayList<MainCategoryInfo>();
    List<MainCategoryInfo> result = new ArrayList<MainCategoryInfo>();
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.hasFixedSize();

        main_adapter = new MainCategoryAdapter(getActivity(),mainlist);
        recList.setAdapter(main_adapter);
        main_adapter.setClickListener(this);

        ll_refresh = (LinearLayout) v.findViewById(R.id.ll_refresh_category);


        ll_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetch_items();
            }
        });

        mQueue = CustomVolleyRequestQueue.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();


        fetch_items();
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void itemClicked(View view, int position, String subcategory) {
        TextView tv_category = (TextView) view.findViewById(R.id.title);
        String category = tv_category.getText().toString();

        if(subcategory.equals("no")) {
            Intent i = new Intent(getActivity(), ItemListActivity.class);
            i.putExtra("category", category);
            i.putExtra("sub_category", "nope");
            startActivity(i);

        }

        else{

            Intent i = new Intent(getActivity(), SubCategory.class);
            i.putExtra("category", category);
            startActivity(i);

        }
    }


    private void fetch_items(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching product list...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, fetch_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                len = 7;
                JSONArray id,name,subcategory,description,img_url;
                try {
                    id = response.getJSONArray(0);
                    name = response.getJSONArray(1);
                    img_url = response.getJSONArray(2);
                    subcategory = response.getJSONArray(4);
                    description = response.getJSONArray(3);

                    title = new String[len];
                    category_img = new String[len];
                    Log.d("Success", response.toString());

                    for (int i=0; i < len; i++) {

                        //title[i]=name.getString(i);
                        //category_img[i]=img_url.getString(i);
                        MainCategoryInfo ci = new MainCategoryInfo();
                        ci.name = name.getString(i);
                        //ci.imgsrc=category_img[i];
                        ci.imgsrc = img_url.getString(i);
                        //.replace("\\","");
                        //ci.imgsrc="http://mediapp.netai.net/Images/SubCategory/HealthCare Devices/RCD.jpg";
                        ci.id = id.getInt(i);
                        ci.description = description.getString(i);
                        ci.subcategory = subcategory.getString(i);

                        result.add(ci);


                    }
                    ll_refresh.setVisibility(View.GONE);
                    mainlist = result;
                    main_adapter.animateTo(mainlist);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    return;
                }


                pDialog.dismiss();
                Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_refresh.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                VolleyLog.e("Error : ", error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity(),"Connection error",Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(req);
    }


    private List<MainCategoryInfo> createList(int size) {

        List<MainCategoryInfo> result = new ArrayList<MainCategoryInfo>();
        for (int i = 0; i < size; i++) {
            MainCategoryInfo sci = new MainCategoryInfo();
            sci.name = title[i];
            sci.imgsrc = category_img[i];

            result.add(sci);

        }
        return result;
    }


}
