<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="工单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repairname">
              <a-input v-model="model.repairname" placeholder="请输入工单名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="选择设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <!-- <j-search-select-tag v-model="model.equipmentid" placeholder="请选择设备" dict="tpm_equipment,equipmentname,id" /> -->
              <a-select
              v-model="model.equipmentid"
              placeholder="请输入设备名称或设备编号"
              show-search
              :filterOption="filterOptions"
              @search="searchDevice"
              allowClear>
                <a-select-option v-for="(item, index) in deviceOptions" :key="index" :value="item.id" :label="item.equipmentname">
                  <span>{{item.equipmentname}}</span>
                  <span style="position: absolute;right: 2%;">{{ item.equipmentcode }}</span>
                </a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="优先级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="priority">
              <j-dict-select-tag  v-model="model.priority"  placeholder="请选择优先级" :type="'radio'" dictCode="repair_priority"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="故障日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="faultdate">
              <j-date placeholder="请选择故障日期" v-model="model.faultdate"  style="width: 100%" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="故障描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="faultdesc">
              <a-textarea v-model="model.faultdesc" placeholder="请输入故障描述"  ></a-textarea>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="24">
            <a-form-model-item label="待办人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="repairuser">
              <a-input v-model="model.repairuser" placeholder="请输入待办人"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" placeholder="请输入备注"  ></a-textarea>
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
    name: 'RepairManageForm',
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
           repairname: [
              { required: true, message: '请输入工单名称!'},
           ],
           equipmentid: [
              { required: true, message: '请选择设备!'},
           ],
           priority: [
              { required: true, message: '请选择优先级!'},
           ],
           faultdate: [
              { required: true, message: '请输入故障日期!'},
           ],
           faultdesc: [
              { required: true, message: '请输入故障描述!'},
           ],
           repairuser: [
              { required: true, message: '请输入待办人!'},
           ],
           repairdate: [
              { required: true, message: '请输入维修时间!'},
           ],
           feedback: [
              { required: true, message: '请输入维修反馈!'},
           ],
           result: [
              { required: true, message: '请输入维修结果!'},
           ],
        },
        url: {
          add: "/cmmsRepair/cmmsRepair/add",
          edit: "/cmmsRepair/cmmsRepair/edit",
          queryById: "/cmmsRepair/cmmsRepair/queryById"
        },
        deviceOptions: [],
        deviceOptionsAll: [],
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
      this.getDeviceOption();
    },
    methods: {
      // 获取设备下拉列表
      getDeviceOption(){
        getAction(`/tpmEquipment/tpmEquipment/selectEquipmentList`).then(res=>{
          console.log(111,res.result)
          this.deviceOptions = res.result.map((res) => {
            return {
              id: res.id,
              equipmentname: res.equipmentname,
              equipmentcode: res.equipmentcode,
            }
          })
          // 存一个完整的设备表
          this.deviceOptionsAll = this.deviceOptions
          console.log(7878,this.deviceOptionsAll)
        })
      },
      // 筛选设备
      searchDevice(value) {
        console.log(1212,value,value.trim().length)
        // 若输入的值删除，则重新赋完整的设备列表
        if (value.trim().length === 0) {
          this.deviceOptions = this.deviceOptionsAll
        }
        // 通过判断字符串中是数字还是文字进而判断是通过设备名筛选还是设备编号筛选
        let panDuan = isNaN(parseFloat(value))
        if (!panDuan) {
          // 数字
          console.log(777)
          let filteredArray = this.deviceOptionsAll.filter(item => item.equipmentcode.includes(value));
          this.deviceOptions = filteredArray
        } else {
          console.log(888)
          let filteredArray = this.deviceOptionsAll.filter(item => item.equipmentname.includes(value));
          this.deviceOptions = filteredArray
        }
        console.log(999,this.deviceOptions)
      },
      // 解决筛选后option不回显问题
      filterOptions(input, option) {
        return this.deviceOptions
      },
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