<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <a-input-number v-model="model.equipmentid" placeholder="请输入设备id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备状态：1运行、2待机、3停机" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入设备状态：1运行、2待机、3停机"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starttime">
              <j-date placeholder="请选择开始时间" v-model="model.starttime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtime">
              <j-date placeholder="请选择结束时间" v-model="model.endtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时长(分钟)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="duration">
              <a-input-number v-model="model.duration" placeholder="请输入时长(分钟)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属年" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="year">
              <a-input-number v-model="model.year" placeholder="请输入所属年" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属月" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="month">
              <a-input-number v-model="model.month" placeholder="请输入所属月" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day">
              <a-input v-model="model.day" placeholder="请输入日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <a-input v-model="model.time" placeholder="请输入时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属年月" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yearmonth">
              <a-input v-model="model.yearmonth" placeholder="请输入所属年月"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属周" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="week">
              <a-input-number v-model="model.week" placeholder="请输入所属周" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="周几" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayofweek">
              <a-input-number v-model="model.dayofweek" placeholder="请输入周几" style="width: 100%" />
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
    name: 'EquipmentOnoffSectionForm',
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
           equipmentid: [
              { required: true, message: '请输入设备id!'},
           ],
        },
        url: {
          add: "/equipmentOnoffSection/equipmentOnoffSection/add",
          edit: "/equipmentOnoffSection/equipmentOnoffSection/edit",
          queryById: "/equipmentOnoffSection/equipmentOnoffSection/queryById"
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