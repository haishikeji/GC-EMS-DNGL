package org.jeecg.modules.cmmsUpkeepItem.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsUpkeepItem.entity.CmmsUpkeepItemDto;

/**
 * @Description: 保养项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
public interface CmmsUpkeepItemMapper extends BaseMapper<CmmsUpkeepItem> {

    /**
     * 分列页表查询
     * 根据设备类型id查找设备类型名称并进行两表字段拼接
     *
     */
    public List<CmmsUpkeepItemDto> getAllCmmsUpkeepItem(Page<CmmsUpkeepItemDto> page, CmmsUpkeepItem cmmsUpkeepItem);

    /**
     * 根据id查询保养项
     *
     */
    public List<CmmsUpkeepItemDto> getCmmsUpkeepItemById(@Param("id") String id);

    /**
     * 根据保养计划id查找保养项
     *
     */
    public List<CmmsUpkeepItemDto> getCmmsUpkeepItemByPid(@Param("upkeepplanid") String upkeepplanid);

    /**
     * 根据设备id查找设备类型id以及设备类型名称，根据设备类型id查询保养项
     *
     */
    public List<CmmsUpkeepItemDto> getCmmsUpkeepItemByEqid(@Param("eqid") String eqid);

}
