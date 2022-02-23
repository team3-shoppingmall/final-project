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
    <v-data-table :headers="headers" :options.sync="options" :items="contents" item-key="faqNo" hide-default-footer="hide-default-footer" :loading="loading" disable-sort="disable-sort" show-expand="show-expand" :single-expand="true" :expanded.sync="expanded" class="elevation-1" @click:row="(item, slot) => slot.expand(!slot.isExpanded)">
        <template v-slot:expanded-item="{ headers, item }">
            <td :colspan="headers.length">
                {{ item.content }}
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
                <v-col cols="auto">
                    <v-icon @click="updateFAQ(item.faqNo)">mdi-pencil</v-icon>
                    <v-icon @click="deleteFAQ(item.faqNo)">mdi-delete</v-icon>
                </v-col>
            </v-row>
        </template>
    </v-data-table>
    <v-row justify="end" class="mt-2">
        <v-col cols="auto">
            <v-btn :to="'/writePost/faq'" outlined>글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import FAQTypeDisplay from '@/components/FAQTypeDisplay.vue'
export default {
    components: {
        FAQTypeDisplay,
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
                    width: '5%',
                    align: 'center',
                }, {
                    text: '종류',
                    value: 'type',
                    width: '10%',
                    align: 'center',
                },
                {
                    text: '제목',
                    value: 'title',
                    width: '85%',
                    align: 'center',
                },
                {
                    text: '',
                    value: 'data-table-expand'
                },
            ],
            search: 'all',

        }
    },
    methods: {
        getFAQ(selectedType) {
            this.loading = true
            this.search = selectedType;
            this.expanded = [],
                axios({
                    method: 'get',
                    url: `/api/faq/get/${selectedType}`,

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
            axios({
                    method: 'delete',
                    url: `/api/faq/deletefaq`,
                    params: {
                        faqNo: num
                    }
                })
                .then(res => {
                    this.contents = res.data;
                    this.loading = false
                    alert("삭제가 완료되었습니다.")
                    this.$router.push(`/community/faq`);
                })

        },
        updateFAQ(num) {
            this.$router.push(`/updatePost/faq/${num}`);

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
