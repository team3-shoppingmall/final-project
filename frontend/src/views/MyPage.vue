<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <div class="text-h3 justify-center">MY PAGE</div>
            <v-simple-table>
                <tbody>
                    <tr>
                        <td class="text-h5" colspan="4">나의 주문처리 현황 </td>
                    </tr>
                    <tr>
                        <td>
                            <v-card outlined>입금 전</v-card>
                        </td>
                        <td>
                            <v-card outlined>배송준비중</v-card>
                        </td>
                        <td>
                            <v-card outlined>배송중</v-card>
                        </td>
                        <td>
                            <v-card outlined>배송완료</v-card>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <v-card outlined>사용적립금</v-card>
                        </td>
                        <td colspan="2">
                            <v-card outlined>총주문</v-card>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
            <MyPageButton class="mt-10" />
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import MyPageButton from '@/components/MyPageButton.vue'
export default {
    components: {
        MyPageButton
    }
}
</script>

<style scoped>
tr:hover {
    background: white !important;
}
</style>
