package org.jeecg.modules.baseSpace.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * ClassName: SpaceDto
 * Package: org.jeecg.modules.baseSpace.entity
 * Description:后端传送前端的区域信息类
 *
 * @author 品讯科技
 * @Create 2024/2/20 14:42
 * @Version 1.0
 */
@Data
public class SpaceDto extends Space {
    /** 父区域名称 */
    private String parentName;
}
