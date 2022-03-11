import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store/index'

import Default from '../layouts/Default/Index.vue'
import Main from '../views/Main.vue'
import ProductList from '../views/ProductList.vue'
import ProductSearch from '../views/ProductSearch.vue'
import ProductDetail from '../views/ProductDetail.vue'
import Basket from '../views/Basket.vue'
import Payment from '../views/Payment.vue'

import TermsOfUse from '../views/Company/TermsOfUse.vue'
import PrivacyPolicy from '../views/Company/PrivacyPolicy.vue'
import ShopGuide from '../views/Company/ShopGuide.vue'

import MyPage from '../views/MyPage.vue'
import MyPageHome from '../views/MyPage/MyPageHome.vue'
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
import QnAPost from '../views/QnA/QnAPost.vue'

import Authentication from '../layouts/Authentication/Index.vue'
import SignIn from '../views/SignIn.vue'
import SignUp from '../views/SignUp.vue'

import Admin from '@/layouts/Admin/Index.vue'
import ProductManage from '@/views/Admin/ProductManage.vue'
import AddProduct from '@/views/Admin/AddProduct.vue'
import OrderManage from '@/views/Admin/OrderManage.vue'
import SalesSettlement from '@/views/Admin/SalesSettlement.vue'
import Admin_Child5 from '@/views/Admin/Admin5.vue'
import MemberManage from '@/views/Admin/MemberManage.vue'
Vue.use(VueRouter)

