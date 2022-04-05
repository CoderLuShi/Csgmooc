package service;

import pojo.Tasks;
import utils.SQLiteUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
public class Job {
    public void job() {
        Login login = new Login();
        List<Tasks> tasks = login.login();
        SendMessage sendMessage = new SendMessage();
        Connection connection = null;
        PreparedStatement preInsert = null;
        PreparedStatement preQuery = null;
        String insertSql = "INSERT INTO TASKS(TMID, TT_NAME, TM_NAME, TC_NAME, DEADLINE, SEND)" +
                "VALUES(?,?,?,?,?,?) ";
        String querySql = "SELECT COUNT(*) AS NUM FROM TASKS WHERE TMID=?";
        try {
            connection = SQLiteUtil.getConnection();
            preInsert = connection.prepareStatement(insertSql);
            preQuery = connection.prepareStatement(querySql);
            for (Tasks task : tasks) {
                preQuery.setLong(1, task.getTmid());
                ResultSet resultSet = preQuery.executeQuery();
                resultSet.next();
                if (resultSet.getInt("NUM") == 0) {
                    preInsert.setLong(1, task.getTmid());
                    preInsert.setString(2, task.getTt_name());
                    preInsert.setString(3, task.getTm_name());
                    preInsert.setString(4, task.getTc_name());
                    preInsert.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(task.getDeadLine())));
                    String message = "[CQ:at,qq=all]\r"+
                            "课程名称：" + task.getTt_name() + "\r"
                            + "作业名称：" + task.getTm_name() + "\r"
                            + "课程节次：" + task.getTc_name() + "\r"
                            + "截止时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(task.getDeadLine()));
                    int statusCode = sendMessage.send(message);
                    if (statusCode == 0) {
                        System.out.println("提醒推送成功！");
                    }
                    preInsert.setInt(6, statusCode);
                    int i = preInsert.executeUpdate();
                    if (i > 0) {
                        System.out.println("作业信息插入成功！");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preInsert != null) {
                try {
                    preInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preQuery != null) {
                try {
                    preQuery.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
