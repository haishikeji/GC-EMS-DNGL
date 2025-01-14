<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentname">
              <a-input v-model="model.equipmentname" placeholder="请输入设备名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="设备分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmenttreeid">
              <!-- <j-category-select v-model="model.equipmenttreeid" pcode="id" placeholder="请选择设备分类" back="name" @change="handleCategoryChange" /> -->
              <!-- <j-tree-select
                ref="treeSelect"
                placeholder="请选择设备分类"
                v-model="model.equipmenttreeid"
                dict="tpm_equipment_tree,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                style="width:89%;"
                >
              </j-tree-select> -->
              <a-tree-select
                v-model="model.equipmenttreeid"
                style="width: 89%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeData"
                placeholder="请选择设备分类"
              ></a-tree-select>
              <a-button type="primary" icon="plus" @click="handleAdd"></a-button>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentcode">
              <span slot="label">
                设备编号&nbsp;
                <a-tooltip title="若未填写设备编号 则自动生成">
                  <a-icon type="exclamation-circle" theme="filled" />
                </a-tooltip>
              </span>
              <a-input v-model="model.equipmentcode" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="spec">
              <a-input v-model="model.spec" placeholder="请输入规格型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="安装地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" placeholder="请输入安装地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="区域" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="spaceid">
              <!-- <a-input v-model="model.spaceid" placeholder="请输入区域"  ></a-input> -->
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择区域"
                v-model="model.spaceid"
                dict="base_space,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                >
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="送检周期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentCycle">
              <a-input-number v-model="model.equipmentCycle" style="width:49%;" placeholder="请输入送检周期" />
              <j-dict-select-tag type="list" v-model="model.cycleUnit" dictCode="cycle_unit" style="width:49%;" placeholder="请选择送检周期单位" />
            </a-form-model-item>
            <!-- <a-form-model-item prop="cycleUnit" style="width:40%;">
            </a-form-model-item> -->
          </a-col>
          <!-- <a-col :span="12">
            <a-form-model-item label="周期单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cycleUnit">
              <j-dict-select-tag type="list" v-model="model.cycleUnit" dictCode="cycle_unit" placeholder="请选择周期单位" />
            </a-form-model-item>
          </a-col> -->
          <a-col :span="12">
            <a-form-model-item label="出厂编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="factoryNo">
              <a-input v-model="model.factoryNo" placeholder="请输入出厂编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="出厂日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="productDate">
              <j-date placeholder="请选择出厂日期"  v-model="model.productDate" :show-time="true" date-format="YYYY-MM-DD" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="启用日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="activeDate">
              <j-date placeholder="请选择启用日期"  v-model="model.activeDate" :show-time="true" date-format="YYYY-MM-DD" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="责任部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="responseDepartment">
              <j-select-depart v-model="model.responseDepartment" placeholder="请输入责任部门" :multi="false"  />
              <!-- <a-input v-model="model.responseDepartment" placeholder="请输入责任部门"  ></a-input> -->
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="responsePerson">
              <!-- <a-input v-model="model.responsePerson" placeholder="请输入负责人"  ></a-input> -->
              <j-search-select-tag v-model="model.responsePerson" placeholder="请选择负责人" dict="sys_user,realname,id"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="领用人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipient">
              <!-- <a-input v-model="model.recipient" placeholder="请输入领用人"  ></a-input> -->
              <j-search-select-tag v-model="model.recipient" placeholder="请选择领用人" dict="sys_user,realname,id"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="管理状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="manageStatus">
              <j-dict-select-tag type="list" v-model="model.manageStatus" dictCode="manage_status" placeholder="请选择管理状态" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <equipment-tree-modal ref="modalForm" @fMethod="modalFormOk2"></equipment-tree-modal>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { duplicateCheck } from '@/api/api'
  import { validateDuplicateValue } from '@/utils/util'
  import EquipmentTreeModal from '@/views/module_tpm/equipmentTree/modules/EquipmentTreeModal'

  export default {
    name: 'EquipmentForm',
    components: {
      EquipmentTreeModal
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
          manageStatus: "1",
          // spaceid: "1780065347359883265",
          // responseDepartment: "67fc001af12a4f9b8458005d3f19934a",
          spec: "无",
        },
        treeData:[],
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
          equipmentname: [
            { required: true, message: '请输入设备名称!'},
          ],
          equipmentcode: [
            { validator: this.validateTemplateCode}
          ],
          equipmenttreeid: [
            { required: true, message: '请选择设备分类!'},
          ],
          spec: [
            { required: true, message: '请输入规格型号!'},
          ],
          spaceid: [
            { required: true, message: '请选择区域!'},
          ],
          responseDepartment: [
            { required: true, message: '请选择责任部门!'},
          ],
          manageStatus: [
            { required: true, message: '请选择管理状态!'},
          ],
        },
        url: {
          add: "/tpmEquipment/tpmEquipment/add",
          edit: "/tpmEquipment/tpmEquipment/edit",
          queryById: "/tpmEquipment/tpmEquipment/queryById"
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
      this.getTpmTreeData()
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
      handleCategoryChange(value,backObj){
         this.model = Object.assign(this.model, backObj);
      }
    }
  }
</script>