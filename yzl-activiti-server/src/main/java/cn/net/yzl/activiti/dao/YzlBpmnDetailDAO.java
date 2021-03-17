package cn.net.yzl.activiti.dao;

import cn.net.yzl.activiti.domain.entity.YzlBpmnDetail;
import cn.net.yzl.activiti.domain.entity.YzlBpmnDetailExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * YzlBpmnDetailDAO继承基类
 */
@Mapper
@Repository
public interface YzlBpmnDetailDAO extends MyBatisBaseDao<YzlBpmnDetail, Long, YzlBpmnDetailExample> {
}