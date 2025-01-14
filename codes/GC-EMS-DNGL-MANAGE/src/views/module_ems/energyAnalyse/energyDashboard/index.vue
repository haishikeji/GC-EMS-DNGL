<template>
  <a-spin :spinning="loading">
    <div class="energy-overView">
      <commonSearch hasAreaSearch ref="commonSearch" type="nodate" @search="handleSearch"></commonSearch>
      <div class="top-container u-flex">
        <div class="louyu u-flex common-box">
          <div class="common-title">区域基本信息</div>
          <img src="../../../../assets/louyu.png" alt="" />
          <a-descriptions :column="2">
            <a-descriptions-item label="建筑名称">{{ energySpace.name }}</a-descriptions-item>
            <a-descriptions-item label="建筑面积">{{ energySpace.builtarea }} ㎡</a-descriptions-item>
            <a-descriptions-item label="空调面积">{{ energySpace.aircondarea }} ㎡</a-descriptions-item>
            <a-descriptions-item label="采暖面积">{{ energySpace.heatarea }} ㎡</a-descriptions-item>
            <!-- <el-descriptions-item label="用能人数">{{energySpace.}}</el-descriptions-item> -->
            <!-- <el-descriptions-item label="建筑年代">{{energySpace.}}</el-descriptions-item> -->
            <!-- <el-descriptions-item label="建筑用途">{{energySpace.}}</el-descriptions-item> -->
          </a-descriptions>
        </div>
        <div class="sort-top5-container common-box">
          <div class="common-title">当月用电量TOP5(kWh)</div>
          <div class="sort-top5" ref="sortTop5"></div>
        </div>
        <div class="info-total">
          <div class="yongdian-xuliang common-box">
            <div class="common-title">用电需量(kVA)</div>
            <div class="info-container u-flex-jaa">
              <div class="item-info">
                <div class="name">即时需量</div>
                <div class="value">{{ demandStatistics.jishi }}</div>
              </div>
              <!-- <a-divider direction="vertical"></a-divider> -->
              <div class="item-info">
                <div class="name">需量峰值</div>
                <div class="value">{{ demandStatistics.fengzhi }}</div>
              </div>
              <!-- <a-divider direction="vertical"></a-divider> -->
              <div class="item-info">
                <div class="name">目标需量</div>
                <div class="value">{{ demandStatistics.mubiao }}</div>
              </div>
            </div>
          </div>
          <div class="u-flex co2-tianqi">
            <div class="co2 common-box">
              <div class="common-title">CO2排放</div>
              <div class="info-container u-flex-jaa">
                <div class="item-info">
                  <div class="name">本月排放(Kg)</div>
                  <div class="value">{{ demandStatistics.co2 }}</div>
                </div>
              </div>
            </div>
            <div class="tianqi common-box">
              <div class="common-title">环境信息</div>
              <div class="info-container u-flex-jaa">
                <div class="item-info">
                  <div class="name">湿度(%)</div>
                  <div class="value">{{ demandStatistics.shidu }}</div>
                </div>
                <!-- <a-divider direction="vertical"></a-divider> -->
                <div class="item-info">
                  <div class="name">温度(℃)</div>
                  <div class="value">{{ demandStatistics.wendu }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-container u-flex">
        <div class="xiaohao common-box">
          <div class="common-title">当月能源消耗总标煤(kg)</div>
          <div class="zongbiao-chart" ref="zongbiao"></div>
          <!-- <div class="legend">
            <div class="u-flex item" v-for="(item, index) in dataLists" :key="index">
              <div class="text">{{item.name}}</div>
              <div class="number">{{item.number}}tec</div>
              <div class="pieValue">{{item.pieValue}}%</div>
            </div>
          </div> -->
        </div>
        <div class="qushi common-box">
          <div class="common-title">总用电趋势图(kWh)</div>
          <div class="totalEle-trend" ref="totalEleTrend"></div>
        </div>
      </div>
    </div>
  </a-spin>
</template>

<script>
import * as echarts from 'echarts'
import commonSearch from '../components/commonSearch'
import { httpAction, getAction } from '@/api/manage'
// import { emsdata } from '../emsdata';
export default {
  name: 'energyDashboard',
  components: {
    commonSearch,
  },
  data() {
    return {
      description: '能耗总览',
      loading: true,
      energySpace: {},
      demandStatistics: {
        jishi: null,
        fengzhi: null,
        mubiao: null,
        co2: null,
        shidu: null,
        wendu: null,
      },
      dataLists: [
        { name: '空调用电', number: 80, pieValue: 20 },
        { name: '动力用电', number: 100, pieValue: 25 },
        { name: '照明用电', number: 140, pieValue: 35 },
        { name: '其他用电', number: 80, pieValue: 20 },
      ],
      sortTop5Name: [],
      nowMonthData: [],
      lastMonthData: [],
      // sortTop5Name: ['电信盘','烧机设备', '插座', '全空气机组', '照明', ],
      // nowMonthData: [7773.50, 8809.40, 10065.20, 17371.00, 20478.60],
      // lastMonthData: [-6979.80, -6458.10 -13762.10, -16628.50, -8037.70],
      totalEleTrend: {
        daysplit: [],
        kongtiao: [],
        zhaoming: [],
        dongli: [],
        qita: [],
      },
      zongbiao: [],
      url: {
        list: '/emsStatistics/energyOverview',
      },
    }
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      this.getEnergyoverview()
    })
  },
  methods: {
    getEnergyoverview() {
      // {spaceId: 277797283102721}
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      getAction(this.url.list, data).then((res) => {
        if(res.success){
          var listData = res.result
          // 用电需量
          this.demandStatistics.jishi = listData.demandStatistics.find((item) => item.type === '即时需量').howManyValue
          this.demandStatistics.fengzhi = listData.demandStatistics.find((item) => item.type === '需量峰值').howManyValue
          this.demandStatistics.mubiao = listData.demandStatistics.find((item) => item.type === '目标需量').howManyValue

          // co2+气象
          this.demandStatistics.co2 = listData.environmentStatistics.find((item) => item.type === 'CO2排放').howManyValue
          this.demandStatistics.shidu = listData.environmentStatistics.find((item) => item.type === '室外湿度')
            ? listData.environmentStatistics.find((item) => item.type === '室外湿度').howManyValue
            : ''
          this.demandStatistics.wendu = listData.environmentStatistics.find((item) => item.type === '室外温度')
            ? listData.environmentStatistics.find((item) => item.type === '室外温度').howManyValue
            : ''

          // top5
          this.sortTop5Name = []
          this.nowMonthData = []
          this.lastMonthData = []
          listData.top5Statistics.forEach((res) => {
            this.sortTop5Name.push(res.equipmentname)
            this.nowMonthData.push(res.howManyValue)
            this.lastMonthData.push(res.howManyValue2)
          })

          // 总标煤
          this.zongbiao = listData.kgceStatistics.map((res) => {
            var item = {
              name: res.type,
              value: res.howManyValue,
            }
            return item
          })

          this.totalEleTrend.daysplit = listData.daysplit
          this.totalEleTrend.kongtiao = listData.kongtiao
          this.totalEleTrend.dongli = listData.dongli
          this.totalEleTrend.zhaoming = listData.zhaoming
          this.totalEleTrend.qita = listData.qita
          this.energySpace = listData.energySpace
          // this.$message.success(res.message);
        }else{
          this.$message.warning(res.message);
        }
      }).finally(() => {
        this.$nextTick(() => {
          this.initSortTop5()
          this.initPieDianliang()
          this.initTotalEleTrend()
        })
        this.loading = false;
      })
    },
    handleSearch(param) {
      this.getEnergyoverview()
    },
    // 当月用电量TOP5 柱状图
    initSortTop5() {
      var _this = this
      var chartDom = this.$refs.sortTop5
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
          data: ['上月', '当月'],
        },
        // grid: {
        //   left: '3%',
        //   right: '4%',
        //   bottom: '12%',
        //   top: '0%',
        //   containLabel: true
        // },
        grid: [
          // 左边柱子
          {
            show: false,
            left: '10%',
            top: '10%',
            bottom: '20',
            containLabel: true,
            width: '25%',
          },
          // 中间
          {
            show: false,
            left: '51%',
            top: '10%',
            bottom: '20',
            width: '30%',
          },
          // 右边柱子
          {
            show: false,
            right: '10%',
            top: '10%',
            bottom: '20',
            containLabel: true,
            width: '25%',
          },
        ],
        xAxis: [
          {
            type: 'value',
            inverse: true,
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            splitLine: {
              show: false,
            },
          },
          {
            gridIndex: 1,
            show: false,
          },
          {
            gridIndex: 2,
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            splitLine: {
              show: false,
            },
          },
          // {
          //   type: 'value'
          // }
        ],
        yAxis: [
          {
            type: 'category',
            inverse: true,
            position: 'right',
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            data: this.sortTop5Name,
          },
          {
            gridIndex: 1,
            type: 'category',
            inverse: true,
            position: 'center',
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: true,
              align: 'center',
              // color: '#CDD7D7',
              fontSize: 14,
            },
            data: this.sortTop5Name,
          },
          {
            gridIndex: 2,
            type: 'category',
            inverse: true,
            position: 'left',
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: false,
            },
            data: this.sortTop5Name,
          },
          // {
          //   type: 'category',
          //   axisTick: {
          //     show: false
          //   },
          //   data: this.sortTop5Name
          // }
        ],
        series: [
          {
            name: '当月',
            type: 'bar',
            xAxisIndex: 2,
            yAxisIndex: 2,
            barWidth: 15,
            stack: 'Total',
            // barWidth: '50%',
            label: {
              show: true,
              position: 'right',
              // color: '#4BE6E6'
            },
            emphasis: {
              focus: 'series',
            },
            data: this.nowMonthData,
          },
          {
            name: '上月',
            type: 'bar',
            stack: 'Total',
            xAxisIndex: 0,
            yAxisIndex: 0,
            barWidth: 15,
            label: {
              show: true,
              position: 'left',
              // color: '#F29740'
            },
            emphasis: {
              focus: 'series',
            },
            data: this.lastMonthData,
          },
        ],
      }
      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 当月能源消耗总标煤 饼图
    initPieDianliang1() {
      var chartDom = this.$refs.zongbiao
      var myChart = echarts.init(chartDom)
      var option

      option = {
        series: [
          {
            type: 'gauge',
            radius: '70%',
            center: ['50%', '50%'],
            clockwise: true, // 仪表盘刻度是否是顺时针增长
            startAngle: '90', // 开始角度
            endAngle: '-269.9999', // 结束角度
            splitNumber: 36, // 仪表盘刻度的分割段数
            detail: {
              offsetCenter: [0, -20],
              formatter: ' ',
            },
            detail: {
              // 仪表盘详情，用于显示数据-value
              show: true, // 是否显示详情,默认 true
              offsetCenter: [0, -4], // 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '600',
              fontSize: '18',
              formatter: '{value}', // 格式化函数或者字符串
            },
            title: {
              //仪表盘标题-name。
              show: true, //是否显示标题,默认 true
              offsetCenter: [0, 16], // 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '100',
              fontSize: '14',
            },
            pointer: {
              // 指针
              show: false,
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: [
                  [0.3437, '#5470C6'],
                  [0.6269, '#91CC75'],
                  [0.8648, '#FAC858'],
                  [1, '#EE6666'],
                  // [0.3437, '#e2e14f'],
                  // [0.6269, '#00a7fb'],
                  // [0.8648, '#00e0fb'],
                  // [1, '#00ffb4']
                ],
                width: 18,
              },
            },
            axisTick: {
              // 刻度线
              show: false,
            },
            splitLine: {
              // 分割线
              show: true,
              distance: -18, // 分割线与轴线的距离（在哪儿显示）
              length: 18,
              lineStyle: {
                color: '#f1f1f1',
                width: 2,
              },
            },
            axisLabel: {
              show: false,
            },
            data: [
              {
                value: 400,
                name: 'tce',
              },
            ],
          },
          {
            type: 'pie',
            name: '内层细圆环',
            radius: ['52%', '53%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA',
            },
            label: {
              show: false,
            },
            data: [100],
          },
          {
            type: 'pie',
            name: '外层细圆环',
            radius: ['72%', '71%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA',
            },
            label: {
              show: false,
            },
            data: [100],
          },
        ],
      }

      option && myChart.setOption(option)
      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 总用电趋势图 柱状图
    initPieDianliang() {
      var chartDom = this.$refs.zongbiao
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'item',
        },
        legend: {
          // orient: 'vertical',
          // top: '10%',
          // right: 10,
          bottom: 0,
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: ['30%', '48%'], // 内圈 外圈
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter(param) {
                // return param.name  + '\n (' + param.percent + '%)';
                return param.name + '\n' + param.value + ' (' + param.percent + '%)'
              },
              // formatter: '{a} <br/>{b}: {c} ({d}%)'
              // position: 'center'
            },
            labelLine: {
              show: true,
            },
            emphasis: {
              label: {
                // 饼图每一个的名称显示
                show: true,
                // fontSize: 40,
                // fontWeight: 'bold'
              },
            },
            data: this.zongbiao,
          },
        ],
      }

      option && myChart.setOption(option)
      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    initTotalEleTrend() {
      console.log(this.totalEleTrend.daysplit)
      var _this = this
      var chartDom = this.$refs.totalEleTrend
      var myChart = echarts.init(chartDom)
      var option

      option = {
        // title: {
        //   text: '用能趋势',
        //   left: 'center'
        // },
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
          // data: ['6月','7月','8月','9月','10月','11月','12月','1月','2月','3月','4月','5月',]
          data: this.totalEleTrend.daysplit,
        },
        yAxis: {
          type: 'value',
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '动力用电',
            type: 'bar',
            stack: 'total',
            color: '#5470C6',
            emphasis: {
              focus: 'series',
            },
            // data: [150, 212, 201, 154, 190, 330, 410, 320, 199, 254, 324, 225]
            data: this.totalEleTrend.dongli,
          },
          {
            name: '空调用电',
            type: 'bar',
            stack: 'total',
            color: '#91CC75',
            // label: { // 是否显示柱形图上数字
            //   show: true
            // },
            // emphasis: { // 悬浮时是否模糊其他柱形图
            //   focus: 'series'
            // },
            // data: [120, 132, 101, 134, 90, 230, 210, 209, 150, 140, 152, 143]
            data: this.totalEleTrend.kongtiao,
          },
          {
            name: '照明用电',
            type: 'bar',
            stack: 'total',
            color: '#EE6666',
            // emphasis: {
            //   focus: 'series'
            // },
            // data: [220, 182, 191, 234, 290, 330, 310, 320, 145, 222, 245, 301]
            data: this.totalEleTrend.zhaoming,
          },
          {
            name: '其他用电',
            type: 'bar',
            stack: 'total',
            color: '#FAC858',
            // barWidth: '50%',
            // emphasis: {
            //   focus: 'series'
            // },
            // data: [320, 302, 301, 334, 390, 330, 320, 300, 310, 316, 304, 322]
            data: this.totalEleTrend.qita,
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
<style lang="less" scoped>
.energy-overView {
  min-height: calc(100vh - 84px);
  padding: 0 10px;
  .common-title {
    text-align: center;
    font-weight: bold;
    font-size: 18px;
    color: #1b2128;
  }
  .common-box {
    box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
    padding: 1.1vh 1.2vh;
    background-color: #fff;
    // padding: 16px 20px;
  }
  .info-container {
    width: 100%;
    .item-info {
      padding: 1.4vh 0;
      // padding: 22px 0;
      text-align: center;
      .name {
        font-size: 14px;
      }
      .value {
        margin-top: 15px;
        font-size: 20px;
      }
    }
  }
  .top-container {
    height: calc(50vh - 124px);
    .louyu {
      flex: 1;
      margin-right: 10px;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 1.2vh;
      // padding-bottom: 30px;
      img {
        object-fit: contain;
      }
      .ant-descriptions {
        background-color: #ffffff;
      }
    }
    .sort-top5-container {
      flex: 1.2;
      .sort-top5 {
        height: calc(100% - 20px);
        padding-top: 16px;
      }
    }
    .info-total {
      flex: 1.5;
      padding-left: 10px;
      .yongdian-xuliang {
        height: 50%;
      }
      .co2-tianqi {
        height: calc(50% - 10px);
        margin-top: 10px;
      }
      .co2 {
        flex: 1;
        margin-right: 10px;
      }
      .tianqi {
        flex: 1;
      }
    }
  }
  .bottom-container {
    height: calc(50vh - 60px);
    margin-top: 10px;
    .xiaohao {
      flex: 1;
      margin-right: 10px;
      .zongbiao-chart {
        height: 90%;
      }
      .legend {
        height: calc(50% - 25px);
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        // align-items: start;
        align-items: flex-start;
        padding: 20px;
        font-size: 14px;
        .item {
          width: 100%;
          align-items: center;
          justify-content: space-around;
          .example {
            width: 20px;
            height: 10px;
            background: #000;
            margin-right: 10px;
          }
          .text {
            min-width: 50px;
          }
          .number {
            min-width: 40px;
            // margin-right: 22px;
          }
          .pieValue {
          }
        }
      }
    }
    .qushi {
      flex: 2.7;
      .totalEle-trend {
        height: calc(100% - 25px);
      }
    }
  }
}
</style>
