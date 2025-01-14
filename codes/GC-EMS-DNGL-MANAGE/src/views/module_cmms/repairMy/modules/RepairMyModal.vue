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
    <repair-my-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></repair-my-form>
  </j-modal>
</template>

<script>

  import RepairMyForm from './RepairMyForm'
  export default {
    name: 'RepairMyModal',
    components: {
      RepairMyForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      // add () {
      //   this.visible=true
      //   this.$nextTick(()=>{
      //     this.$refs.realForm.add();
      //   })
      // },
      // edit (record) {
      //   this.visible=true
      //   this.$nextTick(()=>{
      //     this.$refs.realForm.edit(record);
      //   })
      // },
      feedback(record){
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.feedback(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>