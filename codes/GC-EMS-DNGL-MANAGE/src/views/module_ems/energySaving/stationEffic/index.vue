<template>
  <a-spin :spinning="loading">
    <div class="energy-overView">
      <commonSearch hasAreaSearch ref="commonSearch" type="nodate" @search="handleSearch"></commonSearch>
      <div class="top-container u-flex">
        <div class="nengyuanzhan u-flex common-box">
          <div class="common-title">能源站信息</div>
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
        <div class="peak-flat-valley common-box">
          <div class="common-title">峰平谷电能统计</div>
          <!-- 统计数据 -->
          <div class="number-data u-flex-jac">
            <div class="u-flex-cjac f1">
              <div>电费</div>
              <div>--</div>
            </div>
            <a-divider type="vertical" />
            <div class="u-flex-cjac" style="flex: 1.5;">
              <div>单价信息</div>
              <div class="u-flex">
                <div class="u-flex-cjac" style="margin-right: 6px;">
                  <div>尖时</div>
                  <div>1.0000元/kWh</div>
                </div>
                <div class="u-flex-cjac" style="margin-right: 6px;">
                  <div>峰时</div>
                  <div>1.0000元/kWh</div>
                </div>
                <div class="u-flex-cjac" style="margin-right: 6px;">
                  <div>平时</div>
                  <div>1.0000元/kWh</div>
                </div>
                <div class="u-flex-cjac" style="margin-right: 6px;">
                  <div>谷时</div>
                  <div>1.0000元/kWh</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 饼状图 -->
          <div class="u-flex pies-container">
            <div class="money-pie f1">
              <div class="pie-layout u-flex">
                <div class="pie-chart" ref="moneyPie"></div>
                <div class="legend">
                  <div class="u-flex item" v-for="(item, index) in dataLists" :key="index">
                    <div class="example"></div>
                    <div class="text">{{item.name}}</div>
                    <div class="number">{{item.number}}</div>
                    <!-- KWh -->
                    <div class="pieValue">{{item.pieValue}}%</div>
                  </div>
                </div>
              </div>
            </div>
            <!-- kWh相关饼状图 -->
            <div class="kWh-pie f1">
              <div class="pie-layout u-flex">
                <div class="pie-chart" ref="kWhPie"></div>
                <div class="legend">
                  <div class="u-flex item" v-for="(item, index) in dataLists" :key="index">
                    <div class="example"></div>
                    <div class="text">{{item.name}}</div>
                    <div class="number">{{item.number}}</div>
                    <!-- KWh -->
                    <div class="pieValue">{{item.pieValue}}%</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-container u-flex">
        <div class="yongdian_qushi common-box">
          <div class="common-title">总用电趋势</div>
          <div class="totalEle-trend" ref="totalEleTrend"></div>
        </div>
        <div class="yongleng_qushi common-box">
          <div class="common-title">总用冷量趋势</div>
          <div class="totalCooling-trend" ref="totalCoolingTrend"></div>
        </div>
        <div class="xiaobi_qushi common-box">
          <div class="common-title">系统能效比趋势</div>
          <div class="totalEERatio-trend" ref="totalEERatioTrend"></div>
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
      description: '能效总览',
      loading: false,
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
        { name: '尖时', number: 11514.45, pieValue: 34.37 },
        { name: '峰时', number: 9485.32, pieValue: 28.32 },
        { name: '平时', number: 7969.53, pieValue: 23.79 },
        { name: '谷时', number: 4527.72, pieValue: 13.52 }
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
      this.initPieMoney()
      this.initPieKWH()
      this.initTotalEleTrend()
      this.initTotalCoolingTrend()
      this.initTotalEERatioTrend()
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
    // kWh相关饼图
    initPieKWH(){
      var chartDom = this.$refs.kWhPie
      var myChart = echarts.init(chartDom);
      var option;

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
              formatter: ' '
            },
            detail: { // 仪表盘详情，用于显示数据-value
              show: true, // 是否显示详情,默认 true
              offsetCenter: [0, -4],// 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '600',
              fontSize: '18',
              formatter: "{value}",	// 格式化函数或者字符串
            },
            title: {	//仪表盘标题-name。
              show: true, //是否显示标题,默认 true
              offsetCenter: [0, 16], // 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '100',
              fontSize: '14',
            },
            pointer: { // 指针
              show: false
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: [
                  [0.3437, '#5470C6'],
                  [0.6269, '#91CC75'],
                  [0.8648, '#FAC858'],
                  [1, '#EE6666']
                  // [0.3437, '#e2e14f'],
                  // [0.6269, '#00a7fb'],
                  // [0.8648, '#00e0fb'],
                  // [1, '#00ffb4']
                ],
                width: 18
              }
            },
            axisTick: { // 刻度线
              show: false
            },
            splitLine: { // 分割线
              show: true,
              distance: -18, // 分割线与轴线的距离（在哪儿显示）
              length: 18,
              lineStyle: {
                color: '#f1f1f1',
                width: 2
              }
            },
            axisLabel: {
              show: false
            },
            data: [
              {
                value: 33497.02,
                name: 'KWh'
              }
            ]
          },
          {
            type: 'pie',
            name: '内层细圆环',
            radius: ['52%', '53%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          },
          {
            type: 'pie',
            name: '外层细圆环',
            radius: ['72%', '71%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          }
        ]
      };

      option && myChart.setOption(option);
      window.addEventListener("resize",function (){
        myChart.resize();
      });
    },
    // 金额相关饼图
    initPieMoney(){
      var chartDom = this.$refs.moneyPie
      var myChart = echarts.init(chartDom);
      var option;

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
              formatter: ' '
            },
            detail: { // 仪表盘详情，用于显示数据-value
              show: true, // 是否显示详情,默认 true
              offsetCenter: [0, -4],// 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '600',
              fontSize: '18',
              formatter: "{value}",	// 格式化函数或者字符串
            },
            title: {	//仪表盘标题-name。
              show: true, //是否显示标题,默认 true
              offsetCenter: [0, 16], // 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '100',
              fontSize: '14',
            },
            pointer: { // 指针
              show: false
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: [
                  [0.3437, '#5470C6'],
                  [0.6269, '#91CC75'],
                  [0.8648, '#FAC858'],
                  [1, '#EE6666']
                  // [0.3437, '#e2e14f'],
                  // [0.6269, '#00a7fb'],
                  // [0.8648, '#00e0fb'],
                  // [1, '#00ffb4']
                ],
                width: 18
              }
            },
            axisTick: { // 刻度线
              show: false
            },
            splitLine: { // 分割线
              show: true,
              distance: -18, // 分割线与轴线的距离（在哪儿显示）
              length: 18,
              lineStyle: {
                color: '#f1f1f1',
                width: 2
              }
            },
            axisLabel: {
              show: false
            },
            data: [
              {
                value: 33497.02,
                name: '元'
              }
            ]
          },
          {
            type: 'pie',
            name: '内层细圆环',
            radius: ['52%', '53%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          },
          {
            type: 'pie',
            name: '外层细圆环',
            radius: ['72%', '71%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          }
        ]
      };

      option && myChart.setOption(option);
      window.addEventListener("resize",function (){
        myChart.resize();
      });
    },
    // 总用电趋势图 柱状图
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
          data: ['6月','7月','8月','9月','10月','11月','12月','1月','2月','3月','4月','5月',]
          // data: this.totalEleTrend.daysplit,
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
            name: '尖时',
            type: 'bar',
            stack: 'total',
            color: '#5470C6',
            emphasis: {
              focus: 'series',
            },
            data: [150, 212, 201, 154, 190, 330, 410, 320, 199, 254, 324, 225]
            // data: this.totalEleTrend.dongli,
          },
          {
            name: '峰时',
            type: 'bar',
            stack: 'total',
            color: '#91CC75',
            // label: { // 是否显示柱形图上数字
            //   show: true
            // },
            // emphasis: { // 悬浮时是否模糊其他柱形图
            //   focus: 'series'
            // },
            data: [120, 132, 101, 134, 90, 230, 210, 209, 150, 140, 152, 143]
            // data: this.totalEleTrend.kongtiao,
          },
          {
            name: '平时',
            type: 'bar',
            stack: 'total',
            color: '#EE6666',
            // emphasis: {
            //   focus: 'series'
            // },
            data: [220, 182, 191, 234, 290, 330, 310, 320, 145, 222, 245, 301]
            // data: this.totalEleTrend.zhaoming,
          },
          {
            name: '谷时',
            type: 'bar',
            stack: 'total',
            color: '#FAC858',
            // barWidth: '50%',
            // emphasis: {
            //   focus: 'series'
            // },
            data: [320, 302, 301, 334, 390, 330, 320, 300, 310, 316, 304, 322]
            // data: this.totalEleTrend.qita,
          },
        ],
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 总用冷量趋势图
    initTotalCoolingTrend() {
      var _this = this
      var chartDom = this.$refs.totalCoolingTrend
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
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
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
            name: '用冷量',
            type: 'bar',
            data: [150, 230, 224, 218, 135, 147, 260],
          }
        ]
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 系统能效比趋势图
    initTotalEERatioTrend() {
      var _this = this
      var chartDom = this.$refs.totalEERatioTrend
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
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
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
            name: '能效数值',
            type: 'bar',
            color: '#91CC75',
            data: [150, 230, 224, 218, 135, 147, 260],
          }
        ]
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
    height: 40vh;
    // height: calc(50vh - 124px);
    .nengyuanzhan {
      width: 33%;
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
    .peak-flat-valley{
      // width: calc(100% - 33% - 10px);
      flex: 1;
      // 统计数据部分
      .number-data{
        height: 40px;
      }
      // 饼状图部分
      .pies-container{
        padding-top: 16px;
        height: calc(100% - 60px);
        // 金额饼状图
        .money-pie{
          height: 100%;
        }
        // kWh饼状图
        .kWh-pie {
          height: 100%;
        }
        .pie-layout{
          height: 100%;
        }
        .pie-chart{
          width: 50%;
          height: 100%;
        }
        .legend{
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          // align-items: start;
          align-items: flex-start;
          padding: 10px;
          font-size: 13px;
          .item{
            width: 100%;
            align-items: center;
            .example{
              width: 20px;
              height: 10px;
              background: #000;
              margin-right: 10px;
            }
            .text{
              min-width: 40px;
            }
            .number{
              min-width: 80px;
              // margin-right: 22px;
            }
            .pieValue{

            }
          }
        }
      }
    }
  }
  .bottom-container {
    height: calc(50vh - 60px);
    margin-top: 10px;
    .yongdian_qushi {
      width: 33%;
      margin-right: 10px;
      .totalEle-trend {
        height: calc(100% - 25px);
      }
    }
    .yongleng_qushi {
      flex: 1;
      margin-right: 10px;
      .totalCooling-trend {
        height: calc(100% - 25px);
      }
    }
    .xiaobi_qushi {
      flex: 1;
      .totalEERatio-trend {
        height: calc(100% - 25px);
      }
    }
  }
}
</style>
