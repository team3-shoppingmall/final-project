<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" item-key="reviewNo" class="elevation-1" disable-sort no-data-text="검색된 자료가 없습니다">
        <template #[`item.image`]="{item}">
            <v-row justify="center">
                <v-col cols="auto">
                    <v-carousel :show-arrows="false" cycle interval="3000" hide-delimiters style="height:100px;width:100px">
                        <v-carousel-item v-for="(image,i) in item.image.split(';')" :key="i">
                            <v-dialog max-width="700">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-img v-bind="attrs" v-on="on" min-height="100" max-height="100" :src="`/api/review/reviewImage/${item.productNo}/${image}`" contain></v-img>
                                </template>
                                <v-card>
                                    <v-img :src="`/api/review/reviewImage/${item.productNo}/${image}`"></v-img>
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
        <template #[`item.id`]="{item}">
            <div>
                <HideId :id="item.id" />
            </div>
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
        <v-col cols="auto" class="mr-3">
            <v-row>
                <v-dialog v-model="dialog" persistent max-width="750px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn outlined v-bind="attrs" v-on="on">
                            리뷰 작성하기
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            <span class="text-h5">리뷰 작성</span>
                        </v-card-title>
                        <v-card-text>
                            <v-container>
                                <v-row>
                                    <v-col cols="12">
                                        상품 정보
                                        <ProductDetailDisplay :productNo="productNo" />
                                    </v-col>
                                    <v-col cols="12">
                                        별점
                                        <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" hover length="5" size="64" v-model="star"></v-rating>
                                    </v-col>
                                    <v-col cols="12">
                                        리뷰
                                        <ckeditor :editor="editor" v-model="content" :config="editorConfig"></ckeditor>
                                        <span :class="contentColor">{{content.length}}/600</span>
                                    </v-col>
                                    <v-col cols="12" v-for="(idx) in 4" :key="idx" align="center">
                                        <v-card :loading="false" class="mx-auto my-5">
                                            <v-card-title>
                                                <v-img max-height="250" :src="imageUrl[idx-1]" min-height="250" contain @click="fileInputClick(idx-1)" />
                                            </v-card-title>
                                            <v-card-actions>
                                                <v-file-input v-model="imageFiles[idx-1]" :id="`fileInput${idx-1}`" accept="image/*" truncate-length="14" class="pa-0" hide-details @change="onImageChange(idx-1)"></v-file-input>
                                            </v-card-actions>
                                        </v-card>
                                    </v-col>
                                </v-row>
                            </v-container>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="dialog = false">
                                Close
                            </v-btn>
                            <v-btn color="blue darken-1" text @click="addReview">
                                Save
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        ProductDetailDisplay,
    },
    props: ['productNo'],
    data() {
        return {
            editor: ClassicEditor,
            editorConfig: {
                ckfinder: {},
                toolbar: {
                    shouldNotGroupWhenFull: true
                }
            },
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
                text: '별점',
                value: 'star',
                width: '10%',
                align: 'center',
                divider: true
            }, {
                text: '후기',
                value: 'content',
                width: '58%',
                align: 'center',
                divider: true
            }, {
                text: '작성자',
                value: 'id',
                width: '7%',
                align: 'center',
                divider: true
            }, {
                text: '작성일',
                value: 'regDate',
                width: '8%',
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
            imageFiles: [null, null, null, null],
            imageUrl: [null, null, null, null],

        }
    },
    methods: {
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
        fileInputClick(idx) {
            document.getElementById(`fileInput${idx}`).click();
        },
        onImageChange(index) {
            const file = this.imageFiles[index];
            if (file) {
                this.imageUrl[index] = URL.createObjectURL(file);
                URL.revokeObjectURL(file);
            } else {
                this.imageUrl[index] = null;
            }
        },

        addReview() {
            if (this.content == '') {
                alert('후기를 입력해주세요');
                return;
            }

            // let image = null;
            // for (let i = 0; i < this.imageFiles.length; i++) {
            //     if (this.imageFiles[i] != null) {
            //         if (image == null) {
            //             image = this.imageFiles[i].name;
            //         } else {
            //             image = image + ";" + this.imageFiles[i].name;
            //         }
            //     }
            // }
            // let data = {
            //     productNo: this.productNo,
            //     star: this.star,
            //     content: this.content,
            //     image: image,
            //     id: "test1",
            // };
            // let formData = new FormData();
            // formData.append('data', new Blob([JSON.stringify(data)], {
            //     type: "application/json"
            // }));
            // for (let i = 0; i < this.imageFiles.length; i++) {
            //     if (this.imageFiles[i] != null) {
            //         formData.append(`fileList`, this.imageFiles[i])
            //     }
            // }
            // axios.post(`/api/review/insert`, formData)
            //     .then(() => {
            //         this.dialog = false;
            //         this.content = '';
            //         alert("리뷰 등록 완료");
            //         this.$router.go();
            //     }).catch((err) => {
            //         alert('리뷰 작성에 실패했습니다.')
            //         console.log(err);
            //     })

            axios({
                method: 'post',
                url: `/api/review/insert`,
                data: {
                    productNo: this.productNo,
                    star: this.star,
                    content: this.content,
                    image: this.image,
                    id: "test1",
                }
            }).then(() => {
                this.dialog = false;
                this.content = '';
                alert("리뷰 등록 완료");
                this.$router.go();
            }).catch((err) => {
                alert('리뷰 작성에 실패했습니다.')
                console.log(err);
            })
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
