package com.technorizen.healthcare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technorizen.healthcare.R;
import com.technorizen.healthcare.databinding.FragmentContactUsBinding;
import com.technorizen.healthcare.models.SuccessResAddAddress;
import com.technorizen.healthcare.models.SuccessResGetAppInfo;
import com.technorizen.healthcare.models.SuccessResGetCardDetails;
import com.technorizen.healthcare.retrofit.ApiClient;
import com.technorizen.healthcare.retrofit.HealthInterface;
import com.technorizen.healthcare.util.DataManager;
import com.technorizen.healthcare.util.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.technorizen.healthcare.retrofit.Constant.USER_ID;
import static com.technorizen.healthcare.retrofit.Constant.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {

    FragmentContactUsBinding binding;

    private HealthInterface apiInterface;

    String twitterLink = "",instagramLink = "",facebookLink = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contact_us, container, false);

        apiInterface = ApiClient.getClient().create(HealthInterface.class);

        getAppInfo();

        return binding.getRoot();
    }

    public void getAppInfo()
    {

        String userId =  SharedPreferenceUtility.getInstance(getActivity()).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetAppInfo> call = apiInterface.getAppInfo(map);
        call.enqueue(new Callback<SuccessResGetAppInfo>() {
            @Override
            public void onResponse(Call<SuccessResGetAppInfo> call, Response<SuccessResGetAppInfo> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    SuccessResGetAppInfo data = response.body();
                    if (data.status.equals("1")) {

                        binding.tvEmail.setText(getResources().getString(R.string.email_us)+" "+data.getResult().get(0).getEmail());

                        instagramLink = data.getResult().get(0).getInsta();
                        twitterLink = data.getResult().get(0).getTwitter();
                        facebookLink = data.getResult().get(0).getFacebook();

                        setAllOnclick();

                    } else {
                        showToast(getActivity(), data.message);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<SuccessResGetAppInfo> call, Throwable t) {

                call.cancel();
                DataManager.getInstance().hideProgressMessage();

            }
        });

    }

    public void setAllOnclick()
    {

       binding.btnINsta.setOnClickListener(v ->
               {
                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink));
                   getActivity().startActivity(browserIntent);
               }
               );

        binding.btnfb.setOnClickListener(v ->
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink));
                    getActivity().startActivity(browserIntent);
                }
        );
        binding.btnINsta.setOnClickListener(v ->
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink));
                    getActivity().startActivity(browserIntent);
                }
        );

        binding.btnTwiter.setOnClickListener(v ->
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                    getActivity().startActivity(browserIntent);
                }
        );





    }


}