package com.arcplg.myapplication;

import android.content.ContentValues;
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
    private String inputTitle;
    private String inputDetail;
    private DatabaseHelper dbHelper;


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
        dbHelper = new DatabaseHelper(getContext());

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_add_memo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addMemoButton.setOnClickListener(v -> {
            var isInputted = inputTitle != null && inputDetail != null;;

            if (dbHelper != null && isInputted) {
                var db = dbHelper.getReadableDatabase();
                var values = new ContentValues();

                values.put("title", inputTitle);
                values.put("detail", inputDetail);
                values.put("created_at", System.currentTimeMillis() / 1000L);

                db.insert("memo", null, values);

                Toast.makeText(getContext(), "保存しました。", Toast.LENGTH_SHORT).show();
            } else {
                if (inputDetail == null && inputTitle == null) {
                    binding.detailInput.setError("本文を入力してください");
                    binding.titleInput.setError("タイトルを入力してください");

                    Toast.makeText(getContext(), "タイトルと本文を入力してください。", Toast.LENGTH_SHORT).show();
                } else if (inputDetail == null) {
                    binding.detailInput.setError("本文を入力してください");

                    Toast.makeText(getContext(), "本文を入力してください。", Toast.LENGTH_SHORT).show();
                } else if (inputTitle == null) {
                    binding.titleInput.setError("タイトルを入力してください");

                    Toast.makeText(getContext(), "タイトルを入力してください。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.titleInput.addTextChangedListener(new TextWatcher() {
            // 文字が変更される前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d("text", "beforeTextChanged:");
                Log.d("text", charSequence.toString());
                Log.d("text", String.valueOf(start));
                Log.d("text", String.valueOf(count));
                Log.d("text", String.valueOf(after));
            }

            // 文字が変更された瞬間
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d("text", "onTextChanged:");
                Log.d("text", charSequence.toString());
                Log.d("text", String.valueOf(start));
                Log.d("text", String.valueOf(before));
                Log.d("text", String.valueOf(count));
            }

            // 文字が変更された後
            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("text", "afterTextChanged:");
                Log.d("text", editable.toString());

                inputTitle = editable.toString();
            }
        });

        binding.detailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                inputDetail = editable.toString();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}