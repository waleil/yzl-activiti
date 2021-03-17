package cn.net.yzl.activiti.controller;


import cn.net.yzl.activiti.service.IHistoryService;
import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "activiti历史")
@RestController
public class ActivitiHistoryController {

    @Autowired
    private IHistoryService historyService;

    /**
     * 流程明细
     * @return
     */
    @ApiOperation(value = "流程明细", notes = "流程明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", paramType = "query", dataType = "String", required = true, value = "流程实例id"),
    })
    @GetMapping(value = "/history/detail/{processInstanceId}")
    public ComResponse historyDetail(@PathVariable("processInstanceId") String processInstanceId) {
        return historyService.historyDetail(processInstanceId);
    }

    /**
     * 我发起的流程列表
     * @return
     */
    @ApiOperation(value = "我发起的流程列表", notes = "我发起的流程列表")
    @GetMapping(value = "/history/myCreated/{userName}")
    public ComResponse myCreated(@PathVariable("userName") String userName) {

        return historyService.myCreated(userName);
    }

    /**
     * 我审批的流程列表
     * @return
     */
    @ApiOperation(value = "我审批的流程列表", notes = "我审批的流程列表")
    @GetMapping(value = "/history/myApproved/{userName}")
    public ComResponse myApproved(@PathVariable("userName") String userName) {

        return historyService.myApproved(userName);
    }
}
