package com.example.donorapp.DonationListing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.donorapp.DonationAd;
import com.example.donorapp.LoginPage;
import com.example.donorapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DonationListFragment extends Fragment  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyDonationRecyclerViewAdapter mAdapter;
    private List<Donation> mDonationList = new ArrayList<>();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private String mUserType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DonationListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DonationListFragment newInstance(int columnCount) {
        DonationListFragment fragment = new DonationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserType = this.getArguments().getString("usrType");
        mDonationList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            mAdapter = new MyDonationRecyclerViewAdapter(mDonationList, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        loadDonationList();
        return view;
    }

    public void loadDonationList() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser == null)
        {
            // Return user to log in if they are not authenticated
            Intent intent = new Intent(getContext(), LoginPage.class);
            startActivity(intent);
        }
        String userUid = currentFirebaseUser.getUid();

        final ValueEventListener donationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot donationSnap : dataSnapshot.getChildren()) {
                    loadDonation(donationSnap);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load list of donations! Check your internet connection.", Toast.LENGTH_LONG);
            }
        };

        if (mUserType.equals("donor"))
        {
            try {
                dbRef.child("Requests").addValueEventListener(
                    donationListener
                );

            } catch (Exception e) {
                Log.d("DonationListing", "No donations found, empty list.");
                mDonationList.clear();
            }

        } else if (mUserType.equals("charity")) {
            dbRef.child("Donations").addValueEventListener(
                donationListener
            );
        } else {
            Log.e("DonationListing", "Got an unexpected user type: " + mUserType);
        }


    }

    private void loadDonation(final DataSnapshot snapshot) {
        try {
            String id = snapshot.getKey();
            String donorId = snapshot.child("userID").getValue().toString();
            String title = snapshot.child("title").getValue().toString();
            String description = snapshot.child("description").getValue().toString();
            String status = snapshot.child("status").getValue().toString();
            mDonationList.add(new Donation(id, donorId, title, description, status));
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            return;
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Donation item);
    }

}
