package org.jeecg.modules.sysUploadFile.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.sysUploadFile.entity.SysUploadFile;
import org.jeecg.modules.sysUploadFile.service.ISysUploadFileService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ems_sys_upload_file
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
@Api(tags="ems_sys_upload_file")
@RestController
@RequestMapping("/sysUploadFile/sysUploadFile")
@Slf4j
public class SysUploadFileController extends JeecgController<SysUploadFile, ISysUploadFileService> {
	@Autowired
	private ISysUploadFileService sysUploadFileService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysUploadFile
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ems_sys_upload_file-分页列表查询")
	@ApiOperation(value="ems_sys_upload_file-分页列表查询", notes="ems_sys_upload_file-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUploadFile>> queryPageList(SysUploadFile sysUploadFile,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysUploadFile> queryWrapper = QueryGenerator.initQueryWrapper(sysUploadFile, req.getParameterMap());
		Page<SysUploadFile> page = new Page<SysUploadFile>(pageNo, pageSize);
		IPage<SysUploadFile> pageList = sysUploadFileService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysUploadFile
	 * @return
	 */
	@AutoLog(value = "ems_sys_upload_file-添加")
	@ApiOperation(value="ems_sys_upload_file-添加", notes="ems_sys_upload_file-添加")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_upload_file:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysUploadFile sysUploadFile) {
		sysUploadFileService.save(sysUploadFile);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysUploadFile
	 * @return
	 */
	@AutoLog(value = "ems_sys_upload_file-编辑")
	@ApiOperation(value="ems_sys_upload_file-编辑", notes="ems_sys_upload_file-编辑")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_upload_file:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysUploadFile sysUploadFile) {
		sysUploadFileService.updateById(sysUploadFile);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ems_sys_upload_file-通过id删除")
	@ApiOperation(value="ems_sys_upload_file-通过id删除", notes="ems_sys_upload_file-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_upload_file:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysUploadFileService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ems_sys_upload_file-批量删除")
	@ApiOperation(value="ems_sys_upload_file-批量删除", notes="ems_sys_upload_file-批量删除")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_upload_file:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUploadFileService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ems_sys_upload_file-通过id查询")
	@ApiOperation(value="ems_sys_upload_file-通过id查询", notes="ems_sys_upload_file-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUploadFile> queryById(@RequestParam(name="id",required=true) String id) {
		SysUploadFile sysUploadFile = sysUploadFileService.getById(id);
		if(sysUploadFile==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysUploadFile);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysUploadFile
    */
    //@RequiresPermissions("org.jeecg.modules:ems_sys_upload_file:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysUploadFile sysUploadFile) {
        return super.exportXls(request, sysUploadFile, SysUploadFile.class, "ems_sys_upload_file");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ems_sys_upload_file:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysUploadFile.class);
    }

}
