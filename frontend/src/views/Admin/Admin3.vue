<template>
<v-container fluid>
   <v-row justify="center">
        <v-col cols="2" align-self="center">
            <v-select :items="searchType" v-model="searchTypeNo" hide-details></v-select>
        </v-col>
        <v-col cols="3" align-self="center">
            <v-text-field hide-details></v-text-field>
        </v-col>
        <v-col cols="auto" align-self="center">
            <v-btn class="primary " large>검색</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers">
           
                <template #footer="{}">
                    <v-row class="ma-0" justify="end" >
                        <v-col cols="auto">
                            <v-btn class="primary">변경</v-btn>
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            searchType: [{
                text: '상품번호',
                value: 0,
            }, {
                text: '상품명',
                value: 1,
            }, {
                text: '가격',
                value: 2,
            }, {
                text: '등록일',
                value: 3,
            }, {
                text: '재고',
                value: 4,
            }],
            searchTypeNo: 0,
            options: {},
            headers: [{
                text: '주문번호',
                value: '1',
                divider: true,
                align: 'center',
            }, {
                text: '상품번호',
                value: '1',
                divider: true,
                align: 'center',
            }, {
                text: '상품명',
                value: '3',
                divider: true,
                align: 'center',
            }, {
                text: '가격',
                value: '3',
                divider: true,
                align: 'center',
            }, {
                text: '개수',
                value: '3',
                divider: true,
                align: 'center',
            }, {
                text: '구매일',
                value: '3',
                divider: true,
                align: 'center',
            }, {
                text: '배송상태',
                value: '3',
                divider: true,
                align: 'center',
            }, {
                text: '상태변경(selectbox)',
                value: '3',
                align: 'center',
            }]
        }
    }
}
</script>

<style>

</style>
