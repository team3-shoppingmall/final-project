<template>
<v-container>
    <v-row justify="center" class="pa-5">
        <v-col cols="2">
            <v-btn @click="getReview" :color="colorPicker('all')">전체보기</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="searchProduct" :color="colorPicker('product')">상품관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="searchDelivery" :color="colorPicker('delivery')">배송관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="searchReturn" :color="colorPicker('return')">교환/반품관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="searchEtc" :color="colorPicker('etc')">기타관련</v-btn>
        </v-col>
    </v-row>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :single-expand="true" hide-default-footer :expanded.sync="expanded" :loading="loading" show-expand class="elevation-1" @click:row="(item, slot) => slot.expand(!slot.isExpanded)">
        <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length">
                {{ item.content }}
            </td>
        </template>
    </v-data-table>
    <v-row justify="end" class="mt-2">
        <v-col cols="auto">
            <v-btn :to="'/writePost/faq'">글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            expanded: [],
            headers: [{
                    text: '종류',
                    value: 'type',
                    sortable: false,
                    width: '10%',
                    divider: true
                },
                {
                    text: '제목',
                    value: 'title',
                    sortable: false,
                    width: '90%'
                },
                {
                    text: '',
                    value: 'data-table-expand'
                },
            ],
            search: 'product',

        }
    },
    methods: {
        getReview() {
            this.loading = true
            this.search = 'all',
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
                    params: {
                        page: 1,
                        perPage: 100,
                        search: 'title',
                        searchWord: '',
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                })
        },
        colorPicker(put) {
            if (this.search == put) {
                return 'primary'
            }
        },
        searchProduct(){
            this.search = 'product';
            // 상품관련만 출력
        },
        searchDelivery(){
            this.search = 'delivery';
            // 배송관련만 출력
        },
        searchReturn(){
            this.search = 'return';
            // 교환/반품관련만 출력
        },
        searchEtc(){
            this.search = 'etc';
            // 기타관련만 출력
        }
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
</style>
