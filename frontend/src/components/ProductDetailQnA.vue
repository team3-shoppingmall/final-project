<template>
    <v-container>
    
        <div>
    
            <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1" item-key="qnaNo" @click:row="moveto" disable-sort>
    
                <template #[`item.productNo`]="{item}">
    
                    <div class="text-left">
    
                        <ProductNameDisplay :productNo="item.productNo" />
    
                    </div>
</template>
<template #[`item.type`]="{item}">
    <div class="text-left">
    
        <QnATitleDisplay :type="item.type" />
    
    </div>
</template>
<template #[`item.id`]="{item}">
    <div class="text-left">
    
        <HideId :id="item.id" />
    
    </div>
</template>
<template #[`item.regDate`]="{item}">
    <div>
    
        <DateDisplay :regDate="item.regDate" />
    
    </div>
</template>
        </v-data-table>
    </div>

    <v-row align="center" justify="space-between">
        <v-col cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon @click="getQnA">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto">
            <v-btn :to="`/writePost/product/${productNo}`" outlined>글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import QnATitleDisplay from '@/components/QnATitleDisplay.vue'
import ProductNameDisplay from '@/components/ProductNameDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        QnATitleDisplay,
        ProductNameDisplay,
    },
    props: ['productNo'],
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'qnaNo',
                    width: '10%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '상품명',
                    value: 'productNo',
                    width: '20%',
                    align: 'center',
                    divider: true
                },
                {
                    text: '제목',
                    value: 'type',
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
                    width: '15%',
                    align: 'center',
                },
            ],
            searches: [{
                    text: '상품명',
                    value: 'productname'
                }, {
                    text: '제목',
                    value: 'title'
                },
                {
                    text: '내용',
                    value: 'content'
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
        getQnA() {
            console.log(this.productNo);
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            axios.get(`/api/qna/getQnaByType`, {
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord: this.searchWord,
                    productNo: this.productNo,
                }
            }).then(res => {
                this.contents = res.data;
                this.totalContents = res.data;
                this.loading = false
                // axios.get('/api/qna/getCount', {
                //     params: {
                //         search: this.search,
                //         searchWord: this.searchWord,
                //         // type은 어짜피 상품이니 필요없고 상품번호 추가해서 새로 만드시면 됩니다
                //         productNo : this.productNo,
                //     }
                // }).then(res => {
                //     this.totalContents = res.data;
                //     this.loading = false
                // })
            })
        },
        moveto(item) {
            this.$router.push(`/qna/${item.qnaNo}`)
        },
    },
    watch: {
        options: {
            handler() {
                this.getQnA()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>

</style>
