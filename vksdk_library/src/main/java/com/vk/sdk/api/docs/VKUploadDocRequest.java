package com.vk.sdk.api.docs;

import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.util.VKJsonHelper;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

public class VKUploadDocRequest extends VKUploadDocBase {
    /**
     * Creates a VKUploadDocRequest instance.
     * @param doc file for upload to server
     */
    public VKUploadDocRequest(File doc) {
        super();
        this.mDoc = doc;
        Log.d("File name",mDoc.getName());
        this.mGroupId = 0;
    }

    /**
     * Creates a VKUploadDocRequest instance.
     * @param doc file for upload to server
     * @param groupId community ID (if the document will be uploaded to the community).
     */
    public VKUploadDocRequest(File doc, long groupId) {
        super();
        this.mDoc = doc;
        Log.d("File name",mDoc.getName());
        this.mGroupId = groupId;
    }

    @Override
    protected VKRequest getServerRequest() {
        if (mGroupId != 0)
            return VKApi.docs().getUploadServer(mGroupId);
        return VKApi.docs().getUploadServer();
    }

    /// ВОТ ТУТ вся проблема с наванием файла file
    @Override
    protected VKRequest getSaveRequest(JSONObject response) {
        Log.d("WWORK",response.toString());
        VKRequest saveRequest;
        try {
            Log.d("WWORK",VKJsonHelper.toMap(response).toString());
            //String file = VKJsonHelper.toMap(response).toString().substring(5,VKJsonHelper.toMap(response).toString().length()-1);
            Map<String,Object> file = VKJsonHelper.toMap(response);
            file.put("title",mDoc.getName());
            for (Map.Entry entry : file.entrySet()) {
                String key = (String) entry.getKey();
                String value = entry.getValue().toString();

                Log.d("K & V","value "+value+" key "+key);
                Log.d("value",value);
            }
            Log.d("WWO",file.toString());
            saveRequest = VKApi.docs().save(new VKParameters(file));
        } catch (JSONException e) {
            return null;
        }
        if (mGroupId != 0)
            saveRequest.addExtraParameters(VKUtil.paramsFrom(VKApiConst.GROUP_ID, mGroupId));
        return saveRequest;
    }
}
