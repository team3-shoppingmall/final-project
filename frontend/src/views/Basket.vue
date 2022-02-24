<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">장바구니</div>
            <v-data-table :headers="headers" :items="baskets" :options.sync="options" :server-items-length="totalDesserts" :loading="loading" hide-default-footer show-select="show-select" item-key="name" class="elevation-1">
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
                            <tr>
                                <td colspan="2" class="text-center">
                                    <v-btn outlined="outlined">옵션변경</v-btn>
                                </td>
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
            <v-row class="my-3 px-16" justify="space-between">
                <v-col cols="auto">
                    <v-row>
                        <v-col>
                            <v-btn class="primary">전체 선택</v-btn>
                        </v-col>
                        <v-col>
                            <v-btn class="primary">선택 삭제하기</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="auto">
                    <v-row>
                        <v-col>
                            <v-btn class="primary">주문하기</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
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
            baskets: [],
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
        getBasket() {
            axios.get(`/api/basket/getBasket/${'tester'}`)
                .then(res => {
                    this.baskets = res.data;
                })
        },
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
    }
}
</script>

<style></style>
