<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <v-row>
                <v-col cols="auto">
                    <v-btn :color="colorPicker('주문 내역조회')" @click="selectOrder('주문 내역조회')" width="240px">주문 내역조회</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :color="colorPicker('취소/반품/교환 내역조회')" @click="selectOrder('취소/반품/교환 내역조회')" width="240px">취소/반품/교환 내역조회</v-btn>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col align-self="center" cols="auto">
                    <v-btn class="primary mr-2" @click="changeDate('today')">오늘</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('1week')">1주일</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('1month')">1개월</v-btn>
                    <v-btn class="primary mr-2" @click="changeDate('3month')">3개월</v-btn>
                    <v-btn class="primary" @click="changeDate('6month')">6개월</v-btn>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-menu v-model="menu1" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="searchDate1" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                        </template>
                        <v-date-picker v-model="searchDate1" @input="menu1 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
                    </v-menu>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="searchDate2" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                        </template>
                        <v-date-picker v-model="searchDate2" @input="menu2 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col cols="3" align-self="center">
                    <v-select v-model="stateSelected" :items="states" hide-details></v-select>
                </v-col>
                <v-col cols="7" align-self="center">
                    <v-text-field hide-details="hide-details" v-model="searchWord" @keyup.enter="searchOrder(selectedOrder)"></v-text-field>
                </v-col>
                <v-col cols="2" align-self="center">
                    <v-btn class="primary mr-2" @click="searchOrder(selectedOrder)">조회</v-btn>
                    <v-btn class="primary" @click="reset">초기화</v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">
                    <v-data-table :headers="selectedOrder=='주문 내역조회' ? headers : returnHeaders" :items="orders" :options.sync="options" item-key="orderIdx" :server-items-length="totalContents" :loading="loading" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                        <template #top="{ }">
                            <div class="text-h5 pa-3">{{selectedOrder}}</div>
                        </template>
                        <template #[`item.productName`]="{item}">
                            <v-btn text :to="`/productDetail/${item.productNo}`">
                                <div class="text-left text-truncate" style="max-width: 90px;" v-if="selectedOrder =='주문 내역조회'">
                                    {{ item.productName }}
                                </div>
                                <div class="text-left text-truncate" style="max-width: 220px;" v-if="selectedOrder !='주문 내역조회'">
                                    {{ item.productName }}
                                </div>
                            </v-btn>
                        </template>
                        <template #[`item.imageName`]="{item}">
                            <v-row justify="center">
                                <v-col cols="auto">
                                    <v-img min-height="100" max-height="100" max-width="100" :src="`/api/product/productImage/${item.productNo}/${item.imageName.split(';')[0]}`"></v-img>
                                </v-col>
                            </v-row>
                        </template>
                        <template #[`item.totalPrice`]="{item}">
                            {{ AddComma(item.totalPrice) }}원
                        </template>
                        <template #[`item.orderDate`]="{item}">
                            <div>
                                <DateDisplay :regDate="item.orderDate" />
                            </div>
                        </template>
                        <template v-slot:[`item.btn`]="{item}">
                            <v-btn color="primary" @click="cancelOrder(item)" v-if="item.state != '배송완료'">
                                취소
                            </v-btn>
                            <v-btn color="primary" @click="cancelOrder(item)" v-if="item.state == '배송완료'">
                                환불 및 교환
                            </v-btn>
                        </template>
                    </v-data-table>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import DateDisplay from '@/components/DateDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        DateDisplay,
    },
    data() {
        return {
            orders: [],
            totalContents: 0,
            loading: false,
            options: {},
            headers: [{
                text: '주문번호',
                value: 'orderIdx',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '이미지',
                value: 'imageName',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '개수',
                value: 'orderAmount',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '가격',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '주문 상태',
                value: 'state',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '',
                value: 'btn',
                align: 'center',
                width: '15%',
            }, ],

            returnHeaders: [{
                text: '주문번호',
                value: 'orderIdx',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '이미지',
                value: 'imageName',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '25%',
            }, {
                text: '개수',
                value: 'orderAmount',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '가격',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '주문 상태',
                value: 'state',
                align: 'center',
                width: '15%',
            }, ],
            searchWord: '',
            searchDate1: '',
            searchDate2: '',

            states: [],
            stateSelected: null,

            menu1: false,
            menu2: false,

            selectedOrder: '주문 내역조회',
            selectedColor: true,
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        colorPicker(put) {
            if (this.selectedOrder == put) {
                return 'secondary'
            }
        },
        selectOrder(put) {
            this.orders = [];
            this.selectedOrder = put;
            this.reset();
        },
        searchOrder(put) {
            this.loading = true;
            let pageInfo = '';
            if (put == '주문 내역조회') {
                pageInfo = 'orders';
                this.states = [{
                    text: '기준 선택',
                    value: null,
                }, {
                    text: '입금전',
                    value: '입금전',
                }, {
                    text: '결제완료',
                    value: '결제완료',
                }, {
                    text: '배송준비중',
                    value: '배송준비중',
                }, {
                    text: '배송중',
                    value: '배송중',
                }, {
                    text: '배송완료',
                    value: '배송완료',
                }, ];
            } else {
                pageInfo = 'returns';
                this.states = [{
                    text: '기준 선택',
                    value: null,
                }, {
                    text: '취소완료',
                    value: '취소완료',
                }, {
                    text: '교환완료',
                    value: '교환완료',
                }, {
                    text: '환불완료',
                    value: '환불완료',
                }, ];
            }
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                method: 'get',
                url: `/api/order/getOrderById`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    pageInfo: pageInfo,
                    state: this.stateSelected,
                    searchWord: this.searchWord,
                    searchDate1: this.searchDate1,
                    searchDate2: this.searchDate2,
                    id: this.getLogin.user.id,
                }
            }).then(res => {
                this.orders = res.data.orderList;
                this.totalContents = res.data.count;
            }).catch((err) => {
                this.orders = [];
                this.totalContents = 0;
                console.log(err);
            }).finally(
                this.loading = false
            )
        },
        cancelOrder(item) {
            if (item.state == '입금전' || item.state == '결제완료') {
                let states = [];
                let data = {
                    orderIdx: item.orderIdx,
                    state: '취소완료',
                }
                states.push(data);
                axios.patch(`/api/order/update`, states)
                    .then(res => {
                        if (res.data.length == 0) {
                            alert('주문을 취소하셨습니다');
                            this.searchOrder(this.selectedOrder);
                        } else {
                            alert(`미완료된 변경(총 ${res.data.length}건)\n주문번호 : ${res.data}`)
                        }
                    }).catch(err => {
                        console.log(err)
                    })
            } else if (item.state == '배송준비중') {
                alert('배송 준비중인 주문입니다. 배송 전 변경/취소 게시판에서 요청해주시기 바랍니다.');
                this.$router.push(`/qna/beforeDeliveryQnA`);
            } else {
                alert('배송 후 교환/반품 게시판에서 요청해주시기 바랍니다.');
                this.$router.push(`/qna/afterDeliveryQnA`);
            }
        },
        reset() {
            this.stateSelected = null;
            this.searchWord = null;
            this.searchDate1 = null;
            this.searchDate2 = null;
            let date = new Date();
            this.searchDate1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
            this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.searchOrder(this.selectedOrder);
        },
        changeDate(period) {
            let date = new Date();
            let year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate()

            if (period == '1week') {
                if (day > 6) {
                    day = day - 6;
                } else {
                    if (month > 1) {
                        month = month - 1;
                    } else {
                        year = year - 1;
                        month = month + 11;
                    }
                    day = day + 6;
                }
            } else if (period == '1month') {
                if (month > 1) {
                    month = month - 1;
                } else {
                    year = year - 1;
                    month = month + 11;
                }
            } else if (period == '3month') {
                if (month > 3) {
                    month = month - 3;
                } else {
                    year = year - 1;
                    month = month + 9;
                }
            } else if (period == '6month') {
                if (month > 6) {
                    month = month - 6;
                } else {
                    year = year - 1;
                    month = month + 6;
                }
            }
            this.searchDate1 = `${year}-${month}-${day}`;
            this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;

            this.stateSelected = null;
            this.searchWord = null;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.searchOrder(this.selectedOrder);
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.searchOrder(this.selectedOrder);
            },
            deep: true,
        },
    },
    mounted() {
        let date = new Date();
        this.searchDate1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
        this.searchDate2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
    }
}
</script>

<style scoped>
.v-btn:not(.v-btn--round).v-size--default {
    height: 36px;
    min-width: 45px;
    padding: 5px 10px;
}
</style>
