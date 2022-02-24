<template>
<v-container class="mt-5">
    <v-row justify="center">
        <v-col cols="9">
            <v-row style="height:600px">
                <v-col cols="6" class="pa-5">
                    <v-row>
                        <v-col align="center">
                            <v-carousel :show-arrows="false" cycle interval="2000" hide-delimiters>
                                <v-carousel-item v-for="(image,i) in images" :key="i" :src="image.src"></v-carousel-item>
                            </v-carousel>
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="6" class="pa-5">
                    <v-simple-table>
                        <template slot="default">
                            <tbody>
                                <tr>
                                    <td colspan="3">
                                        <div class="text-h6">
                                            {{product.productName}}
                                            - <span v-if="sizeOption != ''">{{sizeOption.length}} size</span>
                                            <span v-if="sizeOption == ''">{{colorOption.length}} color</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:20%"> 가격 </td>
                                    <td colspan="2" v-if="product.discount != 0" class="text-decoration-line-through">
                                        {{product.price}}원
                                    </td>
                                    <td colspan="2" v-if="product.discount == 0">
                                        {{product.price}}원
                                    </td>
                                </tr>
                                <tr v-if="product.discount != 0">
                                    <td style="width:10%"> 할인가 </td>
                                    <td colspan="2">
                                        {{product.price-product.discount}}원
                                    </td>
                                </tr>
                                <tr v-if="colorOption != ''">
                                    <td style="width:10%"> COLOR </td>
                                    <td colspan="2">
                                        <!-- size 있을 때 -->
                                        <v-chip-group v-model="colorSelection" active-class="deep-purple--text text--accent-4" v-if="sizeOption != ''">
                                            <v-chip label outlined v-for="color in colorOption" :key="color" :value="color">
                                                {{ color }}
                                            </v-chip>
                                        </v-chip-group>
                                        <!-- size 없을 때 -->
                                        <v-chip-group active-class="deep-purple--text text--accent-4" v-if="sizeOption == ''">
                                            <v-chip label outlined v-for="color in colorOption" :key="color" :value="color" @click="addSelected(color, null)">
                                                {{ color }}
                                            </v-chip>
                                        </v-chip-group>
                                    </td>
                                </tr>
                                <tr v-if="sizeOption != ''">
                                    <td style="width:10%"> SIZE </td>
                                    <td colspan="2">
                                        <v-chip-group active-class="deep-purple--text text--accent-4">
                                            <v-chip label outlined v-for="size in sizeOption" :key="size" :value="size" :disabled="colorOption != '' && colorSelection == ''" @click="addSelected(colorSelection, size)">
                                                {{ size }}
                                            </v-chip>
                                        </v-chip-group>
                                    </td>
                                </tr>
                                <tr v-for="(option, idx) in selected" :key="idx">
                                    <td colspan="2">
                                        <v-row justify="space-between" align="center">
                                            <v-col cols="9">
                                                <v-row>
                                                    <v-col>
                                                        <div class="text-h6"> {{product.productName}}</div>
                                                        <div v-if="option.selectedSize != undefined && option.selectedColor != undefined" class="text-subtitle-2"> - {{option.selectedColor}}/{{option.selectedSize}}</div>
                                                        <div v-if="option.selectedSize == undefined && option.selectedColor != undefined" class="text-subtitle-2"> - {{option.selectedColor}}</div>
                                                        <div v-if="option.selectedSize != undefined && option.selectedColor == undefined" class="text-subtitle-2"> - {{option.selectedSize}}</div>
                                                    </v-col>
                                                </v-row>
                                            </v-col>
                                            <v-col cols="3">
                                                <v-row>
                                                    <v-col cols="6">
                                                        <v-text-field type="number" min="1" :rules="[numberRule]" v-model="option.amount" @keyup="amountFilter" @click="amountFilter"></v-text-field>
                                                    </v-col>
                                                    <v-col cols="6" class="mt-5">
                                                        <v-icon @click="deleteSelected(idx)">mdi-delete</v-icon>
                                                    </v-col>
                                                </v-row>
                                            </v-col>
                                        </v-row>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        <div class="text-h6">Total Price : {{totalPrice}}원</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        <v-row justify="center">
                                            <v-col cols="auto">
                                                <v-btn @click="buyItNow">
                                                    BUY IT NOW
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-btn @click="addToBasket">
                                                    ADD TO Basket
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-btn @click="addToWishList">
                                                    WISH LIST
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                    </td>
                                </tr>
                            </tbody>
                        </template>
                    </v-simple-table>
                </v-col>
            </v-row>
            <v-divider class="mt-10 md-10 pt-10 pd-10"></v-divider>
            <v-row justify="center" class="pa-5" id="detailSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')" color="primary">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col v-for="(image, idx) in images" :key="idx" cols="9">
                    <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(idx)}/1000/500`"></v-img>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="guideSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')" color="primary">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col cols="10">
                    <Guide />
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="reviewSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')" color="primary">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col cols="10">
                    <ProductDetailReview :productNo="pageID" />
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="qnaSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')">GUIDE</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('review')">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')" color="primary">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col cols="10">
                    <ProductDetailQnA :productNo="pageID" />
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import Guide from '@/components/Guide.vue'
import ProductDetailQnA from '@/components/ProductDetailQnA.vue'
import ProductDetailReview from '@/components/ProductDetailReview.vue'
export default {
    components: {
        Guide,
        ProductDetailQnA,
        ProductDetailReview,
    },
    data() {
        return {
            pageID: '',
            product: '',
            colorOption: '',
            colorSelection: '',
            sizeOption: '',
            selected: [],
            totalPrice: 0,
            number: 0,
            numberRule: val => {
                if (val == '') return '개수를 입력해주세요'
                return true
            },
            images: [{
                    src: 'https://cdn.vuetifyjs.com/images/carousel/squirrel.jpg',
                },
                {
                    src: 'https://cdn.vuetifyjs.com/images/carousel/sky.jpg',
                },
                {
                    src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
                },
                {
                    src: 'https://cdn.vuetifyjs.com/images/carousel/planet.jpg',
                },
            ],
            detailImages: [],

        }
    },
    methods: {
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
        },
        getProudct() {
            axios.get(`/api/product/getProduct/${this.pageID}`).then(res => {
                if (res.status == 200) {
                    this.product = res.data;
                    if (this.product.color != null) {
                        this.colorOption = this.product.color.split(';');
                    }
                    if (this.product.size != null) {
                        this.sizeOption = this.product.size.split(';');
                    }
                } else {
                    console.log(res.status);
                }
            })
        },
        addSelected(color, size) {
            let data = {
                id: 'tester',
                productNo: this.product.productNo,
                selectedColor: color,
                selectedSize: size,
                amount: 1,
                price: this.product.price - this.product.discount
            }
            for (let i = 0; i < this.selected.length; i++) {
                if (this.selected[i].selectedColor == data.selectedColor && this.selected[i].selectedSize == data.selectedSize) {
                    alert('이미 추가한 옵션입니다. 옵션 개수를 조정해주세요.');
                    return;
                }
            }
            this.selected.push(data);
            this.amountFilter();
        },
        deleteSelected(num) {
            this.selected.splice(num, 1);
            this.amountFilter();
        },
        amountFilter() {
            let amount = 0;
            console.log(this.selected);
            for (let i = 0; i < this.selected.length; i++) {
                console.log(this.selected[i].amount);
                if (this.selected[i].amount > 0 && this.selected[i].amount == Math.round(this.selected[i].amount)) {
                    amount += Number(this.selected[i].amount);
                } else {
                    alert('잘못된 입력입니다.');
                    this.selected[i].amount = 1;
                    amount += Number(this.selected[i].amount);
                }
            }
            this.totalPrice = amount * (this.product.price - this.product.discount);
        },
        buyItNow() {
            this.$router.push({
                name: "Payment",
                params: {
                    Payment: this.selected
                }
            });
        },
        addToBasket() {
            axios.post(`/api/basket/insert`, this.selected)
                .then(() => {
                    alert('장바구니에 저장하셨습니다');
                    this.$router.go();
                }).catch((err) => {
                    alert('저장에 실패하셨습니다');
                    console.log(err);
                })
        },
        addToWishList() {
            axios.post(`/api/wishList/insert`, {
                    id: 'tester',
                    productNo: this.pageID
                })
                .then(() => {
                    alert('관심 상품에 저장하셨습니다');
                }).catch((err) => {
                    alert('이미 추가된 상품입니다');
                    console.log(err);
                })
        },
        scrollTo(tag) {
            let scroll = 0;
            if (tag == 'detail') {
                scroll = document.querySelector("#detailSelected").offsetTop;
            } else if (tag == 'guide') {
                scroll = document.querySelector("#guideSelected").offsetTop;
            } else if (tag == 'review') {
                scroll = document.querySelector("#reviewSelected").offsetTop;
            } else if (tag == 'qna') {
                scroll = document.querySelector("#qnaSelected").offsetTop;
            }
            window.scrollTo({
                top: scroll,
                behavior: 'smooth'
            });
        },
    },
    mounted() {
        this.pageID = this.$route.params.id;
        this.getProudct();
    }
}
</script>

<style>

</style>
