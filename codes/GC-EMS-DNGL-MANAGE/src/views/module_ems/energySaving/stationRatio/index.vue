<template>
  <a-spin :spinning="loading">
    <div class="station-ratio">
      <commonSearch hasAreaSearch ref="commonSearch" type="nodate" @search="handleSearch"></commonSearch>
      <div class="common-container">
        <div class="common-title">能源站能效比趋势</div>
        <div class="echart-container" ref="elRatioTrend"></div>
      </div>
    </div>
  </a-spin>
</template>

<script>
import * as echarts from 'echarts'
import commonSearch from '../components/commonSearch'
import { httpAction, getAction } from '@/api/manage'
export default {
  name: '',
  components: {
    commonSearch,
  },
  data () {
    return {
      description: '能效对比',
      loading: false,
    }
  },
  created () {
  },
  mounted () {
    this.$nextTick(() => {
      this.initElRatioTrend()
      // this.getEnergyoverview()
    })
  },
  methods: {
    getEnergyoverview() {
      // {spaceId: 277797283102721}
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      getAction(this.url.list, data).then((res) => {
        if(res.success){
          // 获取数据
        }else{
          this.$message.warning(res.message);
        }
      }).finally(() => {
        this.$nextTick(() => {
          // this.initSortTop5()
          // this.initPieDianliang()
          // this.initTotalEleTrend()
        })
        this.loading = false;
      })
    },
    handleSearch(param) {
      this.getEnergyoverview()
    },
    // 能源站能效比趋势
    initElRatioTrend() {
      var _this = this
      var chartDom = this.$refs.elRatioTrend
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          bottom: 0,
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '25px',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: ['01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00', '24:00']
        },
        yAxis: {
          type: 'value'
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '2024-05-15',
            type: 'line',
            // color: '#FAC858',
            symbolSize: 0,
            data: [4.13, 4.23, 3.93, 4.43, 4.53, 4.13, 4.56, 5.06, 5.05, 4.87, 4.55, 4.13, 4.23, 3.93, 4.43, 4.53, 4.13, 4.56, 5.06, 5.05, 4.87, 4.55, 4.13, 4.23],
          },
          {
            name: '2024-05-16',
            type: 'line',
            // color: '#FAC858',
            symbolSize: 0,
            data: [4.13, 4.56, 5.06, 5.05, 4.87, 4.55, 4.13, 4.23, 3.93, 4.43, 4.13, 4.56, 5.06, 5.05, 4.87, 4.55, 4.13, 4.23, 3.93, 4.43, 4.13, 4.56, 5.06, 5.05],
          },
          {
            name: '2024-05-17',
            type: 'line',
            symbolSize: 0,
            // color: '#FAC858',
            data: [5.05, 4.87, 4.55, 4.13, 4.23, 3.93, 4.43, 4.23, 3.93, 4.43, 4.53, 5.05, 4.87, 4.55, 4.13, 4.23, 3.93, 4.43, 4.23, 3.93, 4.43, 4.53, 5.05, 4.87],
          },
        ]
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
  }
}
</script>

<style lang="less" scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.station-ratio{
  .common-container{
    background-color: #fff;
    padding: 12px;
    height: calc(100vh - 210px);
    .common-title{
      text-align: center;
      font-weight: 600;
      height: 30px;
      line-height: 30px;
    }
    .echart-container{
      height: calc(100% - 30px);
    }
  }
}
</style>
