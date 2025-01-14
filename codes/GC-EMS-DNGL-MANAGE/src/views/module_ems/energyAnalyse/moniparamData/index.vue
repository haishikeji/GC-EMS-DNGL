<template>
  <a-spin :spinning="loading">
    <div class="demand-analysis" :loading="loading">
      <div class="table-page-search-wrapper search-compoments">
        <a-form-model ref="form" :model="queryParam" layout="inline" @keyup.enter.native="searchQuery" :rules="validatorRules">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="设备名称" prop="equipmentid">
                <j-search-select-tag v-model="queryParam.equipmentid" placeholder="请选择设备" dict="tpm_equipment,equipmentname,id"  />
              </a-form-model-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-model-item label="能源参数" prop="queryparamtype">
                <a-select v-model="queryParam.queryparamtype" placeholder="请选择能源参数" @change="typeChange">
                  <a-select-option
                    v-for="item in options"
                    :key="item"
                    :value="item"
                  >{{item}}</a-select-option>
                </a-select>
                <!-- <j-search-select-tag v-model="queryParam.equipmentid" placeholder="请选择能源参数" dict="tpm_equipment,equipmentname,id"  /> -->
              </a-form-model-item>
            </a-col>
            <a-col :xl="6" :lg="11" :md="12" :sm="24">
              <a-form-model-item label="时间" prop="date">
                <a-range-picker
                format="YYYY-MM-DD"
                value-format="yyyy-MM-DD"
                v-model="queryParam.date"
                :disabled-date="setDisabledDate"
                @openChange="openChange"
                @calendarChange="onCalendarChange"
                />
                <!-- date-format="YYYY-MM-DD" -->
                <!-- @change="onChangeDate"  -->
              </a-form-model-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="getMoniparamData" icon="search">查询</a-button>
                <!-- <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button> -->
              </span>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
      <div class="item-echarts" ref="echarts" :style="{ height: echartsHeight + 'px' }"></div>
    </div>
  </a-spin>
</template>

