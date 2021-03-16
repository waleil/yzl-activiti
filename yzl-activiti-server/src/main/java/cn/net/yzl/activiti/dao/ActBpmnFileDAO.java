package cn.net.yzl.activiti.dao;

import cn.net.yzl.activiti.domain.entity.ActBpmnFile;
import cn.net.yzl.activiti.domain.entity.ActBpmnFileExample;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ActBpmnFileDAO继承基类
 */
@Mapper
public interface ActBpmnFileDAO extends MyBatisBaseDao<ActBpmnFile, Long, ActBpmnFileExample> {
}