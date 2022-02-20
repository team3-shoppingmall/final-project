<template>
<v-container>
    <v-row justify="center" class="pa-5">
        <v-col cols="2">
            <v-btn @click="getFAQ('all')" :color="colorPicker('all')">전체보기</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="getFAQ('product')" :color="colorPicker('product')">상품관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="getFAQ('delivery')" :color="colorPicker('delivery')">배송관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="getFAQ('return')" :color="colorPicker('return')">교환/반품관련</v-btn>
        </v-col>
        <v-col cols="2">
            <v-btn @click="getFAQ('etc')" :color="colorPicker('etc')">기타관련</v-btn>
        </v-col>
    </v-row>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :single-expand="true" hide-default-footer :expanded.sync="expanded" :loading="loading" show-expand class="elevation-1" @click:row="(item, slot) => slot.expand(!slot.isExpanded)">
        <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length">
                {{ item.content }}
            </td>
        </template>
        <template #[`item.type`]="{item}">
            <faqTypeDisplay :type="item.type" />
        </template>
        <template #[`item.icon`]="{}">
            Q
        </template>
        <template #[`item.actions`]="{item}" v-if="admin">
            <v-btn small>
                <!-- <v-icon>{{ icons.mdiPencil }}</v-icon> -->
                수정
            </v-btn>
            <v-btn small @click="deleteFAQ(item.noticeNo)">
                삭제
                <!-- <v-icon> {{ icons.mdiDelete }} </v-icon> -->
            </v-btn>
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
// import {
//     mdiPencil,
//     mdiDelete,
// } from '@mdi/js'
import faqTypeDisplay from '@/components/faqTypeDisplay.vue'
export default {
    components: {
        faqTypeDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            expanded: [],
            admin: true,
            headers: [{
                    text: '',
                    value: 'icon',
                    sortable: false,
                    width: '5%',
                }, {
                    text: '종류',
                    value: 'type',
                    sortable: false,
                    width: '10%',
                },
                {
                    text: '제목',
                    value: 'title',
                    sortable: false,
                    width: '70%',
                },
                {
                    text: '',
                    value: 'actions',
                    sortable: false,
                    width: '15%',
                },
                {
                    text: '',
                    value: 'data-table-expand'
                },
            ],
            // icons: {
            //     mdiPencil,
            //     mdiDelete,
            // },
            search: 'all',

        }
    },
    methods: {
        getFAQ(selectedType) {
            this.loading = true
            this.search = selectedType;
            axios({
                    method: 'get',
                    url: `/api/notice/getNotice`,
                    params: {
                        page: 1,
                        perPage: 100,
                        search: 'title',
                        searchWord: '',
                        // type: selectedType,
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
        deleteFAQ(num) {
            console.log(num);
        }
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
