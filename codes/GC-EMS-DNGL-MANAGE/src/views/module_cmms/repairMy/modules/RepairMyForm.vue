<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="维修工单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repaircode">
              <a-input v-model="model.repaircode" placeholder="请输入维修工单编号" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="维修工单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repairname">
              <a-input v-model="model.repairname" placeholder="请输入工单名称" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentname">
              <a-input v-model="model.equipmentname" placeholder="请输入设备名称" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="故障日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="faultdate">
              <j-date placeholder="请选择故障日期" v-model="model.faultdate"  style="width: 100%" disabled/>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="故障描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="faultdesc">
              <a-textarea v-model="model.faultdesc" placeholder="请输入故障描述" disabled></a-textarea>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="优先级：0普通，1紧急，2特急" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="priority">
              <a-input v-model="model.priority" placeholder="请输入优先级：0普通，1紧急，2特急"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="维修反馈" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="feedback">
              <a-textarea v-model="model.feedback" placeholder="请输入维修反馈"  ></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="维修时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repairdate">
              <j-date placeholder="请选择维修时间" v-model="model.repairdate"  style="width: 100%" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <!-- <a-form-model-item label="是否挂单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-switch :checked="model.status === '2' ? true : false" checked-children="是" un-checked-children="否" @change="changeStatus"/>
            </a-form-model-item> -->
            <a-form-model-item label="本次维修状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-radio-group v-model="model.status" :default-value="4">
                <a-radio :value="2"> 在办 </a-radio>
                <a-radio :value="3"> 挂单 </a-radio>
                <a-radio :value="4"> 结束 </a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="维修工单状态: 0正常、1冻结、2拒绝" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repairstatus">
              <a-input v-model="model.repairstatus" placeholder="请输入维修工单状态: 0正常、1冻结、2拒绝"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" placeholder="请输入备注"></a-textarea>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { putAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'RepairMyForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           faultdesc: [
              { required: true, message: '请输入故障描述!'},
           ],
           repairuser: [
              { required: true, message: '请输入待办人!'},
           ],
           repairdate: [
              { required: true, message: '请输入维修时间!'},
           ],
           status: [
              { required: true, message: '请选择本次维修状态!'},
           ],
           feedback: [
              { required: true, message: '请输入维修反馈!'},
           ],
           result: [
              { required: true, message: '请输入维修结果!'},
           ],
        },
        url: {
          feedback: "/cmmsRepair/cmmsRepair/repair",
          queryById: "/cmmsRepairMy/cmmsRepairMy/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      // add () {
      //   this.edit(this.modelDefault);
      // },
      // edit (record) {
      //   this.model = Object.assign({}, record);
      //   this.visible = true;
      // },
      feedback(record){
        this.model = Object.assign({}, record);
        console.log(this.model)
        this.visible = true;
      },
      changeStatus(checked){
        console.log(checked)
        this.model.status = checked ? '2' : '3'
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            this.model.nodeList = [
              {
                handledate: this.model.repairdate,
                nodename: this.model.status,
                opinion: this.model.feedback,
                remark: this.model.remark,
                relid: this.model.id
              }
            ]
            putAction(this.url.feedback, this.model).then((res)=>{
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