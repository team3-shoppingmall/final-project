<template>
<v-container class="mt-5">
    <v-row justify="center">
        <v-col cols="9">
            <v-row>
                <v-col cols="6" class="pa-5">
                    <v-img max-height="500" max-width="auto" :src="`https://picsum.photos/seed/1/500/500`"></v-img>
                </v-col>
                <v-col cols="6" class="pa-5">
                    <v-simple-table>
                        <template slot="default">
                            <tbody>
                                <tr>
                                    <td colspan="3">
                                        {{product.productname}}
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
                                        {{product.price*(100-product.discount)/100}}원
                                    </td>
                                </tr>
                                <tr v-if="colorOption != []">
                                    <td style="width:10%"> COLOR </td>
                                    <td colspan="2">
                                        <span v-for="color in colorOption" :key="color">
                                            <v-btn>{{color}}</v-btn>
                                        </span>
                                    </td>
                                </tr>
                                <tr v-if="sizeOption != []">
                                    <td style="width:10%"> SIZE </td>
                                    <td colspan="2">
                                        <span v-for="size in sizeOption" :key="size">
                                            <v-btn>{{size}}</v-btn>
                                        </span>
                                    </td>
                                </tr>
                                <tr v-for="(option, idx) in selected" :key="idx">
                                    <td colspan="2">
                                        <v-row justify="space-between" align="center">
                                            <v-col cols="9">
                                                <v-row>
                                                    <v-col>
                                                        <div class="text-h6"> {{option.productName}}</div>
                                                        <div v-if="option.sizeSelected != undefined && option.colorSelected != undefined" class="text-subtitle-2"> - {{option.colorSelected}}/{{option.sizeSelected}}</div>
                                                        <div v-if="option.sizeSelected == undefined && option.colorSelected != undefined" class="text-subtitle-2"> - {{option.colorSelected}}</div>
                                                        <div v-if="option.sizeSelected != undefined && option.colorSelected == undefined" class="text-subtitle-2"> - {{option.sizeSelected}}</div>
                                                    </v-col>
                                                </v-row>
                                            </v-col>
                                            <v-col cols="3">
                                                <v-row>
                                                    <v-col cols="6">
                                                        <v-text-field type="number" min="1" :rules="[numberRule]" v-model="option.amount" @keyup="amountFilter"></v-text-field>
                                                    </v-col>
                                                    <v-col cols="6" class="mt-5">
                                                        <v-icon @click="deleteOption(idx)">mdi-delete</v-icon>
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
                                                <v-btn>
                                                    BUY IT NOW
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-btn>
                                                    ADD TO CART
                                                </v-btn>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-btn>
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

            <v-row class="text-h4 mt-10" justify="center">
                Recommended Items
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row justify="center">
                        <v-col v-for="count in 5" :key="count" cols="2">
                            <v-card @click="moveToDetail(products[0].productno)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(count)}/200/150`"></v-img>
                                <v-card-text>
                                    <div>{{products[0].productname}} - <span v-if="products[0].size != 0">{{products[0].size}} size</span><span v-if="products[0].size==0">{{products[0].color}} color</span></div>
                                    <div v-if="products[0].discount != 0" class="text-decoration-line-through">{{products[0].price}}원</div>
                                    <div v-if="products[0].discount == 0">{{products[0].price}}원</div>
                                    <div v-if="products[0].discount != 0">{{products[0].price*(100-products[0].discount)/100}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>

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
                <v-col cols="9">
                    <v-simple-table>
                        <template slot="default">
                            <tbody>
                                <tr>
                                    <td colspan="2"></td>
                                </tr>
                                <tr>
                                    <td style="width:20%" class="pa-5"> 상품 결제정보 </td>
                                    <td class="pa-5">
                                        <div>
                                            <p>고액결제의 경우 안전을 위해 카드사에서 확인전화를 드릴 수도 있습니다. 확인과정에서 도난 카드의 사용이나 타인 명의의 주문등 정상적인 주문이 아니라고 판단될 경우 임의로 주문을 보류 또는 취소할 수 있습니다.</p>

                                            <p>무통장 입금은 상품 구매 대금은 PC뱅킹, 인터넷뱅킹, 텔레뱅킹 혹은 가까운 은행에서 직접 입금하시면 됩니다.<br>
                                                주문 시 입력한 입금자명과 실제입금자의 성명이 반드시 일치하여야 하며, 7일 이내로 입금을 하셔야 하며 입금되지 않은 주문은 자동취소 됩니다.</p>

                                            <p>A/S 책임자 : 070-1234-5678 Spring 고객센터<br>
                                                품질보증기준: 전자상거래 소비자 보호법에 의거하여 소비자 청약철회 가능한 기준에 따름.<br>
                                                구매자가 미성년자일 경우 법정 대리인이 계약에 동의하지 않을 때 구매를 취소할 수 있습니다.</p>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:20%" class="pa-5"> 배송정보 </td>
                                    <td class="pa-5">
                                        <div>
                                            <p>Spring은 대한민국 택배 no.1 우체국택배를 이용하여<br>
                                                가장 안전하고, 신속하게 배송하여 드립니다 :)<br>
                                                대부분 출고 다음날에 바로 도착하며 (주말 제외)<br>
                                                지역 택배 기사님들의 일정과 기상상황에 따라 변동이 있을 수 있습니다.<br>
                                                기본 배송 준비일은 입고지연 상품 제외, 2~5일 정도가 소요되고 있습니다.</p>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:20%" class="pa-5"> 교환 및 반품정보 </td>
                                    <td class="pa-5">
                                        <div>
                                            <p>[교환/반품 안내]<br>
                                                물품 수령 후(택배 도착일자 기준) 7일 이내에 Q&amp;A "배송 후 교환/반품" 게시판 또는 고객센터 [070-1234-5678]
                                                로 반드시 접수 해주세요. 글쓰기시 양식이 자동으로 등록되어 있으며, 사전에 신청해 주신 상품에 한해서만 교환/반품이 가능합니다.<br>
                                                접수 시 Spring에서 우체국 택배 회수접수를 도와드리고 있습니다.
                                            </p>

                                            <p>
                                                *패킹하여 보내실 때는 물품 수령시와 동일하게 포장해 주세요.<br>
                                                택에 손상이 있는 경우에는 반품과 교환이 모두 불가합니다. <br>
                                                성함,주소,전화번호,보내시는 상품,사유등 반품카드 양식에 맞게 적어 보내주셔야 처리가 가능합니다.
                                            </p>

                                            <p>보내시는 주소지 : 서울시 동대문구 천호대로4 동대문우체국 소포실 물류창고</p>

                                            <p>
                                                &lt;교환반품 불가사항&gt; <br>
                                                - 상품 수령 후 7일 이상 경과된 경우 <br>
                                                - 상품 구매시 교환/환불 불가능이 명시되어 있는경우 <br>
                                                - 사용 흔적(집냄새,향수냄새,체취) / 텍 제거 및 바코드 훼손, 오염이 발견된 상품 <br>
                                                - 세일상품
                                            </p>

                                            <p>-배송시 생긴 구김, 마감 박음질, 제작과정에서 발생하는 냄새나 초크자국 등 대량생산으로 인해 생긴 사유는 불량으로 간주되지 않습니다.</p>

                                            <p>*불량상품의 재발송 시 왕복배송비는 Spring이 부담합니다.</p>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2"></td>
                                </tr>
                            </tbody>
                        </template>
                    </v-simple-table>
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

            <v-row>
                <v-col>
                    <ProductDetailReview />
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

            <v-row>
                <v-col>
                    <ProductDetailQnA />
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
// import axios from 'axios'
import ProductDetailReview from '@/components/ProductDetailReview.vue'
import ProductDetailQnA from '@/components/ProductDetailQnA.vue'
export default {
    components: {
        ProductDetailReview,
        ProductDetailQnA,
    },
    data() {
        return {
            products: [{
                productno: 1,
                imageName: '',
                productname: '블랙트위드 스커트',
                size: 3,
                color: 4,
                price: 20000,
                discount: 5,
            }],
            product: '',
            colorOption: ['빨강', '화이트'],
            sizeOption: ['S', 'M', 'L'],
            images: [{}, {}, {}, {}, {}, {}, {}, {}, ],
            selected: [{
                productName: '블랙트위드 스커트',
                price: 10000,
                colorSelected: 'red',
                sizeSelected: 'S',
                amount: 1,
            }],
            totalPrice: 0,
            number: 0,
            numberRule: val => {
                if (val == '') return '개수를 입력해주세요'
                return true
            }

        }
    },
    methods: {
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
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
        amountFilter(event) {
            let number = event.target.value;
            console.log(number);
            number = Math.round(number);
            if (!(number > 0) || number != event.target.value) {
                alert('잘못된 입력입니다.');
                event.target.value = '';
            }
        },
    },
    watch: {
        selected: {
            Handler() {
                let amount = 0;
                for (let i = 0; i < this.selected.length; i++) {
                    amount += this.selected[i].amount;
                }
                this.totalPrice = amount * this.product.price * (100 - this.product.discount) / 100;
            }
        }
    }
}
</script>

<style>

</style>
