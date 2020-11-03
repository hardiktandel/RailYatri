package com.example.railyatri.View;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    private SpringAnimation translationY;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        translationY = new SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
                .setSpring(new SpringForce()
                        .setFinalPosition(0f)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                );
    }

    public SpringAnimation getTranslationY() {
        return translationY;
    }

    public void setTranslationY(SpringAnimation translationY) {
        this.translationY = translationY;
    }
}
