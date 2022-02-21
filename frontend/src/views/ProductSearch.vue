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
                                                    <v-text-field v-model="priceMin" suffix="원"></v-text-field>
                                                </v-col>
                                                <v-col cols="auto" class="mt-5">~</v-col>
                                                <v-col cols="4">
                                                    <v-text-field v-model="priceMax" suffix="원"></v-text-field>
                                                </v-col>
                                            </v-row>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:30%"> 검색 정렬 기준 </td>
                                        <td>
                                            <v-select v-model="orderSelected" :items="searchOrders"></v-select>
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
                        <v-col v-for="count in 20" :key="count" cols="3">
                            <v-card @click="moveToDetail(product[0].productno)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                                <v-card-text>
                                    <div>{{product[0].productname}} - <span v-if="product[0].size != 0">{{product[0].size}} size</span><span v-if="product[0].size==0">{{product[0].color}} color</span></div>
                                    <div v-if="product[0].discount != 0" class="text-decoration-line-through">{{product[0].price}}원</div>
                                    <div v-if="product[0].discount == 0">{{product[0].price}}원</div>
                                    <div v-if="product[0].discount != 0">{{product[0].price*(100-product[0].discount)/100}}원</div>
                                </v-card-text>
                            </v-card>
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
                    value: 'jacket',
                },
                {
                    text: 'OUTER>코트',
                    value: 'coat',
                },
                {
                    text: 'OUTER>가디건',
                    value: 'cardigan',
                },
                {
                    text: 'OUTER>점퍼',
                    value: 'jumper',
                },
                {
                    text: 'SKIRT',
                    value: 'skirt',
                },
                {
                    text: 'SKIRT>미니',
                    value: 'mini',
                },
                {
                    text: 'SKIRT>미디/롱',
                    value: 'midi-long',
                },
            ],
            typeSelected: '',

            productName: '',
            priceMin: 0,
            priceMax: 99999999,

            searchOrders: [{
                    text: '기준 선택',
                    value: '',
                    disabled: true,
                },
                {
                    text: '신상품 순',
                    value: 'newItem',
                },
                {
                    text: '낮은 가격 순',
                    value: 'lowPrice',
                },
                {
                    text: '높은 가격 순',
                    value: 'highPrice',
                },
            ],
            orderSelected: '',

            page: 1,
            pageLength: 7,
            visibleLength: 5,

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
            console.log(this.priceMin);
            console.log(this.priceMax);
            console.log(this.typeSelected);
            console.log(this.orderSelected);
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
