<template>
<v-container class="mt-5">
    <v-row justify="center" v-if="dataLoaded">
        <v-col cols="9">
            <v-row>
                <v-col cols="6" class="pa-5">
                    <v-row>
                        <v-col align="center">
                            <v-carousel :show-arrows="false" cycle interval="2000" hide-delimiters>
                                <v-carousel-item v-for="(image,i) in images" :key="i" :src="`/api/product/productImage/${pageID}/${image}`" contain></v-carousel-item>
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
                                            - <span v-if="sizeOption != null">{{sizeOption.length}} size</span>
                                            <span v-if="sizeOption == null">{{colorOption.length}} color</span>
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
                                <tr v-if="colorOption != null">
                                    <td style="width:10%"> COLOR </td>
                                    <td colspan="2">
                                        <!-- size 있을 때 -->
                                        <v-chip-group v-model="colorSelection" active-class="deep-purple--text text--accent-4" v-if="sizeOption != null">
                                            <v-chip label outlined v-for="color in colorOption" :key="color" :value="color" :disabled="product.amount == 0 || product.onSale == false">
                                                {{ color }}
                                            </v-chip>
                                        </v-chip-group>
                                        <!-- size 없을 때 -->
                                        <v-chip-group active-class="deep-purple--text text--accent-4" v-if="sizeOption == null">
                                            <v-chip label outlined v-for="color in colorOption" :key="color" :value="color" @click="addSelected(color, null)" :disabled="product.amount == 0 || product.onSale == false">
                                                {{ color }}
                                            </v-chip>
                                        </v-chip-group>
                                    </td>
                                </tr>
                                <tr v-if="sizeOption != null">
                                    <td style="width:10%"> SIZE </td>
                                    <td colspan="2">
                                        <v-chip-group active-class="deep-purple--text text--accent-4" v-model="sizeSelection">
                                            <v-chip label outlined v-for="size in sizeOption" :key="size" :value="size" :disabled="(colorOption != null && colorSelection == null) || product.amount == 0 || product.onSale == false" @click="addSelected(colorSelection, size)">
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
                                                        <v-text-field type="number" min="1" :rules="[numberRule]" v-model="option.basketAmount" @keyup="amountFilter" @click="amountFilter"></v-text-field>
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
                                        <v-row justify="center" v-if="getLogin == null || getLogin.user.authority == 'ROLE_USER'">
                                            <v-col cols="auto" v-if="product.amount > 0 && product.onSale == true">
                                                <v-btn color="primary" @click="buyItNow">
                                                    BUY IT NOW
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto" v-if="product.amount > 0 && product.onSale == true">
                                                <v-btn color="primary" @click="addToBasket">
                                                    ADD TO Basket
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto" v-if="product.amount == 0 || product.onSale == false">
                                                <v-btn color="primary">
                                                    Sold Out
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-btn color="primary" @click="addToWishList">
                                                    WISH LIST
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                        <v-row justify="center" v-if="getLogin!= null && getLogin.user.authority == 'ROLE_ADMIN'">
                                            <v-col cols="auto">
                                                <v-btn color="primary" :to="`/admin/productManage`">
                                                    관리 페이지로 이동
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
                    <v-btn @click="scrollTo('detail')" color="secondary">DETAIL</v-btn>
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
                <v-col v-for="(image, idx) in detailImages" :key="idx" cols="9">
                    <v-lazy :options="{threshold: .5}" min-height="200" transition="fade-transition">
                        <v-img max-height="auto" max-width="auto" :src="`/api/product/detailImage/${pageID}/${image}`"></v-img>
                    </v-lazy>
                </v-col>
            </v-row>

            <v-row justify="center" class="pa-5" id="guideSelected">
                <v-col cols="auto">
                    <v-btn @click="scrollTo('detail')">DETAIL</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('guide')" color="secondary">GUIDE</v-btn>
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
                    <v-lazy :options="{threshold: .5}" min-height="200" transition="fade-transition">
                        <Guide />
                    </v-lazy>
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
                    <v-btn @click="scrollTo('review')" color="secondary">REVIEW</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn @click="scrollTo('qna')">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col cols="10">
                    <v-lazy v-model="isActive" :options="{threshold: .5}" min-height="1210" transition="fade-transition">
                        <ProductDetailReview :productNo="pageID" />
                    </v-lazy>
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
                    <v-btn @click="scrollTo('qna')" color="secondary">QNA</v-btn>
                </v-col>
            </v-row>

            <v-row justify="center">
                <v-col cols="10">
                    <v-lazy v-model="isActive" :options="{threshold: .5}" min-height="200" transition="fade-transition">
                        <ProductDetailQnA :productNo="pageID" />
                    </v-lazy>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" persistent max-width="420">
        <v-card>
            <v-card-title class="text-body-1">
                장바구니 담기
            </v-card-title>
            <v-card-text v-if="overlap == 0">
                선택하신 상품이 장바구니에 저장되었습니다.
            </v-card-text>
            <v-card-text v-else>
                중복된 {{overlap}}개의 상품을 제외하고 장바구니에 저장되었습니다.
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" :to="`/basket`">
                    장바구니로 이동
                </v-btn>
                <v-btn color="white" @click="reset">
                    계속 쇼핑하기
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    <v-dialog v-model="alertDialog" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
import Guide from '@/components/Guide.vue'
import ProductDetailQnA from '@/components/ProductDetailQnA.vue'
import ProductDetailReview from '@/components/ProductDetailReview.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        Guide,
        ProductDetailQnA,
        ProductDetailReview,
    },
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            dataLoaded: false,
            isActive: false,
            pageID: null,
            product: null,
            colorOption: null,
            colorSelection: null,
            sizeOption: null,
            sizeSelection: null,
            selected: [],
            images: [],
            detailImages: [],
            totalPrice: 0,
            numberRule: val => {
                if (val == '') return '개수를 입력해주세요'
                return true
            },

            dialog: false,
            overlap: 0,
        }
    },
    methods: {
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
        },
        getProduct() {
            this.dataLoaded = false;
            axios.get(`/api/product/getProduct/${this.pageID}`).then(res => {
                this.product = res.data;
                if (this.product.color != null) {
                    this.colorOption = this.product.color.split(';');
                }
                if (this.product.size != null) {
                    this.sizeOption = this.product.size.split(';');
                }
                this.images = this.product.imageName.split(';');
                this.detailImages = this.product.detailImageName.split(';');
                this.dataLoaded = true;
            }).catch(err => {
                console.log(err);
                this.dataLoaded = true;
            })
        },
        addSelected(color, size) {
            let data = {
                id: this.getLogin.user.id,
                productNo: this.product.productNo,
                selectedColor: color,
                selectedSize: size,
                basketAmount: 1,
                price: this.product.price,
                discount: this.product.discount
            }
            for (let i = 0; i < this.selected.length; i++) {
                if (this.selected[i].selectedColor == data.selectedColor && this.selected[i].selectedSize == data.selectedSize) {
                    this.alertDialog = true;
                    this.alertType = 'warning';
                    this.alertMessage = '이미 추가한 옵션입니다. 옵션 개수를 조정해주세요';
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
            for (let i = 0; i < this.selected.length; i++) {
                if (this.selected[i].basketAmount > 0 && this.selected[i].basketAmount == Math.round(this.selected[i].basketAmount)) {
                    amount += Number(this.selected[i].basketAmount);
                } else {
                    this.alertDialog = true;
                    this.alertType = 'warning';
                    this.alertMessage = '잘못된 입력입니다';
                    this.selected[i].basketAmount = 1;
                    amount += Number(this.selected[i].basketAmount);
                }
            }
            this.totalPrice = amount * (this.product.price - this.product.discount);
        },
        buyItNow() {
            if (this.getLogin == null) {
                this.$router.push({
                    name: 'SignIn',
                    params: {
                        nextPage: this.$route.path
                    }
                });
            } else if (this.selected.length == 0) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '구매할 상품이 없습니다';
                return;
            } else {
                this.$router.push({
                    name: "Payment",
                    params: {
                        Payment: this.selected
                    }
                });
            }
        },
        addToBasket() {
            if (this.getLogin == null) {
                this.$router.push({
                    name: 'SignIn',
                    params: {
                        nextPage: this.$route.path
                    }
                });
            } else if (this.selected.length == 0) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '장바구니에 추가할 상품이 없습니다';
                return;
            } else {
                axios.get(`/api/basket/getBasketCount/${this.getLogin.user.id}`)
                    .then(res => {
                        if (res.data + this.selected.length > 50) {
                            this.alertDialog = true;
                            this.alertType = 'warning';
                            this.alertMessage = '장바구니에는 50개까지만 저장이 가능합니다';
                        } else {
                            axios.post(`/api/basket/insert`, this.selected)
                                .then(res => {
                                    this.overlap = res.data;
                                    this.dialog = true;
                                }).catch((err) => {
                                    this.alertDialog = true;
                                    this.alertType = 'error';
                                    this.alertMessage = '저장에 실패하셨습니다';
                                    console.log(err);
                                })
                        }
                    })
            }
        },
        reset() {
            this.colorSelection = null;
            this.sizeSelection = null;
            this.selected = [];
            this.totalPrice = 0;
            this.dialog = false;
        },
        addToWishList() {
            if (this.getLogin == null) {
                this.$router.push({
                    name: 'SignIn',
                    params: {
                        nextPage: this.$route.path
                    }
                });
            } else {
                axios.post(`/api/wishList/insert`, {
                        id: this.getLogin.user.id,
                        productNo: this.pageID
                    })
                    .then(() => {
                        this.alertDialog = true;
                        this.alertType = 'success';
                        this.alertMessage = '관심 상품에 저장하셨습니다';
                    }).catch((err) => {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '이미 추가된 상품입니다';
                        console.log(err);
                    })
            }
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
            });
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        '$route'() {
            this.pageID = this.$route.params.id;
            this.getProduct();
        }
    },
    mounted() {
        this.pageID = this.$route.params.id;
        this.getProduct();

    }
}
</script>

<style>

</style>
