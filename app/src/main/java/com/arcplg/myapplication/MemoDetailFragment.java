package com.arcplg.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcplg.myapplication.databinding.FragmentMemoDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMemoDetailBinding binding;


    public MemoDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemoDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoDetailFragment newInstance(String param1, String param2) {
        MemoDetailFragment fragment = new MemoDetailFragment();
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
        binding = FragmentMemoDetailBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = "";
        String detail = "";
        String createdAt = "";

        var args = getArguments();
        var dbHelper = new DatabaseHelper(getContext());
        var db = dbHelper.getReadableDatabase();

        if (args != null) {
            var id = args.getString("id");

            if (id != null) {
                var cursor = db.query("memo", null, "id = ?", new String[]{id}, null, null, null);

                if (cursor.moveToFirst()) {
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    detail = cursor.getString(cursor.getColumnIndexOrThrow("detail"));
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
                }

                binding.titleInput.setText(title);
                binding.detailInput.setText(detail);
                binding.createdAtText.setText(createdAt);
            }
        }
    }
}