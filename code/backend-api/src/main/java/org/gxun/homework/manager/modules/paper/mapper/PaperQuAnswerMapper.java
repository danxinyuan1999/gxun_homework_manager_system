package com.yf.exam.modules.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.paper.dto.ext.PaperQuAnswerExtDTO;
import com.yf.exam.modules.paper.entity.PaperQuAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperQuAnswerMapper extends BaseMapper<PaperQuAnswer> {

    List<PaperQuAnswerExtDTO> list(@Param("paperId") String paperId, @Param("quId") String quId);
}
