<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="工资所属期" prop="salaryPeriod">
        <el-date-picker
          v-model="queryParams.salaryPeriod"
          type="month"
          value-format="yyyy年MM月"
          format="yyyy年MM月"
          placeholder="请选择工资所属期"
          clearable
        />
      </el-form-item>
      <el-form-item class="search-actions">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="salary-cards">
      <div v-for="(row, index) in detailList" :key="row.salaryDetailId || index" class="salary-card">
        <div class="card-header">
          <div class="title">{{ row.nickName }} - {{ row.salaryPeriod }}</div>
          <el-tag size="mini" :type="isConfirmed(row) ? 'success' : 'warning'">
            {{ isConfirmed(row) ? "已确认" : "未确认" }}
          </el-tag>
        </div>
        <div
          class="card-section"
          v-if="hasVisibleFields(row, ['totalEarnings', 'netSalary'])"
        >
          <div class="section-title">汇总</div>
          <div class="kv">
            <div class="kv-item" v-if="shouldShowField(row, 'totalEarnings')">
              <span class="label">应发金额</span>
              <span class="value">{{ row.totalEarnings }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'netSalary')">
              <span class="label">实发金额</span>
              <span class="value">{{ row.netSalary }}</span>
            </div>
          </div>
        </div>
        <div
          class="card-section"
          v-if="hasVisibleFields(row, ['basicSalary', 'basicDailySalary', 'basicWorkDays', 'basicSubtotal'])"
        >
          <div class="section-title">基本工资</div>
          <div class="kv">
            <div class="kv-item" v-if="shouldShowField(row, 'basicSalary')">
              <span class="label">基本工资</span>
              <span class="value">{{ row.basicSalary }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'basicDailySalary')">
              <span class="label">日工资</span>
              <span class="value">{{ row.basicDailySalary }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'basicWorkDays')">
              <span class="label">工作日</span>
              <span class="value">{{ row.basicWorkDays }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'basicSubtotal')">
              <span class="label">小计</span>
              <span class="value">{{ row.basicSubtotal }}</span>
            </div>
          </div>
        </div>
        <div
          class="card-section"
          v-if="hasVisibleFields(row, ['allowanceFullAttendance', 'allowanceSafety', 'allowanceSeniority', 'allowancePosition', 'allowanceFloating', 'allowanceConfidentiality', 'allowanceTransportation','allowanceOnduty', 'allowanceSpecialCertificate', 'allowanceHoliday', 'allowancePerformance', 'allowanceSafetyTraining', 'allowanceAssessment', 'allowanceOther', 'allowanceSubtotal'])"
        >
          <div class="section-title">考核/其它应发工资</div>
          <div class="kv">
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceFullAttendance')">
              <span class="label">全勤奖</span>
              <span class="value">{{ row.allowanceFullAttendance }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceSafety')">
              <span class="label">安全奖</span>
              <span class="value">{{ row.allowanceSafety }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceSeniority')">
              <span class="label">工龄工资</span>
              <span class="value">{{ row.allowanceSeniority }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowancePosition')">
              <span class="label">职务工资</span>
              <span class="value">{{ row.allowancePosition }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceFloating')">
              <span class="label">浮动工资</span>
              <span class="value">{{ row.allowanceFloating }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceConfidentiality')">
              <span class="label">保密工资</span>
              <span class="value">{{ row.allowanceConfidentiality }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceTransportation')">
              <span class="label">交通补贴</span>
              <span class="value">{{ row.allowanceTransportation }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceOnduty')">
              <span class="label">值班补贴</span>
              <span class="value">{{ row.allowanceOnduty }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceSpecialCertificate')">
              <span class="label">特种作业证补贴</span>
              <span class="value">{{ row.allowanceSpecialCertificate }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceHoliday')">
              <span class="label">节假日补贴</span>
              <span class="value">{{ row.allowanceHoliday }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowancePerformance')">
              <span class="label">工作表现奖</span>
              <span class="value">{{ row.allowancePerformance }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceSafetyTraining')">
              <span class="label">安全培训补贴</span>
              <span class="value">{{ row.allowanceSafetyTraining }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceHighTemperature')">
              <span class="label">高温费补贴</span>
              <span class="value">{{ row.allowanceHighTemperature }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceAssessment')">
              <span class="label">绩效考核奖</span>
              <span class="value">{{ row.allowanceAssessment }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceOther')">
              <span class="label">其它应发</span>
              <span class="value">{{ row.allowanceOther }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'allowanceSubtotal')">
              <span class="label">其它应发小计</span>
              <span class="value">{{ row.allowanceSubtotal }}</span>
            </div>
          </div>
        </div>
        <div
          class="card-section"
          v-if="hasVisibleFields(row, ['overtimeDays', 'overtimeAmount', 'overtimeMidShiftDays', 'overtimeMidShiftAmount', 'overtimeNightShiftDays', 'overtimeNightShiftAmount'])"
        >
          <div class="section-title">加班</div>
          <div class="kv">
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeDays')">
              <span class="label">加班天数</span>
              <span class="value">{{ row.overtimeDays }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeAmount')">
              <span class="label">加班金额</span>
              <span class="value">{{ row.overtimeAmount }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeMidShiftDays')">
              <span class="label">中班天数</span>
              <span class="value">{{ row.overtimeMidShiftDays }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeMidShiftAmount')">
              <span class="label">中班补贴金额</span>
              <span class="value">{{ row.overtimeMidShiftAmount }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeNightShiftDays')">
              <span class="label">夜班天数</span>
              <span class="value">{{ row.overtimeNightShiftDays }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'overtimeNightShiftAmount')">
              <span class="label">夜班补贴金额</span>
              <span class="value">{{ row.overtimeNightShiftAmount }}</span>
            </div>
          </div>
        </div>
        <div
          class="card-section"
          v-if="hasVisibleFields(row, ['deductionDiscipline', 'deductionTax', 'deductionHousingFund', 'deductionInsurance', 'deductionWithhold','deductionOther, 'deductionSubtotal'])"
        >
          <div class="section-title">应扣/代扣代缴</div>
          <div class="kv">
            <div class="kv-item" v-if="shouldShowField(row, 'deductionDiscipline')">
              <span class="label">违纪扣款</span>
              <span class="value">{{ row.deductionDiscipline }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionTax')">
              <span class="label">个人所得税</span>
              <span class="value">{{ row.deductionTax }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionHousingFund')">
              <span class="label">代扣公积金</span>
              <span class="value">{{ row.deductionHousingFund }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionInsurance')">
              <span class="label">代扣保险</span>
              <span class="value">{{ row.deductionInsurance }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionWithhold')">
              <span class="label">暂扣工资</span>
              <span class="value">{{ row.deductionWithhold }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionOther')">
              <span class="label">其它应扣</span>
              <span class="value">{{ row.deductionOther }}</span>
            </div>
            <div class="kv-item" v-if="shouldShowField(row, 'deductionSubtotal')">
              <span class="label">应扣小计</span>
              <span class="value">{{ row.deductionSubtotal }}</span>
            </div>
          </div>
        </div>
        <div class="card-actions">
          <el-button
            v-if="!isConfirmed(row)"
            size="mini"
            type="primary"
            @click="handleConfirm(row)"
            v-hasPermi="['kangderui:detail:confirm']"
          >确认</el-button>
        </div>
      </div>
    </div>

    <el-table
      v-loading="loading"
      :data="detailList"
      border
      stripe
      size="small"
      :max-height="520"
      class="salary-table"
    >
      <el-table-column label="基础信息" align="center">
        <el-table-column label="主键ID" align="center" prop="salaryDetailId" v-if="false" />
        <el-table-column label="员工ID" align="center" prop="userId" v-if="false" />
        <el-table-column label="姓名" align="center" prop="nickName" fixed="left" width="100" />
<!--        <el-table-column label="部门名称" align="center" prop="deptName" fixed="left" width="140" show-overflow-tooltip />-->
        <el-table-column label="工资所属期" align="center" prop="salaryPeriod" fixed="left" width="110" />
        <el-table-column label="确认状态" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{ isConfirmed(scope.row) ? "已确认" : "未确认" }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="基本工资" align="center">
        <el-table-column label="基本工资" align="center" prop="basicSalary" />
        <el-table-column label="日工资" align="center" prop="basicDailySalary" />
        <el-table-column label="工作日" align="center" prop="basicWorkDays" />
        <el-table-column label="基本工资小计" align="center" prop="basicSubtotal" />
      </el-table-column>
      <el-table-column label="考核/其它应发工资" align="center">
        <el-table-column label="全勤奖" align="center" prop="allowanceFullAttendance" />
        <el-table-column label="安全奖" align="center" prop="allowanceSafety" />
        <el-table-column label="工龄工资" align="center" prop="allowanceSeniority" />
        <el-table-column label="职务工资" align="center" prop="allowancePosition" />
        <el-table-column label="浮动工资" align="center" prop="allowanceFloating" />
        <el-table-column label="保密工资" align="center" prop="allowanceConfidentiality" />
        <el-table-column label="交通补贴" align="center" prop="allowanceTransportation" />
        <el-table-column label="值班补贴" align="center" prop="allowanceOnduty" />
        <el-table-column label="特种作业证补贴" align="center" prop="allowanceSpecialCertificate" />
        <el-table-column label="节假日补贴" align="center" prop="allowanceHoliday" />
        <el-table-column label="工作表现奖" align="center" prop="allowancePerformance" />
        <el-table-column label="安全培训补贴" align="center" prop="allowanceSafetyTraining" />
        <el-table-column label="高温费补贴" align="center" prop="allowanceHighTemperature" />
        <el-table-column label="绩效考核奖" align="center" prop="allowanceAssessment" />
        <el-table-column label="其它应发" align="center" prop="allowanceOther" />
        <el-table-column label="其它应发小计" align="center" prop="allowanceSubtotal" />
      </el-table-column>
      <el-table-column label="加班" align="center">
        <el-table-column label="加班天数" align="center" prop="overtimeDays" />
        <el-table-column label="加班金额" align="center" prop="overtimeAmount" />
        <el-table-column label="中班天数" align="center" prop="overtimeMidShiftDays" />
        <el-table-column label="中班补贴金额" align="center" prop="overtimeMidShiftAmount" />
        <el-table-column label="夜班天数" align="center" prop="overtimeNightShiftDays" />
        <el-table-column label="夜班补贴金额" align="center" prop="overtimeNightShiftAmount" />
      </el-table-column>
      <el-table-column label="应扣/代扣代缴" align="center">
        <el-table-column label="违纪扣款" align="center" prop="deductionDiscipline" />
        <el-table-column label="个人所得税" align="center" prop="deductionTax" />
        <el-table-column label="代扣公积金" align="center" prop="deductionHousingFund" />
        <el-table-column label="代扣保险" align="center" prop="deductionInsurance" />
        <el-table-column label="暂扣工资" align="center" prop="deductionWithhold" />
        <el-table-column label="其它应扣" align="center" prop="deductionOther" />
        <el-table-column label="应扣小计" align="center" prop="deductionSubtotal" />
      </el-table-column>
      <el-table-column label="汇总" align="center">
        <el-table-column label="应发金额" align="center" prop="totalEarnings" min-width="100" />
        <el-table-column label="实发金额" align="center" prop="netSalary" min-width="100" />
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="80">
        <template slot-scope="scope">
          <el-button
            v-if="!isConfirmed(scope.row)"
            size="small"
            type="primary"
            @click="handleConfirm(scope.row)"
            v-hasPermi="['kangderui:detail:confirm']"
            >确认</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listMyDetail, confirmDetail } from "@/api/kangderui/detail"

export default {
  name: "MySalary",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      detailList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        salaryPeriod: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listMyDetail(this.queryParams).then(response => {
        this.detailList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(error => {
        this.loading = false
        this.handlePasswordRedirect(error)
      })
    },
    handlePasswordRedirect(error) {
      const message = error && error.message ? error.message : error
      if (message === "请先修改初始密码后再查看工资") {
        this.$router.push({ name: "Profile", params: { activeTab: "resetPwd" } })
        return true
      }
      return false
    },
    isConfirmed(row) {
      return String(row.remark) === "1"
    },
    shouldShowValue(value, keepZero = false) {
      if (value === null || value === undefined) {
        return false
      }
      const normalized = String(value).trim().replace(/,/g, "")
      if (normalized === "") {
        return false
      }
      const numericValue = Number(normalized)
      if (!Number.isNaN(numericValue)) {
        return keepZero ? true : numericValue !== 0
      }
      return true
    },
    isAmountField(fieldKey) {
      const nonAmountFields = ["basicWorkDays", "overtimeDays", "overtimeMidShiftDays", "overtimeNightShiftDays"]
      return !nonAmountFields.includes(fieldKey)
    },
    shouldShowField(row, fieldKey) {
      return this.shouldShowValue(row[fieldKey], this.isAmountField(fieldKey))
    },
    hasVisibleFields(row, fieldKeys) {
      return fieldKeys.some(key => this.shouldShowField(row, key))
    },
    handleConfirm(row) {
      const salaryDetailId = row.salaryDetailId
      this.$modal.confirm("确认工资无误后将无法撤销，是否继续？").then(() => {
        return confirmDetail(salaryDetailId)
      }).then(() => {
        this.$modal.msgSuccess("确认成功")
        this.getList()
      }).catch(() => {})
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    }
  }
}
</script>

<style scoped>
.salary-cards {
  display: none;
}

.salary-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  gap: 8px;
}

.card-header .title {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.card-section {
  padding: 6px 0;
  border-top: 1px dashed #ebeef5;
}

.card-section:first-of-type {
  border-top: none;
}

.section-title {
  font-size: 12px;
  color: #909399;
  margin-bottom: 6px;
}

.kv {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 12px;
}

.kv-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 12px;
}

.kv-item .label {
  color: #303133;
}

.kv-item .value {
  color: #909399;
  font-weight: 500;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

@media (max-width: 768px) {
  .salary-cards {
    display: block;
  }

  .salary-table {
    display: none;
  }

  .app-container {
    padding: 12px;
  }

  .card-header .title {
    font-size: 18px;
  }

  .section-title {
    font-size: 15px;
  }

  .kv-item {
    font-size: 16px;
  }

  .kv-item .label {
    font-size: 16px;
  }

  .app-container .el-form--inline .el-form-item {
    display: block;
    margin-right: 0;
  }

  .app-container .el-form--inline .el-form-item__content {
    width: 100%;
  }

  .app-container .el-date-editor {
    width: 100%;
  }

  .search-actions .el-form-item__content {
    display: flex;
    gap: 8px;
  }

  .search-actions .el-button {
    flex: 1;
    font-size: 14px;
  }
}
</style>
