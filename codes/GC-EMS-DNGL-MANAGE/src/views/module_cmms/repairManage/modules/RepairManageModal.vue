<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <repair-manage-form v-if="modalType==='form'" ref="realForm" @ok="submitCallback('form')" :disabled="disableSubmit"></repair-manage-form>
    <repair-manage-dispatch v-if="modalType==='dispatch'" ref="dispatchForm" @ok="submitCallback('dispatch')"></repair-manage-dispatch>
  </j-modal>
</template>

<script>
  import RepairManageForm from './RepairManageForm'
  import RepairManageDispatch from './RepairManageDispatch.vue'
  export default {
    name: 'RepairManageModal',
    components: {
      RepairManageForm,
      RepairManageDispatch
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false,
        modalType: 'form',
      }
    },
    methods: {
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      dispatch (record) {
        console.log(this.modalType)
        this.modalType = 'dispatch'
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.dispatchForm.dispatch(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.modalType = 'form';
      },
      handleOk () {
        if(this.modalType === 'form'){
          this.$refs.realForm.submitForm();
        } 
        if (this.modalType === 'dispatch') {
          this.$refs.dispatchForm.submitForm();
        }
      },
      submitCallback(type){
        this.$emit('ok');
        this.visible = false;
        this.modalType = 'form';
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>