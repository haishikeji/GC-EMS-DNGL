<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <a-input-number v-model="model.equipmentid" placeholder="请输入设备ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="额定功率(KWh)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ratedpower">
              <a-input-number v-model="model.ratedpower" placeholder="请输入额定功率(KWh)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="功率点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnamep">
              <a-input v-model="model.tagnamep" placeholder="请输入功率点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="A相电流点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnameca">
              <a-input v-model="model.tagnameca" placeholder="请输入A相电流点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="A相电压点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnameva">
              <a-input v-model="model.tagnameva" placeholder="请输入A相电压点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="B相电流点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnamecb">
              <a-input v-model="model.tagnamecb" placeholder="请输入B相电流点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="B相电压点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnamevb">
              <a-input v-model="model.tagnamevb" placeholder="请输入B相电压点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="C相电流点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnamecc">
              <a-input v-model="model.tagnamecc" placeholder="请输入C相电流点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="C相电压点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagnamevc">
              <a-input v-model="model.tagnamevc" placeholder="请输入C相电压点位名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实际功率当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvaluep">
              <a-input-number v-model="model.tagvaluep" placeholder="请输入实际功率当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="A相电流当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvalueca">
              <a-input-number v-model="model.tagvalueca" placeholder="请输入A相电流当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="A相电压当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvalueva">
              <a-input-number v-model="model.tagvalueva" placeholder="请输入A相电压当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="B相电流当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvaluecb">
              <a-input-number v-model="model.tagvaluecb" placeholder="请输入B相电流当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="B相电压当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvaluevb">
              <a-input-number v-model="model.tagvaluevb" placeholder="请输入B相电压当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="C相电流当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvaluecc">
              <a-input-number v-model="model.tagvaluecc" placeholder="请输入C相电流当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="C相电压当前值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvaluevc">
              <a-input-number v-model="model.tagvaluevc" placeholder="请输入C相电压当前值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="记录时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="logtime">
              <j-date placeholder="请选择记录时间" v-model="model.logtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态：0正常、1运行、2待机、3停机" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入状态：0正常、1运行、2待机、3停机"  ></a-input>
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
    name: 'EquipmentStatusForm',
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
          add: "/equipmentStatus/equipmentStatus/add",
          edit: "/equipmentStatus/equipmentStatus/edit",
          queryById: "/equipmentStatus/equipmentStatus/queryById"
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