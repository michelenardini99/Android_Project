package com.example.dairys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;
import com.example.dairys.Database.DreamFavorite;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DreamDiary dreamDiary = myDreamDiaries.get(position);

        db = AppDatabase.getInstance(context);

        holder.usernameDream.setText(db.userDao().userFromId(dreamDiary.getUserCreatorId()).get(0).getUsername());
        holder.titleStory.setText(dreamDiary.getTitle());
        holder.storyDream.setText(dreamDiary.getStory());
        SimpleDateFormat spf= new SimpleDateFormat("dd/MM/yyyy");
        holder.diaryDate.setText(spf.format(dreamDiary.getDateInsert()));
        holder.numberLike.setText(dreamDiary.getLike());

        List<DreamFavorite> dreamFavorites = db.dreamFavoriteDao().getIfIsFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId());
        if(!dreamFavorites.isEmpty()){
            holder.loveAnimation.setFrame(35);
        }else{
            holder.loveAnimation.setFrame(0);
        }
        holder.likeAnimation.setFrame(0);

        holder.loveAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.loveAnimation.playAnimation();
                db.dreamFavoriteDao().insertAll(new DreamFavorite(dreamDiary.getDreamId(), db.userDao().userLogged().get(0).getId()));
            }
        });

        holder.likeAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.likeAnimation.playAnimation();
                int like = dreamDiary.getLike();
                like++;
                db.dreamDiaryDao().setNumberLiked(like, dreamDiary.getDreamId());
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
        LottieAnimationView likeAnimation;
        TextView numberLike;
        TextView storyDream;


        public ViewHolder(View view) {
            super(view);
            usernameDream = view.findViewById(R.id.username_dream);
            loveAnimation = view.findViewById(R.id.loveAnimation);
            titleStory = view.findViewById(R.id.titleStory);
            diaryDate = view.findViewById(R.id.diaryDate);
            likeAnimation = view.findViewById(R.id.likeAnimation);
            numberLike = view.findViewById(R.id.numberLike);
            storyDream = view.findViewById(R.id.storyDream);
        }

    }
}
