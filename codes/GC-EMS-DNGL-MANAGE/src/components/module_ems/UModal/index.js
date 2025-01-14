import UModal from './UModal'
import UPrompt from './UPrompt'

export default {
  install(Vue) {
    Vue.component(UModal.name, UModal)

    const JPromptExtend = Vue.extend(UPrompt)
    Vue.prototype.$JPrompt = function (options = {}) {
      // 创建prompt实例
      const vm = new JPromptExtend().$mount()
      vm.show(options)
      // 关闭后销毁
      vm.$on('after-close', () => vm.$destroy())
      return vm
    }
  },
}