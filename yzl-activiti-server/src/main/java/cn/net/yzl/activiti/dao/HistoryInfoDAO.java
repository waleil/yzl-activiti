package cn.net.yzl.activiti.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface HistoryInfoDAO {
    List<Map<String, Object>> selectMyProcessStarted(@Param("userName") String userName);


    List<Map<String, Object>> selectMyTasksCompleted(@Param("userName") String userName);
}
