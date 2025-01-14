<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <div class="cmms-dialog-item-title">巡检路线基本信息</div>
        <a-row>
          <!-- <a-col :span="24">
            <a-form-model-item label="巡检路线编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="linecode">
              <a-input v-model="model.linecode" placeholder="请输入巡检路线编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="巡检路线名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="linename">
              <a-input v-model="model.linename" placeholder="请输入巡检路线名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <!-- <a-input v-model="model.remark" placeholder="请输入备注"></a-input> -->
              <a-textarea v-model="model.remark" rows="2" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <div class="cmms-dialog-item-title u-flex-jab">
          <div>巡检点设置</div>
          <div @click="addInspectSpot"><a-icon type="plus"/>巡检点</div>
        </div>
        <a-table
          size="middle"
          rowKey="id"
          :columns="spotColumns"
          :dataSource="model.detailList"
          :pagination="false"
          class="line-spot-table">
          <template slot="expandedRowRender" slot-scope="record">
            <div class="spot-content-container">
              <a-table
                size="middle"
                rowKey="id"
                :columns="contentColumns"
                :dataSource="record.inspectContentList"
                :pagination="false"
                class="spot-content-table">
                <template slot="expandedRowRender" slot-scope="record">
                  <div class="item-container">
                    <!-- bordered -->
                    <a-table
                      size="middle"
                      rowKey="id"
                      :columns="itemColumns"
                      :dataSource="record.cmmsInspectContentItemList"
                      :pagination="false"
                      class="item-table">
                      <!-- :showHeader="false" -->
                    </a-table>
                  </div>
                </template>
              </a-table>
            </div>
          </template>
        </a-table>
      </a-form-model>
      <inspect-line-modal-add ref="addSpotRef" :selectData="model.detailList" @ok="handleOk"></inspect-line-modal-add>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import InspectLineModalAdd from './InspectLineModalAdd.vue'

  export default {
    name: 'InspectLineForm',
    components: {
      InspectLineModalAdd,
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
          detailList: []
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
          linename: [
            { required: true, message: '请输入巡检路线名称!'},
          ],
        },
        url: {
          add: "/cmmsInspectLine/cmmsInspectLine/add",
          edit: "/cmmsInspectLine/cmmsInspectLine/edit",
          queryById: "/cmmsInspectLine/cmmsInspectLine/queryById"
        },
        // columns: [
        //   // {
        //   //   title: '',
        //   //   dataIndex: '',
        //   //   key:'rowIndex',
        //   //   width:60,
        //   //   align:"center",
        //   //   customRender:function (t,r,index) {
        //   //     return parseInt(index)+1;
        //   //   }
        //   // },
        //   {
        //     title:'巡检点编号',
        //     align:"center",
        //     dataIndex: 'contentcode'
        //   },
        //   {
        //     title:'巡检点名称',
        //     align:"center",
        //     dataIndex: 'contentname'
        //   },
        //   {
        //     title:'备注',
        //     align:"center",
        //     dataIndex: 'remark'
        //   },
        // ],
        // innerColumns: [
        //   // {
        //   //   title: '序号',
        //   //   dataIndex: '',
        //   //   key:'rowIndex',
        //   //   width:60,
        //   //   align:"center",
        //   //   customRender:function (t,r,index) {
        //   //     return parseInt(index)+1;
        //   //   }
        //   // },
        //   {
        //     title:'巡检项目编号',
        //     align:"center",
        //     dataIndex: 'contentcode'
        //   },
        //   {
        //     title:'巡检项目名称',
        //     align:"center",
        //     dataIndex: 'contentname'
        //   },
        //   {
        //     title:'巡检项目类型',
        //     align:"center",
        //     dataIndex: 'itemname'
        //   },
        //   {
        //     title:'巡检设备',
        //     align:"center",
        //     dataIndex: 'equipmentname'
        //   },
        //   // {
        //   //   title:'标准',
        //   //   align:"center",
        //   //   dataIndex: 'inspectionstandards',
        //   // },
        //   {
        //     title:'备注',
        //     align:"center",
        //     dataIndex: 'remark'
        //   }
        // ],
        spotColumns: [
          {
            title:'巡检点编号',
            align:"center",
            dataIndex: 'contentcode'
          },
          {
            title:'巡检点名称',
            align:"center",
            dataIndex: 'contentname'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
        ],
        contentColumns: [
          {
            title:'巡检项目编号',
            align:"center",
            dataIndex: 'contentcode',
            // customRender: (text, row, index) => {
            //   return {
            //     children: text,
            //     attrs: {colSpan: 4}
            //   }
            // }
          },
          {
            title:'巡检项目名称',
            align:"center",
            dataIndex: 'contentname',
            // customRender: (text, row, index) => {
            //   return {
            //     children: text,
            //     attrs: {colSpan: 0}
            //   }
            // }
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipmentname',
            // customRender: (text, row, index) => {
            //   return {
            //     children: text,
            //     attrs: {colSpan: 0}
            //   }
            // }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark',
            // customRender: (text, row, index) => {
            //   return {
            //     children: text,
            //     attrs: {colSpan: 0}
            //   }
            // }
          },
        ],
        itemColumns: [
          {
            title:'巡检项编号',
            align:"left",
            dataIndex: 'itemcode'
          },
          {
            title:'名称',
            align:"left",
            dataIndex: 'itemname'
          },
          // {
          //   title:'设备类型',
          //   align:"center",
          //   dataIndex: 'equipdefname'
          // },
          {
            title:'巡检标准',
            align:"left",
            dataIndex: 'inspectionstandards'
          },
          {
            title:'巡检项类型',
            align:"left",
            dataIndex: 'classification_dictText'
          },
          {
            title:'备注',
            align:"left",
            width: 80,
            dataIndex: 'remark',
          },
        ]
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
        this.visible = true;
      },
      addInspectSpot(){
        this.$refs.addSpotRef.add();
        this.$refs.addSpotRef.title = "选择巡检点";
      },
      edit (record) {
        this.visible = true;
        this.confirmLoading = true
        // this.model = Object.assign({}, record);
        getAction(this.url.queryById, {id: record.id}).then((res) => {
          this.model = res.result
          this.model.detailList.forEach(item => {
            item.id = item.inspectspotid
          });
          console.log(res)
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
            this.model.detailList.forEach(item => {
              item.inspectspotid = item.id
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
        console.log(data)
        this.model.detailList = data
      },
    }
  }
</script>
<style scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
  .line-spot-table{
    background-color: #ecf3ef;
    // tr > td{
    //   border: none;
    // }
  }
  // 巡检点下的巡检项目
  /deep/ .spot-content-container{
    background-color: #fff;
    border: 1px solid #e6ebf5;
    padding: 20px;
    .spot-content-table{
      background-color: #e6f7ff;
      // tr > td{
      //   border: none;
      // }
    }
    .item-container{
      background: #fff;
      padding: 20px;
      border: 1px solid #e6ebf5;
      .item-table{
      }
    }
  }
  .spot-content-container:hover{
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }
  // 表格悬浮颜色
  /deep/ tr:hover:not(.ant-table-expanded-row):not(.ant-table-row-selected) > td{
    background-color: rgba(255,255,255,.4);
  }
</style>