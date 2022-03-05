<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="1" align-self="center">
            <v-select :items="searches" v-model="search" hide-details="hide-details"></v-select>
        </v-col>
        <v-col cols="4" align-self="center" v-if="search == 'productNo' || search == 'productName'">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchProduct"></v-text-field>
        </v-col>

        <v-col cols="2" align-self="center" v-if="search == 'sellSum'" @keyup="searchPolicy">
            <v-text-field hide-details="hide-details" v-model="searchWord1" @keyup.enter="searchProduct"></v-text-field>
        </v-col>
        <v-col cols="2" align-self="center" v-if="search == 'sellSum'" @keyup="searchPolicy">
            <v-text-field hide-details="hide-details" v-model="searchWord2" @keyup.enter="searchProduct"></v-text-field>
        </v-col>

        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="searchProduct">검색</v-btn>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large="large" @click="reset">초기화</v-btn>
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
            editItem: {},
            options: {},
            headers: [{
                text: '상품번호',
                value: 'productNo',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '상품명',
                value: 'productName',
                divider: true,
                align: 'center',
                width: '30%',
            }, {
                text: '가격',
                value: 'sellSum',
                divider: true,
                align: 'center',
                width: '60%',
            }, ],

            searches: [{
                text: '상품번호',
                value: 'productNo',
            }, {
                text: '상품명',
                value: 'productName',
            }, {
                text: '판매금액',
                value: 'sellSum',
            }, ],
            search: 'productNo',
            searchWord1: '',
            searchWord2: '',

            products: [],
            colorList: [],
            sizeList: [],

            menu1: false,
            menu2: false,
        }
    },
    methods: {
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
            if (this.searchWord1 < 0 || this.searchWord1 > 9999999 || this.searchWord1 != Math.round(this.searchWord1)) {
                alert('가격 제한 : 0원 ~ 9,999,999원');
                this.searchWord1 = 0;
            } else if (this.searchWord2 < 0 || this.searchWord2 > 9999999 || this.searchWord2 != Math.round(this.searchWord2)) {
                alert('가격 제한 : 0원 ~ 9,999,999원');
                this.searchWord2 = 9999999;
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
                if (this.search == 'sellSum') {
                    this.searchWord1 = 0;
                    this.searchWord2 = 999999999;
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
</style>
