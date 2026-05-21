package com.arcplg.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arcplg.myapplication.databinding.FragmentAddMemoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMemoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAddMemoBinding binding;

    public AddMemoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMemoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMemoFragment newInstance(String param1, String param2) {
        AddMemoFragment fragment = new AddMemoFragment();
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
        binding = FragmentAddMemoBinding.inflate(inflater, container, false);

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_add_memo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addMemoButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "メモを追加しました", Toast.LENGTH_SHORT).show();
        });

        binding.titleInput.addTextChangedListener(new TextWatcher() {
            // 文字が変更される前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d("text", "beforeTextChanged: " + charSequence.toString());

            }

            // 文字が変更された瞬間
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            // 文字が変更された後
            @Override
            public void afterTextChanged(Editable editable) {

            }


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}