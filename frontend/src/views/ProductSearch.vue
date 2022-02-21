<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <v-row class="text-h3 md-10">
                SEARCH ITEMS
            </v-row>
            <v-row justify="center">
                <v-col cols="6">
                    <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td style="width:20%"> 상품 분류 </td>
                                <td>
                                    <v-select v-model="titleSelected" :items="titles"></v-select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:20%"> 상품명 </td>
                                <td>
                                    <v-text-field v-model="titleDetail"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:20%"> 판매 가격대 </td>
                                <td>
                                    <v-row justify="center">
                                        <v-col cols="5">
                                            <v-text-field v-model="titleDetail"></v-text-field>
                                        </v-col>
                                        <v-col cols="auto" class="mt-5">~</v-col>
                                        <v-col cols="5">
                                            <v-text-field v-model="titleDetail"></v-text-field>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:20%"> 검색 정렬 기준 </td>
                                <td>
                                    <v-select v-model="titleSelected" :items="titles"></v-select>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
                <v-row justify="center" class="mt-3">
                    <v-col cols="auto" v-if="num == '' || num == undefined">
                        <v-btn @click="form">검색</v-btn>
                    </v-col>
                </v-row>
            </v-form>
                </v-col>
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="count in 20" :key="count" cols="3">
                            <v-card>
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/300/250`"></v-img>
                                <v-card-text>
                                    <div>{{product[0].productname}} - <span v-if="product[0].size != 0">{{product[0].size}} size</span><span v-if="product[0].size==0">{{product[0].color}} color</span></div>
                                    <div v-if="discount != 0" class="text-decoration-line-through">{{product[0].price}}원</div>
                                    <div v-if="discount == 0">{{product[0].price}}원</div>
                                    <div>{{product[0].price*(100-product[0].discount)/100}}원</div>
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
            mainCategory: 'SKIRT',
            subCategory: [{
                    text: '미니',
                    value: 'mini'
                },
                {
                    text: '미디/롱',
                    value: 'midi-long'
                }
            ],
            selectedCategory: 'all',

            page: 1,
            pageLength: 7,
            visibleLength: 5,

            product: [{
                imageName: '',
                productname: '블랙트위드 스커트',
                size: 3,
                color: 4,
                price: 20000,
                discount: 5,
            }]

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

<style>

</style>
