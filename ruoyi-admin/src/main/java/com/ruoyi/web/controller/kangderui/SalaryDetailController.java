package com.ruoyi.web.controller.kangderui;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.kangderui.domain.SalaryDetail;
import com.ruoyi.kangderui.service.ISalaryDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工工资明细Controller
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
@RestController
@RequestMapping("/kangderui/detail")
public class SalaryDetailController extends BaseController
{
    @Autowired
    private ISalaryDetailService salaryDetailService;

    /**
     * 查询员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalaryDetail salaryDetail)
    {
        startPage();
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        return getDataTable(list);
    }

    /**
     * 导出员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:export')")
    @Log(title = "员工工资明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalaryDetail salaryDetail)
    {
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        ExcelUtil<SalaryDetail> util = new ExcelUtil<SalaryDetail>(SalaryDetail.class);
        util.exportExcel(response, list, "员工工资明细数据");
    }

    /**
     * 获取员工工资明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:query')")
    @GetMapping(value = "/{salaryDetailId}")
    public AjaxResult getInfo(@PathVariable("salaryDetailId") Long salaryDetailId)
    {
        return success(salaryDetailService.selectSalaryDetailBySalaryDetailId(salaryDetailId));
    }

    /**
     * 新增员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:add')")
    @Log(title = "员工工资明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalaryDetail salaryDetail)
    {
        return toAjax(salaryDetailService.insertSalaryDetail(salaryDetail));
    }

    /**
     * 修改员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:edit')")
    @Log(title = "员工工资明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalaryDetail salaryDetail)
    {
        return toAjax(salaryDetailService.updateSalaryDetail(salaryDetail));
    }

    /**
     * 删除员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:remove')")
    @Log(title = "员工工资明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{salaryDetailIds}")
    public AjaxResult remove(@PathVariable Long[] salaryDetailIds)
    {
        return toAjax(salaryDetailService.deleteSalaryDetailBySalaryDetailIds(salaryDetailIds));
    }
}
