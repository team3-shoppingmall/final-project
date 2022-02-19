<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="7">
            <div class="text-h3">글쓰기</div>
            <v-divider class="my-5"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr>
                                <td style="width:10%"> 제목 </td>
                                <td>
                                    <v-select v-model="titleSelected" :items="titles" v-if="!admin"></v-select>
                                    <v-text-field v-model="titleDetail" v-if="admin"></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 내용 </td>
                                <td>
                                    <v-textarea counter name="input-7-1" rows="11" v-model="content" :rules="rules"></v-textarea>
                                </td>
                            </tr>
                            <tr>
                                <td rowspan="5"> 파일 첨부 </td>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀글 </td>
                                <td>
                                    <v-radio-group v-model="secret" row>
                                        <v-radio label="비밀글" :value="true"></v-radio>
                                        <v-radio label="공유글" :value="false"></v-radio>
                                    </v-radio-group>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
                <v-divider></v-divider>
                <v-row justify="end" class="mt-3">
                    <v-col cols="auto">
                        <v-btn @click="form">작성</v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn @click="moveto">취소</v-btn>
                    </v-col>
                </v-row>
            </v-form>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
// import axios from 'axios'

export default {
    data() {
        return {
            pageID: '',
            admin: false,
            titles: [],
            productTitles: [{
                    text: '제목을 선택해주세요',
                    value: 'default',
                    content: '',
                }, {
                    text: '상품 문의입니다',
                    value: 'product',
                    content: '상품 문의 관련 text',
                },
                {
                    text: '일반 문의입니다',
                    value: 'general',
                    content: '일반 문의 관련 text',
                },
            ],
            deliveryTitles: [{
                text: '제목을 선택해주세요',
                value: 'default',
                content: '',
            }, {
                text: '배송 문의입니다',
                value: 'delivery',
                content: '배송 문의 관련 text',
            }, ],
            beforeDeliveryTitles: [{
                    text: '제목을 선택해주세요',
                    value: 'default',
                    content: '',
                }, {
                    text: '주문 취소 문의입니다',
                    value: 'cancel',
                    content: '주문 취소 문의 관련 text',
                },
                {
                    text: '상품 변경 문의입니다',
                    value: 'change',
                    content: '상품 변경 문의 관련 text',
                },
                {
                    text: '주소 변경 문의입니다',
                    value: 'changeAddress',
                    content: '주소 변경 문의 관련 text',
                }
            ],
            afterDeliveryTitles: [{
                    text: '제목을 선택해주세요',
                    value: 'default',
                    content: '',
                }, {
                    text: '반품 문의입니다',
                    value: 'return',
                    content: '반품 문의 관련 text',
                },
                {
                    text: '교환 문의입니다',
                    value: 'exchange',
                    content: '교환 문의 관련 text',
                },
                {
                    text: '불량 상품/오배송 문의입니다',
                    value: 'error',
                    content: '불량 상품/오배송 문의 관련 text',
                }
            ],
            titleSelected: 'default',
            titleDetail: '',
            content: '',
            secret: true,
            rules: [v => v.length <= 600 || 'Max 600 characters'],
        }
    },
    methods: {
        currentURL() {
            let pageList = ['notice', 'faq']
            for (let i = 0; i < pageList.length; i++) {
                if (this.pageID.indexOf(pageList[i]) != -1) {
                    this.admin = true;
                    this.titleSelected = 'notice';
                }
            }
        },
        moveto() {
            if (this.pageID == 'notice' || this.pageID == 'faq') {
                this.$router.push(`/community/${this.pageID}`)
            } else {
                this.$router.push(`/qna/${this.pageID}`)
            }
        },
        form() {
            if (this.pageID != 'notice' && this.pageID != 'faq') {
                if (this.titleSelected == 'default') {
                    alert('제목을 선택해주세요')
                }
            }

            // // notice or faq인지 여부 찾기
            // console.log(this.pageID);

            // // notice or faq일 경우 제목 보내는 용도
            // console.log(this.titleDetail);

            // // 아닐 경우 어떤 종류의 문의인지 찾기
            // console.log(this.titleSelected);

            // // 현재 내용
            // console.log(this.content);

            // 파일은 방법 찾아보시거나 일단 임시로 넣어서 실험하시면 됩니다

            // // 비밀글 여부
            // console.log(this.secret);

        },
        setSelectItems() {
            if (this.pageID == 'productQnA') {
                this.titles = this.productTitles;
            } else if (this.pageID == 'deliveryQnA') {
                this.titles = this.deliveryTitles;
            } else if (this.pageID == 'beforeDeliveryQnA') {
                this.titles = this.beforeDeliveryTitles;
            } else if (this.pageID == 'afterDeliveryQnA') {
                this.titles = this.afterDeliveryTitles;
            }
        }
    },

    watch: {
        titleSelected: {
            handler() {
                for (let i = 0; i < this.titles.length; i++) {
                    if (this.titleSelected == this.titles[i].value) {
                        this.content = this.titles[i].content;
                    }
                }
            }
        }
    },
    mounted() {
        this.pageID = this.$route.params.id;
        this.currentURL();
        this.setSelectItems();
    }
}
</script>

<style>

</style>
