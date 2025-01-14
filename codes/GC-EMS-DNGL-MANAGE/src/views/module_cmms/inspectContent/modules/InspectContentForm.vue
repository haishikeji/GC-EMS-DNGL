<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <div class="cmms-dialog-item-title">巡检项目基本信息</div>
        <a-row>
          <!-- <a-col :span="24">
            <a-form-model-item label="巡检项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentcode">
              <a-input v-model="model.contentcode" placeholder="请输入巡检项目编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="12">
            <a-form-model-item label="巡检项类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classification">
              <j-dict-select-tag type="radio" v-model="model.classification" dictCode="inspect_item_type"  placeholder="请选择巡检类型" @input="inspectTypeChange()" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="radio" v-model="model.status" dictCode="inspect_content_status" placeholder="请选择状态" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="巡检项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentname">
              <a-input v-model="model.contentname" placeholder="请输入巡检项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" v-if="model.classification === '1'">
            <a-form-model-item label="选择设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <div class="u-flex select-equipment">
                <!-- <j-search-select-tag v-model="model.equipmentid" dict="tpm_equipment,equipmentname,id"  /> -->
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
                <div class="u-flex-jac search-btn" @click="chooseEquipment">
                  <a-icon type="search" style="color: #fff;font-size: 18px;" />
                </div>
              </div>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <!-- <a-input v-model="model.remark" placeholder="请输入备注"></a-input> -->
              <a-textarea v-model="model.remark" rows="2" placeholder="请输入备注" style="width: 100%;"/>
            </a-form-model-item>
          </a-col>
          <!-- <j-popup v-model="model.jPopup" code="demo" field="name" orgFields="name" destFields="name" :multi="true"/> -->
        </a-row>
        <div class="cmms-dialog-item-title u-flex-jab">
          <div>设备巡检设置</div>
          <div @click="addInspectItem"><a-icon type="plus"/>巡检项</div>
        </div>
        <a-table
          ref="table"
          size="middle"
          :scroll="{x:true}"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="model.cmmsInspectContentItemList"
          :pagination="false"
          class="j-table-force-nowrap">
          <!-- :loading="loading" -->
        </a-table>
      </a-form-model>
      <inspect-content-modal-add ref="addItemRef" :equipmentId="model.equipmentid" :selectData="model.cmmsInspectContentItemList" @ok="handleOk"></inspect-content-modal-add>
    </j-form-container>
  </a-spin>
</template>

<script>
  // import '@/assets/less/TableExpand.less'
  // import { mixinDevice } from '@/utils/mixin'
  // import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import InspectContentModalAdd from './InspectContentModalAdd.vue'

  export default {
    name: 'InspectContentForm',
    // mixins:[JeecgListMixin, mixinDevice],
    components: {
      InspectContentModalAdd
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
          cmmsInspectContentItemList: []
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
          classification: [
            { required: true, message: '请先选择巡检项目类型!'},
          ],
          contentname: [
            { required: true, message: '请输入巡检项目名称!'},
          ],
          equipmentid: [
            { required: true, message: '请选择设备!'},
          ],
          status: [
            { required: true, message: '请选择状态!'},
          ],
        },
        url: {
          add: "/cmmsInspectContent/cmmsInspectContent/add",
          edit: "/cmmsInspectContent/cmmsInspectContent/edit",
          queryById: "/cmmsInspectContent/cmmsInspectContent/queryById"
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
          //   title:'id',
          //   align:"center",
          //   dataIndex: 'id'
          // },
          // {
          //   title:'inspectcheckitemid',
          //   align:"center",
          //   dataIndex: 'inspectcheckitemid'
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
            dataIndex: 'inspectionstandards',
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          }
        ],
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
      inspectTypeChange(){
        this.model.cmmsInspectContentItemList = []
        this.$refs.addItemRef.type = this.model.classification
      },
      chooseEquipment(){

      },
      addInspectItem(){
        if (this.model.classification === '1' && this.model.equipmentid == undefined) {
          this.$message.error("请先选择设备！");
        } else {
          this.$refs.addItemRef.type = this.model.classification
          this.$refs.addItemRef.add();
          this.$refs.addItemRef.title = "选择巡检项";
        }
      },
      add () {
        // this.edit(this.modelDefault);
        this.visible = true
      },
      edit (record) {
        this.visible = true;
        this.confirmLoading = true
        // this.model = Object.assign({}, record);
        getAction(this.url.queryById, {id: record.id}).then((res) => {
          this.model = res.result
          this.model.cmmsInspectContentItemList.forEach(item => {
            item.id = item.inspectcheckitemid
          });
          console.log(this.model.cmmsInspectContentItemList)
          this.confirmLoading = false
        })
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
            this.model.cmmsInspectContentItemList.forEach(item => {
              item.inspectcheckitemid = item.id
            });
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
      handleOk(data){
        console.log('handleOk-data', data)
        this.model.cmmsInspectContentItemList = data
      }
    }
  }
</script>
<style scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.select-equipment{
  /deep/ .ant-select-selection{
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    border-right: none;
  }
  .search-btn{
    background-color: #1890FF;
    width: 50px;
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
    border: 1px solid #1890FF;
    cursor: pointer;
  }
}
// .select-equipment:focus .search-btn{
//   border-right-width: 1px !important;
//   outline: 0;
//   box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
// }
</style>