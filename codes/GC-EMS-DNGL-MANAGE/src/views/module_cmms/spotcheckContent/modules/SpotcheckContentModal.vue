<template>
  <u-modal
    :title="title"
    :visible.sync="visible"
    contentFull
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <!-- <cmms-spotcheck-content-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></cmms-spotcheck-content-form> -->
    <spotcheck-content-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></spotcheck-content-form>
  </u-modal>
</template>

<script>
  import SpotcheckContentForm from './SpotcheckContentForm.vue'
  export default {
    name: 'SpotcheckContentModal',
    components: {
        SpotcheckContentForm
    },
    data () {
      return {
        title:'',
        visible: false,
        disableSubmit: false
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