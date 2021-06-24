package com.yf.exam.modules.paper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.exam.core.api.ApiRest;
import com.yf.exam.core.api.controller.BaseController;
import com.yf.exam.core.api.dto.BaseIdReqDTO;
import com.yf.exam.core.api.dto.BaseIdRespDTO;
import com.yf.exam.core.api.dto.BaseIdsReqDTO;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.utils.BeanMapper;
import com.yf.exam.modules.paper.dto.PaperDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.yf.exam.modules.paper.dto.request.PaperAnswerDTO;
import com.yf.exam.modules.paper.dto.request.PaperCreateReqDTO;
import com.yf.exam.modules.paper.dto.request.PaperListReqDTO;
import com.yf.exam.modules.paper.dto.request.PaperQuQueryDTO;
import com.yf.exam.modules.paper.dto.response.ExamDetailRespDTO;
import com.yf.exam.modules.paper.dto.response.ExamResultRespDTO;
import com.yf.exam.modules.paper.dto.response.PaperListRespDTO;
import com.yf.exam.modules.paper.entity.Paper;
import com.yf.exam.modules.paper.service.PaperService;
import com.yf.exam.modules.user.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Api(tags={"作业"})
@RestController
@RequestMapping("/exam/api/paper/paper")
public class PaperController extends BaseController {

    @Autowired
    private PaperService baseService;

    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody PaperDTO reqDTO) {
        Paper entity = new Paper();
        BeanMapper.copy(reqDTO, entity);
        baseService.saveOrUpdate(entity);
        return super.success(new BaseIdRespDTO(entity.getId()));
    }

    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = { RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = { RequestMethod.POST})
    public ApiRest<PaperDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        Paper entity = baseService.getById(reqDTO.getId());
        PaperDTO dto = new PaperDTO();
        BeanUtils.copyProperties(entity, dto);
        return super.success(dto);
    }

    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<PaperListRespDTO>> paging(@RequestBody PagingReqDTO<PaperListReqDTO> reqDTO) {

        IPage<PaperListRespDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }



    @ApiOperation(value = "创建作业")
    @RequestMapping(value = "/create-paper", method = { RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody PaperCreateReqDTO reqDTO) {
        String paperId = baseService.createPaper(UserUtils.getUserId(), reqDTO.getExamId());
        return super.success(new BaseIdRespDTO(paperId));
    }

    @ApiOperation(value = "作业详情")
    @RequestMapping(value = "/paper-detail", method = { RequestMethod.POST})
    public ApiRest<ExamDetailRespDTO> paperDetail(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        ExamDetailRespDTO respDTO = baseService.paperDetail(reqDTO.getId());
        return super.success(respDTO);
    }

    @ApiOperation(value = "作业详情")
    @RequestMapping(value = "/qu-detail", method = { RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> quDetail(@RequestBody PaperQuQueryDTO reqDTO) {
        //根据ID删除
        PaperQuDetailDTO respDTO = baseService.findQuDetail(reqDTO.getPaperId(), reqDTO.getQuId());
        return super.success(respDTO);
    }

    @ApiOperation(value = "填充答案")
    @RequestMapping(value = "/fill-answer", method = { RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> fillAnswer(@RequestBody PaperAnswerDTO reqDTO) {
        //根据ID删除
        baseService.fillAnswer(reqDTO);
        return super.success();
    }


    @ApiOperation(value = "交作业操作")
    @RequestMapping(value = "/hand-exam", method = { RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> handleExam(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        baseService.handExam(reqDTO.getId());
        return super.success();
    }
    @ApiOperation(value = "作业详情")
    @RequestMapping(value = "/paper-result", method = { RequestMethod.POST})
    public ApiRest<ExamResultRespDTO> paperResult(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        ExamResultRespDTO respDTO = baseService.paperResult(reqDTO.getId());
        return super.success(respDTO);
    }

    @ApiOperation(value = "提交作答")
    @RequestMapping(value = "/review-paper", method = { RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> reviewPaper(@RequestBody ExamResultRespDTO reqDTO) {
        //根据ID删除
        baseService.reviewPaper(reqDTO);
        return super.success();
    }
}
