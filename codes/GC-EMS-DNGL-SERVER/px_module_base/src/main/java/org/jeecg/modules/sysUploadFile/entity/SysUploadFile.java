package org.jeecg.modules.sysUploadFile.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: ems_sys_upload_file
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
@Data
@TableName("base_upload_file")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="base_upload_file对象", description="base_upload_file")
public class SysUploadFile implements Serializable {
    private static final long serialVersionUID = 1L;

	/**文件ID*/
	@Excel(name = "文件ID", width = 15)
    @ApiModelProperty(value = "文件ID")
    private java.lang.Long fileid;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private java.lang.String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
	/**关联ID*/
	@Excel(name = "关联ID", width = 15)
    @ApiModelProperty(value = "关联ID")
    private java.lang.String relid;
	/**储存地址*/
	@Excel(name = "储存地址", width = 15)
    @ApiModelProperty(value = "储存地址")
    private java.lang.String url;
	/**原文件名*/
	@Excel(name = "原文件名", width = 15)
    @ApiModelProperty(value = "原文件名")
    private java.lang.String filename;
	/**扩展名*/
	@Excel(name = "扩展名", width = 15)
    @ApiModelProperty(value = "扩展名")
    private java.lang.String extname;
	/**文件大小*/
	@Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
    private java.lang.Integer filesize;

    public SysUploadFile(String relId, String remark) {
        this.relid = relId;
        this.setRemark(remark);
    }

    public SysUploadFile() {
    }
}
