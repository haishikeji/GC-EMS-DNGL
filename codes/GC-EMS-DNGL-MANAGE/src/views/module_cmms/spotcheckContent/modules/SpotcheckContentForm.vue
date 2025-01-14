<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <div class="cmms-dialog-item-title">点检内容基本信息</div>
        <div style="height: 100%;width:100%;display: flex;">
          <div style="height: 100%;width:92%;">
            <a-row>
              <!-- <a-col :span="12">
                <a-form-model-item label="点检内容编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentcode">
                  <a-input v-model="model.contentcode" placeholder="请输入点检内容编号"  ></a-input>
                </a-form-model-item>
              </a-col> -->
              <a-col :span="12">
                <a-form-model-item label="点检内容名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentname">
                  <a-input v-model="model.contentname" placeholder="请输入点检内容名称"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="选择设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
                  <!-- <j-search-select-tag v-model="model.equipmentid" dict="tpm_equipment,equipmentname,id" :disabled="disabledEdit" /> -->
                  <a-select
                  v-model="model.equipmentid"
                  placeholder="请输入设备名称或设备编号"
                  show-search
                  :disabled="disabledEdit"
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
              <a-col :span="12">
                <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
                  <a-textarea v-model="model.remark" placeholder="请输入备注" :auto-size="{ minRows: 2, maxRows: 3 }" />
                </a-form-model-item>
              </a-col>
              <!-- <a-col :span="12">
                <a-form-model-item label="状态：启用：0、禁用:1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
                  <j-dict-select-tag type="list" v-model="model.status" dictCode="spotcheck_content_status" placeholder="请选择状态：启用：0、禁用:1" />
                </a-form-model-item>
              </a-col> -->
            </a-row>
          </div>
          <div style="height: 100%;width:8%;padding-top:0.25%;">
              <a-button type="primary" icon="search" />
          </div>
        </div>
        <div class="cmms-dialog-item-title u-flex-jab">
          <div>设备点检设置</div>
          <div>
            <a-button @click="handleAddSpotItem" type="link" icon="plus">点检项</a-button>
          </div>
        </div>
        <a-table
          ref="table"
          size="middle"
          :scroll="{x:true}"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="model.spotcheckContentItemList"
          :pagination="false"
          :loading="loading"
          class="j-table-force-nowrap">
        </a-table>
        <!-- <cmms-spotcheck-content-modal-add ref="modalForm" @ok="modalFormOk" :modelForm="model" :dataList="model.spotcheckContentItemList" @customEvent="handleCustomEvent"></cmms-spotcheck-content-modal-add> -->
        <spotcheck-content-modal-add  ref="modalForm" @ok="modalFormOk" :modelForm="model" :dataList="model.spotcheckContentItemList" @customEvent="handleCustomEvent"></spotcheck-content-modal-add>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import SpotcheckContentModalAdd from './SpotcheckContentModalAdd.vue'

  export default {
    name: 'SpotcheckContentForm',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
        SpotcheckContentModalAdd
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
          spotcheckContentItemList: []
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        disabledEdit: false,
        confirmLoading: false,
        validatorRules: {
          contentname: [
            { required: true, message: '请输入点检内容名称!'},
          ],
          equipmentid: [
            { required: true, message: '请选择设备!'},
          ],
        },
        columns: [
          // {
          //   title: '序号',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          // {
          //   title:'点检项ID',
          //   align:"center",
          //   dataIndex: 'id'
          // },
          {
            title:'点检项编号',
            align:"center",
            dataIndex: 'itemcode'
          },
          {
            title:'点检项名称',
            align:"center",
            dataIndex: 'itemname'
          },
          {
            title:'标准',
            align:"center",
            dataIndex: 'conditions',
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          }
        ],
        // dataSource: [],
        loading:false,
        url: {
          list: "/cmmsSpotcheckItem/cmmsSpotcheckItem/listbyequipmentid/{equipmentid}",
          add: "/cmmsSpotcheckContent/cmmsSpotcheckContent/add",
          edit: "/cmmsSpotcheckContent/cmmsSpotcheckContent/edit",
          // queryById: "/cmmsSpotcheckContent/cmmsSpotcheckContent/queryById"
          queryById: "/cmmsSpotcheckContent/cmmsSpotcheckContent/queryContentAndItemById"
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
        // this.edit(this.modelDefault);
        this.model = Object.assign({}, this.modelDefault);
        this.visible = true;
      },
      edit (record) {
        this.disabledEdit = true
        getAction(this.url.queryById, record).then((res) => {
            console.log(88,res)
            this.model = res.result
            this.model.spotcheckContentItemList = this.model.spotcheckContentItemList.map((res) => {
              return {
                id: res.spotcheckitemid,
                itemcode: res.itemcode,
                itemname: res.itemname,
                conditions: res.conditions,
                remark: res.remark
              }
            })
        })
        // this.model = Object.assign({}, record);
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
              this.model.status = 1
              this.model.spotcheckContentItemList = this.model.spotcheckContentItemList.map((res) => {
                return {
                  spotcheckitemid: res.id
                }
              })
            }else{
              httpurl+=this.url.edit;
              method = 'put';
              this.model.spotcheckContentItemList = this.model.spotcheckContentItemList.map((res) => {
                return {
                  spotcheckitemid: res.id
                }
              })
            }
            console.log(99,this.model)
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
      // 新增点检项
      handleAddSpotItem(){
        console.log(this.model.equipmentid)
        if (this.model.equipmentid == undefined) {
          this.$message.error("请先选择设备！");
        } else {
          this.$refs.modalForm.add();
          this.$refs.modalForm.title = "选择点检项";
        }
      },
      // 处理子组件传过来的数据
      handleCustomEvent(data) {
        // 处理从子组件传递过来的数据
        console.log("子组件传过来的数据:", data);
        this.model.spotcheckContentItemList = data;
      }
    }
  }
</script>

<style scoped>
@import "~@/assets/less/uStyle.less";
</style>