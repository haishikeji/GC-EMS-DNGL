<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="送检工单id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cmmsSubmissionId">
              <a-input v-model="model.cmmsSubmissionId" placeholder="请输入送检工单id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <a-input v-model="model.equipmentid" placeholder="请输入设备id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检定日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submissionDate">
              <j-date placeholder="请选择检定日期"  v-model="model.submissionDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检定结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submissionResult">
              <j-dict-select-tag type="list" v-model="model.submissionResult" dictCode="device_verification_result" placeholder="请选择检定结果" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="有效日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submissionEffectiveDate">
              <j-date placeholder="请选择有效日期"  v-model="model.submissionEffectiveDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="班组确认" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="teamConfirm">
              <a-input v-model="model.teamConfirm" placeholder="请输入班组确认"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="送检状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submissionStatus">
              <j-dict-select-tag type="list" v-model="model.submissionStatus" dictCode="submission_status" placeholder="请选择送检状态" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="送回日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="submissionReturnDate">
              <j-date placeholder="请选择送回日期"  v-model="model.submissionReturnDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SubmissionEquipmentForm',
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
        },
        url: {
          add: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/add",
          edit: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/edit",
          queryById: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/queryById"
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
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
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