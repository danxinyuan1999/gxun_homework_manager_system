package com.yf.exam.modules.paper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName("el_paper")
public class Paper extends Model<Paper> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("depart_id")
    private String departId;

    @TableField("exam_id")
    private String examId;

    private String title;

    @TableField("total_time")
    private Integer totalTime;

    @TableField("user_time")
    private Integer userTime;

    @TableField("total_score")
    private Integer totalScore;

    @TableField("qualify_score")
    private Integer qualifyScore;

    @TableField("obj_score")
    private Integer objScore;

    @TableField("subj_score")
    private Integer subjScore;

    @TableField("user_score")
    private Integer userScore;

    @TableField("has_saq")
    private Boolean hasSaq;

    private Integer state;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("limit_time")
    private Date limitTime;
}
