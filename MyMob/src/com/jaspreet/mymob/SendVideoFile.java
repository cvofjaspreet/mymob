package com.jaspreet.mymob;

/**
@author Jaspreet 
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;





import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;





import android.annotation.SuppressLint;
import android.os.AsyncTask;


public class SendVideoFile extends AsyncTask<Void, Void, Void> {
	private File file;

	public SendVideoFile(File file) {
		this.file = file;
	
	}

	@SuppressLint("NewApi")
	@Override
	protected Void doInBackground(Void... params) {

		try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://192.168.0.106:9090/plugins/fileupload/userservice");

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
          
          
            if(!file.getPath().isEmpty()){
                FileBody filebodyVideo = new FileBody(new File(file.getPath()));
                reqEntity.addPart("jaspreet",filebodyVideo);
                StringBody body=new StringBody("sfds");
                reqEntity.addPart("abc",body);
            }
            postRequest.setEntity(reqEntity);
          //  postRequest.setEntity(new UrlEncodedFormEntity(postParameters));
            BasicHttpResponse response =  (BasicHttpResponse) httpClient.execute(postRequest);

			HttpEntity entity1=response.getEntity();
		    if(entity1 != null&&(response.getStatusLine().getStatusCode()==201||response.getStatusLine().getStatusCode()==200))
		    {
		         int sc = response.getStatusLine().getStatusCode();
		         String sl = response.getStatusLine().getReasonPhrase();
		         String response_string=convertToString(entity1.getContent());
		        org.json.JSONObject jsonObject=new org.json.JSONObject(response_string);
		       String url=jsonObject.getString("result");
		       
		    }
		    else
		    {}

        
           

        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        }
		
      
		

		
		return null;
	}

	private String convertToString(InputStream content) throws Exception{
		
		InputStreamReader inputStreamReader=new InputStreamReader(content);
		BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
		String s="";
		StringBuffer buffer=new StringBuffer();
		while ((s=bufferedReader.readLine())!=null) {
			buffer.append(s);
		}
			
	return buffer.toString();
}
}
