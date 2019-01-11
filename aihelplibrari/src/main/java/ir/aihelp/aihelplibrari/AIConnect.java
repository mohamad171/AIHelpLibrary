package ir.aihelp.aihelplibrari;

import android.content.Context;
import android.content.Intent;

public class AIConnect {
    String appId = null;
    String AdditionalFields = null;
    Context context;

    public AIConnect(Context context) {
        this.context = context;
    }

    public void AIinit(String appId, String additionalFields) {
        this.appId = appId;
        AdditionalFields = additionalFields;
    }

    public void AIopenChat(){
        if (appId != null && AdditionalFields != null){
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra("appId",appId);
            intent.putExtra("AdditionalFields",AdditionalFields);
            context.startActivity(intent);

        }else{
            throw new NullPointerException("Appid or AdditionalFields not Initialize , please use AIinit(appId,additionalFields) first");
        }
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAdditionalFields() {
        return AdditionalFields;
    }

    public void setAdditionalFields(String additionalFields) {
        AdditionalFields = additionalFields;
    }
}
