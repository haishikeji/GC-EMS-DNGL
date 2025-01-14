package org.jeecg.modules.sysAutoCodeRule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.sysAutoCodeRule.entity.SysAutoCodeRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: ems_sys_auto_code_rule
 * @@author 品讯科技
 * @Date:   2024-01-10
 * @Version: V1.0
 */
public interface SysAutoCodeRuleMapper extends BaseMapper<SysAutoCodeRule> {


    List<SysAutoCodeRule> selectSysAutoCodeResultList(SysAutoCodeRule param);
}
