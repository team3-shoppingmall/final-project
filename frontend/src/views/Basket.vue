<template>
<v-container style="min-height: 910px;">
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3">장바구니</div>
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
                            <tr>
                                <td colspan="2" class="text-center">
                                    <v-btn outlined="outlined">옵션변경</v-btn>
                                </td>
                            </tr>
                        </tbody>
                    </v-simple-table>

                </template>
                <template v-slot:footer="{ }">
                    <v-divider></v-divider>
                    <v-row justify="center">
                        <v-col cols="6">
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
                                </tbody>
                            </v-simple-table>
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
