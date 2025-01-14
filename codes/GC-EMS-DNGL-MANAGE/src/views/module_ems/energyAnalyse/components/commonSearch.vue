<template>
  <div class="table-page-search-wrapper search-compoments">
    <a-form :model="queryParams" ref="queryForm" layout="inline">
      <a-row :gutter="24">
        <a-col :xl="6" :lg="7" :md="8" :sm="24">
          <a-form-item label="区域" prop="spaceId">
            <j-tree-select
              ref="treeSelect"
              placeholder="请选择上级"
              v-model="queryParams.spaceId"
              dict="base_space,name,id"
              pidField="parentid"
              pidValue="0"
              hasChildField="has_child"
            >
            </j-tree-select>
          </a-form-item>
        </a-col>
        <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="type !== 'nodate'">
          <a-form-item label="日期" prop="date">
            <a-range-picker
              v-if="type === 'daterange'"
              :placeholder="['开始月份', '结束月份']"
              format="YYYY-MM"
              value-format="yyyy-MM"
              v-model="queryParams.date"
              :mode="datarangeMode"
              @panelChange="handlePanelChange"
            />
            <a-month-picker
              v-if="type === 'month'"
              v-model="queryParams.yearMonth"
              value-format="yyyy-MM"
              placeholder="选择月"
            />
            <a-date-picker
              v-if="type === 'day'"
              v-model="queryParams.day"
              format="YYYY-MM-DD"
              value-format="yyyy-MM-DD"
              placeholder="选择日期"
              >
            </a-date-picker>
          </a-form-item>
        </a-col>
        <a-col :xl="6" :lg="7" :md="8" :sm="24">
          <a-button type="primary" icon="search" @click="handleQuery">搜索</a-button>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>

<script>
import moment from 'moment'
import { httpAction, getAction } from '@/api/manage'
export default {
  name: '',
  props: {
    type: {
      type: String,
      default: null,
    },
    hasAreaSearch: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      datarangeMode: ['month', 'month'],
      showSearch: false,
      queryParams: {
        spaceId: '288348631531521',
        beginTime: null, // 只有能耗总览不需要
        endTime: null, // 只有能耗总览不需要
        date: [],
        yearMonth: null, // 节能分析
        day: null, // 分项能耗
      },
      params: {
        column: 'createTime',
        order: 'desc',
        hasQuery: 'false',
        field: 'id,parentid,name',
        pageNo: '1',
        pageSize: '100',
      },
      url: {},
    }
  },
  created() {
    this.getNowDate()
  },
  mounted() {},
  methods: {
    /**
     * 查询当天日期
     */
    getNowDate() {
      const nowTime = new Date()
      const year = nowTime.getFullYear()
      let month = nowTime.getMonth() + 1
      let day = nowTime.getDate()
      month = month < 10 ? '0' + month : month
      day = day < 10 ? '0' + day : day
      // const NOW_MONTHS_AGO = `${year}-${month}`
      this.queryParams.beginTime = `${year}-01`
      this.queryParams.endTime = `${year}-${month}`
      this.queryParams.date = [`${year}-01`, `${year}-${month}`]
      this.queryParams.yearMonth = `${year}-${month}`
      this.queryParams.day = `${year}-${month}-${day}`
    },
    changeShow() {
      this.showSearch = !this.showSearch
    },
    /** 搜索按钮操作 */
    handleQuery() {
      // console.log(this.queryParams)
      this.queryParams.beginTime = moment(this.queryParams.date[0]).format('yyyy-MM')
      this.queryParams.endTime = moment(this.queryParams.date[1]).format('yyyy-MM')
      this.$emit('search', this.queryParams)
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.date = []
      this.handleQuery()
    },
    // 月份区间选择
    handlePanelChange(value, mode){
      this.queryParams.date = value
      this.mode = [mode[0] === 'date' ? 'month' : mode[0], mode[1] === 'date' ? 'month' : mode[1]];
    }
  },
}
</script>

<style lang="less" scoped>
.search-compoments {
  background: #fff;
  padding-top: 14px;
  padding-left: 12px;
  margin-bottom: 10px;
  box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
  // .search-icon {
  //   position: absolute;
  //   top: -10px;
  //   right: -10px;
  //   width: 20px;
  //   height: 20px;
  //   font-size: 20px;
  //   color: #fff;
  //   background-color: #1890ff;
  // }
  // .search-container {
  //   box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  //   padding-top: 12px;
  //   margin-bottom: 10px;
  //   .a-form-item--small.a-form-item {
  //     margin-bottom: 14px;
  //   }
  // }
  // .item-echarts {
  //   min-height: calc(100vh - 174px);
  //   padding: 10px;
  //   box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  // }
}
</style>
