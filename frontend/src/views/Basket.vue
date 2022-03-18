<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">장바구니</div>
            <v-data-table v-model="selected" :headers="headers" :items="baskets" :options.sync="options" :loading="loading" hide-default-footer show-select="show-select" item-key="basketIdx" class="elevation-1" disable-sort no-data-text="장바구니가 비었습니다">
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
                    <v-text-field type="number" min="1" :rules="[numberRule]" v-model="item.basketAmount" @click="amountFilter(item)"></v-text-field>
                    <v-btn color="primary" @click="amountFilter(item)">변경</v-btn>
                </template>
                <template v-slot:[`item.totalPrice`]="{ item }">
                    <div class="text-right" max-width="150">
                        {{AddComma((item.price - item.discount) * item.basketAmount)}}원
                    </div>
                </template>
                <template v-slot:footer="{ }">
                    <v-divider></v-divider>
                    <v-row justify="end" class="ma-1">
                        <v-col cols="auto">
                            상품구매금액 {{AddComma(totalPrice)}} 원
                            + 배송비 <span v-if="totalPrice < 50000 && totalPrice > 0">{{AddComma(2500)}}</span><span v-if="totalPrice >= 50000 || totalPrice == 0">0</span> 원
                            = 합계 : <span v-if="totalPrice < 50000 && totalPrice > 0">{{AddComma(totalPrice+2500)}}</span><span v-if="totalPrice >= 50000 || totalPrice == 0">{{AddComma(totalPrice)}}</span> 원
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>
            <v-row class="my-3 px-16" justify="space-between">
                <v-col cols="auto">
                    <v-row>
                        <v-col>
                            <v-btn class="primary" @click="selectAll">전체 선택</v-btn>
                        </v-col>
                        <v-col>
                            <v-btn class="primary" @click="deleteSelected">선택 삭제하기</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="auto">
                    <v-row>
                        <v-col>
                            <v-btn class="primary" @click="goToOrder">주문하기</v-btn>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-dialog v-model="alertDialog" :persistent="alertPath != null" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
            <v-row justify="end" v-if="alertPath != null">
                <v-col cols="auto" class="pr-1 pb-1">
                    <v-btn text @click="alertDialog=false; alertPath = null; getBasket();">확인</v-btn>
                </v-col>
            </v-row>
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        ProductDetailDisplay,
    },
    name: 'Payment',
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            alertPath: null,
            baskets: [],
            selected: [],
            totalPrice: 0,
            loading: true,
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
            numberRule: val => {
                if (val == '') return '개수를 입력해주세요'
                return true
            },
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getBasket() {
            this.loading = true;
            axios.get(`/api/basket/getBasket/${this.getLogin.user.id}`)
                .then(res => {
                    this.baskets = res.data;
                }).finally(
                    this.loading = false
                )
        },
        amountFilter(item) {
            if (!(item.basketAmount > 0 && item.basketAmount == Math.round(item.basketAmount))) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '잘못된 입력입니다';
                this.alertPath = `/basket`;
                return;
            } else if (item.basketAmount > 9999) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '주문량이 너무 많습니다';
                this.alertPath = `/basket`;
                return;
            } else if (item.basketAmount > item.amount) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '재고보다 주문량이 많습니다';
                this.alertPath = `/basket`;
                return;
            } else {
                axios({
                        method: 'patch',
                        url: `/api/basket/updateBasketAmount`,
                        params: {
                            basketIdx: item.basketIdx,
                            basketAmount: item.basketAmount
                        }
                    })
                    .catch(err => {
                        this.alertDialog = true;
                        this.alertType = 'error';
                        this.alertMessage = '변경에 실패하였습니다';
                        console.log(err);
                    })

            }
        },
        selectAll() {
            if (this.selected.length != this.baskets.length) {
                this.selected = this.baskets;
            } else {
                this.selected = [];
            }
        },
        deleteSelected() {
            let deletes = [];
            for (let i = 0; i < this.selected.length; i++) {
                deletes.push(this.selected[i].basketIdx);
            }
            axios.delete(`/api/basket/deleteBasket`, {
                    data: deletes
                })
                .then(() => {
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '선택한 장바구니가 삭제되었습니다';
                    this.getBasket();
                }).catch(err => {
                    this.alertDialog = true;
                    this.alertType = 'error';
                    this.alertMessage = '삭제에 실패하였습니다';
                    console.log(err);
                })
        },
        goToOrder() {
            if (this.selected.length == 0) {
                this.alertDialog = true;
                this.alertType = 'warning';
                this.alertMessage = '구매할 상품이 없습니다';
                return;
            }
            this.$router.push({
                name: "Payment",
                params: {
                    Payment: this.selected
                }
            });
        }
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        selected: {
            handler() {
                this.totalPrice = 0;
                for (let i = 0; i < this.selected.length; i++) {
                    if (this.selected[i].onSale == false || this.selected[i].amount == 0) {
                        this.selected.splice(i, 1);
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '품절 상품입니다';
                    } else if (this.selected[i].basketAmount > this.selected[i].amount) {
                        this.selected.splice(i, 1);
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '재고보다 주문량이 많습니다';
                    } else {
                        this.totalPrice += (this.selected[i].price - this.selected[i].discount) * this.selected[i].basketAmount;
                    }
                }
            }
        }
    },
    mounted() {
        this.getBasket();
    }
}
</script>

<style></style>
