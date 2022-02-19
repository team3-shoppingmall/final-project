import Vue from 'vue'
import VueRouter from 'vue-router'

import Default from '../layouts/Default/Index.vue'
import Main from '../views/Main.vue'
import ProductList from '../views/ProductList.vue'
import ProductSearch from '../views/ProductSearch.vue'
import ProductDetail from '../views/ProductDetail.vue'
import Basket from '../views/Basket.vue'
import Payment from '../views/Payment.vue'
import Question from '../views/Question.vue'
import Admin from '../views/Admin.vue'

import MyPage from '../views/MyPage.vue'
import Order from '../views/MyPage/Order.vue'
import Profile from '../views/MyPage/Profile.vue'
import Wishlist from '../views/MyPage/Wishlist.vue'
import Point from '../views/MyPage/Point.vue'
import Board from '../views/MyPage/Board.vue'

import Community from '../views/Community.vue'
import Notice from '../views/Community/Notice.vue'
import NoticePost from '../views/Community/NoticePost.vue'
import Review from '../views/Community/Review.vue'
import FAQ from '../views/Community/FAQ.vue'

import WritePost from '../views/WritePost.vue'

import QnA from '../views/QnA.vue'
import ProductQnA from '../views/QnA/ProductQnA.vue'
import DeliveryQnA from '../views/QnA/DeliveryQnA.vue'
import BeforeDeliveryQnA from '../views/QnA/BeforeDeliveryQnA.vue'
import AfterDeliveryQnA from '../views/QnA/AfterDeliveryQnA.vue'
import QnAPost from '../views/QnAPost.vue'

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
            }, {
                path: '/productDetail',
                name: 'ProductDetail',
                component: ProductDetail
            }, {
                path: '/basket',
                name: 'Basket',
                component: Basket
            }, {
                path: '/payment',
                name: 'Payment',
                component: Payment
            }, {
                path: '/myPage',
                name: 'MyPage',
                component: MyPage
            }, {
                path: '/community',
                name: 'Community',
                component: Community,
                children: [
                    {
                        path: 'notice',
                        name: 'Notice',
                        component: Notice,
                    },
                    {
                        path: 'noticePost/:id',
                        name: 'NoticePost',
                        component: NoticePost,
                    },
                    {
                        path: 'review',
                        name: 'Review',
                        component: Review
                    }, {
                        path: 'faq',
                        name: 'FAQ',
                        component: FAQ,
                    },
                ]
            },
            {
                path: '/writePost/:id',
                name: 'WritePost',
                component: WritePost,
            },
            {
                path: '/qna',
                name: 'QnA',
                component: QnA,
                children:[
                    {
                        path: 'productQnA',
                        name: 'ProductQnA',
                        component: ProductQnA,
                    },
                    {
                        path: 'deliveryQnA',
                        name: 'DeliveryQnA',
                        component: DeliveryQnA,
                    },
                    {
                        path: 'beforeDeliveryQnA',
                        name: 'BeforeDeliveryQnA',
                        component: BeforeDeliveryQnA,
                    },
                    {
                        path: 'afterDeliveryQnA',
                        name: 'AfterDeliveryQnA',
                        component: AfterDeliveryQnA,
                    }
                ]
            },
            {
                path: 'qna/:id',
                name: 'QnAPost',
                component: QnAPost,
            },
            {
                path: '/question',
                name: 'Question',
                component: Question
            }, {
                path: '/order',
                name: 'Order',
                component: Order
            },
            {
                path: '/profile',
                name: 'Profile',
                component: Profile
            },
            {
                path: '/wishlist',
                name: 'Wishlist',
                component: Wishlist
            },
            {
                path: '/point',
                name: 'Point',
                component: Point
            },
            {
                path: '/board',
                name: 'Board',
                component: Board
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

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
},)

export default router
