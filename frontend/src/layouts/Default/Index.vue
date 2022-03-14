<template>
<v-app>
    <v-app-bar color="primary" app dark height="60px">
        <v-btn color="primary" active-class="no-active" :to="'/'">
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
                        <v-btn color="primary" dark @click="signOut">
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
    <v-dialog v-model="dialog" scrollable width="600px" persistent>
        <v-container style="background-color:white;">
            <v-container class="text-h5">
                <v-row justify="space-between">
                    <v-col align-self="center">Spring Chatbot</v-col>
                    <v-col align-self="center" cols="auto">
                        <v-row>
                            <v-col align-self="center">
                                <v-btn @click="dialog2 = true" text>
                                    <v-icon color="#FF8EA0">mdi-magnify</v-icon>주문 확인
                                </v-btn>
                            </v-col>
                            <v-col align-self="center">
                                <v-icon @click="dialog = false" color="#FF8EA0">mdi-exit-to-app</v-icon>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>
            </v-container>
            <v-divider></v-divider>
            <v-container style="height: 600px; overflow-y:scroll" id="test">
                <v-row v-for="(msg, idx) in messages" :key="idx">
                    <v-col>
                        <v-row justify="end" v-if="msg.author == 'client'">
                            <v-col cols="10">
                                <v-row justify="end">
                                    <v-col cols="auto">
                                        <v-card elevation="2" outlined color="blue lighten-1">
                                            <v-card-text>
                                                <div class="text--primary" v-html="msg.text"></div>
                                            </v-card-text>
                                        </v-card>
                                    </v-col>
                                </v-row>
                            </v-col>
                            <v-col cols="auto">
                                <v-icon color="blue">mdi-alpha-q-box</v-icon>
                            </v-col>
                        </v-row>
                        <v-row v-if="msg.author == 'server'">
                            <v-col cols="auto">
                                <v-icon color="#FF8EA0">mdi-alpha-a-box</v-icon>
                            </v-col>
                            <v-col cols="10">
                                <v-row>
                                    <v-col cols="auto">
                                        <v-card elevation="2" outlined color="#FF8EA0b3">
                                            <v-card-text>
                                                <div class="text--primary" v-html="msg.text"></div>
                                            </v-card-text>
                                        </v-card>
                                    </v-col>
                                </v-row>
                                <v-row v-if="msg.buttons != undefined">
                                    <v-col v-for="button in msg.buttons" :key="button" cols="2">
                                        <v-btn tile @click="selectMessage(msg, button)" color="primary">{{button}}</v-btn>
                                    </v-col>
                                </v-row>
                                <v-row v-if="msg.url != undefined">
                                    <v-col cols="2">
                                        <v-btn tile color="primary" :to="`${msg.url}`" @click="dialog = false">이동</v-btn>
                                    </v-col>
                                </v-row>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>
            </v-container>
            <v-divider></v-divider>
            <v-row>
                <v-col>
                    <v-text-field v-model="message" clearable hide-details @keyup.enter="sendMessage"></v-text-field>
                </v-col>
                <v-col cols="auto" class="mt-3">
                    <v-btn color="primary" @click="sendMessage">입력</v-btn>
                </v-col>
            </v-row>
        </v-container>
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
            textHeight: 200,
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
                }).catch(() => {
                    this.orderState = '해당 주문이 없습니다';
                })
                .finally(
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
            if (this.message == '') {
                return;
            }
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
                    this.previousMessage = res.data.description.replace(/(?:\r\n|\r|\n)/g, '<br />');
                    if (res.data.url != undefined) {
                        this.messages.push({
                            text: res.data.description.replace(/(?:\r\n|\r|\n)/g, '<br />'),
                            buttons: res.data.buttonList,
                            url: res.data.url.substring(21),
                            author: 'server'
                        });
                    } else {
                        this.messages.push({
                            text: res.data.description.replace(/(?:\r\n|\r|\n)/g, '<br />'),
                            buttons: res.data.buttonList,
                            author: 'server'
                        });
                    }
                    this.message = '';
                }).catch(err => {
                    console.log(err);
                })
                .finally(() => {
                    let container = this.$el.querySelector("#test");
                    container.scrollTop = container.scrollHeight;
                })
        },
        moveToUrl(url) {
            this.$router.replace(url);
        },
        itemHeight(item) {
            console.log(item)

            if (item) {
                if (item.text.length > 100) {
                    return 200
                } else
                    return 100
            } else
                return 50
        },
        ...LoginStore.mapActions(['Logout']),
        signOut(){
            this.Logout;
            this.$router.push('/')
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
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() {
        this.reset();
    }
}
</script>

<style scoped>
.v-btn--active.no-active::before {
    opacity: 0 !important;
}
</style>
