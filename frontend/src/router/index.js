import Vue from 'vue'
import VueRouter from 'vue-router'

import Default from '../layouts/Default/Index.vue'
import Main from '../views/Main.vue'
import ProductList from '../views/ProductList.vue'
import ProductSearch from '../views/ProductSearch.vue'
import ProductDetail from '../views/ProductDetail.vue'
import Basket from '../views/Basket.vue'
import Payment from '../views/Payment.vue'
import MyPage from '../views/MyPage.vue'
import WishList from '../views/WishList.vue'
import Community from '../views/Community.vue'
import QnA from '../views/QnA.vue'
import Question from '../views/Question.vue'
import Admin from '../views/Admin.vue'

import Notice from '../views/Notice.vue'
import Review from '../views/Review.vue'
import FAQ from '../views/FAQ.vue'

import Authentication from '../layouts/Authentication/Index.vue'
import SignIn from '../views/SignIn.vue'
import SignUp from '../views/SignUp.vue'

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
                path: '/productList',
                name: 'ProductList',
                component: ProductList
            }, {
                path: '/productSearch',
                name: 'ProductSearch',
                component: ProductSearch
            },
            {
                path: '/productDetail',
                name: 'ProductDetail',
                component: ProductDetail
            },
            {
                path: '/basket',
                name: 'Basket',
                component: Basket
            },
            {
                path: '/payment',
                name: 'Payment',
                component: Payment
            },
            {
                path: '/myPage',
                name: 'MyPage',
                component: MyPage
            },
            {
                path: '/wishList',
                name: 'WishList',
                component: WishList
            },
            {
                path: '/community',
                name: 'Community',
                component: Community,
                children:[
                    {
                        path: 'notice',
                        name: 'Notice',
                        component: Notice,
                    },
                    {
                        path: 'review',
                        name: 'Review',
                        component: Review,
                    },
                    {
                        path: 'faq',
                        name: 'FAQ',
                        component: FAQ,
                    }
                ]
            },
            {
                path: '/qna',
                name: 'QnA',
                component: QnA
            },
            {
                path: '/question',
                name: 'Question',
                component: Question
            },
            {
                path: '/admin',
                name: 'Admin',
                component: Admin
            },
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
