<template>
  <div class="index-container">
    <!-- 首行--运维 -->
    <div class="opera-container u-flex-jab">
      <router-link class="item f1 repair" v-for="(item, index) in operaContent" :key="index" :to="item.to">
        <div class="common-title u-flex">
          <span class="text">{{ item.title }}</span>
        </div>
        <div class="content u-flex-jab">
          
          <div class="tongji u-flex-cjac">
            <div class="number">{{ operaStatistics[item.incomplete] }} / <span class="number1">{{operaStatistics[item.completed]}}</span></div>
            <!-- <div class="number">{{ operaStatistics[item.incomplete] }}</div> -->
            <div class="text">{{ operaTitle[item.incompleteTitle] }} / {{operaTitle[item.completedTitle]}}</div>
          </div>
          <div class="legend f1 u-flex-jac">
            <a-icon :type="item.legend" :class="item.class" class="u-flex-jac"/>
          </div>
        </div>
      </router-link>
    </div>
    <!-- 消息通知+报警通知 -->
    <a-row :gutter="20" class="mt20 alarmandnotice">
      <a-col :span="14">
        <a-card class="box-card">
          <div class="u-flex-jab">
            <div class="common-title u-flex">
              <span class="text">报警通知</span>
            </div>
            <a-button type="link" size="small" @click="chakan">处理/查看</a-button>
            <!-- <router-link to="/alarmManage/handleAlarm">
              <a-button style="float: right; padding: 3px 0" type="text">处理/查看</a-button>
            </router-link> -->
          </div>
          <a-table
            rowKey="id"
            size="middle"
            bordered
            :columns="alarmcolumns"
            :dataSource="alarmList"
            :pagination="false"
          >
            <template slot="htmlSlot" slot-scope="text">
              <div v-html="text"></div>
            </template>

            <template slot="imgSlot" slot-scope="text, record">
              <div style="font-size: 12px; font-style: italic">
                <span v-if="!text">无图片</span>
                <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width: 80px; height: 25px" />
              </div>
            </template>

            <template slot="fileSlot" slot-scope="text">
              <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
              <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
                <span>下载</span>
              </a-button>
            </template>
          </a-table>
        </a-card>
      </a-col>
      <a-col :span="10">
        <a-card class="box-card">
          <div class="u-flex-jab">
            <div class="common-title u-flex">
              <span class="text">消息通知</span>
            </div>
            <a-button type="link" size="small" @click="chakan2">处理/查看</a-button>
            <!-- <router-link to="/alarmManage/notice">
              <a-button style="float: right; padding: 3px 0" type="text">处理/查看</a-button>
            </router-link> -->
          </div>
          <a-table
            rowKey="id"
            size="middle"
            bordered
            :columns="noticecolumns"
            :dataSource="noticeList"
            :pagination="false"
          >
            <template slot="htmlSlot" slot-scope="text">
              <div v-html="text"></div>
            </template>

            <template slot="imgSlot" slot-scope="text, record">
              <div style="font-size: 12px; font-style: italic">
                <span v-if="!text">无图片</span>
                <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width: 80px; height: 25px" />
              </div>
            </template>

            <template slot="fileSlot" slot-scope="text">
              <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
              <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
                <span>下载</span>
              </a-button>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="20" class="mt20">
      <a-col :span="14">
        <div class="trend-charts" ref="elecCharts"></div>
      </a-col>
      <a-col :span="10">
        <div class="trend-charts" ref="envirCharts"></div>
      </a-col>      
    </a-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
// import { listAlarmdata } from "@/api/alarm/alarmdata.js";
// import { listNotice } from "@/api/system/notice";
// import { firstload } from "@/api/homepage.js";
// import { envirinfo } from "@/api/environSafe/hsmsstatistics.js";
  import { httpAction, getAction, postAction } from '@/api/manage'
import { emsdata } from '../module_ems/energyAnalyse/emsdata';

