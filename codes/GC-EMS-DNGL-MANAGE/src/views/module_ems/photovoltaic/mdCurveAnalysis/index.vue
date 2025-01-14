<template>
  <div class="demand-analysis" :loading="loading">
    <!-- <commonSearch ref="commonSearch" type="daterange" @search="handleSearch"></commonSearch> -->
    <div class="item-echarts" ref="echarts" :style="{ height: echartsHeight + 'px' }"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
// import commonSearch from '../components/commonSearch.vue'
import { httpAction, getAction } from '@/api/manage'
import { pvdata } from '../pvdata'
export default {
  name: 'mdCurveAnalysis',
  components: {
    // commonSearch,
  },
  data() {
    return {
      description: '多设备曲线分析',
      loading: true,
      echartsHeight: window.innerHeight - 84 - 60 - 30,
      myChart: null,
      url: {
        list: '/ems/statistics/demandanalysis',
      },
      times: [],
      gd02us: [],
      gd03us: [],
      gd05us: [],
      gd06us: [],
    }
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      setTimeout(() => {
        this.getTest()
      }, 1000)
      // this.getDemandanalysis()
    })
  },
  methods: {
    // 后端好了需要删掉
    getTest() {
      var _this = this;
      pvdata.mdcurveanalysisdata.forEach((item) => {
        item.data.forEach((i) => {
          if (item.meterName === 'GD02') {
            _this.times.push(i.Time.substring(11, 16));
            _this.gd02us.push(i.Ua.toFixed(1));
          }
          else if (item.meterName === 'GD03') {
            _this.gd03us.push(i.Ua.toFixed(1));
          }
          else if (item.meterName === 'GD05') {
            _this.gd05us.push(i.Ua.toFixed(1));
          }
          else if (item.meterName === 'GD06') {
            _this.gd06us.push(i.Ua.toFixed(1));
          }
        })
      })
      this.initEcharts()
    },
    async getDemandanalysis() {
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      getAction(this.url.list, data).then((response) => {
        console.log(response)
        this.monthsplit = response.data.monthsplit
        this.mubiao = response.data.mubiao
        this.shiji = response.data.shiji
        this.loading = false
      })
      this.initEcharts()
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
          data: ['GD02 U', 'GD03 U', 'GD05 U', 'GD06 U'],
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: _this.times,
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
            filterMode: 'empty',
            width: 30,
            height: '80%',
            right: '7%',
          },
        ],
        series: [
          {
            name: 'GD02 U',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#91CC75',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.gd02us,
          },
          {
            name: 'GD03 U',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#EE6666',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.gd03us,
          },
          {
            name: 'GD05 U',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#FAAD14',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.gd05us,
          },
          {
            name: 'GD06 U',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#5470C6',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.gd06us,
          },
        ],
      }

      option && _this.myChart.setOption(option)

      window.addEventListener('resize', function () {
        _this.myChart.resize()
      })
    },
    /** 搜索按钮操作 */
    handleSearch(param) {
      this.getTest()
      // this.getDemandanalysis()
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
  padding: 10px;
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
</style>
