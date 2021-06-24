package com.yf.exam.modules.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.exam.modules.paper.dto.PaperDTO;
import com.yf.exam.modules.paper.dto.request.PaperListReqDTO;
import com.yf.exam.modules.paper.dto.response.PaperListRespDTO;
import com.yf.exam.modules.paper.entity.Paper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperMapper extends BaseMapper<Paper> {

    IPage<PaperListRespDTO> paging(Page page, @Param("query") PaperListReqDTO query);

    List<PaperListRespDTO> list(@Param("query") PaperDTO query);
}
