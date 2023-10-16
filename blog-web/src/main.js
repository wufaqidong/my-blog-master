import Vue from 'vue'
import App from './App.vue'
import './assets/font/iconfont.css'
import './assets/font/iconfont.js'
import store from './store'


import "../src/icons";
import { vueBaberrage } from 'vue-baberrage'
import jsCookie from 'js-cookie'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueImageSwipe from 'vue-image-swipe'
import 'vue-image-swipe/dist/vue-image-swipe.css'


import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import Clipboard from 'clipboard'
import Loading from '@/components/loading/loading';


// 注册全局组件
Vue.component('loading', Loading);

// 创建一个 Vue 实例作为事件总线
Vue.prototype.$bus = new Vue();
Vue.prototype.Clipboard = Clipboard

import hljs from 'highlight.js';

import 'highlight.js/styles/atom-one-dark-reasonable.css' //样式
//创建v-highlight全局指令
Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})
import MetaInfo from 'vue-meta-info';

Vue.use(MetaInfo)
Vue.use(mavonEditor)



Vue.use(VueImageSwipe);
Vue.use(ElementUI);
Vue.prototype.$cookie = jsCookie;  // 在页面里可直接用 this.$cookie 调用
Vue.use(vueBaberrage)

import Empty from '@/components/empty/index.vue'
Vue.component("sy-empty", Empty);
import pagination from '@/components/pagination/index.vue'
Vue.component("sy-pagination", pagination);

import VueLazyLoad from 'vue-lazyload'
// 2.注册插件
Vue.use(VueLazyLoad, {
  preLoad: 1,
  // 懒加载默认加载图片
  loading: 'https://img.shiyit.com/img-loading.png',
  // 加载失败后加载的图片
  error: 'https://img.shiyit.com/loading.gif',
  attempt: 1
  // the default is ['scroll', 'wheel', 'mousewheel', 'resize', 'animationend', 'transitionend']
  // listenEvents: [ 'scroll' ]
})


import router from './router'

window.vm = new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})
router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "instant"
  });
});