<script>
import moment from 'moment';
import * as echarts from 'echarts'
import { httpAction, getAction } from '@/api/manage'
import { emsdata } from '../emsdata'
export default {
  name: 'moniparamData',
  data() {
    return {
      description: '参数历史查询',
      loading: false,
      queryParam: {
        equipmentid: '287813167808513',
        queryparamtype: '',
        date: [],
      },
      echartsHeight: window.innerHeight - 84 - 60 - 30,
      validatorRules: {
        equipmentid: [
          { required: true, message: '查询前请先选择设备!'},
        ],
        queryparamtype: [
          { required: true, message: '查询前请先选择能源参数!'},
        ],
        date: [
          {
            type: 'array',
            required: true,
            message: '查询前请先选择时间!',
            fields: {
              // tpye类型试情况而定,所以如果返回的是date就改成date,如果返回的是string就改成string
              0: { type: 'string', required: true, message: '查询前请先选择开始日期' },
              1: { type: 'string', required: true, message: '查询前请先选择结束日期' },
            },
          },
        ],
      },
      url: {
        nycsList: '/dataVoltage/dataVoltage/xialaList',
        list: '/dataVoltage/dataVoltage/dataList',
      },
      moniparamData: {},
      myChart: null,
      options: [],
      disabledCurrent: null, // 用来判断日期不超过2天的第一个数
      echartsColor: ['#91CC75', '#EE6666', '#FAAD14', '#5470C6'],
      seriesData: [],
      xAxisData: [],
      legendData: [],
    }
  },
  created() {
  },
  mounted() {
    this.getNycsList()
    this.getNowDate()
    // this.$nextTick(() => {
    //   this.getMoniparamData()
    // })
  },
  methods: {
    // 获取能源参数下拉列表
    getNycsList(){
      getAction(this.url.nycsList, null).then((res) => {
        this.options = res
        this.queryParam.queryparamtype = this.options[0]
      }).finally(()=>{
        this.getMoniparamData()
      })
    },
    // 能源参数改变时
    typeChange(value){
      console.log(value)
      // 手动更新视图
      this.$forceUpdate()
    },
    // 时间处理
    // onChangeDate(date, dateString){
    //   console.log(date, dateString);
    //   this.queryParam.logtimebegin= dateString[0]
    //   this.queryParam.logtimeend= dateString[1]
    // },
    /**
     * 获取手动选择的时间段起始值
     */
    onCalendarChange (dates) {
      // 获取手动选择的时间段起始值
      this.disabledCurrent = dates[0]
      this.queryParam.logtimebegin= dates[0]
      this.queryParam.logtimeend= dates[1]
      this.queryParam.date = dates
      // 手动更新视图
      this.$forceUpdate()
    },
    // 动态渲染禁用日期
    setDisabledDate(current) {
      // 限制为前后一天 || // 今天以后的不可选
      if (!this.disabledCurrent) return (current && current > moment().endOf('day'))
      return (current && current < moment(this.disabledCurrent).subtract(1, "days") || current > moment(this.disabledCurrent).add(1, "days")) || (current && current > moment().endOf('day'))
    },
    // 关闭日期选择器后清空判断值
    openChange(status){
      if (!status) this.disabledCurrent = null
    },
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
      this.queryParam.date = [`${year}-${month}-${day}`, `${year}-${month}-${day}`]
      this.queryParam.logtimebegin = `${year}-${month}-${day}`
      this.queryParam.logtimeend = `${year}-${month}-${day}`
    },
    // searchReset(){
    //   this.queryParam = {}
    //   this.getMoniparamData()
    // },
    getMoniparamData() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          getAction(this.url.list, this.queryParam).then((response) => {
            if(response.success){
              var data = response.result
              this.seriesData = data.filter(res=> res.name !== 'logtime').map((item, index)=> {
              var itemData = {
                name: item.name,
                type: 'line',
                symbol: 'none',
                sampling: 'lttb',
                itemStyle: {
                  color: this.echartsColor[index],
                },
                markPoint: {
                  data: [
                    { type: 'max', name: 'Max' },
                    { type: 'min', name: 'Min' },
                  ],
                },
                data: item.data,
              }
              return itemData
            })
            console.log(this.seriesData)
            this.xAxisData = data.find(res=> res.name === 'logtime').data
            console.log(this.xAxisData)
            this.legendData = data.map(res=> res.name).filter(res=> res !== 'logtime')
            console.log(this.legendData)
            }else{
              this.$message.warning(response.message);
            }
          }).finally(() => {
            this.initEcharts()
            this.loading = false
          })
        }
      })
    },
    initEcharts() {
      var _this = this
      var chartDom = _this.$refs.echarts
      _this.myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '20%']
          },
        },
        title: {
          left: 'left',
          text: '参数历史数据',
        },
        legend: {
          data: _this.legendData,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: _this.xAxisData,
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%'],
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100,
            xAxisIndex: 0,
          },
          {
            start: 0,
            end: 10,
          },
          {
            show: true,
            yAxisIndex: 0,
            filterMode: "empty",
            width: 30,
            height: "80%",
            right: "7%",
          },
        ],
        series: _this.seriesData,
      }

      option && _this.myChart.setOption(option, true)

      window.addEventListener('resize', function () {
        _this.myChart.resize()
      })
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.demand-analysis {
  min-height: calc(100vh - 84px);
  padding: 0px 10px 10px 10px;
  .search-container {
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding-top: 12px;
    margin-bottom: 10px;
    .el-form-item--small.el-form-item {
      margin-bottom: 14px;
    }
  }
  .item-echarts {
    // min-height: calc(100vh - 174px);
    padding: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    background-color: white;
  }
}
.search-compoments {
  background: #fff;
  padding-top: 14px;
  padding-left: 12px;
  margin-bottom: 10px;
  box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
}
</style>
