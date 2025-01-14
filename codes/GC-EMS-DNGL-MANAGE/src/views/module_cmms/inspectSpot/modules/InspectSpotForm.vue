<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <div class="cmms-dialog-item-title">巡检点基本信息</div>
        <a-row>
          <!-- <a-col :span="24">
            <a-form-model-item label="巡检项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentcode">
              <a-input v-model="model.contentcode" placeholder="请输入巡检项目编号"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="24">
            <a-form-model-item label="巡检点名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contentname">
              <a-input v-model="model.contentname" placeholder="请输入巡检点名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <!-- <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input> -->
              <a-textarea v-model="model.remark" rows="2" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
        <div class="cmms-dialog-item-title u-flex-jab">
          <div>巡检项目设置</div>
          <div @click="addInspectContent"><a-icon type="plus"/>巡检项目</div>
        </div>
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="model.inspectContentList"
          :pagination="false"
          class="j-table-force-nowrap">
          <!-- :loading="loading" -->
          <div slot="expandedRowRender" slot-scope="record">
            <a-table
              ref="table"
              size="middle"
              bordered
              rowKey="id"
              :columns="innerColumns"
              :dataSource="record.cmmsInspectContentItemList"
              :pagination="false"
              class="j-table-force-nowrap">
            </a-table>
          </div>
        </a-table>
      </a-form-model>
      <inspect-spot-modal-add ref="addContentRef"  :selectData="model.inspectContentList" @ok="handleOk"></inspect-spot-modal-add>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import InspectSpotModalAdd from './InspectSpotModalAdd.vue'

  export default {
    name: 'InspectSpotForm',
    components: {
        InspectSpotModalAdd
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
          inspectContentList: []
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
          contentname: [
            { required: true, message: '请输入巡检路线名称!'},
          ],
        },
        url: {
          add: "/cmmsInspectSpot/cmmsInspectSpot/add",
          edit: "/cmmsInspectSpot/cmmsInspectSpot/edit",
          queryById: "/cmmsInspectSpot/cmmsInspectSpot/queryById",
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
          {
            title:'巡检项目编号',
            align:"center",
            dataIndex: 'contentcode'
          },
          {
            title:'巡检项目名称',
            align:"center",
            dataIndex: 'contentname'
          },
          {
            title:'巡检项目类型',
            align:"center",
            dataIndex: 'classification_dictText'
          },
          {
            title:'巡检设备',
            align:"center",
            dataIndex: 'equipmentname'
          },
          // {
          //   title:'标准',
          //   align:"center",
          //   dataIndex: 'inspectionstandards',
          // },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          }
        ],
        innerColumns: [
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
        this.visible = true;
        // this.edit(this.modelDefault);
      },
      addInspectContent(){
        this.$refs.addContentRef.add();
        this.$refs.addContentRef.title = "选择巡检项目";
      },
      edit (record) {
        this.visible = true;
        this.confirmLoading = true
        // this.model = Object.assign({}, record);
        getAction(this.url.queryById, {id: record.id}).then((res) => {
          this.model = res.result
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
        // this.model.inspectContentList = data;
        this.model.inspectContentList = data.map(item => {
          // item.inspectcheckitemid = item.id
          return item
        });
      },
    }
  }
</script>
<style scoped>
  @import '~@/assets/less/uStyle.less';
</style>