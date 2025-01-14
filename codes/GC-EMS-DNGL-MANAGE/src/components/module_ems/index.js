import UModal from './UModal'
import UEquipmentTree from './UEquipmentTree/UEquipmentTree'

export default {
  install(Vue) {
    Vue.use(UModal)
    Vue.component('UEquipmentTree', UEquipmentTree)

    //注册全局js函数和变量
    Vue.prototype.$UModal = UModal
  }
}