import Vue from 'vue'
import VueRouter from 'vue-router'
import Default from '../layouts/Default/Index.vue'
import Authentication from '../layouts/Authentication/Index.vue'
import About from '../views/About.vue'
import SignIn from '../views/SignIn.vue'
import SignUp from '../views/SignUp.vue'
import Main from '../views/Main.vue'
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Default',
        component: Default,
        children: [
            {
                path: '/',
                name: 'Main',
                component: Main
            }, {
                path: '/about',
                name: 'About',
                component: About
            }
        ]
    }, {
        path: '/authentication',
        name: 'Authentication',
        component: Authentication,
        children: [
            {
                path: 'signIn',
                name: 'SignIn',
                component: SignIn
            }, {
                path: 'signUp',
                name: 'SignUp',
                component: SignUp
            }
        ]
    }

]

const router = new VueRouter(
    {mode: 'history', base: process.env.BASE_URL, routes}
)

export default router
