<template>
<v-container>
    <v-row align="center" justify="center" class="my-3">
        <v-col cols="8" sm="7" md="5" lg="5" xl="5" align="center">
            <v-row align="center">
                <v-col cols="10" align="center">
                    <v-text-field v-model="searchWord" solo hide-details @keyup.enter="searchFAQ" placeholder="궁금하신 내용을 검색해주세요."></v-text-field>
                </v-col>
                <v-col cols="2" align="center">
                    <v-btn @click="searchFAQ" color="primary">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <v-row justify="center" class="pa-5">
        <v-col cols="8">
            <v-row justify="space-between">
                <v-btn width="150px" @click="limit = 5; getFAQ('all')" :color="colorPicker('all')">전체보기</v-btn>
                <v-btn width="150px" @click="limit = 5; getFAQ('product')" :color="colorPicker('product')">상품관련</v-btn>
                <v-btn width="150px" @click="limit = 5; getFAQ('delivery')" :color="colorPicker('delivery')">배송관련</v-btn>
                <v-btn width="150px" @click="limit = 5; getFAQ('return')" :color="colorPicker('return')">교환/반품관련</v-btn>
                <v-btn width="150px" @click="limit = 5; getFAQ('etc')" :color="colorPicker('etc')">기타관련</v-btn>
            </v-row>
        </v-col>
    </v-row>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" item-key="faqNo" hide-default-footer="hide-default-footer" :loading="loading" disable-sort="disable-sort" show-expand="show-expand" :single-expand="true" :expanded.sync="expanded" class="elevation-1" @click:row="(item, slot) => slot.expand(!slot.isExpanded)" no-data-text="검색된 자료가 없습니다">
        <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length" class="pa-5">
                <div v-html="item.content"></div>
            </td>
        </template>
        <template #[`item.type`]="{item}">
            <FAQTypeDisplay :type="item.type" />
        </template>
        <template #[`item.icon`]="{}">
            <v-icon>mdi-quora</v-icon>
        </template>
        <template #[`item.title`]="{item}">
            <v-row justify="space-between">
                <v-col cols="auto">
                    <div class="text-left">{{item.title}}</div>
                </v-col>
                <v-col cols="auto" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">
                    <v-icon @click="updateFAQ(item.faqNo)">mdi-pencil</v-icon>
                    <v-icon @click="deleteFAQ(item.faqNo)">mdi-delete</v-icon>
                </v-col>
            </v-row>
        </template>
        <template v-slot:footer="{ }">
            <v-divider></v-divider>
            <v-btn text width="100%" @click="showMore" v-if="limit == 5 && totalContents > 5">더 보기 <v-icon>mdi-menu-down</v-icon>
            </v-btn>
        </template>
    </v-data-table>
    <v-row justify="end" class="mt-2" v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">
        <v-col cols="auto">
            <v-btn :to="'/writePost/faq'" color="primary">글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import FAQTypeDisplay from '@/components/FAQTypeDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        FAQTypeDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {
                itemsPerPage: 50,
            },
            loading: true,
            expanded: [],
            admin: true,
            headers: [{
                    text: '',
                    value: 'icon',
                    width: '5%',
                    align: 'center',
                }, {
                    text: '종류',
                    value: 'type',
                    width: '12%',
                    align: 'center',
                },
                {
                    text: '제목',
                    value: 'title',
                    width: '83%',
                    align: 'center',
                },
                {
                    text: '',
                    value: 'data-table-expand'
                },
            ],
            search: 'all',
            limit: 5,

            searchWord: '',

        }
    },
    methods: {
        colorPicker(put) {
            if (this.search == put) {
                return 'secondary'
            }
        },
        getFAQ(selectedType) {
            this.loading = true
            this.search = selectedType;
            this.expanded = [];
            axios({
                    method: 'get',
                    url: `/api/faq/getByType`,
                    params: {
                        type: selectedType,
                        limit: this.limit,
                    }
                })
                .then(res => {
                    this.contents = res.data.faqList;
                    this.totalContents = res.data.count;
                    this.loading = false
                })
        },
        showMore() {
            this.limit = 50;
            this.getFAQ(this.search);
        },
        searchFAQ() {
            if (this.searchWord == '') {
                return;
            }
            this.loading = true;
            this.search = 'all';
            this.totalContents = 0;
            this.expanded = [];
            axios.get(`/api/faq/search/${this.searchWord}`)
                .then(res => {
                    this.contents = res.data;
                    this.searchWord = '';
                    this.loading = false;
                })
        },
        deleteFAQ(num) {
            axios.delete(`/api/faq/deletefaq/${num}`)
                .then(() => {
                    alert("삭제가 완료되었습니다.")
                    this.$router.go();
                })

        },
        updateFAQ(num) {
            this.$router.push(`/updatePost/faq/${num}`);

        }
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.getFAQ('all')
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
</style>
