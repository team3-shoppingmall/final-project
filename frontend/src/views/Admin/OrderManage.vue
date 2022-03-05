<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="1" align-self="center">
            <v-select :items="searches" v-model="search" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="4" align-self="center" v-if="search == 'orderIdx' || search == 'productNo' || search == 'productName'">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchProduct"></v-text-field>
        </v-col>

        <v-col cols="2" align-self="center" v-if="search == 'orderDate'">
            <v-menu v-model="menu1" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="searchWord1" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                </template>
                <v-date-picker v-model="searchWord1" @input="menu1 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
            </v-menu>
        </v-col>
        <v-col cols="2" align-self="center" v-if="search == 'orderDate'">
            <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="auto" hide-details>
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="searchWord2" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on" hide-details></v-text-field>
                </template>
                <v-date-picker v-model="searchWord2" @input="menu2 = false" no-title="no-title" scrollable="scrollable"></v-date-picker>
            </v-menu>
        </v-col>

        <v-col cols="4" align-self="center" v-if="search == 'state'">
            <v-select v-model="stateSelected" :items="states" hide-details></v-select>
        </v-col>

        <v-col cols="auto" align-self="center">
            <v-btn class="primary" large="large" @click="searchProduct">검색</v-btn>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary" large="large" @click="reset">초기화</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :options.sync="options" :items="products" item-key="productNo" :server-items-length="totalContents" :loading="loading" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
                <template #[`item.productName`]="{item}">
                    <v-btn text :to="`/productDetail/${item.productNo}`" v-if="item.productNo > 0">
                        <div class="text-truncate" style="max-width: 250px;">
                            {{ item.productName }}
                        </div>
                    </v-btn>
                </template>
                <template #[`item.imageName`]="{item}">
                    <v-row justify="center">
                        <v-col cols="auto">
                            <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                                <v-carousel-item v-for="(image,i) in item.imageName.split(';')" :key="i">
                                    <v-dialog max-width="700">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/product/productImage/${item.productNo}/${image}`" contain></v-img>
                                        </template>
                                        <v-card>
                                            <v-img :src="`/api/product/productImage/${item.productNo}/${image}`"></v-img>
                                        </v-card>
                                    </v-dialog>
                                </v-carousel-item>
                            </v-carousel>
                        </v-col>
                    </v-row>
                </template>
                <template #[`item.detailImageName`]="{item}">
                    <v-row justify="center">
                        <v-col cols="auto">
                            <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                                <v-carousel-item v-for="(image,i) in item.detailImageName.split(';')" :key="i">
                                    <v-dialog max-width="700">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/product/detailImage/${item.productNo}/${image}`" contain></v-img>
                                        </template>
                                        <v-card>
                                            <v-img :src="`/api/product/detailImage/${item.productNo}/${image}`"></v-img>
                                        </v-card>
                                    </v-dialog>
                                </v-carousel-item>
                            </v-carousel>
                        </v-col>
                    </v-row>
                </template>
                <template #[`item.price`]="{item}">
                    {{ AddComma(item.price) }}
                </template>
                <template #[`item.discount`]="{item}">
                    {{ AddComma(item.discount) }}
                </template>
                <template #[`item.color`]="{item}">
                    <div v-if="item.color != null">
                        <div v-for="idx in item.color.split(';').length" :key="idx">
                            {{item.color.split(';')[idx-1]}}
                        </div>
                    </div>
                </template>
                <template #[`item.size`]="{item}">
                    <div v-if="item.size != null">
                        <div v-for="idx in item.size.split(';').length" :key="idx">
                            {{item.size.split(';')[idx-1]}}
                        </div>
                    </div>
                </template>
                <template #[`item.regDate`]="{item}">
                    <div>
                        <DateDisplay :regDate="item.regDate" />
                    </div>
                </template>
                <template #[`item.onSale`]="{item}">
                    <v-btn text @click="changeOnSale(item)">
                        {{item.onSale}}
                    </v-btn>
                </template>
                <template v-slot:[`item.btn`]="{item}">
                    <v-btn color="primary" :to="`/admin/updateProduct/${item.productNo}`">
                        수정
                    </v-btn>
                </template>
                <template #footer="{}">
                    <v-row class="ma-0" justify="end">
                        <v-col cols="auto">
                            <v-btn class="primary">변경</v-btn>
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>
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
            options: {},
            headers: [{
                text: '주문번호',
                value: 'orderIdx',
                divider: true,
                align: 'center',
                width: '6%',
            }, {
                text: '상품번호',
                value: 'productNo',
                divider: true,
                align: 'center',
                width: '6%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '15%',
            }, {
                text: '가격',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '개수',
                value: 'orderAmount',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '구매자',
                value: 'id',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '주문 상태',
                value: 'state',
                divider: true,
                align: 'center',
                width: '5%',
            }, {
                text: '상태 변경',
                value: 'stateChange',
                divider: true,
                align: 'center',
                width: '9%',
            }, ],

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
            searchWord2: '',

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
        }
    },
    methods: {
        searchOrder() {
            let type1 = null;
            let type2 = null;
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
            this.stateSelected = null;
            this.searchWord1 = null;
            this.searchWord2 = null;
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.searchOrder();
        },
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.searchOrder();
            },
            deep: true,
        },
        search: {
            handler() {
                this.searchWord1 = '';
                this.searchWord2 = '';
                this.stateSelected = null;
                if (this.search == 'orderDate') {
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
</style>
