<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    @close="close"
    destroyOnClose
    :visible="visible"
    >
    <a-spin :spinning="confirmLoading">
      <!-- 基础信息详情 -->
      <a-descriptions title="" bordered size="middle" :column="2">
        <a-descriptions-item label="维修工单编号">
          {{detailData.repaircode}}
        </a-descriptions-item>
        <a-descriptions-item label="维修工单名称">
          {{detailData.repairname}}
        </a-descriptions-item>
        <a-descriptions-item label="录入人">
          {{detailData.createBy}}
        </a-descriptions-item>
        <a-descriptions-item label="工单创建时间">
          {{detailData.createTime}}
        </a-descriptions-item>
        <a-descriptions-item label="设备编号">
          {{detailData.equipmentcode}}
        </a-descriptions-item>
        <a-descriptions-item label="设备名称">
          {{detailData.equipmentname}}
        </a-descriptions-item>
        <a-descriptions-item label="优先级">
          <!-- 0 普通 1 紧急 2 特急	 -->
          <a-tag v-if="detailData.priority === '0'">普通</a-tag>
          <a-tag v-if="detailData.priority === '1'" color="orange">紧急</a-tag>
          <a-tag v-if="detailData.priority === '2'" color="red">特急</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="故障日期">
          {{detailData.faultdate}}
        </a-descriptions-item>
        <a-descriptions-item label="故障描述" :span="2">
          {{detailData.faultdesc}}
        </a-descriptions-item>
        <a-descriptions-item label="待办人">
          {{detailData.repairuser}}
        </a-descriptions-item>

        <!-- <a-descriptions-item label="维修时间">
          {{detailData.repairdate}}
        </a-descriptions-item> -->
        <!-- <a-descriptions-item label="维修反馈">
          {{detailData.repaircode}}
        </a-descriptions-item> -->
        <!-- <a-descriptions-item label="处理结果">
          {{detailData.repaircode}}
        </a-descriptions-item> -->
        <a-descriptions-item label="当前维修状态">
          <!-- 0 上报故障 1 维修检查 2 维修挂单 3 维修结束 -->
          <a-tag v-if="detailData.status === '0'" color="orange">上报故障</a-tag>
          <a-tag v-if="detailData.status === '1'" color="blue">维修派单</a-tag>
          <a-tag v-if="detailData.status === '2'" color="blue">维修检查</a-tag>
          <a-tag v-if="detailData.status === '3'">维修挂单</a-tag>
          <a-tag v-if="detailData.status === '4'">维修结束</a-tag>
        </a-descriptions-item>
      </a-descriptions>
      <!-- 分割线 -->
      <a-divider orientation="left" style="font-size: 14px;"> 操作日志 </a-divider>
      <!-- 操作日志详情 -->
      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="logData"
        class="j-table-force-nowrap cmms-table">

        <span slot="nodename" slot-scope="text">
          <!-- 0 上报故障 1 维修检查 2 维修挂单 3 维修结束 -->
          <span v-if="text === '0'">上报故障</span>
          <span v-if="text === '1'">维修派单</span>
          <span v-if="text === '2'">维修检查</span>
          <span v-if="text === '3'">维修挂单</span>
          <span v-if="text === '4'">维修结束</span>
        </span>

      </a-table>
    </a-spin>

  </a-drawer>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  export default {
    name: 'RepairManageDetail',
    data () {
      return {
        title:"详情",
        width:'60%',
        visible: false,
        confirmLoading: false, // 加载中
        detailData: {}, // 详情
        logData: [], // 操作日志表格数据
        // 表头
        columns: [
          // {
          //   title: '序号',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title:'节点名称',
            align:"center",
            dataIndex: 'nodename',
            scopedSlots: { customRender: 'nodename' }
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'handledate'
          },
          {
            title:'处理意见',
            align:"center",
            dataIndex: 'opinion'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
        ],
      }
    },
    methods: {
      detail (record) {
        this.visible = true
        this.title = record.repairname
        // this.title = record.repairname + ' 详情'
        // this.detailData = record
        this.confirmLoading = true
        getAction('/cmmsRepair/cmmsRepair/queryById',{id:record.id}).then((res)=>{
          if(res.success){
            this.detailData = res.result
            this.logData = res.result.nodeList
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.confirmLoading = false;
        })
      },
      close () {
        this.visible = false;
      },
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
  .drawer-footer{
    position: absolute;
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>