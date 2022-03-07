<template>
<v-container>
    <v-row justify="center" class="mt-1">
        <v-col cols="9">
            <v-row class="text-h4">
                BEST ITEMS
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in bestProducts" :key="idx" cols="3">
                            <v-card @click="moveToDetail(product.productNo)">
                                <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                                <v-card-text style="height:120px">
                                    <div style="height:50px">
                                        {{product.productName}}
                                        - <span v-if="product.size != null">{{product.size.split(';').length-1}} size</span>
                                        <span v-if="product.size == null">{{product.color.split(';').length-1}} color</span>
                                    </div>
                                    <div v-if="product.discount != 0" class="text-decoration-line-through">{{AddComma(product.price)}}원</div>
                                    <div v-if="product.discount == 0">{{AddComma(product.price)}}원</div>
                                    <div v-if="product.discount != 0">{{AddComma(product.price-product.discount)}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <v-divider></v-divider>
            <v-row class="text-h4 mt-10 text-uppercase">
                {{mainCategory}}
            </v-row>
            <v-row>
                <v-col cols="auto" v-for="category in subCategory" :key="category.value">
                    <v-btn :to="`/productList/${mainCategory}/${category.value}`">
                        {{category.text}}
                    </v-btn>
                </v-col>
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in products" :key="idx" cols="3">
                            <v-lazy :options="{threshold: .5}" transition="fade-transition">
                                <v-card @click="moveToDetail(product.productNo)">
                                    <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                                    <v-card-text style="height:120px">
                                        <div style="height:50px">
                                            {{product.productName}}
                                            - <span v-if="product.size != null">{{product.size.split(';').length-1}} size</span>
                                            <span v-if="product.size == null">{{product.color.split(';').length-1}} color</span>
                                        </div>
                                        <div v-if="product.discount != 0" class="text-decoration-line-through">{{AddComma(product.price)}}원</div>
                                        <div v-if="product.discount == 0">{{AddComma(product.price)}}원</div>
                                        <div v-if="product.discount != 0">{{AddComma(product.price-product.discount)}}원</div>
                                    </v-card-text>
                                </v-card>
                            </v-lazy>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <div class="text-center">
                <v-pagination v-model="page" :length="pageLength" :total-visible="visibleLength"></v-pagination>
            </div>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            mainCategory: '',
            subCategory: '',
            selectedCategory: '',

            page: 1,
            itemsPerPage: 12,
            pageLength: 0,
            visibleLength: 5,

            bestProducts: [],
            products: [],
        }
    },
    methods: {
        setSubCategory() {
            this.subCategory = [{
                text: 'ALL',
                value: 'all',
                type: 'all'
            }, {
                text: '자켓',
                value: 'jacket',
                type: 'outer',
            }, {
                text: '가디건',
                value: 'cardigan',
                type: 'outer',
            }, {
                text: '점퍼',
                value: 'jumper',
                type: 'outer',
            }, {
                text: '코트',
                value: 'coat',
                type: 'outer',
            }, {
                text: '슬리브리스',
                value: 'sleeveless',
                type: 'top',
            }, {
                text: '티셔츠',
                value: 'tshirts',
                type: 'top',
            }, {
                text: '니트',
                value: 'knit',
                type: 'top',
            }, {
                text: '셔츠',
                value: 'shirt',
                type: 'top',
            }, {
                text: '블라우스',
                value: 'blouse',
                type: 'top',
            }, {
                text: '맨투맨',
                value: 'mtm',
                type: 'top',
            }, {
                text: '슬랙스',
                value: 'slacks',
                type: 'pants',
            }, {
                text: '데님',
                value: 'denim',
                type: 'pants',
            }, {
                text: '면',
                value: 'cotton',
                type: 'pants',
            }, {
                text: 'SHORT',
                value: 'shorts',
                type: 'pants',
            }, {
                text: '미니',
                value: 'mini',
                type: 'skirt',
            }, {
                text: '미디/롱',
                value: 'midi-long',
                type: 'skirt',
            }, {
                text: '원피스',
                value: 'ops',
                type: 'dress'
            }, {
                text: '점프수트',
                value: 'jumpSuit',
                type: 'dress'
            }, ];
            for (let i = this.subCategory.length - 1; i > 0; i--) {
                if (this.mainCategory != this.subCategory[i].type) {
                    this.subCategory.splice(i, 1);
                }
            }
        },
        getProductList() {
            axios({
                method: 'get',
                url: `/api/product/getBestProductList`,
                params: {
                    type1: this.mainCategory,
                    type2: this.selectedCategory
                }
            }).then(res => {
                this.bestProducts = res.data;
            }).catch((err) => {
                console.log(err);
            })
            axios({
                method: 'get',
                url: `/api/product/getProductList`,
                params: {
                    page: this.page,
                    perPage: this.itemsPerPage,
                    type1: this.mainCategory,
                    type2: this.selectedCategory,
                    minPrice: 0,
                    maxPrice: 99999999,
                }
            }).then(res => {
                this.products = res.data.productList;
                this.pageLength = Math.ceil(res.data.count / this.page);
            }).catch((err) => {
                console.log(err);
            })
        },
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        moveToDetail(num) {
            this.$router.push(`/productDetail/${num}`)
        },
    },
    watch: {
        '$route'() {
            this.mainCategory = this.$route.params.id;
            this.selectedCategory = this.$route.params.sub;
            this.getProductList();
            this.setSubCategory();
        },
        page: {
            handler() {
                this.getProductList();
            }
        }
    },
    mounted() {
        this.mainCategory = this.$route.params.id;
        this.selectedCategory = this.$route.params.sub;
        this.getProductList();
        this.setSubCategory();
    }
}
</script>

<style>

</style>
