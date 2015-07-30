package org.cryse.lkong.logic.request;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.cryse.lkong.logic.HttpDelegate;
import org.cryse.lkong.logic.LKongWebConstants;
import org.cryse.lkong.model.PrivateMessageModel;
import org.cryse.lkong.model.converter.ModelConverter;
import org.cryse.lkong.utils.LKAuthObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GetPrivateMessagesRequest extends AbstractAuthedHttpRequest<List<PrivateMessageModel>> {
    private long mStartSortKey;
    private long mTargetUserId;
    public GetPrivateMessagesRequest(LKAuthObject authObject, long targetUserId, long startSortKey) {
        super(authObject);
        this.mStartSortKey = startSortKey;
        this.mTargetUserId = targetUserId;
    }

    public GetPrivateMessagesRequest(HttpDelegate httpDelegate, LKAuthObject authObject, long targetUserId, long startSortKey) {
        super(httpDelegate, authObject);
        this.mStartSortKey = startSortKey;
        this.mTargetUserId = targetUserId;
    }

    @Override
    protected Request buildRequest() {
        String url = String.format(LKongWebConstants.PRIVATE_MESSAGES_URL, mTargetUserId);
        url = url + (mStartSortKey >= 0 ? "&nexttime=" + Long.toString(mStartSortKey) : "");
        return new Request.Builder()
                .addHeader("Accept-Encoding", "gzip")
                .url(url)
                .build();
    }

    @Override
    protected List<PrivateMessageModel> parseResponse(Response response) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PrivateMessageModel> results = new ArrayList<PrivateMessageModel>();
        String responseString = gzipToString(response);
        JSONObject rootObject = new JSONObject(responseString);
        if(!rootObject.has("data")) {
            return results;
        }
        JSONArray jsonArray = rootObject.getJSONArray("data");

        int dataSetLength = jsonArray.length();
        for(int i = 0; i < dataSetLength; i++ ) {
            PrivateMessageModel model = new PrivateMessageModel();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.has("id"))
                model.setId(jsonObject.getString("id"));
            if(jsonObject.has("uid"))
                model.setUserId(jsonObject.getLong("uid"));
            if (jsonObject.has("msgfromid"))
                model.setMessageFromId(jsonObject.getInt("msgfromid"));
            if(jsonObject.has("message"))
                model.setMessage(jsonObject.getString("message"));
            if (jsonObject.has("dateline"))
                model.setDateline(dateFormat.parse(jsonObject.getString("dateline")));
            if(jsonObject.has("sortkey"))
                model.setSortKey(jsonObject.getLong("sortkey"));
            model.setAvatarUrl(ModelConverter.uidToAvatarUrl(model.getUserId()));
            results.add(model);
        }
        return results;
    }
}
