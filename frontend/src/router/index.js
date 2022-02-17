import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Review from '../views/Review.vue'
import Notice from '../views/Notice.vue'
import FAQ from '../views/FAQ.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },{
    path: '/review',
    name: 'Review',
    component: Review
  },{
    path: '/FAQ',
    name: 'FAQ',
    component: FAQ
  },{
    path: '/notice',
    name: 'Notice',
    component: Notice
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
