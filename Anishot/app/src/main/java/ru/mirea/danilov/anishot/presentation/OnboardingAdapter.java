package ru.mirea.danilov.anishot.presentation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.Holder> {
    private static final int[] ART = {
            R.drawable.art_onb_camera,
            R.drawable.art_onb_match,
            R.drawable.art_onb_list
    };
    private static final String[][] CHIPS = {
            {"фото", "стоп-кадр"},
            {"серия", "таймкод"},
            {"дневник", "оценка"}
    };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.clear();
        holder.art.setImageResource(ART[position]);
        holder.chipA.setText(CHIPS[position][0]);
        holder.chipB.setText(CHIPS[position][1]);
        holder.anims.add(loop(holder.art, View.SCALE_X, 1f, 1.08f, 8400, 0));
        holder.anims.add(loop(holder.art, View.SCALE_Y, 1f, 1.08f, 8400, 0));
        holder.anims.add(loop(holder.chipA, View.TRANSLATION_Y, -8f, 8f, 2800, 0));
        holder.anims.add(loop(holder.chipB, View.TRANSLATION_Y, 6f, -10f, 3400, 180));
    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        holder.clear();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return ART.length;
    }

    private static ObjectAnimator loop(View view, android.util.Property<View, Float> property,
                                       float from, float to, long duration, long delay) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, property, from, to);
        animator.setDuration(duration);
        animator.setStartDelay(delay);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        return animator;
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView art;
        final TextView chipA;
        final TextView chipB;
        final List<Animator> anims = new ArrayList<>();

        Holder(View itemView) {
            super(itemView);
            art = itemView.findViewById(R.id.imageArt);
            chipA = itemView.findViewById(R.id.chipA);
            chipB = itemView.findViewById(R.id.chipB);
        }

        void clear() {
            for (Animator animator : anims) {
                animator.cancel();
            }
            anims.clear();
        }
    }
}
