<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row class="inspect-order-layout">
          <a-col :span="6" class="inspect-tree">
            <a-tree
              :tree-data="treeData"
              show-icon
              showLine
              default-expand-all
              :selected-keys="selectedKeys"
              :replaceFields="replaceFields"
              @select="onSelect"
              >
              <!-- 每项 最前面的 箭头的 icon -->
              <a-icon slot="switcherIcon" type="caret-down" />
              <!-- slot="praent" 配合 treeData数据中的 slots 设置目录下 的 icon -->
              <!-- <a-icon slot="parent" style="color:#0DD3FF" type="folder-open" /> -->
              <!-- slot="child" 配合 treeData数据children下的 slots 设置children目录 的 icon -->
              <!-- <a-icon slot="child" style="color:#FED037" type="folder-open" /> -->
              <!-- 数据中 每个 节点 的 scopedSlots 字段(插槽) 用来设置 所有(子父节点)节点  的 统一的 icon-->
              <template slot="handle" slot-scope="item">
                <!--item.title 为treeData的 title 字段-->
                <span>{{item.title}}</span>
                <!-- completionStatus === '1'表示已完成 -->
                <a-icon type="check-circle" theme="twoTone" two-tone-color="#52c41a" v-if="item.completionStatus === '1'" style="margin-left: 4px;"/>
              </template>
            </a-tree>
          </a-col>
          <a-col :span="18" class="itemList-table">
            <a-button type="primary" @click="handleFeedback" style="margin-bottom: 8px;float: right;z-index: 9999;">提交反馈</a-button>
            <a-table
              ref="table"
              size="middle"
              :scroll="{x:true}"
              bordered
              rowKey="id"
              :columns="columns"
              :dataSource="model.itemList"
              :pagination="false"
              class="j-table-force-nowrap">
              <template slot="iresult" slot-scope="text, record,index">
                <a-form-model-item :prop="'itemList.'+index+'.iresult'" :rules="validatorRules.iresult" style="margin-bottom: 0px;">
                  <a-radio-group name="radioGroup" v-model="record.iresult">
                    <a-radio value="0">
                      正常
                    </a-radio>
                    <a-radio value="1">
                      异常
                    </a-radio>
                  </a-radio-group>
                </a-form-model-item>
              </template>
              <template slot="iremark" slot-scope="text, record,index">
                <a-form-model-item :prop="'itemList.'+index+'.iremark'" :rules="validatorRules.iremark" style="margin-bottom: 0px;">
                  <a-input v-model="record.iremark" placeholder="请输入点检备注"></a-input>
                </a-form-model-item>
              </template>
            </a-table>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

