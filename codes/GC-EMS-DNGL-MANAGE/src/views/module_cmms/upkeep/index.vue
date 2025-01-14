<template>
    <a-tabs default-active-key="1" @change="callback" class="card-container">
        <a-tab-pane key="1" tab="我的下发" force-render>
            <upkeep-list :key="timer1" :url="url1" :columns="columns1"></upkeep-list>
        </a-tab-pane>
        <a-tab-pane key="2" tab="我的操作" force-render>
            <upkeep-list :key="timer2" :url="url2" :columns="columns2"></upkeep-list>
        </a-tab-pane>
    </a-tabs>
</template>

<script>

import UpkeepList from './UpkeepList.vue'

export default ({
    components: {
        UpkeepList
    },
    data() {
        return {
            timer1: '',
            timer2: '',
            url1: {
                list: "/cmmsUpkeep/cmmsUpkeep/selectMyArrangeList",
                delete: "/cmmsUpkeep/cmmsUpkeep/delete",
                deleteBatch: "/cmmsUpkeep/cmmsUpkeep/deleteBatch",
                exportXlsUrl: "/cmmsUpkeep/cmmsUpkeep/exportXls",
                importExcelUrl: "cmmsUpkeep/cmmsUpkeep/importExcel",
                status: 1
            },
            url2: {
                list: "/cmmsUpkeep/cmmsUpkeep/selectMyOperateList",
                delete: "/cmmsUpkeep/cmmsUpkeep/delete",
                deleteBatch: "/cmmsUpkeep/cmmsUpkeep/deleteBatch",
                exportXlsUrl: "/cmmsUpkeep/cmmsUpkeep/exportXls",
                importExcelUrl: "cmmsUpkeep/cmmsUpkeep/importExcel",
                status: 2
            },
            columns1: [
                {
                    title: '序号',
                    dataIndex: '',
                    key:'rowIndex',
                    width:60,
                    align:"center",
                    customRender:function (t,r,index) {
                    return parseInt(index)+1;
                    }
                },
                {
                    title:'保养工单编号',
                    align:"center",
                    dataIndex: 'upkeepcode'
                },
                // {
                //   title:'保养名称(标题)',
                //   align:"center",
                //   dataIndex: 'upkeepname'
                // },
                // {
                //   title:'保养计划ID',
                //   align:"center",
                //   dataIndex: 'upkeepplanid'
                // },
                // {
                //   title:'保养工作项目ID',
                //   align:"center",
                //   dataIndex: 'upkeepprojid'
                // },
                // {
                //   title:'工作项目名称',
                //   align:"center",
                //   dataIndex: 'projectname'
                // },
                {
                    title:'计划名称',
                    align:"center",
                    dataIndex: 'planname'
                },
                {
                    title:'设备',
                    align:"center",
                    dataIndex: 'equipmentid_dictText'
                },
                {
                    title:'地点',
                    align:"center",
                    dataIndex: 'address'
                },
                // {
                //   title:'厂商名称',
                //   align:"center",
                //   dataIndex: 'supplier'
                // },
                // {
                //   title:'厂商电话',
                //   align:"center",
                //   dataIndex: 'suppliertel'
                // },
                // {
                //   title:'联系人',
                //   align:"center",
                //   dataIndex: 'linker'
                // },
                // {
                //   title:'联系人电话',
                //   align:"center",
                //   dataIndex: 'linkertel'
                // },
                {
                    title:'负责人',
                    align:"center",
                    dataIndex: 'chargeruser_dictText'
                },
                // {
                //   title:'描述',
                //   align:"center",
                //   dataIndex: 'upkeepdesc'
                // },
                {
                    title:'状态',
                    align:"center",
                    dataIndex: 'status_dictText'
                },
                {
                    title:'备注',
                    align:"center",
                    dataIndex: 'remark'
                },
                // {
                //   title:'处理意见',
                //   align:"center",
                //   dataIndex: 'opinion'
                // },
            ],
            columns2: [
                {
                    title: '序号',
                    dataIndex: '',
                    key:'rowIndex',
                    width:60,
                    align:"center",
                    customRender:function (t,r,index) {
                    return parseInt(index)+1;
                    }
                },
                {
                    title:'保养工单编号',
                    align:"center",
                    dataIndex: 'upkeepcode'
                },
                // {
                //   title:'保养名称(标题)',
                //   align:"center",
                //   dataIndex: 'upkeepname'
                // },
                // {
                //   title:'保养计划ID',
                //   align:"center",
                //   dataIndex: 'upkeepplanid'
                // },
                // {
                //   title:'保养工作项目ID',
                //   align:"center",
                //   dataIndex: 'upkeepprojid'
                // },
                // {
                //   title:'工作项目名称',
                //   align:"center",
                //   dataIndex: 'projectname'
                // },
                {
                    title:'计划名称',
                    align:"center",
                    dataIndex: 'planname'
                },
                {
                    title:'设备',
                    align:"center",
                    dataIndex: 'equipmentid_dictText'
                },
                {
                    title:'地点',
                    align:"center",
                    dataIndex: 'address'
                },
                // {
                //   title:'厂商名称',
                //   align:"center",
                //   dataIndex: 'supplier'
                // },
                // {
                //   title:'厂商电话',
                //   align:"center",
                //   dataIndex: 'suppliertel'
                // },
                // {
                //   title:'联系人',
                //   align:"center",
                //   dataIndex: 'linker'
                // },
                // {
                //   title:'联系人电话',
                //   align:"center",
                //   dataIndex: 'linkertel'
                // },
                // {
                //     title:'负责人',
                //     align:"center",
                //     dataIndex: 'chargeruser_dictText'
                // },
                // {
                //   title:'描述',
                //   align:"center",
                //   dataIndex: 'upkeepdesc'
                // },
                {
                    title:'状态',
                    align:"center",
                    dataIndex: 'status_dictText'
                },
                {
                    title:'备注',
                    align:"center",
                    dataIndex: 'remark'
                },
                // {
                //   title:'处理意见',
                //   align:"center",
                //   dataIndex: 'opinion'
                // },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align:"center",
                    fixed:"right",
                    width:147,
                    scopedSlots: { customRender: 'action' }
                }
            ],
        }
    },
    methods: {
        callback(key) {
            if (key == 1) {
                // 我的下发
                this.timer1 = new Date().getTime()
            } else if (key == 2) {
                // 我的操作
                this.timer2 = new Date().getTime()
            } 
        },
    },
})
</script>

<style scoped>
::v-deep .ant-card-body{
    padding-top: 10px !important;
}
</style>
