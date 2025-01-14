package org.jeecg.modules.elecfeeAdd.controller;

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
import org.jeecg.modules.elecfeeAdd.entity.ElecfeeAdd;
import org.jeecg.modules.elecfeeAdd.service.IElecfeeAddService;

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
 * @Description: 附加费
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Api(tags="附加费")
@RestController
@RequestMapping("/elecfeeAdd/elecfeeAdd")
@Slf4j
public class ElecfeeAddController extends JeecgController<ElecfeeAdd, IElecfeeAddService> {
	@Autowired
	private IElecfeeAddService elecfeeAddService;

	/**
	 * 分页列表查询
	 *
	 * @param elecfeeAdd
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "附加费-分页列表查询")
	@ApiOperation(value="附加费-分页列表查询", notes="附加费-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ElecfeeAdd>> queryPageList(ElecfeeAdd elecfeeAdd,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ElecfeeAdd> queryWrapper = QueryGenerator.initQueryWrapper(elecfeeAdd, req.getParameterMap());
		Page<ElecfeeAdd> page = new Page<ElecfeeAdd>(pageNo, pageSize);
		IPage<ElecfeeAdd> pageList = elecfeeAddService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param elecfeeAdd
	 * @return
	 */
	@AutoLog(value = "附加费-添加")
	@ApiOperation(value="附加费-添加", notes="附加费-添加")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_add:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ElecfeeAdd elecfeeAdd) {
		elecfeeAddService.save(elecfeeAdd);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param elecfeeAdd
	 * @return
	 */
	@AutoLog(value = "附加费-编辑")
	@ApiOperation(value="附加费-编辑", notes="附加费-编辑")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_add:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ElecfeeAdd elecfeeAdd) {
		elecfeeAddService.updateById(elecfeeAdd);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "附加费-通过id删除")
	@ApiOperation(value="附加费-通过id删除", notes="附加费-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_add:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		elecfeeAddService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "附加费-批量删除")
	@ApiOperation(value="附加费-批量删除", notes="附加费-批量删除")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_add:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.elecfeeAddService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "附加费-通过id查询")
	@ApiOperation(value="附加费-通过id查询", notes="附加费-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ElecfeeAdd> queryById(@RequestParam(name="id",required=true) String id) {
		ElecfeeAdd elecfeeAdd = elecfeeAddService.getById(id);
		if(elecfeeAdd==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(elecfeeAdd);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param elecfeeAdd
    */
    //@RequiresPermissions("org.jeecg.modules:base_elecfee_add:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ElecfeeAdd elecfeeAdd) {
        return super.exportXls(request, elecfeeAdd, ElecfeeAdd.class, "附加费");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("base_elecfee_add:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ElecfeeAdd.class);
    }

}
