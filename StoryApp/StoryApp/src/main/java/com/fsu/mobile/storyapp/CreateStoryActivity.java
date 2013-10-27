package com.fsu.mobile.storyapp;

/**
 * Created by davides on 10/27/13.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
public class CreateStoryActivity extends Activity
        implements CreateSettingsDialog.CreateSettingsDialogListener{
    private String Title;
    private String Story;
    private String Pnumber;
    private String Max_words;
    private String Next_contributor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.create_view);

        //get phone number
        Pnumber = getMyPhoneNumber();

        Button createStory_Button = (Button)findViewById(R.id.createButton);
        Button cancelStory_Button = (Button)findViewById(R.id.cancelButton);
        createStory_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSettingsDialog dialog = new CreateSettingsDialog();
                Title = null;
                Story = null;
                Title = ((EditText)findViewById(R.id.newStoryTitle)).getText().toString().trim();
                Story = ((EditText)findViewById(R.id.newStoryText)).getText().toString().trim();

                if (Title.isEmpty() || Story.isEmpty())
                    Toast.makeText(getApplicationContext(),"Everything must be filled in!",Toast.LENGTH_LONG).show();
                else
                    dialog.show(getFragmentManager(),"settingsDialog");
            }
        });

        cancelStory_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getMyPhoneNumber(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr.getLine1Number() != null)
            return mTelephonyMgr.getLine1Number().substring(1);
        else
            return "0000000000";
    }

    public void onDialogClick(String maxWords, String nextContributor)
    {
        Max_words = "";
        Next_contributor = "";
        Max_words = maxWords;
        Next_contributor = nextContributor;
        if (Max_words.isEmpty() || Next_contributor.isEmpty())
            Toast.makeText(getApplicationContext(),"Everything must be filled in!",Toast.LENGTH_LONG).show();
        else {
            //createStory();
            String amp = "&";
            String base = "http://myligaapi.elementfx.com/teleApp/editStory.php";//?flag=add&" +
            // "story=";
            String url = base +"?flag=add" + amp +
                    "story=" + Story + amp +
                    "words_max="+ Max_words + amp +
                    "next=" + Next_contributor + amp +
                    "creator=" + Pnumber + amp +
                    "title=" + Title;
            url = url.replaceAll(" ", "%20");
            new ProcessingTask().execute(url);
        }
    }

    protected class ProcessingTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String ... urls) {
            String flag = "0";
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(urls[0]);

            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    Log.e(CreateStoryActivity.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                JSONObject jsonObject = new JSONObject(builder.toString());
                flag = jsonObject.getString("flag");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return flag;
        }
        @Override
        protected void onPreExecute(){
            setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected void onPostExecute(String result) {
            setProgressBarIndeterminateVisibility(false);
            if (result.equals("1"))
                Toast.makeText(getApplicationContext(),"Story created successfully",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Error creating story, try again later",Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
