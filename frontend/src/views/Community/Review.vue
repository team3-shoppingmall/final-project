<template>
<v-container>
    <div>
        <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1" disable-sort>
            <template v-slot:body="{ items }">
                <tbody>
                    <tr v-for="item in items" :key="item.reviewNo">
                        <td style="text-align: center;">{{ item.reviewNo }}</td>
                        <td>
                            <productNameDisplay :productno="item.productno" />
                        </td>
                        <td style="text-align: center;" class="pa-1">
                            <v-rating hover length="5" readonly size="10" :value="item.star"></v-rating>
                        </td>
                        <td>{{ item.content }}</td>
                        <td>
                            <hideId :id="item.id" />
                        </td>
                        <td style="text-align: center;">
                            <dateDisplay :regDate="item.regDate" />
                        </td>
                    </tr>
                </tbody>
            </template>
        </v-data-table>
    </div>

    <v-row align="center" justify="space-between">
        <v-col class="d-flex" cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon @click="getReview">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import hideId from '@/components/hideId.vue'
import dateDisplay from '@/components/dateDisplay.vue'
import productNameDisplay from '@/components/productNameDisplay.vue'
export default {
    components: {
        hideId,
        dateDisplay,
        productNameDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'reviewno',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '상품명',
                    value: 'productno',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '별점',
                    value: 'star',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '후기',
                    value: 'content',
                    width: '45%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '작성자',
                    value: 'id',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '작성일',
                    value: 'regDate',
                    width: '10%',
                    align: 'center'
                },
            ],
            searches: [{
                    text: '상품명',
                    value: 'productname'
                },
                {
                    text: '작성자',
                    value: 'id'
                }
            ],
            search: 'id',
            searchWord: '',
        }
    },
    methods: {
        getReview() {
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/review/getReview`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        search: this.search,
                        searchWord: this.searchWord,
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                    axios({
                            method: 'get',
                            url: '/api/review/getCount',
                            params: {
                                search: this.search,
                                searchWord: this.searchWord,
                            }
                        })
                        .then(res => {
                            this.totalContents = res.data;
                        })
                })
        },
    },
    watch: {
        options: {
            handler() {
                this.getReview()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
table td {
    border-right: 1px solid #dddddd;
}

table td:last-child {
    border-right: none
}
</style>
