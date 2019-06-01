package com.example.donorapp.DonationListing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.donorapp.DonationListing.DonationListFragment.OnListFragmentInteractionListener;
import com.example.donorapp.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Donation} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDonationRecyclerViewAdapter extends RecyclerView.Adapter<MyDonationRecyclerViewAdapter.ViewHolder> {

    private final List<Donation> mDonations;
    private final OnListFragmentInteractionListener mListener;

    public MyDonationRecyclerViewAdapter(List<Donation> items, OnListFragmentInteractionListener listener) {
        mDonations = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_donation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mDonations.get(position);
        holder.mIdView.setText(mDonations.get(position).id);
//        holder.mDonorView.setText(mDonations.get(position).donorId);
        holder.mTitleView.setText(mDonations.get(position).title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDonations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
//        public final TextView mDonorView;
        public final TextView mTitleView;
        public Donation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.donation_id);
//            mDonorView = (TextView) view.findViewById(R.id.donation_donorId);
            mTitleView = (TextView) view.findViewById(R.id.donation_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
