<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equipmentid">
              <!-- <a-input v-model="model.equipmentid" placeholder="请输入设备ID"  ></a-input> -->
              <j-search-select-tag v-model="model.equipmentid" placeholder="请选择设备" dict="tpm_equipment,equipmentname,id"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="点位名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagname">
              <!-- <a-input v-model="model.tagname" placeholder="请输入点位名"  ></a-input> -->
              <a-select
                v-model="model.tagname"
                placeholder="请选择点位"
                show-search
                allowClear
                :options="pointOptions">
                </a-select>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="12">
            <a-form-model-item label="点位地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagaddress">
              <a-input v-model="model.tagaddress" placeholder="请输入点位地址"  ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="12">
            <a-form-model-item label="点位类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagtype">
              <j-search-select-tag v-model="model.tagtype" placeholder="请选择点位类型" dict="tpm_tag_type" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="能源分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="energytypeid">
              <!-- <j-search-select-tag v-model="model.energytypeid" placeholder="请选择能源分类" dict="base_energy_type,name,id"/> -->
              <a-select
                v-model="model.energytypeid"
                placeholder="请选择能源分类"
                show-search
                @change="handleChange"
                :filterOption="filterOption"
                :options="energytypeOptions">
                </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="energyitemid">
              <span slot="label">
                能耗分项&nbsp;
                <a-tooltip title="请先选择能源分类">
                  <a-icon type="exclamation-circle" theme="filled" />
                </a-tooltip>
              </span>
              <!-- <j-search-select-tag v-model="model.energyitemid" placeholder="请选择能耗分项" dict="base_energy_item,energyitemname,id"/> -->
              <a-select
                v-model="model.energyitemid"
                placeholder="请选择能耗分项"
                show-search
                @change="handleChange2"
                :filterOption="filterOption"
                :options="energyitemOptions">
                </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="max">
              <a-input-number v-model="model.max" placeholder="请输入上限" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="min">
              <a-input-number v-model="model.min" placeholder="请输入下限" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="报警延时(分钟)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delay">
              <a-input-number v-model="model.delay" placeholder="请输入报警延时(分钟)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="其他值1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="param1">
              <a-input v-model="model.param1" placeholder="请输入其他值1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="其他值2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="param2">
              <a-input v-model="model.param2" placeholder="请输入其他值2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <!-- <a-col :span="12">
            <a-form-model-item label="当前点位值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tagvalue">
              <a-input-number v-model="model.tagvalue" placeholder="请输入当前点位值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="获得时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="logtime">
              <j-date placeholder="请选择当前点位值获得时间"  v-model="model.logtime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col> -->
          <!-- <a-col :span="12">
            <a-form-model-item label="最后一次报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastalarmtime">
              <j-date placeholder="请选择最后一次报警时间"  v-model="model.lastalarmtime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col> -->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'TagForm',
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
          energyitemid: ''
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
          equipmentid: [
            { required: true, message: '请选择设备!'},
          ],
          tagname: [
            { required: true, message: '请选择点位!'},
          ],
          tagtype: [
            { required: true, message: '请选择点位类型!'},
          ],
        },
        url: {
          add: "/tpmTag/tpmTag/add",
          edit: "/tpmTag/tpmTag/edit",
          queryById: "/tpmTag/tpmTag/queryById",
          getPoint: "/tpmParams/tpmParams/tagList",
          getEnergytype: "/energyType/energyType/xialaList",
          getEnergyitem: "/energyItem/energyItem/xialaList",
        },
        // 点位数据
        pointOptions: [],
        // 能源分类
        energytypeOptions: [],
        // 能耗分项
        energyitemOptions: [],
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
      // 获取点位下拉列表
      this.getPointOptions()
      // 获取能源分类
      this.getEnergytypeOptions()
    },
    methods: {
      add () {
        // this.edit(this.modelDefault);
        this.model = {}
        this.visible = true;
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.getEnergyitemOptions()
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
      getPointOptions() {
        getAction(this.url.getPoint).then((res) => {
          this.pointOptions = res.result.map(item => {
            return {
              // value: item.id,
              value: item.tagname,
              label: item.tagname
            }
          })
        })
      },
      getEnergytypeOptions(){
        getAction(this.url.getEnergytype).then((res) => {
          console.log(111,res.result)
          this.energytypeOptions = res.result.map(item => {
            return {
              value: item.id,
              label: item.name
            }
          })
        })
      },
      getEnergyitemOptions(){
        getAction(this.url.getEnergyitem,{energytypeid:this.model.energytypeid}).then((res) => {
          this.energyitemOptions = res.result.map(item => {
            return {
              value: item.id,
              label: item.energyitemname
            }
          })
        })
      },
      handleChange(value){
        console.log(22,value)
        this.model.energyitemid = ''
        getAction(this.url.getEnergyitem,{energytypeid:value}).then((res) => {
          console.log(333,res.result)
          this.energyitemOptions = res.result.map(item => {
            return {
              value: item.id,
              label: item.energyitemname
            }
          })
        })
      },
      handleChange2(){
        // 解决选不中问题
        this.model = {...this.model}
      },
      filterOption (value, option) {
        return option.componentOptions.children[0].text.indexOf(value) >= 0
      },
    }
  }
</script>