const treeData1 = [
  {
    title: '测试123456',
    key: '1111111111111',
    type: '巡检路线',
    completionStatus: '0',
    slots: { icon: 'parent', },
    // ⚠️重点这这里⚠️每一条数据上都添加scopedSlots属性
    scopedSlots:{ title:"handle" },
    children: [
      {
        title: '测试巡检点的修改NO1',
        key: '12223333333',
        type: '巡检点',
        slots: { icon: 'child', },
        // ⚠️重点这这里⚠️每一条数据上都添加scopedSlots属性
        scopedSlots:{ title:"handle" },
        children: [
          {
            title: '半自动立钻Z5180C',
            key: '55555555555',
            type: '巡检内容',
            scopedSlots:{ title:"handle" },
            ItemList: [
              // 巡检项相关信息
            ]
          },
        ],
      },
    ],
  },
  {
    title: '测试巡检点的修改NO2',
    key: '456293324552',
    type: '巡检点',
    completionStatus: '1',
    scopedSlots:{ title:"handle" },
    children: [
      {
        title: '半自动立钻Z5180D',
        key: '1335633666666',
        type: '巡检内容',
        ItemList: [
          // 巡检项相关信息
        ]
      },
    ],
  },
];

  import { httpAction, getAction, putAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'InspectForm',
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
        model: {
          itemList: []
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
          result: [
            { required: true, message: '请输入点检结果!'},
          ],
        },
        url: {
          add: "/cmmsInspect/cmmsInspect/add",
          edit: "/cmmsInspect/cmmsInspect/edit",
          queryById: "/cmmsInspect/cmmsInspect/queryById",
          queryTreeById: "/cmmsInspect/cmmsInspect/queryTreeById",
          queryItemByCountId: "/cmmsInspect/cmmsInspect/queryItemByCountId",
          feedback: "/cmmsInspect/cmmsInspect/feedback",
        },
        treeData: [],
        selectedKeys: [], // （受控）设置选中的树节点
        replaceFields: {
          children:'children',
          title:'title',
          key:'id'
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
            title:'巡检项编号',
            align:"center",
            dataIndex: 'itemcode'
          },
          {
            title:'巡检项名称',
            align:"center",
            dataIndex: 'itemname'
          },
          {
            title:'巡检项类型',
            align:"center",
            dataIndex: 'classification',
            customRender:function (t, r, index) {
              if (t === '0') return '环境'
              if (t === '1') return '设备'
              if (t === '2') return '其他'
            }
          },
          {
            title:'巡检设备',
            align:"center",
            dataIndex: 'equipmentname',
            customRender:function (t, r, index) {
              return t?t:'-'
            }
          },
          {
            title:'标准',
            align:"center",
            dataIndex: 'inspectionstandards',
          },
          {
            title:'巡检结果',
            align:"center",
            dataIndex: 'iresult',
            scopedSlots: { customRender: 'iresult' }
          },
          {
            title:'巡检备注',
            align:"center",
            dataIndex: 'iremark',
            scopedSlots: { customRender: 'iremark' }
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
      edit (record) {
        // this.model = Object.assign({}, record);
        this.visible = true;
        this.confirmLoading = true
        // getAction(this.url.queryById, {id: record.id}).then((res) => {
        //   console.log(res)
        //   this.model = res.result
        //   // console.log(res.result.planList)
        //   var planList = res.result.planList
        //   var orderData = []
        //   if(planList.length>0){
        //     for (let p = 0; p < planList.length; p++) {
        //       // 巡检路线
        //       const planItem = planList[p];
        //       planItem.children = []
        //       if(planItem.inspectLineVo){
        //         for (let s = 0; s < planItem.inspectLineVo.detailList.length; s++) {
        //           // 巡检点
        //           const spotItem = planItem.inspectLineVo.detailList[s];
        //           spotItem.children = []
        //           if(spotItem.inspectContentList){
        //             for (let i = 0; i < spotItem.inspectContentList.length; i++) {
        //               const contentItem = spotItem.inspectContentList[i];
        //               console.log(spotItem, contentItem)
        //               // 巡检内容数据
        //               if(contentItem){
        //                 var contentNode = {
        //                   key: contentItem.id,
        //                   title: contentItem.equipmentname,
        //                   slots: {
        //                     icon: 'smile',
        //                   },
        //                   inspectTcontentItemList: contentItem.inspectTcontentItemList
        //                 }
        //                 spotItem.children.push(contentNode)
        //               }
        //             }
        //           }
        //           // 巡检点数据
        //           var spotNode = {
        //             key: spotItem.id,
        //             title: spotItem.contentname,
        //             slots: {
        //               icon: 'smile',
        //             },
        //             children: spotItem.children,
        //           }
        //           planItem.children.push(spotNode)
        //         }
        //         // 路线数据
        //         var planNode = {
        //           key: planItem.inspectLineVo.id,
        //           title: planItem.inspectLineVo.linename,
        //           slots: {
        //             icon: planItem.tasktype? 'smile': 'smile',
        //           },
        //           children: planItem.children,
        //         }
        //         orderData.push(planNode)
        //       }
        //     }
        //   }
        //   console.log(orderData)
        //   this.treeData = orderData
        //   // if(planList.length>0){
        //   //   planList.map(item => {
        //   //     var spotList = item.detailList
        //   //     if(spotList.length > 0){
        //   //       spotList.map(spot => {
        //   //         var 
        //   //         return {
        //   //           key: item.id,
        //   //           title: item.linename,
        //   //           slots: {
        //   //             icon: 'smile',
        //   //           },
        //   //           children: item.inspectLineVo,
        //   //         }
        //   //       })
        //   //     }
        //   //     return {
        //   //       key: item.id,
        //   //       title: item.linename,
        //   //       slots: {
        //   //         icon: 'smile',
        //   //       },
        //   //       children: item.inspectLineVo,
        //   //     }
        //   //   })
        //   // }
        //   this.confirmLoading = false
        // })
        getAction(this.url.queryTreeById, {id: record.id}).then((res) => {
          console.log(res)
          if(this.selectedKeys.length === 0){
            this.model = res.result
          }
          // this.model.itemList = []
          this.treeData = this.setIcon(res.result.children)
          this.confirmLoading = false
        })
      },
      // 设置树结构图标
      setIcon(data) {
        var setIconData = data
        for (let item of setIconData) {
          //   判断是不是父级
          if (item.children && item.children.length > 0) {
            //是父级的话递归调用
            this.setIcon(item.children);
            item.scopedSlots = { title: "handle" }
          } else {
            item.scopedSlots = { title: "handle" }
          }
        }
        return setIconData
      },
      onSelect(selectedKeys, info) {
        //  二次点击树节点时selectedKeys为空,直接return中断执行
        if (selectedKeys.length === 0) return
        this.selectedKeys = selectedKeys
        // console.log('selected', selectedKeys, info);
        console.log(info.selectedNodes[0].data.props)
        var props = info.selectedNodes[0].data.props
        if(props.type === '巡检内容'){
          // this.model.itemList = props.inspectTcontentItemList
          this.model.itemList = props.inspectTcontentItemList.map(item => {
            item.tcontequiptid = props.equipmentid
            item.equipmentname = props.equipmentname
            item.classification = props.classification
            return item
          })
          this.$forceUpdate()
          // this.getItemList(props.id)
        }
      },
      getItemList(id){
        getAction(this.url.queryItemByCountId, {countId: id, inspectId: this.model.id}).then((res) => {
          console.log(res)
          this.model.itemList = res.result
          console.log(this.model)
          this.$forceUpdate()
        })
      },
      handleFeedback(){
        console.log(this.model.itemList)
        this.confirmLoading = true
        putAction(this.url.feedback, this.model.itemList).then((res) => {
          if(res.success){
            this.$message.success(res.message);
            this.edit(this.model)
          }else{
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.confirmLoading = false;
        })
      },
    }
  }
</script>
<style lang="less" scoped>
  .inspect-order-layout{
    height: calc(100vh - 150px);
    overflow: hidden;
    .inspect-tree{
      height: calc(100vh - 150px);
      overflow: auto;
    }
    .itemList-table{
      height: calc(100vh - 150px);
      overflow: auto;
    }
  }
</style>
