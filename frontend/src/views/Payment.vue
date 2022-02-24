<template>
<v-container style="min-height: 910px;">
    {{selected}}
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">ORDER</div>
            <v-data-table :headers="headers" :items="selected" :options.sync="options" :server-items-length="totalDesserts" :loading="loading" hide-default-footer show-select="show-select" item-key="name" class="elevation-1">
                <template v-slot:[`item.info`]="{ item }">
                    <ProductDetailDisplay :productNo="item.productNo" />
                </template>
                <template v-slot:[`item.option`]="{ item }">
                    <v-simple-table>
                        <tbody>
                            <tr>
                                <td>색상</td>
                                <td>{{item.selectedColor}}</td>
                            </tr>
                            <tr>
                                <td>사이즈</td>
                                <td>{{item.selectedSize}}</td>
                            </tr>
                            <tr>
                                <td>개수</td>
                                <td>{{item.amount}}</td>
                            </tr>
                        </tbody>
                    </v-simple-table>
                </template>
                <template v-slot:[`item.totalPrice`]="{ item }">
                    {{item.price * item.amount}}
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

            <div class="text-t6 mt-10">주문 정보</div>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td> 주문하시는 분 </td>
                                <td>
                                    <v-text-field v-model="name" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 주소 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="zipcode" outlined hide-details label="우편번호" dense></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-3 px-1 primary" height="100%" style="font-size:1.2rem">검색</v-btn>
                                    </div>

                                    <v-text-field v-model="addr1" outlined hide-details label="기본주소" dense></v-text-field>
                                    <v-text-field v-model="addr2" outlined hide-details label="상세주소" dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="tel" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 이메일 </td>
                                <td>
                                    <v-text-field v-model="email" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>

            <div class="text-t6 mt-10">배송 정보</div>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td> 배송지 선택 </td>
                                <td>
                                    <v-radio-group v-model="secret" row>
                                        <v-radio label="주문자 정보와 동일" :value="true"></v-radio>
                                        <v-radio label="새로운 배송지" :value="false"></v-radio>
                                    </v-radio-group>
                                </td>
                            </tr>
                            <tr>
                                <td> 받으시는 분 </td>
                                <td>
                                    <v-text-field v-model="name" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 주소 </td>
                                <td>
                                    <div class="d-flex ">
                                        <v-text-field v-model="zipcode" outlined hide-details label="우편번호" dense></v-text-field>
                                        <v-btn class="align-self-center ml-2 py-3 px-1 primary" height="100%" style="font-size:1.2rem">검색</v-btn>
                                    </div>

                                    <v-text-field v-model="addr1" outlined hide-details label="기본주소" dense></v-text-field>
                                    <v-text-field v-model="addr2" outlined hide-details label="상세주소" dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="tel" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 배송메세지 </td>
                                <td>
                                    <v-text-field v-model="email" outlined hide-details dense></v-text-field>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>

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
                            <v-radio-group v-model="secret" row>
                                <v-radio label="주문자 정보와 동일" :value="true"></v-radio>
                                <v-radio label="새로운 배송지" :value="false"></v-radio>
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
// import axios from 'axios'
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
            totalDesserts: 1,
            loading: false,
            options: {
                itemsPerPage: 50,
            },
            headers: [{
                text: '상품정보',
                value: 'info'
            }, {
                text: '선택 옵션',
                value: 'option'
            }, {
                text: '합계',
                value: 'totalPrice'
            }, ]
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        test() {
            console.log("test")
        }
        // selectAll(event) {     let checkAll = event.value;     if (checkAll) for (let
        // i = 0; i < this.desserts.length; i++) { this.desserts[i].isCheck = true; }
        // else         for (let i = 0; i < this.desserts.length; i++) {
        // this.desserts[i].isCheck = false; } },
    },
    mounted() {
        this.selected = this.$route.params.Payment;
        for (let i = 0; i < this.selected.length; i++) {
            this.totalPrice += this.selected[i].price * this.selected[i].amount;
        }
    }
}
</script>

<style></style>
