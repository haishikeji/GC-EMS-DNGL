<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container>
      <a-form-model :model="formModel" :rules="formRules" ref="form" slot="detail">
        <div class="cmms-dialog-item-title">送检工单信息</div>
          <a-row style="padding-top: 24px;">
            <a-col :span="24">
              <a-form-model-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="planname">
                <!-- <a-input v-model="model.planname" placeholder="请输入计划名称"  ></a-input> -->
                <j-image-upload text="上传" v-model="formModel.uploadFileList" isMultiple></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        <div class="cmms-dialog-item-title">送检设备信息</div>
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
              <a-col :xl="6" :lg="7" :md="8" :sm="24">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="getSJSBOptions2" icon="search">查询</a-button>
                  <a-button type="primary" @click="getSJSBOptions3" icon="reload" style="margin-left: 8px">重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
    <!-- 查询区域-END -->

<!--    <a-button @click="handleFK">提交反馈</a-button>-->
    <!-- <j-form-container>
      <a-form-model :model="formModel" :rules="formRules" ref="form" slot="detail"> -->
        <a-table
          size="small"
          :scroll="{x:true}"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="formModel.submissionEquipmentList"
          :pagination="false"
          class="j-table-force-nowrap">

          <template slot="remark" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.remark'" :rules="formRules.remark" style="margin-bottom: 0px;">
              <a-input v-model="record.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </template>
          <template slot="teamConfirm" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.teamConfirm'" :rules="formRules.teamConfirm" style="margin-bottom: 0px;">
              <a-input v-model="record.teamConfirm" placeholder="请输入班组确认"></a-input>
            </a-form-model-item>
          </template>
          <template slot="submissionReturnDate" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.submissionReturnDate'" :rules="formRules.submissionReturnDate" style="margin-bottom: 0px;">
              <a-date-picker placeholder="请选择送回日期"  v-model="record.submissionReturnDate" :show-time="true" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </template>
          <template slot="submissionDate" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.submissionDate'" :rules="formRules.submissionDate" style="margin-bottom: 0px;">
              <a-date-picker placeholder="请选择检定日期"  v-model="record.submissionDate" :show-time="true" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </template>

          <template slot="submissionStatus" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.submissionStatus'" :rules="formRules.submissionStatus" style="margin-bottom: 0px;">
              <j-dict-select-tag placeholder="请选择送检状态" v-model="record.submissionStatus" dictCode="submission_status"/>
            </a-form-model-item>
          </template>
          <template slot="submissionResult" slot-scope="text, record,index">
            <a-form-model-item :prop="'submissionEquipmentList.'+index+'.submissionResult'" :rules="formRules.submissionResult" style="margin-bottom: 0px;">
              <j-dict-select-tag placeholder="请选择检定结果" v-model="record.submissionResult" dictCode="device_verification_result"/>
            </a-form-model-item>
          </template>

        </a-table>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SubmissionEquipmentModalFK from "@views/module_cmms/submission/modulesFK/SubmissionEquipmentModalFK";
import {getAction, httpAction} from "@api/manage";

export default {
  name: 'SubmissionEquipmentformFK',
  mixins:[JeecgListMixin, mixinDevice],
  components: {
    SubmissionEquipmentModalFK
  },
  data () {
    return {
      confirmLoading: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 2 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      // 设备下拉列表
      deviceOption: [],
      //送检工单id
      csId: '',
      //送检工单设备查询参数
      queryParam: {},
      // // 送检设备及相关数据
      // submissionEquipmentList: [],
      formModel: {
        // 送检设备及相关数据
        submissionEquipmentList: [],
      },
      formRules:{},
      description: '送检工单设备表管理页面',
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
          dataIndex: 'submissionDate',
          scopedSlots: { customRender: 'submissionDate' }
        },
        {
          title:'检定结果',
          align:"center",
          width:90,
          dataIndex: 'submissionResult_dictText',
          scopedSlots: { customRender: 'submissionResult' }
        },
        {
          title:'班组确认',
          align:"center",
          dataIndex: 'teamConfirm',
          scopedSlots: { customRender: 'teamConfirm' }
        },
        {
          title:'备注',
          align:"center",
          dataIndex: 'remark',
          scopedSlots: { customRender: 'remark' }
        },
        {
          title:'送检状态',
          align:"center",
          width:100,
          dataIndex: 'submissionStatus_dictText',
          scopedSlots: { customRender: 'submissionStatus' }
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
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align:"center",
        //   fixed:"right",
        //   width:147,
        //   scopedSlots: { customRender: 'fk' }
        // }
      ],
      url: {
        list: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/list",
        fk: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/fk",
        deviceList: "cmmsSubmissionEquipment/cmmsSubmissionEquipment/listByCmmsSubmissionId",
        queryById: "/cmmsSubmission/cmmsSubmission/queryById",
        deviceData: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/voListByCmmsSubmissionId"
      },
    }
  },
  methods: {
    handleFK(){
    },
    getSJSBOptions(cmmsSubmissionId) {
      this.csId = cmmsSubmissionId
      // getAction(this.url.list, {cmmsSubmissionId:cmmsSubmissionId}).then((res) => {
      //   this.formModel.submissionEquipmentList = res.result.records
      // })
      // 获取工单详情
      getAction(this.url.queryById, {id:cmmsSubmissionId}).then((res) => {
        console.log(1111,res)
        this.formModel.submissionEquipmentList = res.result.submissionEquipmentList
        const b = res.result.uploadFileList.map((res) => {
          return res.url
        })
        this.formModel.uploadFileList = b.join(',')
      })
      // 获取设备下拉列表
      getAction(this.url.deviceList, {cmmsSubmissionId:cmmsSubmissionId}).then((res) => {
        this.deviceOption = res.result.map((res) => {
          return {
            value: res.equipmentid,
            label: res.equipmentname
          }
        })
      })
    },
    getSJSBOptions2() {
      this.queryParam.cmmsSubmissionId = this.csId
      getAction(this.url.deviceData, this.queryParam).then((res) => {
        this.formModel.submissionEquipmentList = res.result
      })
    },
    getSJSBOptions3() {
      this.queryParam = {}
      this.queryParam.cmmsSubmissionId = this.csId
      getAction(this.url.deviceData, this.queryParam).then((res) => {
        this.formModel.submissionEquipmentList = res.result
      })
    },
    submitForm () {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          httpurl+=this.url.fk;
          method = 'put';
          // if(!this.model.id){
          //   httpurl+=this.url.add;
          //   method = 'post';
          // }else{
          //   httpurl+=this.url.edit;
          //   method = 'put';
          // }
          if (this.formModel.uploadFileList.length != 0) {
            const a  = this.formModel.uploadFileList.split(',')
            this.formModel.uploadFileList = a.map((res) => {
              return {
                url: res
              }
            })
          }else{
            this.formModel.uploadFileList = []
          }
          this.formModel.id = this.csId
          console.log(this.formModel)
          httpAction(httpurl,this.formModel,method).then((res)=>{
            if(res.success){
              that.$message.success(res.message);
              that.$emit('ok');
            }else{
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
          })
        }

      })
    },
  }
}
</script>
<style scoped>
/* @import '~@assets/less/common.less'; */
@import "~@/assets/less/uStyle.less";
</style>