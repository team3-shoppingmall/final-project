<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="noticeNo" class="elevation-1" @click:row="moveto" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
        <template #[`item.title`]="{item}">
            <div class="text-left">
                {{ item.title }}
            </div>
        </template>
        <template #[`item.id`]="{item}">
            <div>
                <HideId :id="item.id" />
            </div>
        </template>
    </v-data-table>

    <v-row align="center" justify="space-between">
        <v-col cols="8" sm="7" md="5" lg="4" xl="3">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search" hide-details></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord" hide-details @keyup.enter="getNotice"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn @click="searchNotice" color="primary">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">
            <v-btn :to="'/writePost/notice'" color="primary">글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        HideId,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'noticeNo',
                    width: '10%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '제목',
                    value: 'title',
                    width: '60%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '작성자',
                    value: 'id',
                    width: '10%',
                    align: 'center'
                },
            ],
            searches: [{
                text: '제목',
                value: 'title'
            }, ],
            search: 'title',
            searchWord: '',
        }
    },
    methods: {
        getNotice() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
                    params: {
                        page: page,
                        perPage: itemsPerPage,
                        search: this.search,
                        searchWord: this.searchWord,
                    }
                })
                .then(res => {
                    this.contents = res.data.noticeList;
                    this.totalContents = res.data.count;
                }).finally(
                    this.loading = false
                )
        },
        searchNotice() {
            if (this.options.page != 1) {
                this.options.page = 1;
            } else {
                this.getNotice();
            }
        },
        moveto(item) {
            this.$router.push(`/community/noticePost/${item.noticeNo}`)
        },

    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: { //변수 값이 변경될 때 연산을 처리하거나 변수 값에 따라 화면을 제어할 때 사용
        options: {
            handler() {
                this.getNotice()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
</style>
