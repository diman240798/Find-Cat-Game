package com.nanicky.devteam.findcat;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import srsdt1.findacat.R;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {
    private String[] achievementsDescs;
    private String[] achievementsNames;
    private Context context;
    private boolean[] enabledAchs;
    private String[] imagesNames;

    class ViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public CardView cardView;
        /* access modifiers changed from: private */
        public TextView desc;
        /* access modifiers changed from: private */
        public ImageView image;
        /* access modifiers changed from: private */
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.ach_cv);
            this.image = (ImageView) view.findViewById(R.id.ivImg);
            this.name = (TextView) view.findViewById(R.id.tvText1);
            this.desc = (TextView) view.findViewById(R.id.tvText);
        }
    }

    public AchievementsAdapter(String[] strArr, String[] strArr2, String[] strArr3, boolean[] zArr, Context context2) {
        this.achievementsNames = strArr;
        this.achievementsDescs = strArr2;
        this.imagesNames = strArr3;
        this.enabledAchs = zArr;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(this.achievementsNames[i]);
        viewHolder.desc.setText(this.achievementsDescs[i]);
        if (this.enabledAchs[i]) {
            viewHolder.image.setImageResource(this.context.getResources().getIdentifier(this.imagesNames[i], "drawable", this.context.getPackageName()));
        } else {
            viewHolder.image.setImageResource(this.context.getResources().getIdentifier("aclose", "drawable", this.context.getPackageName()));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            viewHolder.cardView.setUseCompatPadding(true);
        }
    }

    public int getItemCount() {
        return this.achievementsNames.length;
    }
}
