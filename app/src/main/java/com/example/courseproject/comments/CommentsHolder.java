package com.example.courseproject.comments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseproject.R;
import com.example.courseproject.model.Comment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommentsHolder extends RecyclerView.ViewHolder {
    private TextView mText;
    private TextView mAuthor;
    private TextView mTimestamp;

    public CommentsHolder(@NonNull View itemView) {
        super(itemView);
        mText = itemView.findViewById(R.id.tv_comment_text);
        mAuthor = itemView.findViewById(R.id.tv_comment_author);
        mTimestamp = itemView.findViewById(R.id.tv_comment_date);
    }

    public void bind(Comment item) {
        mText.setText(item.getText());
        mAuthor.setText(item.getAuthor());


        DateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH); //parsing date
        try {
            Date comment = compareFormat.parse(item.getTimestamp());
            String mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment); //split parsed date
            String [] splittedDate = mFormat.split(" ");

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1); //current time -1 day
            Date current = calendar.getTime();

            if(current.getTime()  > comment.getTime()) {  //compare current and parsed date
                mTimestamp.setText(splittedDate[0]);
            }
            else {
                mTimestamp.setText(splittedDate[1]);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}