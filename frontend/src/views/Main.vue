<template>
<v-container class="pt-1">
    <v-row justify="center">
        <v-col class="px-0">
            <v-carousel cycle height="400" hide-delimiter-background show-arrows-on-hover>
                <v-carousel-item v-for="(slide, i) in slides" :key="i">
                    <v-sheet :color="colors[i]" height="100%">
                        <v-row class="fill-height" align="center" justify="center">
                            <div class="text-h2">
                                {{ slide }} Slide
                            </div>
                        </v-row>
                    </v-sheet>
                </v-carousel-item>
            </v-carousel>
        </v-col>
    </v-row>
    <v-row class="text-h2 font-weight-black" justify="center">
        EVENT
    </v-row>
    <v-row class="my-3" justify="center">
        <v-col cols="10">
            <v-row>
                <v-col v-for="(product, idx) in eventProducts" :key="idx" cols="3">
                    <v-card @click="moveToDetail(product.productNo)">
                        <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                        <v-card-text style="height:120px">
                            <div style="height:50px">
                                {{product.productName}}
                                - <span v-if="product.size != null">{{product.size.split(';').length}} size</span>
                                <span v-if="product.size == null">{{product.color.split(';').length}} color</span>
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
    <v-row class="text-h2 font-weight-medium" justify="center">
        WEEKLY BEST
    </v-row>
    <v-row class="my-3" justify="center">
        <v-col cols="9">
            <v-row>
                <v-col v-for="(product, idx) in weeklyBestProducts" :key="idx" cols="3">
                    <v-card @click="moveToDetail(product.productNo)">
                        <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                        <v-card-text style="height:120px">
                            <div style="height:50px">
                                {{product.productName}}
                                - <span v-if="product.size != null">{{product.size.split(';').length}} size</span>
                                <span v-if="product.size == null">{{product.color.split(';').length}} color</span>
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
    <v-row class="text-h2 font-weight-medium" justify="center">
        NEW
    </v-row>
    <v-row class="my-3" justify="center">
        <v-col cols="9">
            <v-row>
                <v-col v-for="(product, idx) in newProducts" :key="idx" cols="3">
                    <v-card @click="moveToDetail(product.productNo)">
                        <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                        <v-card-text style="height:120px">
                            <div style="height:50px">
                                {{product.productName}}
                                - <span v-if="product.size != null">{{product.size.split(';').length}} size</span>
                                <span v-if="product.size == null">{{product.color.split(';').length}} color</span>
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
    <v-row class="text-h2 font-weight-medium" justify="center">
        BEST
    </v-row>
    <v-row class="my-3" justify="center">
        <v-col cols="9">
            <v-row>
                <v-col v-for="(product, idx) in bestProducts" :key="idx" cols="3">
                    <v-card @click="moveToDetail(product.productNo)">
                        <v-img min-height="300" max-height="300" :src="`/api/product/productImage/${product.productNo}/${product.imageName.split(';')[0]}`"></v-img>
                        <v-card-text style="height:120px">
                            <div style="height:50px">
                                {{product.productName}}
                                - <span v-if="product.size != null">{{product.size.split(';').length}} size</span>
                                <span v-if="product.size == null">{{product.color.split(';').length}} color</span>
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
</v-container>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            colors: [
                'indigo',
                'warning',
                'pink darken-2',
                'red lighten-1',
                'deep-purple accent-4',
            ],
            slides: [
                'First',
                'Second',
                'Third',
                'Fourth',
                'Fifth',
            ],
            dataLoaded: false,
            eventProducts: [],
            weeklyBestProducts: [],
            newProducts: [],
            bestProducts: [],
        }
    },
    methods: {
        getProduct() {
            this.dataLoaded = false;
            axios.get(`/api/product/getMainPages`)
                .then(res => {
                    this.eventProducts = res.data.eventList;
                    this.weeklyBestProducts = res.data.weeklyBestList;
                    this.newProducts = res.data.newList;
                    this.bestProducts = res.data.bestList;
                }).finally(
                    this.dataLoaded = true
                )
        },
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        moveToDetail(num) {
            this.$router.push(`/productDetail/${num}`)
        },
    },
    mounted() {
        this.getProduct();
    }
}
</script>

<style>

</style>
