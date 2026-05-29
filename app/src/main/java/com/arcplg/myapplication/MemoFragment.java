package com.arcplg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcplg.myapplication.placeholder.PlaceholderContent;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class MemoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DatabaseHelper dbHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MemoFragment() {
    }

    // TODO: Customize parameter initialization
    public static MemoFragment newInstance(int columnCount) {
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_list, container, false);
        var listFromDB = new ArrayList<PlaceholderContent.PlaceholderItem>();

        if (dbHelper != null) {
            var db = dbHelper.getReadableDatabase();
            var cursor = db.query("memo", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                var id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                var title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

                listFromDB.add(new PlaceholderContent.PlaceholderItem(id, title, ""));
            }
            cursor.close();
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(listFromDB));

            var swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                final ColorDrawable background = new ColorDrawable(Color.RED);
                final Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.icon_delete);

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    var position = viewHolder.getBindingAdapterPosition();
                    var itemToDelete = listFromDB.get(position);

                    if (dbHelper != null) {
                        var db = dbHelper.getWritableDatabase();

                        db.delete("memo", "id = ?", new String[]{itemToDelete.id});

                        listFromDB.remove(position);
                        var recyclerViewAdapter = recyclerView.getAdapter();

                        if (recyclerViewAdapter != null) {
                            recyclerViewAdapter.notifyItemRemoved(position);
                        }
                    }

                    Snackbar.make(view, "削除しました", Snackbar.LENGTH_LONG).setAction("元に戻す", v -> {
                        if (dbHelper != null) {
                            var db = dbHelper.getWritableDatabase();
                            var values = new ContentValues();

                            values.put("id", itemToDelete.id);
                            values.put("title", itemToDelete.content);
                            values.put("detail", itemToDelete.details);
                            values.put("created_at", System.currentTimeMillis() / 1000L);

                            db.insert("memo", null, values);
                            listFromDB.add(position, itemToDelete);
                            recyclerView.getAdapter().notifyItemInserted(position);
                        }
                    }).show();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;
                    int itemHeight = itemView.getBottom() - itemView.getTop();

                    // dXが0未満 → 左方向の移動のみ
                    if (dX < 0) {
                        c.clipRect(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());

                        background.setBounds(
                                itemView.getRight() + (int) dX,
                                itemView.getTop(),
                                itemView.getRight(),
                                itemView.getBottom()
                        );
                        background.draw(c);

                        if (icon != null) {
                            int iconWidth = icon.getIntrinsicWidth();
                            int iconHeight = icon.getIntrinsicHeight();

                            int iconLeft = itemView.getRight() - (itemHeight - iconHeight) / 2 - iconWidth;
                            int iconTop = itemView.getTop() + (itemHeight - iconHeight) / 2;
                            int iconRight = itemView.getRight() - (itemHeight - iconHeight) / 2;
                            int iconBottom = iconTop + iconHeight;

                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                            icon.draw(c);
                         }
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

            new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
        }

        return view;
    }
}