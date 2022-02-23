<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">ORDER</div>
            <!-- @click:row="moveTo" -->
            <v-data-table :headers="headers" :items="desserts" :options.sync="options" :server-items-length="totalDesserts" :loading="loading" hide-default-footer show-select="show-select" item-key="name" class="elevation-1">
                <template v-slot:[`item.image`]="{ item }">
                    <v-img :src="`https://picsum.photos/seed/${item.image}/140/140`" width="100px" height="100px"></v-img>
                    <!-- <div class="red text-right"> {{ item.image }} </div> -->
                </template>
                <template v-slot:[`item.info`]="{ item }">
                    <v-simple-table>
                        <tbody>
                            <tr>
                                <td>색상</td>
                                <td>{{item.info}}</td>
                            </tr>
                            <tr>
                                <td>사이즈</td>
                                <td>{{item.info}}</td>
                            </tr>
                            <tr>
                                <td>개수</td>
                                <td>{{item.info}}</td>
                            </tr>
                        </tbody>
                    </v-simple-table>

                </template>
                <template v-slot:footer="{ }">
                    <v-divider></v-divider>
                    <v-row justify="end">
                        <v-col cols="auto">
                            상품구매금액 49,800 + 배송비 2,500 = 합계 : 52,300원
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
                        <td>총 상품금액</td>
                        <td>배송비</td>
                        <td>결제 예정 금액</td>
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
export default {
    data() {
        return {
            selected: [],
            totalDesserts: 1,
            desserts: [{
                name: 'Frozen Yogurt1',
                image: 1,
                info: 159,
                fat: 6.0,
                carbs: 24,
                protein: 4.0,
                iron: '1%',
                isCheck: false
            }, {
                name: 'Frozen Yogurt2',
                image: 2,
                info: 160,
                fat: 6.0,
                carbs: 24,
                protein: 4.0,
                iron: '1%',
                isCheck: false
            }],
            loading: false,
            options: {},
            headers: [{
                text: 'image',
                value: 'image',
                width: '100px'
            }, {
                text: 'Dessert (100g serving)',
                align: 'start',
                sortable: false,
                value: 'name'
            }, {
                text: '상품정보',
                value: 'info'
            }, {
                text: '판매가',
                value: 'iron'
            }, {
                text: '합계',
                value: 'iron'
            }, ]
        }
    },
    methods: {
        moveTo() {
            this
                .$router
                .push('/')
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
