<template>
<v-app>
    <v-app-bar color="primary" app dark height="60px">

        <v-btn color="primary" :to="'/'">
            <v-icon>mdi-home</v-icon>
        </v-btn>

        <v-row v-if="width">
            <v-col cols="12" align-self="center">
                <v-row class="mx-10">
                    <v-col cols="auto">
                        <v-menu open-on-hover bottom offset-y v-for="(page ,idx) in pages" :key="idx">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn color="primary" v-if="page.name != 'Community'" dark v-bind="attrs" v-on="on" :to="page.to">
                                    {{page.name}}
                                </v-btn>
                            </template>
                            <v-list>
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn color="primary" dark :to=" '/productSearch'">
                            <v-icon>mdi-magnify</v-icon>
                        </v-btn>
                    </v-col>
                    <v-spacer></v-spacer>
                    <v-col cols="auto">
                        <v-menu open-on-hover bottom offset-y v-for="(page ,idx) in pages" :key="idx">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn color="primary" v-if="page.name == 'Community'" dark v-bind="attrs" v-on="on" :to="page.to">
                                    {{page.name}}
                                </v-btn>
                            </template>
                            <v-list>
                                <v-list-item v-for="(item, index) in page.items" :key="index" :to="item.to">
                                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                                </v-list-item>
                            </v-list>
                        </v-menu>
                        <v-btn color="primary" dark :to="'/myPage/home'">
                            MyPage
                        </v-btn>
                        <v-btn color="primary" dark :to="'/basket'">
                            BASKET
                        </v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="getLogin">
                        <v-btn color="primary" v-if="getLogin.user.authority=='ROLE_ADMIN'" dark :to="'/admin/ProductManage'">
                            ADMIN
                        </v-btn>
                        <v-btn color="primary" v-if="getLogin.user.authority!='ROLE_ADMIN'" dark :to="'/myPage/home'">
                            {{getLogin.user.id}}
                        </v-btn>
                        <v-btn color="primary" dark @click="Logout">
                            SignOut
                        </v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="!getLogin">
                        <v-btn color="primary" dark :to="'/authentication/signIn'">
                            SignIn
                        </v-btn>
                        <v-btn color="primary" dark :to="'/authentication/signUp'">
                            SignUp
                        </v-btn>
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
    <v-dialog v-model="dialog" width="600px" persistent>
        <v-card>
            <v-card-title class="text-h5 grey lighten-2">
                <v-row justify="space-between">
                    <v-col>Spring Chatbot</v-col>
                    <v-col cols="auto">
                        <v-row>
                            <v-col>

                                <v-btn @click="dialog2 = true" text>
                                    <v-icon color="#FF8EA0">mdi-magnify</v-icon>주문 확인
                                </v-btn>
                            </v-col>
                            <v-col>
                                <v-icon @click="dialog = false" color="#FF8EA0">mdi-exit-to-app</v-icon>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>
            </v-card-title>
            <v-virtual-scroll :items="messages" :item-height="120" height="600" id="virtualScroll">
                <template v-slot:default="{ item }">
                    <v-list-item v-if="item.author == 'client'">
                        <v-list-item-content class="mb-5">
                            <v-list-item-title>
                                <v-row justify="end">
                                    <v-col cols="10">
                                        <v-row justify="end">
                                            <v-col cols="auto">
                                                <v-card elevation="2" outlined color="blue lighten-1">
                                                    <v-card-text>
                                                        <div class="text--primary">{{item.text}}</div>
                                                    </v-card-text>
                                                </v-card>
                                            </v-col>
                                        </v-row>
                                    </v-col>
                                </v-row>
                            </v-list-item-title>
                        </v-list-item-content>
                        <v-list-item-icon>
                            <v-icon color="blue">mdi-alpha-q-box</v-icon>
                        </v-list-item-icon>
                    </v-list-item>
                    <v-list-item v-if="item.author == 'server'">
                        <v-list-item-icon>
                            <v-icon color="#FF8EA0">mdi-alpha-a-box</v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>
                                <v-row>
                                    <v-col cols="10">
                                        <v-row>
                                            <v-col cols="auto">
                                                <v-card elevation="2" outlined color="#FF8EA0b3">
                                                    <v-card-text>
                                                        <div class="text--primary">{{item.text}}</div>
                                                    </v-card-text>
                                                </v-card>
                                            </v-col>
                                        </v-row>
                                    </v-col>
                                </v-row>
                            </v-list-item-title>
                            <v-list-item-subtitle v-if="item.buttons != undefined">
                                <v-btn tile v-for="button in item.buttons" :key="button" @click="selectMessage(item, button)" color="primary">{{button}}</v-btn>
                            </v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                </template>
            </v-virtual-scroll>
            <v-divider></v-divider>
            <v-card-text>
                <v-row>
                    <v-col>
                        <v-text-field v-model="message" clearable hide-details @keyup.enter="sendMessage"></v-text-field>
                    </v-col>
                    <v-col cols="auto" class="mt-3">
                        <v-btn color="primary" @click="sendMessage">입력</v-btn>
                    </v-col>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog2" width="400px" persistent>
        <v-card>
            <v-card-title class="text-h5 grey lighten-2">
                <v-row justify="space-between">
                    <v-col>주문 확인</v-col>
                    <v-col cols="auto">
                        <v-icon @click="dialog2 = false; orderState = ''" color="#FF8EA0">mdi-exit-to-app</v-icon>
                    </v-col>
                </v-row>
            </v-card-title>
            <v-divider></v-divider>
            <v-card-text>
                <v-row>
                    <v-col>
                        <v-text-field prefix="주문번호 : " v-model="orderNo" clearable hide-details @keyup.enter="searchOrder"></v-text-field>
                    </v-col>
                    <v-col cols="auto" class="mt-3">
                        <v-btn color="primary" @click="searchOrder">확인</v-btn>
                    </v-col>
                </v-row>
                <v-row v-if="orderState != ''">
                    <v-col>
                        주문상태 : {{orderState}}
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
            dialog2: false,
            message: '',
            orderNo: '',
            orderState: '',
            messages: [],
            previousMessage: '',
            pages: [{
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
                },

                {
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
                },
            ],
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
        reset() {
            this.messages = [];
            this.message = '';
            this.messages.push({
                text: '안녕하세요 spring 입니다. 무엇을 도와드릴까요?',
                author: 'server'
            });
            this.previousMessage = '안녕하세요 spring 입니다. 무엇을 도와드릴까요?';
        },
        searchOrder() {
            axios.get(`/api/order/getOrder/${this.orderNo}`)
                .then(res => {
                    this.orderState = res.data;
                }).finally(
                    this.orderNo = ''
                )
        },
        selectMessage(item, selected) {
            this.message = selected;
            if (item.text == '주소를 변경할 주문의 현재 배송 상태를 선택해주세요') {
                this.message = '주소변경' + selected;
            }
            if (item.text == '배송 상태를 선택해주세요') {
                this.message = '배송문의' + selected;
            }
            this.sendMessage();
        },
        sendMessage() {
            this.messages.push({
                text: this.message,
                author: 'client'
            });

            if (this.message == '종료') {
                this.dialog = false;
                return;
            }
            let id = null;
            if (this.getLogin == null) {
                var timestamp = new Date().getUTCMilliseconds();
                id = 'tempUser' + timestamp;
            } else {
                id = this.getLogin.user.id;
            }
            axios({
                    method: 'post',
                    url: `/api/chatbot/chatbot`,
                    params: {
                        query: this.message,
                        id: id,
                    }
                })
                .then(res => {
                    this.previousMessage = res.data.description;
                    if (res.data.buttonList != null) {
                        this.messages.push({
                            text: res.data.description,
                            buttons: res.data.buttonList,
                            author: 'server'
                        });
                    } else {
                        this.messages.push({
                            text: res.data.description,
                            author: 'server'
                        });
                    }
                    this.message = '';
                }).catch(err => {
                    console.log(err);
                })
                .finally(() => {
                    var container = this.$el.querySelector("#virtualScroll");
                    container.scrollTop = container.scrollHeight;
                })
        },
        ...LoginStore.mapMutations(['Logout']),
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
    },
    mounted() {
        this.reset();
    }
}
</script>

<style scoped>
</style>
