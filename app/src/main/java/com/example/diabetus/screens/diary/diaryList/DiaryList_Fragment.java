package com.example.diabetus.screens.diary.diaryList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diabetus.R;
import com.example.diabetus.common.RecyclerViewClickListener;
import com.example.diabetus.screens.diary.DiaryActivityModel;
import com.example.diabetus.screens.diary.DiaryListAdapter;
import com.example.diabetus.screens.mealEditor.MealFragment;

import java.util.Objects;

public class DiaryList_Fragment extends Fragment implements RecyclerViewClickListener {

    public static final String BUNDLE_RECYCLER_LAYOUT = "DiaryList_Fragment.diaryList.layout";
    public static final String BUNDLE_ENTRY = "DiaryList_Fragment.diaryList.entry";
    private DiaryListAdapter diaryListAdapter;
    private DiaryActivityModel diaryActivityModel;
    private RecyclerView diaryList;

    public DiaryList_Fragment() {
        super(R.layout.fragment_diary_list_);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diaryActivityModel = new DiaryActivityModel(requireActivity().getApplication());

        diaryListAdapter = new DiaryListAdapter(diaryActivityModel.getDiaryList().getValue(), this);
        diaryListAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_list_, container, false);

        diaryList = view.findViewById(R.id.diary_list);
        diaryList.setAdapter(diaryListAdapter);
        diaryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        diaryListAdapter.update(diaryActivityModel.getDiaryList().getValue());
        diaryActivityModel.getDiaryList().observe(getViewLifecycleOwner(), diaryListObjects -> diaryListAdapter.update(diaryActivityModel.getDiaryList().getValue()));

        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(BUNDLE_RECYCLER_LAYOUT)) {
            Parcelable savedRecyclerLayoutState = bundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            if (diaryList.getLayoutManager() != null) {
                new Handler().postDelayed(() -> diaryList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState), 500);
            }
        }

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            if (diaryList.getLayoutManager() != null) {
                diaryList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.diarylist, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_addDiaryEntry) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, Objects.requireNonNull(diaryList.getLayoutManager()).onSaveInstanceState());
                    NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.action_diaryList_Fragment_to_entryFragment, bundle);
//                    NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.entryFragment);
                }
                return false;
            }
        }, getViewLifecycleOwner());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void recyclerViewListClicked(View v, int position){
        Bundle bundle = new Bundle();
        bundle.putParcelable(MealFragment.ARG_DIARY_ENTRY, diaryListAdapter.getItem(position));
        bundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, Objects.requireNonNull(diaryList.getLayoutManager()).onSaveInstanceState());
        NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.action_diaryList_Fragment_to_entryFragment, bundle);
//        NavHostFragment.findNavController(DiaryList_Fragment.this).navigate(R.id.entryFragment, bundle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, Objects.requireNonNull(diaryList.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        diaryActivityModel.refreshDiaryList();
    }
}