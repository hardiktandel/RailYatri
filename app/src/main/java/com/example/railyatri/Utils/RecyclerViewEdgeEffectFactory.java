package com.example.railyatri.Utils;

import android.graphics.Color;
import android.widget.EdgeEffect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railyatri.View.CustomViewHolder;

import java.util.Objects;

public class RecyclerViewEdgeEffectFactory extends RecyclerView.EdgeEffectFactory {

    @NonNull
    @Override
    protected EdgeEffect createEdgeEffect(@NonNull RecyclerView recyclerView, int direction) {

        EdgeEffect edgeEffect = new EdgeEffect(recyclerView.getContext()) {

            @Override
            public void onPull(float deltaDistance) {
                super.onPull(deltaDistance);
                handlePull(deltaDistance);
            }

            @Override
            public void onPull(float deltaDistance, float displacement) {
                super.onPull(deltaDistance, displacement);
                handlePull(deltaDistance);
            }

            private void handlePull(float deltaDistance) {
                int sign = (direction == DIRECTION_BOTTOM) ? -1 : 1;
                float translationYDelta = sign * recyclerView.getWidth() * deltaDistance * Const.OVERSCROLL_TRANSLATION_MAGNITUDE;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    CustomViewHolder viewHolder = (CustomViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if (((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).getOrientation() == RecyclerView.VERTICAL) {
                        viewHolder.getTranslationY().cancel();
                        viewHolder.itemView.setTranslationY(viewHolder.itemView.getTranslationY() + translationYDelta);
                    }
                }
            }

            @Override
            public void onRelease() {
                super.onRelease();
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    CustomViewHolder viewHolder = (CustomViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    viewHolder.getTranslationY().start();
                }
            }

            @Override
            public void onAbsorb(int velocity) {
                super.onAbsorb(velocity);
                int sign = (direction == DIRECTION_BOTTOM) ? -1 : 1;
                float translationVelocity = sign * velocity * Const.FLING_TRANSLATION_MAGNITUDE;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    CustomViewHolder viewHolder = (CustomViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    viewHolder.getTranslationY().setStartVelocity(translationVelocity).start();
                }
            }
        };
        edgeEffect.setColor(Color.WHITE);
        return edgeEffect;
    }
}