const routes = [

    {
        path: '/',
        // name: 'Default',
        component: Default,
        children: [
            {
                path: '/',
                name: 'Main',
                component: Main
            }, {
                path: '/termsOfUse',
                name: 'TermsOfUse',
                component: TermsOfUse
            }, {
                path: '/privacyPolicy',
                name: 'PrivacyPolicy',
                component: PrivacyPolicy
            }, {
                path: '/shopGuide',
                name: 'ShopGuide',
                component: ShopGuide
            }, {
                path: '/productList/:id/:sub',
                name: 'ProductList',
                component: ProductList
            }, {
                path: '/productSearch',
                name: 'ProductSearch',
                component: ProductSearch
            }, {
                path: '/productDetail/:id',
                name: 'ProductDetail',
                component: ProductDetail
            }, {
                path: '/basket',
                name: 'Basket',
                component: Basket,
                beforeEnter: (to, from, next) => {
                    console.log(`${from.path} ---> ${to.path}`)
                    const isLogin = store.getters['LoginStore/getLogin']
                    if (isLogin) {
                        if (isLogin.user.authority == 'ROLE_ADMIN') {
                            next({
                                name: 'Main',
                            })
                        }
                        next();
                    } else {
                        next({
                            name: 'SignIn',
                            params: {
                                nextPage: to.fullPath
                            }
                        })
                    }
                },
            }, {
                path: '/payment',
                name: 'Payment',
                component: Payment,
                beforeEnter: (to, from, next) => {
                    console.log(`${from.path} ---> ${to.path}`)
                    const isLogin = store.getters['LoginStore/getLogin']
                    if (isLogin) {
                        if (isLogin.user.authority == 'ROLE_ADMIN') {
                            next({
                                name: 'Main',
                            })
                        }
                        next();
                    } else {
                        next({
                            name: 'SignIn',
                            params: {
                                nextPage: '/'
                            }
                        })
                    }
                },
            }, {
                path: '/myPage',
                name: 'MyPage',
                component: MyPage,
                beforeEnter: (to, from, next) => {
                    console.log(`${from.path} ---> ${to.path}`)
                    const isLogin = store.getters['LoginStore/getLogin']
                    if (isLogin) {
                        if (isLogin.user.authority == 'ROLE_ADMIN') {
                            next({
                                name: 'Main',
                            })
                        }
                        next();
                    } else {
                        next({
                            name: 'SignIn',
                            params: {
                                nextPage: to.fullPath
                            }
                        })
                    }
                },
                children: [
                    {
                        path: 'home',
                        name: 'MyPageHome',
                        component: MyPageHome
                    }, {
                        path: 'order',
                        name: 'Order',
                        component: Order
                    }, {
                        path: 'profile',
                        name: 'Profile',
                        component: Profile
                    }, {
                        path: 'wishlist',
                        name: 'Wishlist',
                        component: Wishlist
                    }, {
                        path: 'point',
                        name: 'Point',
                        component: Point
                    }, {
                        path: 'board',
                        name: 'Board',
                        component: Board
                    }
                ]
            }, {
                path: '/community',
                name: 'Community',
                component: Community,
                children: [
                    {
                        path: 'notice',
                        name: 'Notice',
                        component: Notice
                    }, {
                        path: 'noticePost/:id',
                        name: 'NoticePost',
                        component: NoticePost
                    }, {
                        path: 'review',
                        name: 'Review',
                        component: Review
                    }, {
                        path: 'faq',
                        name: 'FAQ',
                        component: FAQ
                    }
                ]
            }, {
                path: '/writePost/:id',
                name: 'WritePost',
                component: WritePost
            }, {
                path: '/writePost/:id/:productNo',
                name: 'writeProductPost',
                component: WritePost
            }, {
                path: '/replyPost/:id/:original',
                name: 'ReplyPost',
                component: WritePost
            }, {
                path: '/updatePost/:id/:num',
                name: 'UpdatePost',
                component: WritePost
            }, {
                path: '/qna',
                name: 'QnA',
                component: QnA,
                children: [
                    {
                        path: 'productQnA',
                        name: 'ProductQnA',
                        component: ProductQnA
                    }, {
                        path: 'deliveryQnA',
                        name: 'DeliveryQnA',
                        component: DeliveryQnA
                    }, {
                        path: 'beforeDeliveryQnA',
                        name: 'BeforeDeliveryQnA',
                        component: BeforeDeliveryQnA
                    }, {
                        path: 'afterDeliveryQnA',
                        name: 'AfterDeliveryQnA',
                        component: AfterDeliveryQnA
                    }
                ]
            }, {
                path: '/qna/:id',
                name: 'QnAPost',
                component: QnAPost
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
            },  {
                path: 'signIn/naver',
                name: 'Naver',
                component: SignIn
            }, {
                path: 'signUp',
                name: 'SignUp',
                component: SignUp
            }, {
                path: 'signUp/social',
                name: 'Social',
                component: SignUp
            }
        ]
    }, {
        path: '/admin',
        name: 'Admin',
        component: Admin,
        beforeEnter: (to, from, next) => {
            console.log(`${from.path} ---> ${to.path}`)
            const isLogin = store.getters['LoginStore/getLogin']
            if (isLogin) {
                if (isLogin.user.authority == 'ROLE_ADMIN') {
                    next();
                } else {
                    alert("접근 권한이 없습니다");
                    next('/')
                }
            } else {
                next({
                    name: 'SignIn',
                    params: {
                        nextPage: to.fullPath
                    }
                })
            }
        },
        children: [
            {
                path: 'productManage',
                name: 'ProductManage',
                component: ProductManage
            }, {
                path: 'addProduct',
                name: 'AddProduct',
                component: AddProduct
            }, {
                path: 'updateProduct/:id',
                name: 'UpdateProduct',
                component: AddProduct
            }, {
                path: 'orderManage',
                name: 'OrderManage',
                component: OrderManage
            }, {
                path: 'salesSettlement',
                name: 'SalesSettlement',
                component: SalesSettlement
            }, {
                path: 'admin5',
                name: 'Admin5',
                component: Admin_Child5
            }, {
                path: 'memberManage',
                name: 'MemberManage',
                component: MemberManage
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    scrollBehavior() {
        return {x: 0, y: 0}
    },
    base: process.env.BASE_URL,
    routes
},)

export default router
