package startup.com.mediapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String rating;
    EditText comments;
    Button submit;
    RadioGroup radioGroup;


    private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        View v =  inflater.inflate(R.layout.fragment_feedback, container, false);

        comments =(EditText) v.findViewById(R.id.commentet);
        radioGroup = (RadioGroup) v.findViewById(R.id.rate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.excellent:

                        rating = "Excellent";
                        break;
                    case R.id.very_good:

                        rating = "Very Good";
                        break;

                    case R.id.good:

                        rating = "Good";
                        break;

                    case R.id.average:

                        rating = "Average";
                        break;

                    case R.id.poor:

                        rating = "Poor";
                        break;
                }
            }
        });
        submit=(Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(comments.getText())) {
                    comments.setError("Please enter comments");
                    return;
                }

                if (radioGroup.getCheckedRadioButtonId() <= 0) {//Grp is your radio group object
                    Toast.makeText(getActivity().getApplicationContext(), "Please select anyone rating ", Toast.LENGTH_SHORT);
                    return;
                }

                comments.setText("");
                radioGroup.clearCheck();
                Toast.makeText(getActivity(), "Thanks for the feedback", Toast.LENGTH_SHORT).show();
                return;
            }
        });


        return v;


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


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.excellent:
                if (checked)
                    rating="Excellent";
                break;
            case R.id.very_good:
                if (checked)
                    rating="Very Good";
                break;

            case R.id.good:
                if (checked)
                    rating="Good";
                break;

            case R.id.average:
                if (checked)
                    rating="Average";
                break;

            case R.id.poor:
                if (checked)
                    rating="Poor";
                break;
        }
    }

    public void submit(View v){

        this.onCreate(null);
        return;


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
}