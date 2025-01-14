package org.jeecg.modules.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 品讯科技
 * @since 2018-12-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 删除角色与用户关系
     * @author 品讯科技
     * @date 2024-08
     * @param roleId
     */
    @Delete("delete from sys_user_role where role_id = #{roleId}")
    void deleteRoleUserRelation(@Param("roleId") String roleId);


    /**
     * 删除角色与权限关系
     * @author 品讯科技
     * @param roleId
     * @date 2024-08
     */
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermissionRelation(@Param("roleId") String roleId);

}
