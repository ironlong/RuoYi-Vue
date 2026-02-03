package com.ruoyi.kangderui.service;

import java.util.List;
import com.ruoyi.kangderui.domain.SalaryDetail;

/**
 * 员工工资明细Service接口
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
public interface ISalaryDetailService 
{
    /**
     * 查询员工工资明细
     * 
     * @param salaryDetailId 员工工资明细主键
     * @return 员工工资明细
     */
    public SalaryDetail selectSalaryDetailBySalaryDetailId(Long salaryDetailId);

    /**
     * 查询员工工资明细列表
     * 
     * @param salaryDetail 员工工资明细
     * @return 员工工资明细集合
     */
    public List<SalaryDetail> selectSalaryDetailList(SalaryDetail salaryDetail);

    /**
     * 新增员工工资明细
     * 
     * @param salaryDetail 员工工资明细
     * @return 结果
     */
    public int insertSalaryDetail(SalaryDetail salaryDetail);

    /**
     * 修改员工工资明细
     * 
     * @param salaryDetail 员工工资明细
     * @return 结果
     */
    public int updateSalaryDetail(SalaryDetail salaryDetail);

    /**
     * 批量删除员工工资明细
     * 
     * @param salaryDetailIds 需要删除的员工工资明细主键集合
     * @return 结果
     */
    public int deleteSalaryDetailBySalaryDetailIds(Long[] salaryDetailIds);

    /**
     * 删除员工工资明细信息
     * 
     * @param salaryDetailId 员工工资明细主键
     * @return 结果
     */
    public int deleteSalaryDetailBySalaryDetailId(Long salaryDetailId);
}
