<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="1" align-self="center">
            <v-select :items="searches" v-model="search" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="4" align-self="center" v-if="search == 'productNo' || search == 'productName'">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchSales"></v-text-field>
        </v-col>

        <v-col cols="2" align-self="center" v-if="search == 'priceSum'" @keyup="searchPolicy">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchSales"></v-text-field>
        </v-col>
        <v-col cols="2" align-self="center" v-if="search == 'priceSum'" @keyup="searchPolicy">
            <v-text-field hide-details="hide-details" v-model="searchWord2" @keyup.enter="searchSales"></v-text-field>
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

        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="searchSales">검색</v-btn>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="reset">초기화</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers" :options.sync="options" :items="sales" item-key="productNo" :server-items-length="totalContents" :loading="loading" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
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
                            <v-img min-height="100" max-height="100" max-width="100" :src="`/api/product/productImage/${item.productNo}/${item.imageName.split(';')[0]}`"></v-img>
                        </v-col>
                    </v-row>
                </template>
                <template #[`item.priceSum`]="{item}">
                    {{ AddComma(item.priceSum) }}원
                </template>
            </v-data-table>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <div class="text-h4">총 판매 금액 : {{AddComma(totalPrice)}}원</div>
        </v-col>
    </v-row>
    <v-dialog v-model="alertDialog" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            sales: [],
            totalContents: 0,
            loading: false,
            options: {},
            headers: [{
                text: '상품번호',
                value: 'productNo',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '이미지',
                value: 'imageName',
                divider: true,
                align: 'center',
                width: '20%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '30%',
            }, {
                text: '가격',
                value: 'priceSum',
                align: 'center',
                width: '40%',
            }, ],

            searches: [{
                text: '상품번호',
                value: 'productNo',
            }, {
                text: '상품명',
                value: 'productName',
            }, {
                text: '판매금액',
                value: 'priceSum',
            }, {
                text: '주문 날짜',
                value: 'orderDate',
            }, ],
            search: 'orderDate',
            searchWord1: '',
            searchWord2: '',
            totalPrice: 0,

            menu1: false,
            menu2: false,
        }
    },
    methods: {
        getSales() {
            this.sales = [];
            this.totalContents = 0;
            this.totalPrice = 0;
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                method: 'get',
                url: `/api/order/getSalesSettlement`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord1: this.searchWord1,
                    searchWord2: this.searchWord2,
                }
            }).then(res => {
                this.sales = res.data.salesList;
                this.totalContents = res.data.countList.length;
                for (let i = 0; i < res.data.countList.length; i++) {
                    this.totalPrice += res.data.countList[i];
                }
            }).catch((err) => {
                console.log(err);
            }).finally(
                this.loading = false
            )
        },
        searchSales() {
            if (this.options.page != 1) {
                this.options.page = 1;
            } else {
                this.getSales();
            }
        },
        reset() {
            this.searchWord1 = null;
            this.searchWord2 = null;
            if (this.search == 'priceSum') {
                this.searchWord1 = 0;
                this.searchWord2 = 999999999;
            } else if (this.search == 'orderDate') {
                let date = new Date();
                this.searchWord1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
                this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
            }
            this.options.page = 1;
            this.options.itemsPerPage = 10;
            this.getSales();
        },
        searchPolicy() {
            if (this.searchWord1 < 0 || this.searchWord1 > 999999999 || this.searchWord1 != Math.round(this.searchWord1)) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '가격 제한 : 0원 ~ 999,999,999원';
                this.searchWord1 = 0;
            } else if (this.searchWord2 < 0 || this.searchWord2 > 999999999 || this.searchWord2 != Math.round(this.searchWord2)) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '가격 제한 : 0원 ~ 999,999,999원';
                this.searchWord2 = 999999999;
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
                this.getSales();
            },
            deep: true,
        },
        search: {
            handler() {
                this.searchWord1 = '';
                this.searchWord2 = '';
                if (this.search == 'priceSum') {
                    this.searchWord1 = 0;
                    this.searchWord2 = 999999999;
                } else if (this.search == 'orderDate') {
                    let date = new Date();
                    this.searchWord1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
                    this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
                }
            }
        },
    },
    mounted() {
        let date = new Date();
        this.searchWord1 = `${date.getFullYear()-5}-${date.getMonth()+1}-${date.getDate()}`;
        this.searchWord2 = `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`;
    }
}
</script>

<style scoped>
</style>
