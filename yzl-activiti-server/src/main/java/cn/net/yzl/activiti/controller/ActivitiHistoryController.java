package cn.net.yzl.activiti.controller;


import cn.net.yzl.activiti.service.IHistoryService;
import cn.net.yzl.common.entity.ComResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


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

}
