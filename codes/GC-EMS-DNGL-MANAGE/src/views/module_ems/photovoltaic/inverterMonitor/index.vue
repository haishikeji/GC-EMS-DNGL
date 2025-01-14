<template>
  <div class="page-header-index-wide">
    <a-row>
      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-card :body-style="{ padding: '10px 8px 8px' }" class="topcard" size="small" :bordered="false" title="逆变器信息">
          <div class="tri-colorLight" ref="flexDivGroup">
            <div class="item-content" ref="itemFlexDiv" cl>
              <!-- <div class="item-title">{{ invInfo[0].fInvertername }}</div> -->
              <div style="width: 100%">
                <a-row :gutter="24">
                  <a-col :span="12">
                    <div class="item-inv"></div>
                  </a-col>
                  <a-col :span="12">
                    <div style="text-align: left;">
                      <span>并网电价：{{ stationInfo.fSubsidyprice }} 元/kW·h</span>
                    </div>
                    <div style="text-align: left">
                      <span>电站装机容量：{{ stationInfo.fInstalledcapacity }} kW</span>
                    </div>
                    <div style="text-align: left">
                      <span>逆变器数量：{{ stationInfo.inverterNum }} 个</span>
                    </div>
                    <div style="text-align: left">
                      <span>并网电压：{{ stationInfo.fGridvoltage }} V</span>
                    </div>
                    <div style="text-align: left">
                      <span>年利用小时数：{{ elecdetail.effectiveHours }} h</span>
                    </div>
                    <div style="text-align: left">
                      <span>总发电量：{{ totalEpiValue }} kWh</span>
                    </div>
                  </a-col>
                </a-row>
              </div>
            </div>
          </div>
        </a-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-row>
          <a-col :span="24">
            <a-card
              :loading="loading"
              :body-style="{ padding: '10px 8px 8px' }"
              size="small"
              class="topcard"
              :bordered="false"
              title="发电量信息"
            >
              <a-row>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div class="i fuzhao">
                      <div style="height:90%; width:90%;" class="img1"></div>
                    </div>
                    <span>日发电量</span>
                    <p>{{ elecdetail.powerByDay }} kW·h</p>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div class="i wenshi">
                      <div style="height:90%; width:90%;" class="img2"></div>
                    </div>
                    <span>月发电量</span>
                    <p>{{ elecdetail.powerByMonth }} kW·h</p>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div class="i fengsu">
                      <div style="height:90%; width:90%;" class="img3"></div>
                    </div>
                    <span>年发电量</span>
                    <p>{{ elecdetail.powerByYear }} kW·h</p>
                  </div>
                </a-col>
              </a-row>
            </a-card>
          </a-col>
        </a-row>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-row>
          <a-card
            :loading="loading"
            :body-style="{ padding: '10px 8px 8px' }"
            class="topcard"
            size="small"
            :bordered="false"
            title="收益信息"
          >
            <a-row>
              <a-col :span="8">
                <div class="head-info" :class="center && 'center'">
                  <div class="i rs">
                    <div style="height:90%; width:90%;" class="img4"></div>
                  </div>
                  <span>日收益</span>
                  <p>{{ elecdetail.powerByDayProfit }} 元</p>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="head-info" :class="center && 'center'">
                  <div class="i ys">
                    <div style="height:90%; width:90%;" class="img5"></div>
                  </div>
                  <span>月收益</span>
                  <p>{{ elecdetail.powerByMonthProfit }} 元</p>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="head-info" :class="center && 'center'">
                  <div class="i ns">
                    <div style="height:90%; width:90%;" class="img6"></div>
                  </div>
                  <span>年收益</span>
                  <p>{{ elecdetail.powerByYearProfit }} 元</p>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </a-row>
      </a-col>
    </a-row>
    <a-row style="margin: 12px 0px 0px 0px">
      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-card
          :loading="loading"
          :body-style="{ padding: '10px 8px 8px' }"
          class="middlecard"
          size="small"
          :bordered="false"
          title="实时功率"
        >
          <a-row :gutter="24">
            <a-col :span="12" style="width: 100%">
              <div class="totalEle-trend" ref="realPowerChart"></div>
              <!-- <span style="margin-top: 120px">实时发电功率(kW)</span> -->
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="16" :style="{ padding: '0px 12px 0px 4px' }">
        <a-card
          :body-style="{ padding: '0px 8px 8px' }"
          class="middlecard"
          size="small"
          :bordered="false"
          title="功率及发电量趋势"
          style="margin-left: -4px"
        >
          <div class="totalEle-trend" ref="powerChart"></div>
        </a-card>
      </a-col>
    </a-row>
    <a-row>
      <a-col :sm="24" :md="12" :xl="24" :style="{ padding: '13px 12px 0px 4px' }">
        <a-card :body-style="{ padding: '0px 8px 8px' }" class="bottomcard" size="small" :bordered="false" style="margin-left: -4px">
          <a-row>
            <a-col :sm="24" :md="12" :xl="10" class="inv_col">
              <a-table size="middle" bordered :pagination="false" :columns="columns1" :dataSource="tableData1">
              </a-table>
            </a-col>
            <a-col :sm="24" :md="12" :xl="4">
              <div class="inv1"></div>
            </a-col>
            <a-col :sm="24" :md="12" :xl="10" class="inv_col">
              <a-table size="middle" bordered :pagination="false" :columns="columns2" :dataSource="tableData2">
              </a-table>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { httpAction, getAction } from '@/api/manage'
