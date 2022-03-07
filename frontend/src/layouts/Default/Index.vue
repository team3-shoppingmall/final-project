<template>
<v-app>
    <v-app-bar color="primary" app dark height="60px">
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
        <v-row v-if="width">
            <v-col cols="10" align-self="center">
                <v-row class="mx-10">
                    <v-col cols="auto">
                        <v-menu open-on-hover bottom offset-y v-for="(page ,idx) in pages" :key="idx">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn color="primary" dark v-bind="attrs" v-on="on" :to="page.to">
                                    {{page.name}}
                                </v-btn>
                            </template>

                            <v-list v-if="page.name=='Community'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                            <v-list v-if="page.name=='OUTER'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                            <v-list v-if="page.name=='TOP'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                            <v-list v-if="page.name=='PANTS'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                            <v-list v-if="page.name=='SKIRT'">
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                            <v-list v-if="page.name=='DRESS'">
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
    <v-main style="min-height: 970px;">
        <router-view />
        <!-- <v-btn v-show="fab" fab dark fixed bottom right color="#FF8EA0">
            ChatBot
        </v-btn> -->
        <v-col cols="auto" style="position:fixed; top:63%; right:10px">
            <v-row justify="end" class="mb-0">
                <v-btn fab dark elevation="2" right color="#FF8EA0" :to="'/'">
                    <v-icon>mdi-home</v-icon>
                </v-btn>
            </v-row>
            <v-row justify="end">
                <v-btn v-scroll="onScroll" v-show="fab" fab dark elevation="2" right color="#FF8EA0" @click="toTop">
                    <v-icon>mdi-chevron-up</v-icon>
                </v-btn>
            </v-row>
        </v-col>
    </v-main>
    <v-footer color="primary lighten-1" padless>
        <Footer />
    </v-footer>
</v-app>
</template>

<script>
import Footer from '@/components/Footer.vue'
export default {
    data() {
        return {
            fab: false,
            pages: [{
                name: 'MAIN',
                to: '/',
            }, {
                name: 'ADMIN',
                to: '/admin/ProductManage',
            }, {
                name: 'BASKET',
                to: '/basket',
            }, {
                name: 'Community',
                to: '/community/notice',
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
                to: '/myPage/home',
            }, {
                name: 'Payment',
                to: '/payment',
            }, {
                name: 'ProductDetail',
                to: '/productDetail/1',
            }, {
                name: 'OUTER',
                to: '/productList/outer/all',
                items: [{
                    title: '재킷',
                    to: '/productList/outer/jacket',
                }, {
                    title: '가디건',
                    to: '/productList/outer/cardigan',
                }, {
                    title: '점퍼',
                    to: '/productList/outer/jumper',
                }, {
                    title: '코트',
                    to: '/productList/outer/coat',
                }, ],
            }, {
                name: 'TOP',
                to: '/productList/top/all',
                items: [{
                    title: '슬리브리스',
                    to: '/productList/top/sleeveless',
                }, {
                    title: '티셔츠',
                    to: '/productList/top/tshirts',
                }, {
                    title: '니트',
                    to: '/productList/top/knit',
                }, {
                    title: '셔츠',
                    to: '/productList/top/shirt',
                }, {
                    title: '블라우스',
                    to: '/productList/top/blouse',
                }, {
                    title: '맨투맨',
                    to: '/productList/top/mtm',
                }, ],
            }, {
                name: 'PANTS',
                to: '/productList/pants/all',
                items: [{
                    title: '슬랙스',
                    to: '/productList/pants/slacks',
                }, {
                    title: '데님',
                    to: '/productList/pants/denim',
                }, {
                    title: '면',
                    to: '/productList/pants/cotton',
                }, {
                    title: 'SHORT',
                    to: '/productList/pants/shorts',
                }, ],
            }, {
                name: 'SKIRT',
                to: '/productList/skirt/all',
                items: [{
                    title: '미니',
                    to: '/productList/skirt/mini',
                }, {
                    title: '미디/롱',
                    to: '/productList/skirt/midi-long',
                }, ],
            }, {
                name: 'DRESS',
                to: '/productList/dress/all',
                items: [{
                    title: '원피스',
                    to: '/productList/dress/ops',
                }, {
                    title: '점프수트',
                    to: '/productList/dress/jumpSuit',
                }, ],
            }, {
                name: 'ProductSearch',
                to: '/productSearch',
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
    },
    methods: {
        onScroll(e) {
            if (typeof window === 'undefined') return
            const top = window.pageYOffset || e.target.scrollTop || 0
            this.fab = top > 20
        },
        toTop() {
            this.$vuetify.goTo(0)
        }
    },
    computed: {
        width() {
            switch (this.$vuetify.breakpoint.name) {
                case 'xs':
                    return false;
                case 'sm':
                    return true;
                case 'md':
                    return true;
                case 'lg':
                    return true;
                case 'xl':
                    return true;
                default:
                    return true;
            }
        },

    }
}
</script>

<style scoped>
</style>
