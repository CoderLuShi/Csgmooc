package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pojo.Tasks;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Administrator
 */
public class Login {
    public List<Tasks> login(){
        //登录接口
        final String loginUri="https://www.csgmooc.com/uniBaseApi/portal/login/passwordLogin";
        //获取新taken接口
        final String getUserDetail="https://www.csgmooc.com/uni-gateway/jxapi2/auth/getUserDetail";
        //获取学期信息接口
        final String getGradeList="https://www.csgmooc.com/uni-gateway/jxapi2//teacher/teachtask/getGradeList";
        //获取作业信息接口
        final String getTasks="https://www.csgmooc.com/uni-gateway/jxapi2//student/task/gettaskmodule";
        //authtoken
        String authtoken=null;
        //学生ID
        String stuid=null;
        //学期ID
        String gid=null;
        //任务实体对象列表
        List<Tasks> tasks =null;
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        //创建请求体
        HttpPost httpPost = new HttpPost();
        HttpGet httpGet = new HttpGet();
        CloseableHttpResponse response;
        httpPost.setURI(URI.create(loginUri));
        //构造请求参数
        JSONObject params = new JSONObject();
        params.put("username","学号");
        params.put("password","密码");
        params.put("schoolNum","GWNH9iO0ts0=");
        try {
            //设置请求参数
            httpPost.setEntity(new StringEntity(params.toString()));
            //设置请求头
            httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            //发送请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                System.out.println("登录成功");
                //获取authtoken
                authtoken = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8")).getJSONObject("data").getString("authtoken");
            }else {
                System.out.println("登录失败！状态码："+response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URIBuilder uriBuilder=new URIBuilder(getUserDetail);
            uriBuilder.addParameter("authType","0");
            uriBuilder.addParameter("timeStamap",String.valueOf(System.currentTimeMillis()));
            httpGet.setURI(uriBuilder.build());
            //设置请求头将authtoken参数信息携带
            httpGet.setHeader("authtoken",authtoken);
            httpGet.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode()==200){
                String responseData = EntityUtils.toString(response.getEntity(), "UTF-8");
                authtoken = JSONObject.parseObject(responseData).getJSONObject("data").getString("authtoken");
                stuid = JSONObject.parseObject(responseData).getJSONObject("data").getString("userId");
                System.out.println("获取成功！新的token:  "+authtoken);
                System.out.println("获取学生ID:"+stuid);
            }else {
                System.out.println("获取新token时失败");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        try {
            URIBuilder uriBuilder=new URIBuilder(getGradeList);
            uriBuilder.addParameter("tag","0");
            uriBuilder.addParameter("timeStamap",String.valueOf(System.currentTimeMillis()));
            httpGet.setURI(uriBuilder.build());
            //设置请求头将authtoken参数信息携带
            httpGet.setHeader("authtoken",authtoken);
            httpGet.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode()==200){
                gid = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8")).getJSONArray("data").getJSONObject(0).getString("id");
                System.out.println("最新学期ID："+gid);
            }else {
                System.out.println("获取学期ID时失败");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


        try {
            URIBuilder uriBuilder=new URIBuilder(getTasks);
            uriBuilder.addParameter("stuid",stuid);
            uriBuilder.addParameter("gid",gid);
            uriBuilder.addParameter("ttid","");
            uriBuilder.addParameter("sclassid","");
            uriBuilder.addParameter("key","");
            uriBuilder.addParameter("taskTypeJson","");
            uriBuilder.addParameter("sublastType","");
            uriBuilder.addParameter("type","0");
            uriBuilder.addParameter("pcFlag","0");
            uriBuilder.addParameter("timeStamap",String.valueOf(System.currentTimeMillis()));
            httpGet.setURI(uriBuilder.build());
            //设置请求头将authtoken参数信息携带
            httpGet.setHeader("authtoken",authtoken);
            httpGet.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode()==200){
                String responseData = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(responseData);
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("taskModuleInfos");
                tasks = JSON.parseArray(jsonArray.toString(), Tasks.class);
            }else {
                System.out.println("获取作业信息时失败");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }
}
