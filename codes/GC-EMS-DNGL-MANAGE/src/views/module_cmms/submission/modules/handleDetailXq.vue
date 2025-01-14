<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="true"
    @close="close"
    destroyOnClose
    :visible="visible">
    <a-descriptions bordered>
      <a-descriptions-item label="送检编号">
        {{dataList.sjcode}}
      </a-descriptions-item>
      <a-descriptions-item label="送检人">
        {{dataList.realname}}
      </a-descriptions-item>
      <a-descriptions-item label="检定方式">
        {{dataList.verificateMethodText}}
      </a-descriptions-item>
      <a-descriptions-item label="检定单位">
        {{dataList.verificateClientName}}
      </a-descriptions-item>
      <a-descriptions-item label="送检设备数量">
        {{dataList.totalNum}}
      </a-descriptions-item>
      <a-descriptions-item label="返回设备数量">
        {{dataList.returnNum}}
      </a-descriptions-item>
      <a-descriptions-item label="送检工单状态">
        {{dataList.submissionGdstatusText}}
      </a-descriptions-item>
      <a-descriptions-item label="备注" :span="2">
        {{dataList.remark}}
      </a-descriptions-item>
      <!-- <a-descriptions-item label="Status" :span="3">
        <a-badge status="processing" text="Running" />
      </a-descriptions-item> -->
      <!-- <a-descriptions-item label="Config Info">
        Data disk type: MongoDB
        <br />
        Database version: 3.4
        <br />
        Package: dds.mongo.mid
        <br />
        Storage space: 10 GB
        <br />
        Replication factor: 3
        <br />
        Region: East China 1<br />
      </a-descriptions-item> -->
      <a-descriptions-item label="反馈图片" :span="3">
        <div style="display:flex; flex-wrap:wrap" >
          <img v-for="item in dataList.uploadFileList" :key="item.fileid" :src="imgURL+item.url" height="90px" style="padding: 5px 10px 5px 0px;"/>
        </div>
      </a-descriptions-item>
    </a-descriptions>
    <a-divider orientation="left" style="font-size: 14px;"> 送检设备 </a-divider>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="getSJSBOptions2">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备">
              <!-- <j-search-select-tag placeholder="请选择设备" v-model="queryParam.equipmentid" dict="tpm_equipment,equipmentname,id"/> -->
              <a-select placeholder="请选择设备" v-model="queryParam.equipmentid" show-search allowClear :options="deviceOption"></a-select>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检定结果">
              <j-dict-select-tag placeholder="请选择检定结果" allowClear v-model="queryParam.submissionResult" dictCode="device_verification_result"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="送检状态">
              <j-dict-select-tag placeholder="请选择送检状态" allowClear v-model="queryParam.submissionStatus" dictCode="submission_status"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24" style="padding-left:0px;">
            <span style="float: left;overflow: hidden;">
              <a-button type="primary" @click="getSJSBOptions3" icon="reload" style="margin-left: 8px;">重置</a-button>
              <a-button type="primary" @click="getSJSBOptions2" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-table
      size="small"
      :scroll="{x:true}"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="dataList.submissionEquipmentList"
      :pagination="false"
      class="j-table-force-nowrap">
    </a-table>
    <a-divider orientation="left" style="font-size: 14px;"> 操作日志 </a-divider>
    <a-table
      size="small"
      :scroll="{x:true}"
      bordered
      rowKey="id"
      :columns="columns2"
      :dataSource="dataList.orderNodeSubmissionVOList"
      :pagination="false"
      class="j-table-force-nowrap">
    </a-table>
  </a-drawer>
</template>

<script>

import {getAction, httpAction} from "@api/manage";

  export default {
    name: 'handleDetailXq',
    data () {
      return {
        title:"",
        width:1000,
        visible: false,
        disableSubmit: false,
        // 设备下拉列表
        deviceOption: [],
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          // {
          //   title:'送检工单id',
          //   align:"center",
          //   dataIndex: 'cmmsSubmissionId'
          // },
          {
            title:'设备',
            align:"center",
            dataIndex: 'equipmentname'
          },
          {
            title:'检定日期',
            align:"center",
            dataIndex: 'submissionDate'
          },
          {
            title:'检定结果',
            align:"center",
            width:90,
            dataIndex: 'submissionResultText'
          },
          {
            title:'班组确认',
            align:"center",
            dataIndex: 'teamConfirm'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark',
          },
          {
            title:'送检状态',
            align:"center",
            width:100,
            dataIndex: 'submissionStatusText',
          },
          {
            title:'送回日期',
            align:"center",
            dataIndex: 'submissionReturnDate',
            scopedSlots: { customRender: 'submissionReturnDate' }
          },
          {
            title:'有效日期',
            align:"center",
            dataIndex: 'submissionEffectiveDate'
          },
        ],
        columns2: [
          {
            title:'节点名称',
            align:"center",
            dataIndex: 'nodename'
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'createTime'
          },
        ],
        url: {
            queryById: "/cmmsSubmission/cmmsSubmission/queryById",
            deviceList: "cmmsSubmissionEquipment/cmmsSubmissionEquipment/listByCmmsSubmissionId",
            deviceData: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/voListByCmmsSubmissionId"
        },
        csId: '',
        // 详情数据
        dataList: {
          orderNodeSubmissionVOList: [],
          submissionEquipmentList: [],
          uploadFileList: []
        },
        imgURL: window._CONFIG['domianURL']+"/sys/common/static/"
      }
    },
    methods: {
      findHandle(id){
        this.csId = id
          // 获取送检详情
          getAction(this.url.queryById, {id:id}).then((res) => {
              console.log(77,res)
              this.dataList = res.result
          })
          getAction(this.url.deviceList, {cmmsSubmissionId:id}).then((res) => {
            this.deviceOption = res.result.map((res) => {
              return {
                value: res.equipmentid,
                label: res.equipmentname
              }
            })
          })
          this.visible = true
      },
      getSJSBOptions2() {
        this.queryParam.cmmsSubmissionId = this.csId
        getAction(this.url.deviceData, this.queryParam).then((res) => {
          this.dataList.submissionEquipmentList = res.result
        })
      },
      getSJSBOptions3() {
        this.queryParam = {}
        this.queryParam.cmmsSubmissionId = this.csId
        getAction(this.url.deviceData, this.queryParam).then((res) => {
          this.dataList.submissionEquipmentList = res.result
        })
      },
    //   add () {
    //     this.visible=true
    //     // this.$nextTick(()=>{
    //     //   this.$refs.realForm.add();
    //     // })
    //   },
    //   edit (record) {
    //     this.visible=true
    //     // this.$nextTick(()=>{
    //     //   this.$refs.realForm.edit(record);
    //     // });
    //   },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      submitCallback(){
        // this.$emit('ok');
        this.visible = false;
      },
      handleOk () {
        // this.$refs.realForm.submitForm();
      },
      handleCancel () {
        this.close()
      }
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