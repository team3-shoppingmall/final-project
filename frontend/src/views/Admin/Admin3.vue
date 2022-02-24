<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="2" align-self="center">
            <v-select :items="searchType" v-model="searchTypeNo" hide-details></v-select>
        </v-col>
        <v-col cols="3" align-self="center">
            <v-text-field hide-details></v-text-field>
        </v-col>
        <v-col align-self="center" cols="auto">
            <v-menu ref="menu1" v-model="menu1" :close-on-content-click="false" :return-value.sync="date1" transition="scale-transition" offset-y="offset-y" min-width="auto">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="date1" label="처음" prepend-icon="mdi-calendar" readonly="readonly" v-bind="attrs" v-on="on" hide-details="hide-details"></v-text-field>
                </template>
                <v-date-picker v-model="date1" no-title="no-title" scrollable="scrollable">
                    <v-spacer></v-spacer>
                    <v-btn text="text" color="primary" @click="menu1 = false">
                        Cancel
                    </v-btn>
                    <v-btn text="text" color="primary" @click="$refs.menu1.save(date1)">
                        OK
                    </v-btn>
                </v-date-picker>
            </v-menu>
        </v-col>
        <v-col align-self="center" cols="auto">
            <v-menu ref="menu2" v-model="menu2" :close-on-content-click="false" :return-value.sync="date2" transition="scale-transition" offset-y="offset-y" min-width="auto">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="date2" label="끝" prepend-icon="mdi-calendar" readonly="readonly" v-bind="attrs" v-on="on" hide-details="hide-details"></v-text-field>
                </template>
                <v-date-picker v-model="date2" no-title="no-title" scrollable="scrollable">
                    <v-spacer></v-spacer>
                    <v-btn text="text" color="primary" @click="menu2 = false">
                        Cancel
                    </v-btn>
                    <v-btn text="text" color="primary" @click="$refs.menu2.save(date2)">
                        OK
                    </v-btn>
                </v-date-picker>
            </v-menu>
        </v-col>
        <v-col align-self="center" cols="auto">
            <v-btn class="primary">조회하기</v-btn>
        </v-col>
    </v-row>
    <v-row>
        <v-col>
            <v-data-table :headers="headers">

                <template #footer="{}">
                    <v-row class="ma-0" justify="end">
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
            menu1: false,
            date1: '',
            menu2: false,
            date2: '',
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
