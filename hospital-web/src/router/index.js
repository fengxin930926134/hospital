import Vue from 'vue'
import Router from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

//配置父容器
NProgress.configure({ parent: '#app' });
//关闭旋转
NProgress.configure({ showSpinner: false });
//速度
NProgress.configure({ trickleSpeed: 200 });

Vue.use(Router)

//路由重复问题解决 加在刚引用的下方部位
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
   return originalPush.call(this, location).catch(err => err)
}

//定义routes路由的集合，数组类型 单个路由均为对象类型，path代表的是路径，component代表组件
const routes = [

    {
      path: '/',
      component: () => import('@/views/index'),
      redirect: 'home',
      children: [
        {
          // 当 /home 匹配成功，
          // /home/index 会被渲染在 index 的 <router-view> 中
          path: 'home',
          name: 'Home',
          meta: { name: '首页'},
          component: () => import('@/views/home/index')
        },

        {
          path: 'info',
          name: 'Info',
          meta: { name: '个人信息'},
          component: () => import('@/views/user/info')
        },

        //门诊挂号
        {
          path: '/appointment',
          name: 'Appointment',
          meta: { name: '门诊挂号'},
          component: () => import('@/views/selfHelp/register')
        },

        //门诊挂号
        {
          path: '/pay',
          name: 'Pay',
          meta: { name: '自助缴费'},
          component: () => import('@/views/selfHelp/pay')
        },
        
        {
          path: '/record',
          name: 'Record',
          meta: { name: '挂号记录'},
          component: () => import('@/views/selfHelp/record')
        },

        {
          path: '/reg',
          name: 'Reg',
          meta: { name: '挂号信息'},
          component: () => import('@/views/reg/index')
        },

        //科室管理
        {
          path: 'dept',
          name: 'Dept',
          meta: { name: '科室管理'},
          component: () => import('@/views/dept/index')
        },

        //药品管理
        {
          path: 'drug',
          name: 'Drug',
          meta: { name: '药品管理'},
          component: () => import('@/views/drug/index')
        },

        {
          path: 'checklist',
          name: 'Checklist',
          meta: { name: '检查单管理'},
          component: () => import('@/views/checklist/index')
        },

        {
          path: 'history',
          name: 'History',
          meta: { name: '查看病历'},
          component: () => import('@/views/history/index')
        },

        {
          path: 'prescribe',
          name: 'Prescribe',
          meta: { name: '处方管理'},
          component: () => import('@/views/prescribe/index')
        },

        {
          path: 'drugstore',
          name: 'Drugstore',
          meta: { name: '药房管理'},
          component: () => import('@/views/drugstore/index')
        },

        //医技管理
        {
          path: 'medical',
          name: 'Medical',
          meta: { name: '医技管理'},
          component: () => import('@/views/medical/index')
        },

        //用户管理
        {
          path: 'user',
          name: 'User',
          meta: { name: '用户管理'},
          component: () => import('@/views/user/index')
        },

        {
          path: 'upPassword',
          name: 'UpPassword',
          meta: { name: '修改密码'},
          component: () => import('@/views/user/upPassword')
        },

        {
          path: '404',
          name: '404',
          meta: { name: '404'},
          component: () => import('@/views/404')
        }
      ]
    },

    {
      path: '/login',
      component: () => import('@/views/login')
    },

    //可以配置重定向
    { path:'/*', redirect:"/404" }
]

//实例化VueRouter并将routes添加进去
const router = new Router({
//ES6简写，等于routes：routes
    routes
});

//路由守卫
router.beforeEach((to, from, next)=>{
  //启动进度条
  NProgress.start()
  NProgress.inc()
  //设置浏览器标题
  window.document.title = to.meta.title || '医院管理系统'
  if(sessionStorage.getItem('token')){
    //实际还需要验证token有效期
    if(to.path === '/login'){
      //登录状态下 访问login.vue页面 会跳到主页
      next({path: '/'})
    }else{
      next()
    }
  }else{
      // 如果没有session ,访问任何页面。都会进入到 登录页
      if (to.path === '/login') { // 如果是登录页面的话，直接next() -->解决注销后的循环执行bug
      next()
    } else { // 否则 跳转到登录页面
      next({ path: '/login' })
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

//抛出这个这个实例对象方便外部读取以及访问
export default router
