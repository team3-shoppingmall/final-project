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
                                                    <v-text-field v-model="minPrice" suffix="원"></v-text-field>
                                                </v-col>
                                                <v-col cols="auto" class="mt-5">~</v-col>
                                                <v-col cols="4">
                                                    <v-text-field v-model="maxPrice" suffix="원"></v-text-field>
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
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in products" :key="idx" cols="3">
                            <v-card @click="moveToDetail(product.productNo)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(idx)}/300/250`"></v-img>
                                <v-card-text>
                                    <div>
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
                    value: '',
                    disabled: true,
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
            typeSelected: '',

            productName: '',
            minPrice: 0,
            maxPrice: 99999999,

            searchOrders: [{
                    text: '기준 선택',
                    value: '',
                    disabled: true,
                },
                {
                    text: '신상품 순',
                    value: 'regDate desc',
                },
                {
                    text: '낮은 가격 순',
                    value: 'regDate asc',
                },
                {
                    text: '높은 가격 순',
                    value: 'price desc',
                },
            ],
            searchOrder: '',

            page: 1,
            pageLength: 0,
            visibleLength: 5,
            products: '',
            product: [{
                productno: 1,
                imageName: '',
                productname: '블랙트위드 스커트',
                size: 3,
                color: 4,
                price: 20000,
                discount: 5,
            }],

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
            axios({
                method: 'get',
                url: `/api/product/getProductListByType`,
                params: {
                    page: this.page,
                    perPage: 12,
                    type1: this.typeSelected.split(';')[0],
                    type2: this.typeSelected.split(';')[1],
                    searchWord: this.productName,
                    minPrice: this.minPrice,
                    maxPrice: this.maxPrice,
                    searchOrder: this.searchOrder,
                }
            }).then(res => {
                this.products = res.data;
                axios({
                    method: 'get',
                    url: `/api/product/getProductCountByType`,
                    params: {
                        type1: this.typeSelected[0],
                        type2: this.typeSelected[1],
                        searchWord: this.productName,
                        minPrice: this.minPrice,
                        maxPrice: this.maxPrice,
                        searchOrder: this.searchOrder,
                    }
                }).then(res => {
                    this.pageLength = Math.ceil(res.data / this.page);
                }).catch((err) => {
                    console.log(err);
                })
            }).catch((err) => {
                console.log(err);
            })
        },
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
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