import { pvdata } from '../pvdata'

export default {
  name: 'inverterMonitor',
  components: {},
  data() {
    return {
      description: '逆变器运行监控',
      loading: true,
      center: null,
      elecdetail: {}, // 发电量信息
      envirInfo: {}, // 气象信息
      invInfo: [], // 逆变器信息
      stationInfo: {}, // 电站信息
      totalEpiValue: 0, // 总发电量
      times: [], // 功率及发电量趋势-时间
      powers: [], // 功率及发电量趋势-功率
      irradiances: [], // 功率及发电量趋势-辐照强度
      columns1: [
        {
          title: '输入回路',
          align: 'center',
          dataIndex: 'equipmentname',
        },
        {
          title: '电压(V)',
          align: 'center',
          dataIndex: 'voltage',
        },
        {
          title: '电流(A)',
          align: 'center',
          dataIndex: 'current',
        },
        {
          title: '功率(kW)',
          align: 'center',
          dataIndex: 'power',
        },
      ],
      tableData1: [
        {
          equipmentname: 'GD02',
          voltage: 471.8,
          current: 0.5,
          power: 0.24,
        },
        {
          equipmentname: 'GD02',
          voltage: 471.8,
          current: 0.5,
          power: 0.24,
        },
      ],
      columns2: [
        {
          title: '参数',
          align: 'center',
          dataIndex: 'equipmentname',
        },
        {
          title: 'A相',
          align: 'center',
          dataIndex: 'a',
          // 下面这个真不好用，浪费我好多时间
          // customCell: (text, index) => {
          //   return {
          //     colSpan: index > 1 ? 3 : 1,
          //   }
          // },
          customRender: (text, row, index) => {
            if (index > 1) {
              return {
                children: text,
                attrs: { colSpan: 3 },
              }
            }
            else{
              return {
                children: text,
                attrs: { colSpan: 1 },
              }
            }
          },
        },
        {
          title: 'B相',
          align: 'center',
          dataIndex: 'b',
          customRender: this.sharedOnCell
        },
        {
          title: 'C相',
          align: 'center',
          dataIndex: 'c',
          customRender: this.sharedOnCell
        },
      ],
      tableData2: [
        {
          equipmentname: '电压(V)',
          a: 234.1,
          b: 233.5,
          c: 232.9,
        },
        {
          equipmentname: '电流(A)',
          a: 2.8,
          b: 2.5,
          c: 2.24,
        },
        {
          equipmentname: '总有功功率(kW)',
          a: 0.241,
        },
        {
          equipmentname: '功率因数',
          a: 0,
        },
        {
          equipmentname: '频率(Hz)',
          a: 49.96,
        },
      ],
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  },
  mounted() {
    this.$nextTick(() => {
      setTimeout(() => {
        this.getTest()
      }, 1000)
      // this.getEnergyoverview()
    })
  },
  methods: {
    sharedOnCell(text, row, index) {
      if (index < 2) {
        return {
          children: text,
          attrs: { colSpan: 1 },
        }
      }
      else {
        return {
          children: text,
          attrs: { colSpan: 0 },
        }
      }
    },
    getTest() {
      var listData = pvdata.stationMonitorData
      console.log(listData)
      this.totalEpiValue = listData.totalEpiValue
      this.elecdetail = pvdata.pvDashboardData.elecdetail
      this.envirInfo = listData.envirInfo
      this.invInfo = listData.invInfo
      this.stationInfo = listData.stationInfo
      listData.dayPowerValue.forEach((i) => {
        this.times.push(i.fTime)
        this.powers.push(i.fP)
        this.irradiances.push(i.irradianceValue)
      })

      this.initRealPowerChart()
      this.initPowerChart()
    },
    initRealPowerChart() {
      var _this = this
      var chartDom = _this.$refs.realPowerChart
      var myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            min: 0,
            max: 30,
            progress: {
              show: true,
            },
            detail: {
              // 仪表盘展示文字
              // show: false,
              formatter: '{value}',
              fontSize: '18',
              color: "#838383"
            },
            radius: '100%', // 仪表盘放大/缩小
            // axisLine: { lineStyle: { width: 5 } }, // 修改线段宽度
            axisLine: { lineStyle: {
                color: [[1, '#1FD09D']],
                width: 10
            } },
            axisLabel: {
                textStyle: {
                    color: "#1FD09D"
                }
            },
            // 分隔线
              splitLine: {
                show: true,
                // 这里刻度线长短
                // length: 12,
                lineStyle: {
                  width: 2,
                  color: '#1FD09D'
                }
              },
              // 刻度线
              axisTick: {
                show: true,
                splitNumber: 5,
                length: 10,
                lineStyle: {
                  color: '#B0B0B0',
                  width: 1,
                },
              },
              // 刻度标签(数字)
              axisLabel: {
                // distance: 2,
                fontSize: '10',
                color: '#B0B0B0',
              },
              // 仪表盘指针
              pointer: {
                show: true,
                length: '65%',
                width: 5 // 指针粗细
              },
              // 仪表盘指针样式
              itemStyle: {
                color: '#1FD09D',
              },
            data: [
              {
                value: _this.stationInfo.realPower,
                name: '功率',
              },
            ],
          },
        ],
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    initPowerChart() {
      var _this = this
      var chartDom = _this.$refs.powerChart
      var myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '20%']
          },
        },
        legend: {
          data: ['功率', '辐照度'],
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: _this.times,
        },
        yAxis: [
          {
            type: 'value',
            name: '功率',
            boundaryGap: [0, '30%'],
          },
          {
            type: 'value',
            name: '辐照度',
            boundaryGap: [0, '30%'],
          },
        ],
        // dataZoom: [
        //   {
        //     type: 'inside',
        //     start: 0,
        //     end: 100,
        //     xAxisIndex: 0,
        //   },
        //   {
        //     start: 0,
        //     end: 10,
        //   },
        //   {
        //     show: true,
        //     yAxisIndex: 0,
        //     filterMode: "empty",
        //     width: 30,
        //     height: "80%",
        //     right: "7%",
        //   },
        // ],
        series: [
          {
            name: '功率',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#2FC9CB',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.powers,
          },
          {
            name: '辐照度',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            yAxisIndex: 1, // 依据哪儿个y周坐标显示数据
            itemStyle: {
              color: '#B6A2DE',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.irradiances,
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
.chart-card-header {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 50px;

  .meta {
    position: relative;
    overflow: hidden;
    //   width: 100%;
    color: #ffffff;
    font-size: 20px;
    line-height: 38px;
    float: left;
  }
}

.chart-card-action {
  cursor: pointer;
  position: absolute;
  top: 0;
  right: 0;
}

.chart-card-footer {
  border-top: 1px solid #e8e8e8;
  padding-top: 9px;
  margin-top: 8px;

  > * {
    position: relative;
  }

  .field {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin: 0;
  }
}

.chart-card-content {
  margin-bottom: 12px;
  position: relative;
  height: 46px;
  width: 100%;

  .content-fix {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
  }
}

.total {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  color: #ffffff;
  // margin-top: 4px;
  margin-bottom: 0;
  font-size: 30px;
  line-height: 38px;
  height: 38px;
  float: right;
}
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: center;
  padding: 0 32px 0 0;
  min-width: 125px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }
  p {
    line-height: 38px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
  .bm-view {
    width: 100%;
    height: 300px;
  }

  .img1 {
    background: url('../../../../assets/images/rf2.png') no-repeat center center;
    background-size: 70%;
  }
  .img2 {
    background: url('../../../../assets/images/yf2.png') no-repeat center center;
    background-size: 70%;
  }
  .img3 {
    background: url('../../../../assets/images/nf2.png') no-repeat center center;
    background-size: 70%;
  }
  .img4 {
    background: url('../../../../assets/images/rs2.png') no-repeat center center;
    background-size: 70%;
  }
  .img5 {
    background: url('../../../../assets/images/ys2.png') no-repeat center center;
    background-size: 70%;
  }
  .img6 {
    background: url('../../../../assets/images/ns2.png') no-repeat center center;
    background-size: 70%;
  }

  .i{
    font-size: 2vw;
    border-radius: 100%;
    width: 80px;
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: center;
    // text-align: center;
    // line-height: 60px;
  }
  .fuzhao{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #5eb2f77c, #8998F6);
  }
  .wenshi{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #e5beff, #C680FD);
  }
  .fengsu{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #56E2CE, #07B79F);
  }
  .rs{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #92e2fc, #43A3F7);
  }
  .ys{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #6fe69c, #2ACAA1);
  }
  .ns{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #fdcb4a, #FCA413);
  }
}
.head-title {
  font-size: 15px;
  margin-left: 20px;
}

.totalEle-trend {
  height: 250px;
  margin: auto;
}
.topcard {
  height: 255px;
}
.middlecard {
  height: 300px;
}
.bottomcard {
  height: 283px;
}
// 三色灯
.tri-colorLight {
  display: flex;
  background: #fff;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  margin: 1px 0px 0px 0px;
  // width: 20%;
  .item-container {
    padding: 10px;
    box-sizing: border-box;
  }
  .item-content {
    width: 100%;
    // border: 2px solid #c3c3c3;
    color: rgba(0, 0, 0, 0.45);
    // padding: 16px 50px 20px;
    // padding-top: 16px;
    // padding-bottom: 20px;
    margin: 10px 6px;
    // box-shadow: 0 2px 10px 2px rgba(59, 78, 128, 0.5) inset;
    text-align: center;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .item-inv {
      background-image: url(../../../../assets/inv.png);
      background-repeat: no-repeat;
      background-size: 75%;
      height: 181px;
      width: 132px;
      margin: 0px 0px 0px 40px;
      background-position: 50% 0;
      float: left;
    }
    .item-title {
      font-size: 18px;
      margin-bottom: 20px;
      font-weight: bold;
    }
    .item-cc {
      float: right;
      text-align: left;
      margin: 0px 70px 0px 0px;
    }
  }
  .item_content_rad {
    border-radius: 18px;
  }
}
.inv1 {
  background-image: url(../../../../assets/inv1.png);
  background-repeat: no-repeat;
  background-size: 75%;
  height: 264px;
  width: 401px;
  margin: 40px 0px 0px 0px;
  float: left;
}
.inv_col {
  border: 1px solid #e8e8e8;
  height: 285px;
}
::v-deep .ant-card{
  border-radius: 5px;
  box-shadow: -1px 0 2px rgba(180, 180, 180, 0.156), /*左侧阴影*/
                1px 0 2px rgba(180, 180, 180, 0.156), /*右侧阴影*/
                -2px 2px 2px rgba(180, 180, 180, 0.156), /*上方阴影*/
                2px 2px 2px rgba(180, 180, 180, 0.156);
}
::v-deep .ant-card-head{
  background-color: #01ADA8 !important;
  color: #fff;
}
::v-deep .ant-table-thead{
  background-color: #01ADA8 !important;
  color: #fff;
}
</style>