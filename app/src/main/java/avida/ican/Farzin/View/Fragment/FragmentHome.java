package avida.ican.Farzin.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.R;
import butterknife.BindView;

public class FragmentHome extends BaseFragment {


    //private OnFragmentInteractionListener mListener;
    @BindView(R.id.txt_msg)
    TextView txtMsg;
    @BindView(R.id.btn_msg)
    Button btnMsg;
    private int a=0;

    public FragmentHome() {
        // Required empty public constructor
    }


    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        App.canRecreatFragment = true;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                a++;
                txtMsg.setText("A now is :"+a);
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        /*try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
/*
    *//*
      This interface must be implemented by activities that contain this
      fragment to allow an interaction in this fragment to be communicated
      to the activity and potentially other fragments contained in that
      activity.
      <p>
      See the Android Training lesson <a href=
      "http://developer.android.com/training/basics/fragments/communicating.html"
      >Communicating with Other Fragments</a> for more information.

     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
