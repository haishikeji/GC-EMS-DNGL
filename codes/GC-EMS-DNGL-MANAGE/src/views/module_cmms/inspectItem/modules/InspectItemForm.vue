<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!-- <a-col :span="24">
            <a-form-model-item label="巡检项编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemcode">
              <a-input v-model="model.itemcode" placeholder="请输入巡检项编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemname">
              <a-input v-model="model.itemname" placeholder="请输入名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="巡检项类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classification">
              <j-dict-select-tag type="list" v-model="model.classification" dictCode="inspect_item_type" placeholder="请选择巡检类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="model.classification === '1'">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipdefid">
              <!-- <a-input v-model="model.equipdefid" placeholder="请选择设备类型"  ></a-input> -->
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择设备类型"
                v-model="model.equipdefid"
                dict="tpm_equipment_tree,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                >
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="标签(多个用逗号隔开)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tag">
              <a-input v-model="model.tag" placeholder="请输入标签(多个用逗号隔开)"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="巡检标准" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inspectionstandards">
              <a-input v-model="model.inspectionstandards" placeholder="请输入巡检标准"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <!-- <a-input v-model="model.remark" placeholder="请输入备注"></a-input> -->
              <a-textarea v-model="model.remark" rows="3" placeholder="请输入备注" />
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
    name: 'InspectItemForm',
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
           itemname: [
              { required: true, message: '请输入巡检项名称!'},
           ],
           equipdefid: [
              { required: true, message: '请选择设备!'},
           ],
           inspectionstandards: [
              { required: true, message: '请输入巡检标准!'},
           ],
           classification: [
              { required: true, message: '请选择巡检项类型!'},
           ],
        },
        url: {
          add: "/cmmsInspectItem/cmmsInspectItem/add",
          edit: "/cmmsInspectItem/cmmsInspectItem/edit",
          queryById: "/cmmsInspectItem/cmmsInspectItem/queryById"
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