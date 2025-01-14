<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="日期">
              <a-date-picker v-model="queryParams.day" valueFormat="YYYY-MM-DD" placeholder="选择日期"> </a-date-picker>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="类型" prop="tagtype">
              <a-select v-model="queryParams.tagtype" placeholder="请选择">
                <a-select-option v-for="(value, key) of tageTypeList" :value="key" :key="key">{{ value }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item>
              <a-button type="primary" icon="search" @click="handleQuery">搜索</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="trend-charts" ref="trendCharts" :style="{ height: echartsHeight + 'px' }"></div>
  </a-card>
</template>

<script>
import * as echarts from 'echarts'
import { httpAction, getAction } from '@/api/manage'
export default {
  name: 'equipmentInfo',
  data() {
    return {
      loading: true,
      // 时间
      timeList: [],
      echartsHeight: window.innerHeight - 84 - 60 - 30,
      // 值
      tagvalueList: [],
      // 类型
      tageTypeList: {
        electricity: '电量',
        currentA: 'A相电流',
        currentB: 'B相电流',
        currentC: 'C相电流',
        voltageA: 'A相电压',
        voltageB: 'B相电压',
        voltageC: 'C相电压',
        // power: "功率",
      },
      equipmentid: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 1000,
        equipmentid: null,
        day: null,
        tagtype: null,
      },
      url: {
        list: '/emsStatistics/searchEquipmentDetail',
      },
    }
  },
  created() {
    this.queryParams.equipmentid = this.$route.params && this.$route.params.equipmentid
    // this.queryParams.day = "2023-08-15";
    this.getNowDate()
    this.queryParams.tagtype = 'electricity'
    this.handleQuery()
  },
  mounted() {
    // this.$nextTick(() => {
    //   this.handleQuery();
    // });
  },
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
      this.queryParams.day = `${year}-${month}-${day}`
    },
    handleQuery() {
      var _this = this
      if (this.queryParams.day == '' || this.queryParams.day == null) {
        this.$modal.msgError('请选择日期！')
        return false
      }
      _this.loading = true
      getAction(this.url.list, _this.queryParams).then((response) => {
        _this.timeList = []
        _this.tagvalueList = []
        response.result.forEach((item) => {
          _this.timeList.push(item.time)
          _this.tagvalueList.push(item.tagvalue)
        })
        // Generate mock data
        this.loading = false

        this.initEcharts()
      })
    },
    initEcharts() {
      var _this = this
      var chartDom = this.$refs.trendCharts
      var myChart = echarts.init(chartDom)
      var option

      var tagtypename = ''
      switch (_this.queryParams.tagtype) {
        case 'electricity':
          tagtypename = '累计电量(kW)'
          break
        case 'currentA':
          tagtypename = 'A相电流(A)'
          break
        case 'currentB':
          tagtypename = 'B相电流(A)'
          break
        case 'currentC':
          tagtypename = 'C相电流(A)'
          break
        case 'voltageA':
          tagtypename = 'A相电压(V)'
          break
        case 'voltageB':
          tagtypename = 'B相电压(V)'
          break
        case 'voltageC':
          tagtypename = 'C相电压(V)'
          break
        case 'power':
          tagtypename = '功率(kWH)'
          break
        default:
          break
      }
      option = {
        // Make gradient line here
        // visualMap: [
        //   {
        //     show: false,
        //     type: "continuous",
        //   },
        // ],
        title: [
          {
            left: 'center',
            text: tagtypename,
          },
        ],
        tooltip: {
          trigger: 'axis',
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
        ],
        xAxis: [
          {
          type: 'category',
          boundaryGap: false,
            data: _this.timeList,
          },
        ],
        yAxis: [{}],
        series: [
          {
            type: 'line',
            showSymbol: false,
            data: _this.tagvalueList,
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
          },
        ],
      }
      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less">
.trend-charts {
  padding: 10px;
  margin-top: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
