<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!-- <a-col :span="24">
            <a-form-model-item label="保养编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemcode">
              <a-input v-model="model.itemcode" placeholder="请输入保养编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="保养项名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemname">
              <a-input v-model="model.itemname" placeholder="请输入保养项名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipdefid">
              <!-- <a-input v-model="model.equipdefid" placeholder="请输入设备类型ID（设备树，可单选可多选，多选设备类型用逗号连接）"  ></a-input> -->
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择设备类型"
                :multiple="multiples"
                v-model="model.equipdefid"
                dict="tpm_equipment_tree,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                >
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="保养标准" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemcontent">
              <a-input v-model="model.itemcontent" placeholder="请输入保养标准"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
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
    name: 'UpkeepItemForm',
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
        multiples: false,
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
            { required: true, message: '请输入保养项名称!'},
          ],
          equipdefid: [
            { required: true, message: '请选择设备类型!'},
          ],
        },
        url: {
          add: "/cmmsUpkeepItem/cmmsUpkeepItem/add",
          edit: "/cmmsUpkeepItem/cmmsUpkeepItem/edit",
          queryById: "/cmmsUpkeepItem/cmmsUpkeepItem/queryById"
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
        // this.edit(this.modelDefault);
        this.multiples = true;
        this.visible = true;
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.multiples = false;
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