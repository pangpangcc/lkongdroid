package org.cryse.lkong.utils.htmltextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.cryse.lkong.utils.ScaleTransformation;

import java.lang.ref.WeakReference;

public class ClickableImageSpan extends DynamicDrawableSpan {
    private AsyncTargetDrawable mDrawable;
    private WeakReference<Context> mContext;
    private String mSource;
    private String mSourceMiddle;
    private int mPlaceHolderRes;
    private int mErrorRes;
    private int mMaxWidth;
    private int mMaxHeight;
    private Object mIdentityTag;
    private Object mPicassoTag;
    private WeakReference<ImageSpanContainer> mContainer;

    /**
     * @param verticalAlignment one of {@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM} or
     * {@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ClickableImageSpan(
            Context context,
            ImageSpanContainer container,
            Object identityTag,
            Object picassoTag,
            String source,
            @DrawableRes int placeholderRes,
            @DrawableRes int errorRes,
            int maxWidth,
            int maxHeight,
            int verticalAlignment
    ) {
        super(verticalAlignment);
        mContext = new WeakReference<Context>(context);
        mContainer = new WeakReference<ImageSpanContainer>(container);
        mIdentityTag = identityTag;
        mPicassoTag = picassoTag;
        mSource = source;
        if(source.contains("sinaimg"))
            mSourceMiddle = source.replace("/large/", "/bmiddle/");
        else
            mSourceMiddle = source;
        mPlaceHolderRes = placeholderRes;
        mErrorRes = errorRes;
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        mDrawable = new AsyncTargetDrawable(mContext.get(), mContainer.get(), mIdentityTag, mMaxWidth, mMaxHeight);
        // Picasso.with(context).load(mPlaceHolderRes).tag(mPicassoTag).error(mErrorRes).placeholder(mPlaceHolderRes).into(mDrawable);
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = null;

        if (mDrawable != null) {
            drawable = mDrawable;
        } else {
            Log.e("ClickableImageSpan", "drawable is null");
        }
        return drawable;
    }

    /**
     * Returns the source string that was saved during construction.
     */
    public String getSource() {
        return mSource;
    }

    public void loadImage(ImageSpanContainer container) {
        mContainer = new WeakReference<ImageSpanContainer>(container);
        mDrawable.setContainer(container);
        Picasso.with(mContext.get()).load(mSourceMiddle).tag(mPicassoTag).error(mErrorRes).placeholder(mPlaceHolderRes).transform(new ScaleTransformation(mMaxWidth, mMaxHeight, Color.WHITE)).into((AsyncTargetDrawable) mDrawable);
    }
}
