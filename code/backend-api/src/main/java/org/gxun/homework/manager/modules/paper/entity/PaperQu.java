package com.yf.exam.modules.paper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("el_paper_qu")
public class PaperQu extends Model<PaperQu> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("paper_id")
    private String paperId;

    @TableField("qu_id")
    private String quId;

    @TableField("qu_type")
    private Integer quType;

    private Boolean answered;

    private String answer;

    private Integer sort;

    private Integer score;

    @TableField("actual_score")
    private Integer actualScore;

    @TableField("is_right")
    private Boolean isRight;

}
