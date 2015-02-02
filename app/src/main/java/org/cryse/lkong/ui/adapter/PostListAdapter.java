package org.cryse.lkong.ui.adapter;

import android.content.Context;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cryse.lkong.R;
import org.cryse.lkong.model.PostModel;
import org.cryse.lkong.model.converter.ModelConverter;
import org.cryse.lkong.utils.UIUtils;
import org.cryse.lkong.utils.htmltextview.HtmlTagHandler;
import org.cryse.lkong.utils.htmltextview.HtmlTextUtils;
import org.cryse.lkong.utils.htmltextview.UrlImageGetter;
import org.cryse.utils.DateFormatUtils;
import org.cryse.widget.recyclerview.RecyclerViewBaseAdapter;
import org.cryse.widget.recyclerview.RecyclerViewHolder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostListAdapter extends RecyclerViewBaseAdapter<PostModel> {
    private final String mTodayPrefix;
    private OnItemReplyClickListener mOnItemReplyClickListener;
    public PostListAdapter(Context context, List<PostModel> mItemList) {
        super(context, mItemList);
        mTodayPrefix = getString(R.string.datetime_today);
    }

    public void setOnItemReplyClickListener(OnItemReplyClickListener onItemReplyClickListener) {
        this.mOnItemReplyClickListener = onItemReplyClickListener;
    }

    @Override
    public RecyclerViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_post, parent, false);
        return new ViewHolder(v, mOnItemReplyClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder)holder;
            Object item = getObjectItem(position);
            if(item instanceof PostModel) {
                PostModel postModel = (PostModel)item;

                UrlImageGetter urlImageGetter = new UrlImageGetter(getContext(), viewHolder.mMessageTextView)
                        .setEmoticonSize(UIUtils.getSpDimensionPixelSize(getContext(), R.dimen.text_size_body1))
                        .setPlaceHolder(R.drawable.ic_default_avatar)
                        .setError(R.drawable.ic_default_avatar);
                Spanned spannedText = HtmlTextUtils.htmlToSpanned(postModel.getMessage(), urlImageGetter, new HtmlTagHandler());
                viewHolder.mMessageTextView.setText(spannedText);
                viewHolder.mMessageTextView.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.mAuthorTextView.setText(postModel.getAuthorName());
                viewHolder.mDatelineTextView.setText(DateFormatUtils.formatFullDateDividByToday(postModel.getDateline(), mTodayPrefix));
                viewHolder.mOrdinalTextView.setText(getString(R.string.format_post_ordinal, postModel.getOrdinal()));
                Picasso.with(getContext())
                        .load(ModelConverter.uidToAvatarUrl(postModel.getAuthorId()))
                        .error(R.drawable.ic_default_avatar)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(viewHolder.mAuthorAvatarImageView);
            }
        }
    }

    public static class ViewHolder extends RecyclerViewHolder {
        // each data item is just a string in this case
        @InjectView(R.id.recyclerview_item_post_textview_author_name)
        TextView mAuthorTextView;
        @InjectView(R.id.recyclerview_item_post_textview_dateline)
        TextView mDatelineTextView;
        @InjectView(R.id.recyclerview_item_post_textview_ordinal)
        TextView mOrdinalTextView;
        @InjectView(R.id.recyclerview_item_post_textview_message)
        TextView mMessageTextView;
        @InjectView(R.id.recyclerview_item_post_imageview_author_avatar)
        ImageView mAuthorAvatarImageView;

        @InjectView(R.id.recyclerview_item_post_button_replay)
        Button mReplyButton;

        OnItemReplyClickListener mOnItemReplyClickListener;
        public ViewHolder(View v, OnItemReplyClickListener onItemReplyClickListener) {
            super(v);
            ButterKnife.inject(this, v);
            mOnItemReplyClickListener = onItemReplyClickListener;
            mReplyButton.setOnClickListener(view -> {
                if(mOnItemReplyClickListener != null) {
                    mOnItemReplyClickListener.onReplyClick(view, getPosition());
                }
            });
        }
    }

    public interface OnItemReplyClickListener {
        public void onReplyClick(View view, int position);
    }
}