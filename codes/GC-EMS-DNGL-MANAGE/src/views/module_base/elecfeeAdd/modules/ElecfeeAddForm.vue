<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deptid">
              <j-select-depart v-model="model.deptid" :multi="false"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="费用类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="addtype">
              <j-dict-select-tag type="list" v-model="model.addtype" dictCode="elecfee_add_addtype" placeholder="请选择费用类别" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电价(元/kw)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="price">
              <a-input-number v-model="model.price" placeholder="请输入电价(元/kw)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="begintime">
              <j-date placeholder="请选择开始时间" v-model="model.begintime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtime">
              <j-date placeholder="请选择结束时间" v-model="model.endtime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" placeholder="请输入备注" rows="3" ></a-textarea>
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
    name: 'ElecfeeAddForm',
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
           addtype: [
              { required: true, message: '请选择费用类别!'},
           ],
           price: [
              { required: true, message: '请输入电价!'},
           ],
           begintime: [
              { required: true, message: '请输入开始时间!'},
           ],
        },
        url: {
          add: "/elecfeeAdd/elecfeeAdd/add",
          edit: "/elecfeeAdd/elecfeeAdd/edit",
          queryById: "/elecfeeAdd/elecfeeAdd/queryById"
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