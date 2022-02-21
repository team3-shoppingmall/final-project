<template>
<v-app>
    <v-app-bar color="primary" app dark height="60px">
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
        <v-row>
            <v-col cols="10" align-self="center">
                <v-row class="mx-10">
                    <v-col cols="auto">
                        <v-menu open-on-hover bottom offset-y v-for="(page ,idx) in pages" :key="idx">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn color="primary" dark v-bind="attrs" v-on="on" :to="page.to" v-if="page.name != 'Community'">
                                    {{page.name}}
                                </v-btn>
                                <v-btn color="primary" dark v-bind="attrs" v-on="on" v-if="page.name == 'Community'">
                                    {{page.name}}
                                </v-btn>
                            </template>

                            <v-list v-if="page.name=='Community'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                </v-row>
            </v-col>
        </v-row>
    </v-app-bar>
    <v-main>
        <router-view />
    </v-main>
    <v-footer  color="primary lighten-1" padless>
        <Footer />
    </v-footer>
</v-app>
</template>

<script>
import Footer from '@/components/Footer.vue'
export default {
    data() {
        return {
            pages: [{
                name: 'MAIN',
                to: '/',
            }, {
                name: 'ADMIN',
                to: '/admin',
            }, {
                name: 'BASKET',
                to: '/basket',
            }, {
                name: 'Community',
                items: [{
                        title: 'NOTICE',
                        to: '/community/notice',
                    },
                    {
                        title: 'REVIEW',
                        to: '/community/review',
                    },
                    {
                        title: 'FAQ',
                        to: '/community/faq',
                    },
                    {
                        title: '상품문의',
                        to: '/qna/productQnA',
                    },
                    {
                        title: '배송문의',
                        to: '/qna/deliveryQnA',
                    },
                    {
                        title: '배송 전 변경/취소',
                        to: '/qna/beforeDeliveryQnA',
                    },
                    {
                        title: '배송 후 교환/반품',
                        to: '/qna/afterDeliveryQnA',
                    },
                ],
            }, {
                name: 'MyPage',
                to: '/myPage',
            }, {
                name: 'Payment',
                to: '/payment',
            }, {
                name: 'ProductDetail',
                to: '/productDetail/1',
            }, {
                name: 'ProductList',
                to: '/productList/skirt/mini',
            }, {
                name: 'ProductSearch',
                to: '/productSearch',
            }, {
                name: 'WishList',
                to: '/wishList',
            }, {
                name: 'Sign In',
                to: '/authentication/signIn',
            }, {
                name: 'Sign Up',
                to: '/authentication/signUp',
            }, ],
        }
    },
    components: {
        Footer,
    }

}
</script>

<style scoped>
</style>
