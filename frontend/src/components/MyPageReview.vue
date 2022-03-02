<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="reviewNo" class="elevation-1" disable-sort no-data-text="검색된 자료가 없습니다">
        <template #[`item.image`]="{item}">
            <v-row justify="center">
                <v-col cols="auto">
                    <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                        <v-carousel-item>
                            <v-dialog max-width="700">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/review/reviewImage/${item.reviewNo}/${item.image}`" contain v-if="item.image != null"></v-img>
                                </template>
                                <v-card>
                                    <v-img :src="`/api/review/reviewImage/${item.reviewNo}/${item.image}`"></v-img>
                                </v-card>
                            </v-dialog>
                        </v-carousel-item>
                    </v-carousel>
                </v-col>
            </v-row>
        </template>
        <template #[`item.star`]="{item}">
            <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" length="5" readonly size="10" :value="item.star"></v-rating>
        </template>
        <template #[`item.content`]="{item}">
            <v-row justify="space-between" align="center">
                <v-col>
                    <div v-html="item.content" class="text-left"></div>
                </v-col>
                <v-col cols="auto">
                    <v-icon @click="updateReview(item.reviewNo)" v-if="admin">mdi-pencil</v-icon>
                    <v-icon @click="deleteReview(item.reviewNo)" v-if="admin">mdi-delete</v-icon>
                </v-col>
            </v-row>
        </template>
        <template #[`item.productName`]="{item}">
            <v-btn text :to="`/productDetail/${item.productNo}`">
                <div class="text-left text-truncate" style="max-width: 100px;">
                    {{ item.productName }}
                </div>
            </v-btn>
        </template>
        <template #[`item.regDate`]="{item}">
            <DateDisplay :regDate="item.regDate" />
        </template>
    </v-data-table>

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
                    <v-btn icon="icon" @click="getReview">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import DateDisplay from '@/components/DateDisplay.vue'
export default {
    components: {
        DateDisplay,
    },
    // props: ['id'],
    data() {
        return {
            admin: true,
            dialog: false,
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                text: '번호',
                value: 'reviewNo',
                width: '7%',
                align: 'center',
                divider: true
            }, {
                text: '이미지',
                value: 'image',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '상품명',
                value: 'productName',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '별점',
                value: 'star',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '후기',
                value: 'content',
                width: '54%',
                align: 'center',
                divider: true
            }, {
                text: '작성일',
                value: 'regDate',
                width: '10%',
                align: 'center'
            }],
            searches: [{
                text: '작성자',
                value: 'id'
            }],
            search: 'id',
            searchWord: '',
            star: 5,
            content: '',
            contentColor: 'black--text',
            imageFile: '',
            imageUrl: '',

            id: 'tester',
        }
    },
    methods: {
        // productNo 대신 id로
        // 여기서 id: this.id 로 넣어서 해주세요
        getReview() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options;
            axios({
                method: 'get',
                url: `/api/review/getAllReviews`,
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord: this.searchWord,
                    productNo: this.productNo
                }
            }).then(res => {
                this.contents = res.data.reviewList;
                this.totalContents = res.data.count;
            }).finally(() => {
                this.loading = false;
            })
        },

        // 이미지
        fileInputClick() {
            document.getElementById(`fileInput`).click();
        },
        onImageChange() {
            const file = this.imageFile;
            if (file) {
                this.imageUrl = URL.createObjectURL(file);
                URL.revokeObjectURL(file);
            } else {
                this.imageUrl = null;
            }
        },

        addReview() {
            if (this.content == '') {
                alert('후기를 입력해주세요');
                return;
            }
            let data = {
                productNo: this.productNo,
                star: this.star,
                content: this.content,
                image: this.imageFile.name,
                id: this.id,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            formData.append(`fileList`, this.imageFile);
            axios.post(`/api/review/insert`, formData)
                .then(() => {
                    this.dialog = false;
                    this.content = '';
                    alert("리뷰 등록 완료");
                    this.$router.go();
                }).catch((err) => {
                    alert('리뷰 작성에 실패했습니다.')
                    console.log(err);
                })

            // axios({
            //     method: 'post',
            //     url: `/api/review/insert`,
            //     data: {
            //         productNo: this.productNo,
            //         star: this.star,
            //         content: this.content,
            //         image: this.image,
            //         id: this.id,
            //     }
            // }).then(() => {
            //     this.dialog = false;
            //     this.content = '';
            //     alert("리뷰 등록 완료");
            //     this.$router.go();
            // }).catch((err) => {
            //     alert('리뷰 작성에 실패했습니다.')
            //     console.log(err);
            // })
        },
        deleteReview(num) {
            axios.delete(`/api/review/delete/${num}`)
                .then(() => {
                    alert("삭제가 완료되었습니다.")
                    this.$router.go();
                })
        },
        updateReview(num) {
            this.$router.push(`/updatePost/review/${num}`);
        }
    },
    watch: {
        options: {
            handler() {
                this.getReview()
            },
            deep: true
        },
        content: {
            handler() {
                if (this.content.length > 600) {
                    this.contentColor = 'red--text';
                } else {
                    this.contentColor = 'black--text';
                }
            }
        }
    }
}
</script>

<style>
.ck-editor__editable {
    min-height: 200px;
}
</style>
