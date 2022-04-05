package service;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SendMessage {
    public int send(String message) {
        int statusCode=1;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://IP:5600/send_group_msg");
        //构造请求参数
        JSONObject params = new JSONObject();
        params.put("group_id", "群号");
        params.put("message", message);
        try {
            //设置请求参数,设置Charset.forName("UTF-8")避免中文请求乱码
            httpPost.setEntity(new StringEntity(params.toString(), StandardCharsets.UTF_8));
            //设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            String responseData = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject responseJSON = JSONObject.parseObject(responseData);
            statusCode=responseJSON.getInteger("retcode");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (statusCode==0){
            return 0;
        }else {
            return 1;
        }
    }
}
