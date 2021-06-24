package com.yf.exam.modules.paper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
@TableName("el_paper_qu_answer")
public class PaperQuAnswer extends Model<PaperQuAnswer> {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("paper_id")
    private String paperId;

    @TableField("answer_id")
    private String answerId;

    @TableField("qu_id")
    private String quId;

    @TableField("is_right")
    private Boolean isRight;

    private Boolean checked;

    private Integer sort;

    private String abc;
    
}
