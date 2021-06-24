package com.yf.exam.modules.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.yf.exam.modules.paper.entity.PaperQu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperQuMapper extends BaseMapper<PaperQu> {

    int sumObjective(@Param("paperId") String paperId);
    int sumSubjective(@Param("paperId") String paperId);
    List<PaperQuDetailDTO> listByPaper(@Param("paperId") String paperId);
}