export default {
  name: 'Index',
  dicts: ['sys_alarm_type', 'sys_alarm_level', 'sys_notice_type'],
  data() {
    return {
      noticecolumns: [
        {
          title: '消息标题',
          align: 'center',
          dataIndex: 'noticeTitle',
          customRender: function (text) {
            return !text ? '' : text.length > 20 ? text.substr(0, 20) + "..." : text
          },
        },
        {
          title: '消息类型',
          align: 'center',
          dataIndex: 'noticeType',
        },
        {
          title: '生成时间',
          align: 'center',
          dataIndex: 'createTime',
        },
      ],
      alarmcolumns: [
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'equipmentname',
        },
        {
          title: '报警描述',
          align: 'center',
          dataIndex: 'remark',
        },
        {
          title: '报警类型',
          align: 'center',
          dataIndex: 'alarmtype',
        },
        {
          title: '报警时间',
          align: 'center',
          dataIndex: 'alarmtime',
        },
      ],
      // 运维统计前端绘制
      operaContent: [
        { type: 'repair', title: '设备当前运行数', completed: 'machineCompleted', completedTitle: 'machineCompleted', incomplete: 'machineIncomplete', 
          incompleteTitle: 'machineIncomplete', legend: 'hdd', class: 'spotcheck', to: '/ehm/status' },
        // { type: 'repair', title: '未处理报警', completed: 'alarmCompleted', completedTitle: 'alarmCompleted', incomplete: 'alarmIncomplete', 
        //   incompleteTitle: 'alarmIncomplete', legend: 'a-icon-warning warning', to: '/alarmManage/handleAlarm' },
        { type: 'repair', title: '待维修任务', completed: 'repairCompleted', completedTitle: 'repairCompleted', incomplete: 'repairIncomplete', 
          incompleteTitle: 'repairIncomplete', legend: 'setting', class: 'repair', to: '/cmms/repair/myRepair' },
        { type: 'repair', title: '待保养任务', completed: 'upkeepCompleted', completedTitle: 'upkeepCompleted', incomplete: 'upkeepIncomplete', 
          incompleteTitle: 'upkeepIncomplete', legend: 'book', class: 'upkeep', to: '/cmms/upkeep/upkeep' },
        { type: 'repair', title: '待巡检任务', completed: 'inspectCompleted', completedTitle: 'inspectCompleted', incomplete: 'inspectIncomplete', 
          incompleteTitle: 'inspectIncomplete', legend: 'interaction', class: 'inspect', to: '/cmms/inspect' },
      ],
      // 运维统计后端数据
      operaStatistics: {
        repairCompleted: '-',
        repairIncomplete: '-',
        upkeepCompleted: '-',
        upkeepIncomplete: '-',
        alarmCompleted: '-',
        alarmIncomplete: '-',
        inspectCompleted: '-',
        inspectIncomplete: '-',
        machineCompleted: '-',
        machineIncomplete: '-',
      },
      // 运维统计后端数据
      operaTitle: {
        repairCompleted: '总数',
        repairIncomplete: '今日数',
        upkeepCompleted: '总数',
        upkeepIncomplete: '今日数',
        alarmCompleted: '总数',
        alarmIncomplete: '今日数',
        inspectCompleted: '总数',
        inspectIncomplete: '今日数',
        machineCompleted: '总数',
        machineIncomplete: '运行数',
      },
      // 报警数据表格数据
      alarmList: [],
      // 消息数据表格数据
      noticeList: [],
      // 今日用电量
      TodayElectricity: [],
      // 环境温度
      EnvironmentTemp: [],
      temperatureData: [],
      //设备名称
      equipmentnames:[],
      //温度
      temps:[],
      //湿度
      humis:[],
        url: {
            list: "/homepate/statistics/firstload",
            equipment: "/equipmentStatistics/equipmentStatistics/machineSum",
            repair: "/cmmsRepair/cmmsRepair/indexrepairnum",
            upkeep: "/cmmsUpkeep/cmmsUpkeep/getMyChargeNumDay",
            upkeepAll: "/cmmsUpkeep/cmmsUpkeep/getMyChargeNum",
            inspect: "/cmmsInspect/cmmsInspect/indexinspectnum",
            todayelectricity: "/dataElectricity/dataElectricity/todayelectricity",
            envirinfo: "/hsms/statistics/envirinfo",
        },
    }
  },
  created() {
  },
  mounted() {
    this.$nextTick(() => {
      // 延迟绘制，否则echarts会超出父容器
      setTimeout(() => {
        this.getList();
      },1000);
      // this.getDemandanalysis()
    })
    // this.getAlarmList()
    // this.getNoticeList()
    var _this = this;
    setInterval(function () {
    //   _this.getInfo();
        // _this.getTest();
    }, 60000);
  },
  methods: {
    /** 首次打开加载整个页面 */
    getList() {
    //   this.getInfo();
        this.getInfo2();
        // this.getTest();
    },
    // 处理/查看
    chakan(){
      this.$router.push({path: '/msgcharge'})
    },
    chakan2(){
      this.$router.push({path: '/msgnotice'})
    },
    // 正式需要删除
    getTest() {
        if (emsdata.homedata.RepairSum != null) {
          this.operaStatistics.repairCompleted = emsdata.homedata.RepairSum[0].howManyTimes;
          this.operaStatistics.repairIncomplete = emsdata.homedata.RepairSum[1].howManyTimes;
        }
        
        if (emsdata.homedata.UpkeepSum != null) {
          this.operaStatistics.upkeepCompleted = emsdata.homedata.UpkeepSum[0].howManyTimes;
          this.operaStatistics.upkeepIncomplete = emsdata.homedata.UpkeepSum[1].howManyTimes;
        }
        
        if (emsdata.homedata.AlarmSum != null) {
          this.operaStatistics.alarmCompleted = emsdata.homedata.AlarmSum[0].howManyTimes;
          this.operaStatistics.alarmIncomplete = emsdata.homedata.AlarmSum[1].howManyTimes;
        }
        
        if (emsdata.homedata.InspeceSum != null) {
          this.operaStatistics.inspectCompleted = emsdata.homedata.InspeceSum[0].howManyTimes;
          this.operaStatistics.inspectIncomplete = emsdata.homedata.InspeceSum[1].howManyTimes;
        }

        if (emsdata.homedata.MachineSum != null) {
          this.operaStatistics.machineIncomplete = emsdata.homedata.MachineSum[1].howManyTimes;
          this.operaStatistics.machineCompleted = emsdata.homedata.MachineSum[4].howManyTimes;
        }

        this.TodayElectricity = emsdata.homedata.TodayElectricity;
        this.EnvironmentTemp = emsdata.homedata.EnvironmentTemp;

        this.alarmList = emsdata.homedata.alarmData;
        this.noticeList = emsdata.homedata.noticeData;

        this.temperatureData = emsdata.homedata.envirData;

        this.initEcharts();
        this.initTempAndHumiEcharts();
    },
    getInfo() {
      getAction(this.url.list, null).then((response) => {
        console.log(response.data)
        if (response.data.RepairSum != null) {
          this.operaStatistics.repairCompleted = response.data.RepairSum[0].howManyTimes;
          this.operaStatistics.repairIncomplete = response.data.RepairSum[1].howManyTimes;
        }
        
        if (response.data.UpkeepSum != null) {
          this.operaStatistics.upkeepCompleted = response.data.UpkeepSum[0].howManyTimes;
          this.operaStatistics.upkeepIncomplete = response.data.UpkeepSum[1].howManyTimes;
        }
        
        if (response.data.AlarmSum != null) {
          this.operaStatistics.alarmCompleted = response.data.AlarmSum[0].howManyTimes;
          this.operaStatistics.alarmIncomplete = response.data.AlarmSum[1].howManyTimes;
        }
        
        if (response.data.InspeceSum != null) {
          this.operaStatistics.inspectCompleted = response.data.InspeceSum[0].howManyTimes;
          this.operaStatistics.inspectIncomplete = response.data.InspeceSum[1].howManyTimes;
        }

        if (response.data.MachineSum != null) {
          this.operaStatistics.machineIncomplete = response.data.MachineSum[1].howManyTimes;
          this.operaStatistics.machineCompleted = response.data.MachineSum[4].howManyTimes;
        }

        this.TodayElectricity = response.data.TodayElectricity;
        this.EnvironmentTemp = response.data.EnvironmentTemp;

        this.initEcharts();
      });

      
      envirinfo().then((response) => {
        this.temperatureData = response.data;
        // console.log("--------start--------");
        // for(var i in this.temperatureData){
        //   console.log("-->"+this.temperatureData[i].equipmentname+"--"+this.temperatureData[i].howManyTimes+"--"+this.temperatureData[i].howManyTimes2);
        // }
        // console.log("--------end----------");
        this.initTempAndHumiEcharts();
        
      });
    },
    getInfo2() {
      getAction(this.url.equipment, null).then((res) => {
        console.log('equipment',res)
        this.operaStatistics.machineIncomplete = res.result.runSum;
        this.operaStatistics.machineCompleted = res.result.allSum;
      })
      postAction(this.url.repair, null).then((res) => {
        console.log('repair',res)
        this.operaStatistics.repairCompleted = res.result.totalnum;
        this.operaStatistics.repairIncomplete = res.result.todaynum;
      })
      getAction(this.url.upkeep, null).then((res) => {
        console.log('upkeep',res)
        this.operaStatistics.upkeepIncomplete = res.result;
      })
      getAction(this.url.upkeepAll, null).then((res) => {
        console.log('upkeepAll',res)
        this.operaStatistics.upkeepCompleted = res.result;
      })
      postAction(this.url.inspect, null).then((res) => {
        console.log('inspect',res)
        this.operaStatistics.inspectCompleted = res.result.totalnum;
        this.operaStatistics.inspectIncomplete = res.result.todaynum;
      })
      getAction(this.url.todayelectricity, null).then((res) => {
        console.log('todayelectricity',res.result)
        this.TodayElectricity = res.result;
        this.initEcharts();
      })
      getAction(this.url.envirinfo, null).then((res) => {
        console.log('envirinfo',res)
        this.temperatureData = res.result;
        this.initTempAndHumiEcharts();
      })


      this.alarmList = emsdata.homedata.alarmData;
      this.noticeList = emsdata.homedata.noticeData;
      // this.EnvironmentTemp = emsdata.homedata.EnvironmentTemp;

      // this.initEcharts();
      // this.initTempAndHumiEcharts();
    },
    /** 查询报警数据列表 */
    getAlarmList() {
      var queryParams = {
        pageNum: 1,
        pageSize: 5,
        status: '0',
      }
      listAlarmdata(queryParams).then((response) => {
        this.alarmList = response.rows;
      });
    },
    /** 查询消息列表 */
    getNoticeList() {
      var queryParams = {
        pageNum: 1,
        pageSize: 5,
      }
      listNotice(queryParams).then(response => {
        this.noticeList = response.rows;
      });
    },
    initEcharts(){
      var _this = this
      var chartDom = this.$refs.elecCharts
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '10%'];
          }
        },
        title: {
          left: 'center',
          text: '今日用电量'
        },
        // toolbox: {
        //   feature: {
        //     dataZoom: {
        //       yAxisIndex: 'none'
        //     },
        //     restore: {},
        //     saveAsImage: {}
        //   }
        // },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.TodayElectricity.hours
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%']
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100
          },
          {
            start: 0,
            end: 10
          }
        ],
        series: [
          {
            name: '分时用电量',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 158, 68)'
                },
                {
                  offset: 1,
                  color: 'rgb(255, 70, 131)'
                }
              ])
            },
            data: this.TodayElectricity.elec
          }
        ]
      };
      

      option && myChart.setOption(option);

      window.addEventListener("resize",function (){
        myChart.resize();
      });

    },


    initTempAndHumiEcharts(){
      
      for(var i in this.temperatureData){
        console.log("-->"+this.temperatureData[i].equipmentname+"--"+this.temperatureData[i].howManyTimes+"--"+this.temperatureData[i].howManyTimes2);
        this.equipmentnames[i] = this.temperatureData[i].equipmentname;
        this.temps[i] = this.temperatureData[i].howManyTimes;
        this.humis[i] = this.temperatureData[i].howManyTimes2;
      }

      var _this = this
      var chartDom = this.$refs.envirCharts
      console.log("温湿度", this.$refs)
      console.log("温湿度1", this.$refs.envirCharts)
      console.log("温湿度2", this.$refs.elecCharts)
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        
        legend: {
          data: ['湿度', '温度']
        },
        xAxis: [
          {
            type: 'category',
            data: this.equipmentnames,
            axisPointer: {
              type: 'shadow'
            },
            axisLabel: {
              rotate: 25, // 设置刻度标签旋转角度为45度
              // formatter: function(value) {
              //   if (value.length > 8) { // 当文本长度大于5时，截断并显示省略号
              //     return value.substring(0, 8) + '...';
              //   } else {
              //     return value;
              //   }
              // },
            },
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '湿度',
            interval: 50,
            axisLabel: {
              formatter: '{value} %'
            }
          },
          {
            type: 'value',
            name: '温度',
            interval: 5,
            axisLabel: {
              formatter: '{value} °C'
            }
          }
        ],
        series: [
          {
            name: '湿度',
            type: 'bar',
            tooltip: {
              valueFormatter: function (value) {
                return value + ' %';
              }
            },
            data: this.humis
          },
          {
            name: '温度',
            type: 'line',
            yAxisIndex: 1,
            tooltip: {
              valueFormatter: function (value) {
                return value + ' °C';
              }
            },
            data: this.temps
          }
        ]
      };

      option && myChart.setOption(option);
      
      window.addEventListener("resize",function (){
        myChart.resize();
      });
    },
  }
}
</script>

