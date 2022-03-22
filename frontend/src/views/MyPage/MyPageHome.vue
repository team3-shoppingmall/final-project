<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h4">나의 주문처리 현황</div>
            <v-simple-table class="mt-5">
                <tbody>
                    <tr>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[0].state}}</div>
                            <div class="text-subtitle-1">{{states[0].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[1].state}}</div>
                            <div class="text-subtitle-1">{{states[1].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[2].state}}</div>
                            <div class="text-subtitle-1">{{states[2].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[3].state}}</div>
                            <div class="text-subtitle-1">{{states[3].count}}</div>
                        </td>
                        <td style="text-align: center;">
                            <div class="text-h6">{{states[4].state}}</div>
                            <div class="text-subtitle-1">{{states[4].count}}</div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[5].state}}</div>
                            <div class="text-subtitle-1">{{states[5].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[6].state}}</div>
                            <div class="text-subtitle-1">{{states[6].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[7].state}}</div>
                            <div class="text-subtitle-1">{{states[7].count}}</div>
                        </td>
                        <td style="text-align: center;" class="v-data-table__divider">
                            <div class="text-h6">{{states[8].state}}</div>
                            <div class="text-subtitle-1">{{states[8].count}}</div>
                        </td>
                        <td style="text-align: center;">
                            <div class="text-h6">총 주문</div>
                            <div class="text-subtitle-1">{{sum}}건</div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: center;" class="text-h6" colspan="5">총 포인트 : {{AddComma(point)}}P</td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    data() {
        return {
            states: [{
                state: '입금전',
                count: 0,
            }, {
                state: '결제완료',
                count: 0,
            }, {
                state: '배송준비중',
                count: 0,
            }, {
                state: '배송중',
                count: 0,
            }, {
                state: '배송완료',
                count: 0,
            }, {
                state: '구매확정',
                count: 0,
            }, {
                state: '취소완료',
                count: 0,
            }, {
                state: '교환완료',
                count: 0,
            }, {
                state: '환불완료',
                count: 0,
            }, ],
            sum: 0,
            point: '',
        }
    },
    methods: {
        AddComma(num) {
            var regexp = /\B(?=(\d{3})+(?!\d))/g;
            return `${num}`.toString().replace(regexp, ",");
        },
        getData() {
            this.sum = 0;
            axios.get(`/api/order/getOrderGroupByState/${this.getLogin.user.id}`)
                .then(res => {
                    let temp = res.data.orderCounts;
                    for (let i = 0; i < temp.length; i++) {
                        for (let j = 0; j < this.states.length; j++) {
                            if (temp[i].state == this.states[j].state) {
                                this.states[j].count = temp[i].orderNo;
                                this.sum += temp[i].orderNo;
                            }
                        }
                    }
                    this.point = res.data.point;
                }).catch((err) => {
                    for (let j = 0; j < this.states.length; j++) {
                        this.states[j].count = 0;
                    }
                    this.point = 0;
                    console.log(err);
                })
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    mounted() {
        this.getData();
    }
}
</script>

<style>

</style>
