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
                    <v-row justify="end" class="ma-1">
                        <v-col cols="auto">
                            상품구매금액 {{AddComma(totalPrice - totalDiscount)}} 원
                            + 배송비 <span v-if="totalPrice - totalDiscount<50000">{{AddComma(2500)}}</span><span v-if="totalPrice - totalDiscount>=50000">0</span> 원
                            = 합계 : <span v-if="totalPrice - totalDiscount<50000">{{AddComma(totalPrice - totalDiscount+2500)}}</span><span v-if="totalPrice - totalDiscount>=50000">{{AddComma(totalPrice - totalDiscount)}}</span> 원
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
                                                <v-text-field v-model="memberInfo.zipcode" outlined hide-details label="우편번호" dense type="number" @click="execDaumPostcode('order')" hide-spin-buttons readonly></v-text-field>
                                                <v-btn color="primary" class="mx-1" @click="execDaumPostcode('order')">검색</v-btn>
                                            </div>

                                            <v-text-field v-model="memberInfo.addr1" outlined hide-details label="기본주소" dense class="mt-2" @click="execDaumPostcode('order')" readonly></v-text-field>
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
                                            <v-text-field v-model="delivery.name" outlined hide-details dense :readonly="deliverySelect"></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 주소 </td>
                                        <td>
                                            <div class="d-flex ">
                                                <v-text-field v-model="delivery.zipcode" outlined hide-details label="우편번호" dense type="number" @click="deliverySelect ? '' : execDaumPostcode('delivery')" hide-spin-buttons readonly></v-text-field>
                                                <v-btn color="primary" class="mx-1" @click="deliverySelect ? '' : execDaumPostcode('delivery')">검색</v-btn>
                                            </div>

                                            <v-text-field v-model="delivery.addr1" outlined hide-details label="기본주소" dense class="mt-2" @click="deliverySelect ? '' : execDaumPostcode('delivery')" readonly></v-text-field>
                                            <v-text-field v-model="delivery.addr2" outlined hide-details label="상세주소" dense class="mt-2" :readonly="deliverySelect"></v-text-field>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td> 전화번호 </td>
                                        <td>
                                            <v-text-field v-model="delivery.tel" outlined hide-details dense :readonly="deliverySelect"></v-text-field>
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
                        <td style="width:33%; text-align: center;" class="v-data-table__divider text-body-1">총 주문 금액</td>
                        <td style="width:33%; text-align: center;" class="v-data-table__divider text-body-1">총 할인 금액</td>
                        <td style="width:33%; text-align: center;" class="text-body-1">총 결제 예정 금액</td>
                    </tr>
                    <tr>
                        <td style="width:33%; text-align: center;" class="v-data-table__divider text-h6"><span v-if="totalPrice - totalDiscount<50000">{{AddComma(totalPrice + 2500)}}</span><span v-if="totalPrice - totalDiscount>=50000">{{AddComma(totalPrice)}}</span> 원</td>
                        <td style="width:33%; text-align: center;" class="v-data-table__divider text-h6">- {{AddComma(totalDiscount)}} 원</td>
                        <td style="width:33%; text-align: center;" class="text-h6">= <span v-if="totalPrice - totalDiscount<50000">{{AddComma(totalPrice - totalDiscount+2500)}}</span><span v-if="totalPrice - totalDiscount>=50000">{{AddComma(totalPrice - totalDiscount)}}</span> 원</td>
                    </tr>
                </tbody>
            </v-simple-table>

            <v-simple-table>
                <tbody>
                    <tr>
                        <td style="width:10%">포인트</td>
                        <td style="width:20%; margin: 0 0">
                            <v-text-field type="number" hide-spin-buttons v-model="point" outlined hide-details dense prefix="원" reverse @change="pointCheck"></v-text-field>
                        </td>
                        <td>(총 사용 가능한 포인트 : {{memberInfo.point}}원)</td>
                    </tr>
                </tbody>
            </v-simple-table>
            <div class="text-t6 mt-10">결제 수단</div>
            <v-row>
                <v-col cols="8" style="min-height: 350px">
                    <v-row>
                        <v-col>
                            <v-radio-group v-model="orderMethod" row>
                                <v-radio label="무통장 입금" value="cash"></v-radio>
                                <v-radio label="카드 결제" value="credit"></v-radio>
                                <v-radio label="휴대폰 결제" value="mobile"></v-radio>
                            </v-radio-group>
                        </v-col>
                    </v-row>
                    <v-divider></v-divider>
                    <v-row>
                        <v-col>
                            <v-simple-table v-if="orderMethod == 'cash'">
                                <tbody>
                                    <tr>
                                        <td style="width:15%">입금자명</td>
                                        <td>입금 시 입금자명은 주문하시는 분 성함과 동일하게 입력해주세요</td>
                                    </tr>
                                    <tr>
                                        <td>입금 은행</td>
                                        <td>
                                            <v-select v-model="bankSelected" :items="banks"></v-select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>현금영수증</td>
                                        <td>
                                            <v-radio-group v-model="cashReceipts" row>
                                                <v-radio label="현금영수증 신청" :value="true"></v-radio>
                                                <v-radio label="신청 안함" :value="false"></v-radio>
                                            </v-radio-group>
                                            <v-text-field v-if="cashReceipts == true" type="number" hide-spin-buttons v-model="cashReceiptsNum" outlined dense></v-text-field>
                                        </td>
                                    </tr>
                                </tbody>
                            </v-simple-table>
                            <div v-if="orderMethod != 'cash'">
                                <v-simple-table>
                                    <tbody>
                                        <tr>
                                            <td style="width:15%"></td>
                                            <td>
                                                <div class="text-h6">
                                                    <v-icon class="red--text">mdi-alert-box-outline</v-icon>소액 결제의 경우 PG사 정책에 따라 결제 금액 제한이 있을 수 있습니다
                                                </div>
                                                <div class="text-h6">
                                                    <v-icon class="red--text">mdi-alert-box-outline</v-icon>결제하기 버튼을 눌러 결제를 해주세요
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </v-simple-table>
                            </div>
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="4">
                    <v-spacer class="mt-5"></v-spacer>
                    <v-row justify="center">
                        <v-col cols="11">
                            <v-row justify="space-between" align="center">
                                <v-col cols="auto" class="text-h6">최종 결제 금액</v-col>
                                <v-col cols="auto" class="text-h4">
                                    <span v-if="totalPrice - totalDiscount<50000">{{AddComma(totalPrice - totalDiscount - point+2500)}}</span><span v-if="totalPrice - totalDiscount>=50000">{{AddComma(totalPrice - totalDiscount - point)}}</span> 원
                                </v-col>
                            </v-row>
                            <v-row justify="space-between" align="center">
                                <v-col cols="auto">총 적립 예정 금액</v-col>
                                <v-col cols="auto">
                                    {{AddComma(Math.round((totalPrice - totalDiscount - point)*0.02))}}원
                                </v-col>
                            </v-row>
                        </v-col>
                    </v-row>
                    <v-row justify="center">
                        <v-col cols="auto">
                            <v-checkbox v-model="readyToBuy" :label="`결제정보를 확인하였으며, 구매진행에 동의합니다.`"></v-checkbox>
                            <v-btn @click="addOrder" color="primary" block>결제하기</v-btn>
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
            selected: [],
            totalPrice: 0,
            totalDiscount: 0,
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
            delivery: {
                name: '',
                zipcode: '',
                addr1: '',
                addr2: '',
                tel: '',
            },
            deliveryMessage: '',
            orderMethod: 'cash',
            point: 0,
            readyToBuy: false,

            banks: [{
                text: '선택해주세요',
                value: '',
                disabled: true,
            }, {
                text: '신한은행 : 000-000-000000 (주) 스프링',
                value: 'shinhan'
            }, {
                text: 'KB국민은행 : 000000-00-000000 (주) 스프링',
                value: 'kb'
            }, {
                text: 'IBK기업은행 : 000-000000-00-000 (주) 스프링',
                value: 'ibk'
            }, {
                text: '우리은행 : 0000-000-000000 (주) 스프링',
                value: 'woori'
            }, ],
            bankSelected: '',
            cashReceipts: false,
            cashReceiptsNum: '',
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getMember() {
            axios.get(`/api/member/getMemberInfo/${this.getLogin.user.id}`)
                .then(res => {
                    this.memberInfo = res.data;
                })
        },
        searchAddress() {
            console.log('test');
        },
        addOrder() {
            if (this.memberInfo.name == '') {
                alert('주문하시는 분의 성함을 입력해주세요');
                return;
            }
            if (this.memberInfo.zipcode == '' || this.memberInfo.addr1 == '' || this.memberInfo.addr2 == '') {
                alert('주문하시는 분의 주소를 입력해주세요');
                return;
            }
            if (this.memberInfo.tel == '') {
                alert('주문하시는 분의 전화번호를 입력해주세요');
                return;
            }
            if (this.memberInfo.email == '') {
                alert('주문하시는 분의 전화번호를 입력해주세요');
                return;
            }
            if (this.delivery.name == '') {
                alert('받으시는 분의 성함을 입력해주세요');
                return;
            }
            if (this.delivery.zipcode == '' || this.delivery.addr1 == '' || this.delivery.addr2 == '') {
                alert('받으시는 분의 주소를 입력해주세요');
                return;
            }
            if (this.delivery.tel == '') {
                alert('받으시는 분의 전화번호를 입력해주세요');
                return;
            }
            if (!(this.point >= 0 && (this.point == Math.round(this.point)))) {
                alert('포인트가 유효하지 않습니다');
                this.point = 0;
                return;
            }
            if (this.point > this.memberInfo.point) {
                alert('사용 가능한 포인트보다 많은 포인트를 사용하셨습니다. 포인트를 다시 입력해주세요');
                this.point = 0;
                return;
            }
            if (this.orderMethod == 'cash') {
                if (this.bankSelected == '') {
                    alert('입금계좌를 선택해주세요');
                    return;
                }
                this.orderMethod = this.orderMethod + ';' + this.bankSelected + ';' + this.cashReceipts;
                if (this.cashReceipts == true) {
                    if (this.cashReceiptsNum == '') {
                        alert('현금영수증을 발행할 현금영수증 카드나 핸드폰 번호를 입력해주세요');
                        return;
                    }
                    this.orderMethod = this.orderMethod + ';' + this.cashReceiptsNum;
                }
            }
            if (this.readyToBuy == false) {
                alert('구매 진행에 동의해주세요');
                return;
            }
            let orderList = [];
            let basketIdxList = [];
            for (let i = 0; i < this.selected.length; i++) {
                let order = {
                    id: this.getLogin.user.id,
                    productNo: this.selected[i].productNo,
                    selectedColor: this.selected[i].selectedColor,
                    selectedSize: this.selected[i].selectedSize,
                    orderAmount: this.selected[i].basketAmount,
                    totalPrice: (this.selected[i].price - this.selected[i].discount) * this.selected[i].basketAmount,
                    orderMethod: this.orderMethod,
                    name: this.delivery.name,
                    tel: this.delivery.tel,
                    zipCode: this.delivery.zipcode,
                    address: this.delivery.addr1,
                    detailAddr: this.delivery.addr2,
                    message: this.deliveryMessage
                }
                orderList.push(order);
                basketIdxList.push(this.selected[i].basketIdx);
            }

            let formData = new FormData();
            formData.append('orderList', new Blob([JSON.stringify(orderList)], {
                type: "application/json"
            }))
            formData.append('basketIdxList', new Blob([JSON.stringify(basketIdxList)], {
                type: "application/json"
            }))
            let pointData = {
                id: this.getLogin.user.id,
                point: -this.point,
                content: '상품 결제',
            }
            formData.append('pointData', new Blob([JSON.stringify(pointData)], {
                type: "application/json"
            }))

            axios.post(`/api/order/insertOrder`, formData)
                .then(() => {
                    alert('주문이 완료되었습니다');
                    this.$router.push('/');
                }).catch(err => {
                    alert('주문에 실패하였습니다');
                    console.log(err);
                })
        },
        execDaumPostcode(target) {
            new window.daum.Postcode({
                oncomplete: (data) => {
                    let postcode = '';
                    let address = '';
                    let extraAddress = '';

                    if (data.userSelectedType === "R") {
                        // 사용자가 도로명 주소를 선택했을 경우
                        address = data.roadAddress;
                    } else {
                        // 사용자가 지번 주소를 선택했을 경우(J)
                        address = data.jibunAddress;
                    }

                    // // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    // if (data.userSelectedType === "R") {
                    //     // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    //     // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    //     if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
                    //         extraAddress += data.bname;
                    //     }
                    //     // 건물명이 있고, 공동주택일 경우 추가한다.
                    //     if (data.buildingName !== "" && data.apartment === "Y") {
                    //         extraAddress +=
                    //             extraAddress !== "" ?
                    //             `, ${data.buildingName}` :
                    //             data.buildingName;
                    //     }
                    //     // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    //     if (extraAddress !== "") {
                    //         extraAddress = `(${extraAddress})`;
                    //     }
                    // } else {
                    //     extraAddress = "";
                    // }
                    // 우편번호를 입력한다.
                    postcode = data.zonecode;

                    if (target == 'order') {
                        this.memberInfo.zipcode = postcode;
                        this.memberInfo.addr1 = address;
                        this.memberInfo.addr2 = extraAddress;
                    } else if (target == 'delivery') {
                        this.delivery.zipcode = postcode;
                        this.delivery.addr1 = address;
                        this.delivery.addr2 = extraAddress;
                    }
                },
            }).open();
        },
        pointCheck() {
            if (!(this.point >= 0 && (this.point == Math.round(this.point)) && this.point != '')) {
                alert('포인트가 유효하지 않습니다');
                this.point = 0;
                return;
            }
            if (this.point > this.memberInfo.point) {
                alert('사용 가능한 포인트보다 많은 포인트를 사용하셨습니다. 포인트를 다시 입력해주세요');
                this.point = 0;
                return;
            }
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        deliverySelect: {
            handler() {
                if (this.deliverySelect == true) {
                    this.delivery = this.memberInfo;
                } else {
                    this.delivery = {
                        name: '',
                        zipcode: '',
                        addr1: '',
                        addr2: '',
                        tel: '',
                    };
                    this.deliveryMessage = '';
                }
            }
        },
    },
    mounted() {
        this.selected = this.$route.params.Payment;
        if (this.selected == undefined) {
            this.$router.push('/')
        } else {
            this.getMember();
            for (let i = 0; i < this.selected.length; i++) {
                this.totalPrice += this.selected[i].price * this.selected[i].basketAmount;
                this.totalDiscount += this.selected[i].discount * this.selected[i].basketAmount;
            }
        }
    }
}
</script>

<style scoped>
</style>
