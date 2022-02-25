<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <v-row class="text-h3 md-10">
                SEARCH ITEMS
            </v-row>
            <v-row justify="center">
                <v-col cols="5">
                    <v-form ref="form">
                        <v-simple-table>
                            <template slot="default">
                                <tbody>
                                    <tr>
                                        <td style="width:30%"> 상품 분류 </td>
                                        <td>
                                            <v-select v-model="typeSelected" :items="types"></v-select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:30%"> 상품명 </td>
                                        <td>
                                            <v-text-field v-model="productName"></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:30%"> 판매 가격대 </td>
                                        <td>
                                            <v-row justify="center">
                                                <v-col cols="4">
                                                    <v-text-field v-model="minPrice" suffix="원" @keyup="pricePolicy"></v-text-field>
                                                </v-col>
                                                <v-col cols="auto" class="mt-5">~</v-col>
                                                <v-col cols="4">
                                                    <v-text-field v-model="maxPrice" suffix="원" @keyup="pricePolicy"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:30%"> 검색 정렬 기준 </td>
                                        <td>
                                            <v-select v-model="searchOrder" :items="searchOrders"></v-select>
                                        </td>
                                    </tr>
                                </tbody>
                            </template>
                        </v-simple-table>
                        <v-row justify="center" class="mt-3">
                            <v-col cols="auto">
                                <v-btn @click="searchProduct" outlined>검색</v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-col>
            </v-row>
            <div class="text-subtitle-1">
                총 {{totalLength}}개의 상품이 검색되었습니다
            </div>
            <div class="text-center text-h3 mt-10" v-if="noSearch">
                검색된 상품이 없습니다.
            </div>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in products" :key="idx" cols="3">
                            <v-card @click="moveToDetail(product.productNo)" min-height="385">
                                <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                                <v-card-text style="height:120px">
                                    <div style="height:50px">
                                        {{product.productName}}
                                        - <span v-if="product.size != null">{{product.size.split(';').length-1}} size</span>
                                        <span v-if="product.size == null">{{product.color.split(';').length-1}} color</span>
                                    </div>
                                    <div v-if="product.discount != 0" class="text-decoration-line-through">{{product.price}}원</div>
                                    <div v-if="product.discount == 0">{{product.price}}원</div>
                                    <div v-if="product.discount != 0">{{product.price-product.discount}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <div class="text-center" v-if="this.pageLength != 0">
                <v-pagination v-model="page" :length="pageLength" :total-visible="visibleLength"></v-pagination>
            </div>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            types: [{
                    text: '기준 선택',
                    value: null,
                },
                {
                    text: 'OUTER',
                    value: 'outer',
                },
                {
                    text: 'OUTER>자켓',
                    value: 'outer;jacket',
                },
                {
                    text: 'OUTER>코트',
                    value: 'outer;coat',
                },
                {
                    text: 'OUTER>가디건',
                    value: 'outer;cardigan',
                },
                {
                    text: 'OUTER>점퍼',
                    value: 'outer;jumper',
                },
                {
                    text: 'SKIRT',
                    value: 'skirt',
                },
                {
                    text: 'SKIRT>미니',
                    value: 'skirt;mini',
                },
                {
                    text: 'SKIRT>미디/롱',
                    value: 'skirt;midi-long',
                },
            ],
            typeSelected: null,

            productName: null,
            minPrice: 0,
            maxPrice: 9999999,

            searchOrders: [{
                    text: '기준 선택',
                    value: null,
                },
                {
                    text: '신상품 순',
                    value: 'regDate desc',
                },
                {
                    text: '낮은 가격 순',
                    value: 'price asc',
                },
                {
                    text: '높은 가격 순',
                    value: 'price desc',
                },
            ],
            searchOrder: null,

            page: 1,
            pageLength: 0,
            visibleLength: 5,
            totalLength: 0,
            products: [],
            noSearch: false,

        }
    },
    methods: {
        search(category) {
            this.selectedCategory = category;
        },
        colorPicker(put) {
            if (this.selectedCategory == put) {
                return 'primary'
            }
        },
        moveToDetail(num) {
            this.$router.push(`/productDetail/${num}`)
        },
        searchProduct() {
            this.noSearch = false;
            let type1 = null;
            let type2 = null;
            if (this.typeSelected != null) {
                type1 = this.typeSelected.split(';')[0];
                type2 = this.typeSelected.split(';')[1];
            }
            axios({
                method: 'get',
                url: `/api/product/getProductList`,
                params: {
                    page: this.page,
                    perPage: 12,
                    type1: type1,
                    type2: type2,
                    searchWord: this.productName,
                    minPrice: this.minPrice,
                    maxPrice: this.maxPrice,
                    searchOrder: this.searchOrder,
                }
            }).then(res => {
                this.products = res.data.productList;
                this.pageLength = Math.ceil(res.data.count / 12);
                this.totalLength = res.data.count;
            }).catch((err) => {
                this.products = [];
                this.pageLength = 0;
                this.noSearch = true;
                console.log(err);
            })
        },
        pricePolicy() {
            if (this.minPrice < 0 || this.minPrice > 9999999 || this.minPrice != Math.round(this.minPrice)) {
                alert('0원 ~ 9,999,999원의 상품만 검색이 가능합니다');
                this.minPrice = 0;
            } else if (this.maxPrice < 0 || this.maxPrice > 9999999 || this.maxPrice != Math.round(this.maxPrice)) {
                alert('0원 ~ 9,999,999원의 상품만 검색이 가능합니다');
                this.maxPrice = 9999999;
            }
        },
    },
    watch: {
        page: {
            handler() {
                this.searchProduct();
            }
        }
    },
    mounted() {
        this.mainCategory = this.$route.params.id;
        this.search(this.$route.params.sub)
    }
}
</script>

<style scoped>
</style>
