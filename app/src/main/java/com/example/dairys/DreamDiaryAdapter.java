package com.example.dairys;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;
import com.example.dairys.Database.DreamFavorite;
import com.example.dairys.Database.DreamTag;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.List;

public class DreamDiaryAdapter extends RecyclerView.Adapter<DreamDiaryAdapter.ViewHolder>{

    List<DreamDiary> myDreamDiaries;
    Context context;
    AppDatabase db;

    public DreamDiaryAdapter(List<DreamDiary> myDreamDiaries, Context context) {
        this.myDreamDiaries = myDreamDiaries;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.dream_card, parent, false);
        DreamDiaryAdapter.ViewHolder viewHolder = new DreamDiaryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DreamDiary dreamDiary = myDreamDiaries.get(position);
        db = AppDatabase.getInstance(context);

        holder.usernameDream.setText(db.userDao().userFromId(dreamDiary.getUserCreatorId()).get(0).getUsername());
        holder.titleStory.setText(dreamDiary.getTitle());
        holder.storyDream.setText(dreamDiary.getStory());
        holder.diaryDate.setText(dreamDiary.getDateInsert());
        holder.numberLike.setText(String.valueOf(dreamDiary.getLike()));

        List<DreamTag> dreamTags = db.dreamTagDao().getDreamTag(dreamDiary.getDreamId());
        dreamTags.forEach( dreamTag -> {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Chip chip = (Chip) layoutInflater.inflate(R.layout.single_input_chip_layout, null, false);
            chip.setText(dreamTag.getTagName());
            holder.chipGroup.addView(chip);
        });

        List<DreamFavorite> dreamFavorites = db.dreamFavoriteDao().getIfIsFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId());
        if(!dreamFavorites.isEmpty()){
            holder.loveAnimation.setFrame(35);
            holder.loveAnimation.setMinAndMaxFrame(35, 75);
        }else{
            holder.loveAnimation.setFrame(0);
            holder.loveAnimation.setMinAndMaxFrame(0, 35);
        }

        holder.loveAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DreamFavorite> dreamFav = db.dreamFavoriteDao().getIfIsFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId());
                if(dreamFav.isEmpty()){
                    db.dreamFavoriteDao().insertAll(new DreamFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId()));
                    holder.loveAnimation.setMinAndMaxFrame(0, 35);
                }else{
                    db.dreamFavoriteDao().removeFromFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId());
                    holder.loveAnimation.setMinAndMaxFrame(35, 75);
                }
                holder.loveAnimation.playAnimation();
            }
        });

        holder.likeAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int like = dreamDiary.getLike();
                if(!holder.isLiked){
                    like++;
                    holder.likeAnimation.setMinAndMaxFrame(25, 81);
                    holder.isLiked = true;
                }else{
                    like--;
                    holder.likeAnimation.setMinAndMaxFrame(106, 148);
                    holder.isLiked = false;
                }
                holder.likeAnimation.playAnimation();
                db.dreamDiaryDao().setNumberLiked(like, dreamDiary.getDreamId());
                holder.numberLike.setText(String.valueOf(like));
            }
        });

    }

    @Override
    public int getItemCount() {
        return myDreamDiaries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameDream;
        LottieAnimationView loveAnimation;
        TextView titleStory;
        TextView diaryDate;
        ChipGroup chipGroup;
        LottieAnimationView likeAnimation;
        TextView numberLike;
        TextView storyDream;
        boolean isLiked;


        public ViewHolder(View view) {
            super(view);
            chipGroup = view.findViewById(R.id.chipGroupDreamCard);
            usernameDream = view.findViewById(R.id.username_dream);
            loveAnimation = view.findViewById(R.id.loveAnimation);
            titleStory = view.findViewById(R.id.titleStory);
            diaryDate = view.findViewById(R.id.diaryDate);
            likeAnimation = view.findViewById(R.id.likeAnimation);
            numberLike = view.findViewById(R.id.numberLike);
            storyDream = view.findViewById(R.id.storyDream);
            isLiked = false;
        }

    }
}
