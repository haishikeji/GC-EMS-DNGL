<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemname">
              <a-input v-model="model.itemname" placeholder="请输入点检项名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmenttreeid">
              <!-- <j-search-select-tag v-model="model.equipmenttreeid" dict="tpm_equipment_tree,name,id"  /> -->
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择设备类型"
                :multiple="multiples"
                v-model="model.equipmenttreeid"
                dict="tpm_equipment_tree,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                >
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="点检项编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemcode">
              <a-input v-model="model.itemcode" placeholder="请输入点检项编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="标准" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conditions">
              <a-input v-model="model.conditions" placeholder="请输入标准"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="点检项分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemtype">
              <j-dict-select-tag type="list" v-model="model.itemtype" dictCode="spotcheck_item_type" placeholder="请选择点检项分类" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标签" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tag">
              <a-input v-model="model.tag" placeholder="请输入标签"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <!-- <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input> -->
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
    name: 'SpotcheckItemForm',
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
        multiples: false,
        confirmLoading: false,
        validatorRules: {
          itemname: [
            { required: true, message: '请输入点检项名称!'},
          ],
          equipmenttreeid: [
            { required: true, message: '请选择设备类型!'},
          ],
        },
        url: {
          add: "/cmmsSpotcheckItem/cmmsSpotcheckItem/add",
          edit: "/cmmsSpotcheckItem/cmmsSpotcheckItem/edit",
          queryById: "/cmmsSpotcheckItem/cmmsSpotcheckItem/queryById"
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