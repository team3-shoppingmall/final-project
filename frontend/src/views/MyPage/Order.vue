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
                    <v-btn class="primary mr-2">오늘</v-btn>
                    <v-btn class="primary mr-2">1주일</v-btn>
                    <v-btn class="primary mr-2">1개월</v-btn>
                    <v-btn class="primary mr-2">3개월</v-btn>
                    <v-btn class="primary">6개월</v-btn>
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
                    <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchProduct"></v-text-field>
                </v-col>
                <v-col cols="2" align-self="center">
                    <v-btn class="primary mr-2">조회</v-btn>
                    <v-btn class="primary" @click="reset">초기화</v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">
                    <v-data-table :headers="headers" :items="orders" :options.sync="options" :server-items-length="totalContents" :loading="loading" class="elevation-1 my-3" dense="dense">
                        <template #top="{ }">
                            <div class="text-h5 pa-3">{{selectedOrder}}</div>
                        </template>
                        <template #[`item.regDate`]="{item}">
                            <div>
                                <DateDisplay :regDate="item.regDate" />
                            </div>
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
export default {
    components: {
        DateDisplay,
    },
    data() {
        return {
            searchTypeNo: 0,
            totalContents: 0,
            loading: false,
            editItem: {},
            options: {},
            headers: [{
                text: '주문일자',
                align: 'start',
                sortable: false,
                value: 'name'
            }, {
                text: '상품이미지',
                value: 'calories'
            }, {
                text: '상품정보',
                value: 'fat'
            }, {
                text: '수량',
                value: 'carbs'
            }, {
                text: '상품 구매 금액',
                value: 'protein'
            }, {
                text: '주문 상태',
                value: 'iron'
            }],

            searches: [{
                text: '주문번호',
                value: 'orderIdx',
            }, {
                text: '상품번호',
                value: 'productNo',
            }, {
                text: '상품명',
                value: 'productName',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
            }, {
                text: '주문 상태',
                value: 'state',
            }, ],
            search: 'orderIdx',
            searchWord1: '',
            searchDate1: '',
            searchDate2: '',

            states: [{
                text: '기준 선택',
                value: null,
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
            }, {
                text: '취소완료',
                value: '취소완료',
            }, {
                text: '교환완료',
                value: '교환완료',
            }, {
                text: '환불완료',
                value: '환불완료',
            }, ],
            stateSelected: null,

            orders: [],

            menu1: false,
            menu2: false,

            selectedOrder: '주문 내역조회',
            selectedColor: true,

        }
    },
    methods: {
        colorPicker(put) {
            if (this.selectedOrder == put) {
                return 'secondary'
            }
        },
        selectOrder(put) {
            this.selectedOrder = put;
            this.getDataFromApi(put);
        },
        searchProduct() {
            let type1 = null;
            let type2 = null;
            if (this.typeSelected != null) {
                type1 = this.typeSelected.split(';')[0];
                type2 = this.typeSelected.split(';')[1];
            }
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                method: 'get',
                url: `/api/product/getProductAll`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    type1: type1,
                    type2: type2,
                    search: this.search,
                    searchWord1: this.searchWord1,
                    searchWord2: this.searchWord2,
                }
            }).then(res => {
                this.products = res.data.productList;
                this.totalContents = res.data.count;
            }).catch((err) => {
                this.products = [];
                this.totalContents = 0;
                this.noSearch = true;
                console.log(err);
            })
        },
        reset() {
            this.typeSelected = null;
            this.searchWord1 = null;
            this.searchWord2 = null;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.searchProduct();
        },
        changeOnSale(item) {
            axios.patch(`/api/product/updateOnSale/${item.productNo}`)
                .then(() => {
                    alert('판매 여부가 변경되었습니다');
                    this.searchProduct();
                }).catch(err => {
                    alert('변경 실패했습니다.')
                    console.log(err);
                })
        },
        searchPolicy() {
            if (this.search == 'price') {
                if (this.searchWord1 < 0 || this.searchWord1 > 9999999 || this.searchWord1 != Math.round(this.searchWord1)) {
                    alert('가격 제한 : 0원 ~ 9,999,999원');
                    this.searchWord1 = 0;
                } else if (this.searchWord2 < 0 || this.searchWord2 > 9999999 || this.searchWord2 != Math.round(this.searchWord2)) {
                    alert('가격 제한 : 0원 ~ 9,999,999원');
                    this.searchWord2 = 9999999;
                }
            } else {
                if (this.searchWord1 < 0 || this.searchWord1 > 9999 || this.searchWord1 != Math.round(this.searchWord1)) {
                    alert('개수 제한 : 0개 ~ 9,999개');
                    this.searchWord1 = 0;
                } else if (this.searchWord2 < 0 || this.searchWord2 > 9999 || this.searchWord2 != Math.round(this.searchWord2)) {
                    alert('개수 제한 : 0개 ~ 9,999개');
                    this.searchWord2 = 9999;
                }
            }
        },
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.searchProduct();
            },
            deep: true,
        },
        search: {
            handler() {
                this.searchWord1 = '';
                this.searchWord2 = '';
                this.typeSelected = null;
                if (this.search == 'price') {
                    this.searchWord1 = 0;
                    this.searchWord2 = 9999999;
                } else if (this.search == 'amount') {
                    this.searchWord1 = 0;
                    this.searchWord2 = 9999;
                } else if (this.search == 'regDate') {
                    let date = new Date();
                    this.searchWord1 = `${date.getFullYear()-10}-${date.getMonth()+1}-${date.getDate()}`;
                    this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
                }
            }
        },

    },
}
</script>

<style scoped>
.v-btn:not(.v-btn--round).v-size--default {
    height: 36px;
    min-width: 45px;
    padding: 5px 10px;
}
</style>
