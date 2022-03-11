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
        <v-col cols="auto" style="position:fixed; top:78%; right:10px; z-index: 1;">
            <v-row justify="end" class="mb-0">
                <v-btn fab dark elevation="2" right color="#FF8EA0" @click.stop="dialog = true">
                    <v-icon>mdi-chat</v-icon>
                </v-btn>
            </v-row>
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
    <v-dialog v-model="dialog" width="600px">
        <v-card>
            <v-card-title class="text-h5 grey lighten-2">
                <v-row justify="space-between">
                    <v-col>Spring Chatbot</v-col>
                    <v-col cols="auto">
                        <v-icon @click="messages = []; dialog = false">mdi-exit-to-app</v-icon>
                    </v-col>
                </v-row>

            </v-card-title>
            <v-virtual-scroll :items="messages" :item-height="50" height="600" id="virtualScroll">
                <template v-slot:default="{ item }">
                    <v-list-item v-if="item.author == 'client'">
                        <v-list-item-content>
                            <v-list-item-title>
                                <v-row justify="end">
                                    <v-col cols="auto">
                                        {{ item.text }}
                                    </v-col>
                                </v-row>
                            </v-list-item-title>
                        </v-list-item-content>
                        <v-list-item-icon>
                            <v-icon>mdi-alpha-q-box</v-icon>
                        </v-list-item-icon>
                    </v-list-item>
                    <v-list-item v-if="item.author == 'server'">
                        <v-list-item-icon>
                            <v-icon>mdi-alpha-a-box</v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>{{ item.text }}</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </template>
            </v-virtual-scroll>
            <v-divider></v-divider>
            <v-card-text>
                <v-row>
                    <v-col cols="10">
                        <v-text-field v-model="message" clearable hide-details @keyup.enter="sendMessage"></v-text-field>
                    </v-col>
                    <v-col cols="2" class="mt-3">
                        <v-btn color="primary" @click="sendMessage">입력</v-btn>
                    </v-col>
                </v-row>
            </v-card-text>

        </v-card>
    </v-dialog>
    <v-footer color="primary lighten-1" padless>
        <Footer />
    </v-footer>
</v-app>
</template>

<script>
import Footer from '@/components/Footer.vue'
import axios from 'axios'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    data() {
        return {
            fab: false,
            dialog: false,
            message: '',
            messages: [],
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
        },
        sendMessage() {
            this.messages.push({
                text: this.message,
                author: 'client'
            });

            let id = null;
            if (this.getLogin == null) {
                var timestamp = new Date().getUTCMilliseconds();
                id = 'tempUser' + timestamp;
            } else {
                id = this.getLogin.user.id;
            }

            axios({
                    method: 'get',
                    url: `/api/chatbot/chatbotform`,
                    params: {
                        query: this.message,
                        id: id,
                    }
                })
                .then(res => {
                    this.messages.push({
                        text: res.data,
                        author: 'server'
                    });
                    this.message = '';
                }).finally(() => {
                    var container = this.$el.querySelector("#virtualScroll");
                    container.scrollTop = container.scrollHeight;
                })
        },
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
        ...LoginStore.mapGetters(['getLogin']),
    }
}
</script>

<style scoped>
</style>
