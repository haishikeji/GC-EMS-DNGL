package org.jeecg.modules.cmmsUpkeepProject.controller;

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
import org.jeecg.modules.cmmsUpkeepProject.entity.CmmsUpkeepProject;
import org.jeecg.modules.cmmsUpkeepProject.service.ICmmsUpkeepProjectService;

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
 * @Description: 保养工作项目
 * @@author 品讯科技
 * @Date:   2024-01-10
 * @Version: V1.0
 */
@Api(tags="保养工作项目")
@RestController
@RequestMapping("/cmmsUpkeepProject/cmmsUpkeepProject")
@Slf4j
public class CmmsUpkeepProjectController extends JeecgController<CmmsUpkeepProject, ICmmsUpkeepProjectService> {
	@Autowired
	private ICmmsUpkeepProjectService cmmsUpkeepProjectService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cmmsUpkeepProject
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "保养工作项目-分页列表查询")
	@ApiOperation(value="保养工作项目-分页列表查询", notes="保养工作项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<CmmsUpkeepProject>> queryPageList(CmmsUpkeepProject cmmsUpkeepProject,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CmmsUpkeepProject> queryWrapper = QueryGenerator.initQueryWrapper(cmmsUpkeepProject, req.getParameterMap());
		Page<CmmsUpkeepProject> page = new Page<CmmsUpkeepProject>(pageNo, pageSize);
		IPage<CmmsUpkeepProject> pageList = cmmsUpkeepProjectService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cmmsUpkeepProject
	 * @return
	 */
	@AutoLog(value = "保养工作项目-添加")
	@ApiOperation(value="保养工作项目-添加", notes="保养工作项目-添加")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_project:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody CmmsUpkeepProject cmmsUpkeepProject) {
		cmmsUpkeepProjectService.save(cmmsUpkeepProject);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cmmsUpkeepProject
	 * @return
	 */
	@AutoLog(value = "保养工作项目-编辑")
	@ApiOperation(value="保养工作项目-编辑", notes="保养工作项目-编辑")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_project:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody CmmsUpkeepProject cmmsUpkeepProject) {
		cmmsUpkeepProjectService.updateById(cmmsUpkeepProject);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "保养工作项目-通过id删除")
	@ApiOperation(value="保养工作项目-通过id删除", notes="保养工作项目-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_project:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cmmsUpkeepProjectService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "保养工作项目-批量删除")
	@ApiOperation(value="保养工作项目-批量删除", notes="保养工作项目-批量删除")
	//@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_project:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cmmsUpkeepProjectService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "保养工作项目-通过id查询")
	@ApiOperation(value="保养工作项目-通过id查询", notes="保养工作项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<CmmsUpkeepProject> queryById(@RequestParam(name="id",required=true) String id) {
		CmmsUpkeepProject cmmsUpkeepProject = cmmsUpkeepProjectService.getById(id);
		if(cmmsUpkeepProject==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cmmsUpkeepProject);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cmmsUpkeepProject
    */
    //@RequiresPermissions("org.jeecg.modules:ems_cmms_upkeep_project:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmmsUpkeepProject cmmsUpkeepProject) {
        return super.exportXls(request, cmmsUpkeepProject, CmmsUpkeepProject.class, "保养工作项目");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ems_cmms_upkeep_project:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmmsUpkeepProject.class);
    }

}
