package com.google.developer.colorvalue.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.colorvalue.CardActivity;
import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Cursor mCursor;
    Context context;

    public CardAdapter(Context context)
    {
        this.context = context;
        mCursor = null;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        // TODO bind data to view

        Card card = getItem(position);
        if (card.getID() != -1) {
            String name = card.getName();
            String hexname = card.getHex();
            int color = card.getColorInt();
            float luminance = Color.luminance(color);
            final Uri uri = card.getUri();

            //setting text color based on luminance
            holder.name.setText(hexname);
            if (luminance > 0.50)
            {
                holder.name.setTextColor(context.getResources().getColor(R.color.textBlack));
            }
            else {
                holder.name.setTextColor(context.getResources().getColor(R.color.textwhite));
            }

            holder.cardView.setBackgroundColor(color);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CardActivity.class);
                    intent.putExtra("uri", uri.toString());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }
    /**
     * Return a {@link Card} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Card}
     */
    public Card getItem(int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return new Card(mCursor);
            }
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.color_name);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
