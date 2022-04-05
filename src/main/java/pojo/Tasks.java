/**
  * Copyright 2022 json.cn 
  */
package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {
    private int ttid;
    private long tcid;
    private long tmid;
    private long tm_subid;
    private int tm_type;
    private String tm_name;
    private String tt_name;
    private String tc_name;
    private long tm_sumlasttime;
    private long tm_residuetime;
    private int is_limit;
    private int task_setting;
    private int number;
    private String g_number;
    private int complete_number;
    private String g_complete_number;
    private Times times;
    private long deadLine;
    private int reformFlag;
    private String deadLineOfGroup;
    private String reformFlagOfGroup;
    private int stuTaskStatu;
    private String p_type;
    private int t_statu;
    private long sclassid;
    private int classid;
    private long homeworkid;
    private int task_statu;
    private String courseId;
}