<style lang="less" scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.index-container {
  width: 100%;
  min-height: calc(100vh - 124px);
  .common-title{
    &::before{
      content: '';
      display: inline-block;
      width: 4px;
      height: 20px;
      background-image: linear-gradient(150deg, #5EB3F7, #6680F4);
      border-radius: 2px;
    }
    .text{
      padding-left: 8px;
      line-height: 20px;
      font-size: 14px;
    }
  }
  .opera-container{
    .item{
      padding: 16px 0 16px 20px;
      background: #fff;
      box-shadow: 0 2px 12px 0 rgba(48, 65, 86, 0.12);
      border-radius: 6px;
      margin-right: 12px;
      .content{
        padding: 20px 0 12px 10px;
        .tongji{
          .number{
            font-weight: bolder;
            font-size: 28px;
            color: #3ea9ff;
            font-family: '微软雅黑', serif;
            .number1{
              color: #ffb048;
              font-size: 30px;
            }
          }
          .text{
            font-size: 12px;
            margin-top: 4px;
            color: #737373;
            letter-spacing: 0.1em;
          }
        }
        .legend{
          // width: 50%;
          i{
            font-size: 2vw;
            border-radius: 100%;
            width: 60px;
            height: 60px;
            // text-align: center;
            // line-height: 60px;
          }
          .repair{
            color: #6680f4;
            background-image: linear-gradient(150deg, #5eb2f77c, #6680f47f);
          }
          .upkeep{
            color: #f9ba27;
            background-image: linear-gradient(150deg, #f7e55e76, #f9ba276b);
          }
          .spotcheck{
            color: #27b150;
            background-image: linear-gradient(150deg, #5ef78c72, #40cbe174);
          }
          .inspect{
            color: #e16b40;
            background-image: linear-gradient(150deg, #ffd3c372, #e16b406e);
          }
          .warning{
            color: #ff4949;
            background-image: linear-gradient(150deg, #eb7f7f77, #ff494979);
          }
        }
      }
    }
    .item:last-of-type{
      margin-right: 0;
    }
  }
  .box-card{
    .a-card__header{
      border: none;
    }
    .a-table th.a-table__cell.is-leaf, .a-table td.a-table__cell{
      border-right: none;
    }
    .a-table th.a-table__cell > .cell{
      color: #556485;
      // color: #648ff5;
    }
    .a-table .a-table__header-wrapper th, .a-table .a-table__fixed-header-wrapper th{
      background-color: #f2f7fd;
      // background-color: #e6f2ff;
    }
  }
  .trend-charts{
    width: 100%;
    min-height: calc(100vh - 18vh - 5.8vh - 84px - 58px - 57px - 50px);
    padding: 10px;
    margin-top: 10px;
    box-shadow: 0 2px 10px rgba(0,0,0,.1);
    background: #fff;
    border-radius: 4px;
  }
  .alarmandnotice{
    margin-top: 10px;
  }
  .ant-table-wrapper {
    margin-top: 10px;
  }
}
</style>
