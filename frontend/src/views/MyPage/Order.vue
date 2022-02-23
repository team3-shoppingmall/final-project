<template>
<v-container fluid>

    <v-row justify="center">
        <v-col cols="9">
            <v-row>
                <v-col cols="auto">
                    <v-btn :class="selectedColor == true ? 'primary' : 'secondary'" @click="selectOrder(1)" width="240px">주문 내역조회</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :class="selectedColor != true ? 'primary' : 'secondary'" @click="selectOrder(2)" width="240px">취소/반품/교환 내역조회</v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col align-self="center" cols="auto">
                    <v-select></v-select>
                </v-col>
                <v-col align-self="center" cols="auto">
                    <v-btn>오늘</v-btn>
                    <v-btn>1주일</v-btn>
                    <v-btn>1개월</v-btn>
                    <v-btn>3개월</v-btn>
                    <v-btn>6개월</v-btn>
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
                <v-col align-self="center">
                    <v-text-field label="" solo="solo" hide-details="hide-details"></v-text-field>
                </v-col>
                <v-col cols="auto" align-self="center">
                    <v-btn>조회</v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">
                    <v-data-table :headers="headers" :items="desserts" :options.sync="options" :server-items-length="totalDesserts" :loading="loading" class="elevation-1 my-3" dense="dense">
                        <template #top="{ }">
                            <div class="text-h5 pa-3">{{selectedOrder}}</div>
                        </template>
                    </v-data-table>
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
            menu1: false,
            date1: '',
            menu2: false,
            date2: '',

            selectedOrder: '주문 내역조회',
            selectedColor: true,

            totalDesserts: 0,
            desserts: [],
            loading: true,
            options: {},
            headers: [{
                text: '주문일자',
                align: 'start',
                sortable: false,
                value: 'name'
            }, {
                text: '상품이미지',
                value: 'calories'
            }, {
                text: '상품정보',
                value: 'fat'
            }, {
                text: '수량',
                value: 'carbs'
            }, {
                text: '상품 구매 금액',
                value: 'protein'
            }, {
                text: '주문 상태',
                value: 'iron'
            }]
        }
    },
    // watch: {
    //     options: {
    //         handler() {
    //             this.getDataFromApi()
    //         },
    //         deep: true
    //     }
    // },
    mounted() {
        this.getDataFromApi();
    },
    methods: {
        selectOrder(selected) {
            switch (selected) {
                case 1:
                    this.selectedColor = true;
                    this.selectedOrder = '주문 내역조회';
                    this.getDataFromApi();
                    break;
                case 2:
                    this.selectedColor = false;
                    this.selectedOrder = '취소/반품/교환 내역조회';
                    this.desserts = [];
                    break;
            }
        },
        getDataFromApi() {
            this.loading = true
            this
                .fakeApiCall()
                .then(data => {
                    this.desserts = data.items
                    this.totalDesserts = data.total
                    this.loading = false
                })
        },
        /**
         * In a real application this would be a call to fetch() or axios.get()
         */
        fakeApiCall() {
            return new Promise((resolve, reject) => {
                console.log(reject)
                const {
                    sortBy,
                    sortDesc,
                    page,
                    itemsPerPage
                } = this.options

                let items = this.getDesserts()
                const total = items.length

                if (sortBy.length === 1 && sortDesc.length === 1) {
                    items = items.sort((a, b) => {
                        const sortA = a[sortBy[0]]
                        const sortB = b[sortBy[0]]

                        if (sortDesc[0]) {
                            if (sortA < sortB)
                                return 1
                            if (sortA > sortB)
                                return -1
                            return 0
                        } else {
                            if (sortA < sortB)
                                return -1
                            if (sortA > sortB)
                                return 1
                            return 0
                        }
                    })
                }

                if (itemsPerPage > 0) {
                    items = items.slice((page - 1) * itemsPerPage, page * itemsPerPage)
                }

                setTimeout(() => {
                    resolve({
                        items,
                        total
                    })
                }, 1000)
            })
        },
        getDesserts() {
            return [{
                name: 'Frozen Yogurt',
                calories: 159,
                fat: 6.0,
                carbs: 24,
                protein: 4.0,
                iron: '1%'
            }, {
                name: 'Ice cream sandwich',
                calories: 237,
                fat: 9.0,
                carbs: 37,
                protein: 4.3,
                iron: '1%'
            }, {
                name: 'Eclair',
                calories: 262,
                fat: 16.0,
                carbs: 23,
                protein: 6.0,
                iron: '7%'
            }, {
                name: 'Cupcake',
                calories: 305,
                fat: 3.7,
                carbs: 67,
                protein: 4.3,
                iron: '8%'
            }, {
                name: 'Gingerbread',
                calories: 356,
                fat: 16.0,
                carbs: 49,
                protein: 3.9,
                iron: '16%'
            }, {
                name: 'Jelly bean',
                calories: 375,
                fat: 0.0,
                carbs: 94,
                protein: 0.0,
                iron: '0%'
            }, {
                name: 'Lollipop',
                calories: 392,
                fat: 0.2,
                carbs: 98,
                protein: 0,
                iron: '2%'
            }, {
                name: 'Honeycomb',
                calories: 408,
                fat: 3.2,
                carbs: 87,
                protein: 6.5,
                iron: '45%'
            }, {
                name: 'Donut',
                calories: 452,
                fat: 25.0,
                carbs: 51,
                protein: 4.9,
                iron: '22%'
            }, {
                name: 'KitKat',
                calories: 518,
                fat: 26.0,
                carbs: 65,
                protein: 7,
                iron: '6%'
            }]
        }
    },
    components: {}
}
</script>

<style scoped>
.v-btn:not(.v-btn--round).v-size--default {
    height: 36px;
    min-width: 45px;
    padding: 5px 10px;
}
</style>
