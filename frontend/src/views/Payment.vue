<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">ORDER</div>
            <v-data-table :headers="headers" :items="selected" hide-default-footer item-key="idx" class="elevation-1" disable-sort no-data-text="데이터가 없습니다.">
                <template v-slot:[`item.info`]="{ item }">
                    <div class="text-left">
                        <ProductDetailDisplay :productNo="item.productNo" />
                    </div>
                </template>
                <template v-slot:[`item.selectedColor`]="{ item }">
                    {{item.selectedColor}}
                </template>
                <template v-slot:[`item.selectedSize`]="{ item }">
                    {{item.selectedSize}}
                </template>
                <template v-slot:[`item.basketAmount`]="{ item }">
                    {{item.basketAmount}}
                </template>
                <template v-slot:[`item.totalPrice`]="{ item }">
                    <div class="text-right" max-width="150">
                        {{AddComma((item.price - item.discount) * item.basketAmount)}}원
                    </div>
                </template>
                <template v-slot:footer="{ }">
                    <v-divider></v-divider>
                    <v-row justify="end">
                        <v-col cols="auto">
                            상품구매금액 {{AddComma(totalPrice)}} 원
                            + 배송비 <span v-if="totalPrice<50000">{{AddComma(2500)}}</span><span v-if="totalPrice>=50000">0</span> 원
                            = 합계 : <span v-if="totalPrice<50000">{{AddComma(totalPrice+2500)}}</span><span v-if="totalPrice>=50000">{{AddComma(totalPrice)}}</span> 원
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>

            <v-row>
                <v-col cols="7">
                    <div class="text-t5 mt-10 mb-5">주문 정보</div>
                    <v-form ref="form">
                        <v-simple-table>
                            <template slot="default">
                                <tbody>
                                    <tr>
                                        <td style="width:20%"> 주문하시는 분 </td>
                                        <td>
                                            <v-text-field v-model="memberInfo.name" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 주소 </td>
                                        <td>
                                            <div class="d-flex mt-2">
                                                <v-text-field v-model="memberInfo.zipcode" outlined hide-details label="우편번호" dense></v-text-field>
                                                <v-btn color="primary" class="mx-1">검색</v-btn>
                                            </div>

                                            <v-text-field v-model="memberInfo.addr1" outlined hide-details label="기본주소" dense class="mt-2"></v-text-field>
                                            <v-text-field v-model="memberInfo.addr2" outlined hide-details label="상세주소" dense class="mt-2"></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 전화번호 </td>
                                        <td>
                                            <v-text-field v-model="memberInfo.tel" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 이메일 </td>
                                        <td>
                                            <v-text-field v-model="memberInfo.email" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                </tbody>
                            </template>
                        </v-simple-table>
                    </v-form>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="7">
                    <div class="text-t6 mt-10">배송 정보</div>
                    <v-form ref="form">
                        <v-simple-table>
                            <template slot="default">
                                <tbody>
                                    <tr>
                                        <td style="width: 20%"> 배송지 선택 </td>
                                        <td>
                                            <v-radio-group row v-model="deliverySelect">
                                                <v-radio label="주문자 정보와 동일" :value="true"></v-radio>
                                                <v-radio label="새로운 배송지" :value="false"></v-radio>
                                            </v-radio-group>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 받으시는 분 </td>
                                        <td>
                                            <v-text-field v-model="delivery.name" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 주소 </td>
                                        <td>
                                            <div class="d-flex ">
                                                <v-text-field v-model="delivery.zipcode" outlined hide-details label="우편번호" dense class="mt-2"></v-text-field>
                                                <v-btn class="align-self-center ml-2 py-3 px-1 primary" height="100%" style="font-size:1.2rem">검색</v-btn>
                                            </div>

                                            <v-text-field v-model="delivery.addr1" outlined hide-details label="기본주소" dense class="mt-2"></v-text-field>
                                            <v-text-field v-model="delivery.addr2" outlined hide-details label="상세주소" dense class="mt-2"></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 전화번호 </td>
                                        <td>
                                            <v-text-field v-model="delivery.tel" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 배송메세지 </td>
                                        <td>
                                            <v-text-field v-model="deliveryMessage" outlined hide-details dense></v-text-field>
                                        </td>
                                    </tr>
                                </tbody>
                            </template>
                        </v-simple-table>
                    </v-form>
                </v-col>
            </v-row>

            <div class="text-t6 mt-10">결제 예정 금액</div>
            <v-simple-table>
                <tbody>
                    <tr>
                        <td>총 상품금액</td>
                        <td>배송비</td>
                        <td>결제 예정 금액</td>
                    </tr>
                    <tr>
                        <td>{{AddComma(totalPrice)}} 원</td>
                        <td><span v-if="totalPrice<50000">{{AddComma(2500)}}</span><span v-if="totalPrice>=50000">0</span> 원</td>
                        <td><span v-if="totalPrice<50000">{{AddComma(totalPrice+2500)}}</span><span v-if="totalPrice>=50000">{{AddComma(totalPrice)}}</span> 원</td>
                    </tr>
                    <tr>적립금</tr>
                </tbody>
            </v-simple-table>

            <div class="text-t6 mt-10">결제 수단</div>
            <v-simple-table>
                <tbody>
                    <tr>
                        <td>
                            <v-radio-group v-model="payMethod" row>
                                <v-radio label="무통장 입금" value="cash"></v-radio>
                                <v-radio label="신용카드/체크카드" value="credit"></v-radio>
                            </v-radio-group>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            결제 버튼 등
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
export default {
    components: {
        ProductDetailDisplay,
    },
    name: 'Payment',
    data() {
        return {
            selected: [],
            totalPrice: 0,
            loading: false,
            options: {
                itemsPerPage: 50,
            },
            headers: [{
                text: '상품정보',
                value: 'info',
                divider: true,
                align: 'center',
                width: '55%',
            }, {
                text: '색상',
                value: 'selectedColor',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '사이즈',
                value: 'selectedSize',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '개수',
                value: 'basketAmount',
                divider: true,
                align: 'center',
                width: '10%',
            }, {
                text: '합계',
                value: 'totalPrice',
                divider: true,
                align: 'center',
                width: '15%',
            }, ],

            memberInfo: '',

            deliverySelect: false,
            delivery: '',
            deliveryMessage: '',
            payMethod: 'credit',

            id: 'tester'
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getMember() {
            axios.get(`/api/member/getMemberInfo/${this.id}`)
                .then(res => {
                    this.memberInfo = res.data;
                })
        }
    },
    watch: {
        deliverySelect: {
            handler() {
                if (this.deliverySelect == true) {
                    this.delivery = this.memberInfo;
                } else {
                    this.delivery = {
                        name: '',
                        zipCode: '',
                        address: '',
                        detailAddr: '',
                        tel: '',
                        message: '',
                    };
                }
            }
        }
    },
    mounted() {
        this.selected = this.$route.params.Payment;
        console.log(this.selected);
        this.getMember();
        for (let i = 0; i < this.selected.length; i++) {
            this.totalPrice += (this.selected[i].price - this.selected[i].discount) * this.selected[i].basketAmount;
        }
    }
}
</script>

<style></style>
