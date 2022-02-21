<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <v-row class="text-h4">
                Best Items
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="count in 8" :key="count" cols="3">
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
            <v-divider></v-divider>
            <v-row class="text-h4 mt-10">
                {{mainCategory}}
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-btn @click="search('all')" :color="colorPicker('all')">
                        ALL
                    </v-btn>
                </v-col>
                <v-col cols="auto" v-for="category in subCategory" :key="category.value">
                    <v-btn @click="search(category.value)" :color="colorPicker(category.value)">
                        {{category.text}}
                    </v-btn>
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
                productno: 1,
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
        moveToDetail(num) {
            this.$router.push(`/productDetail/${num}`)
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
