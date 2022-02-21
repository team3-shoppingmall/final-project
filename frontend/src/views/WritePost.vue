<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="7">
            <div class="text-h3" v-if="originalNo == undefined">글쓰기</div>
            <div class="text-h3" v-if="originalNo != undefined">답변</div>
            <v-divider class="my-5"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr v-if="originalNo == undefined">
                                <td style="width:10%"> 제목 </td>
                                <td>
                                    <v-select v-model="titleSelected" :items="titles" v-if="!admin"></v-select>
                                    <v-text-field v-model="titleDetail" v-if="admin"></v-text-field>
                                </td>
                            </tr>
                            <tr v-if="this.pageID == 'faq'">
                                <td style="width:10%"> 종류 </td>
                                <td>
                                    <v-select v-model="faqTypeSelected" :items="faqType"></v-select>
                                </td>
                            </tr>
                            <tr>
                                <td> 내용 </td>
                                <td>
                                    <v-textarea counter rows="11" v-model="content" :rules="rules"></v-textarea>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined">
                                <td rowspan="5"> 파일 첨부 </td>
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined">
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined">
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined">
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined">
                                <td>
                                    <v-file-input accept="image/*"></v-file-input>
                                </td>
                            </tr>
                            <tr v-if="!admin">
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
                    <v-col cols="auto" v-if="originalNo != undefined">
                    <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="form" outlined>작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="formUpdate" outlined>수정</v-btn>
                    </v-col>
                        <v-btn @click="replyForm" outlined>답변 작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="qnaForm" outlined>QNA 작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="qnaFormUpdate" outlined>QNA 수정</v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn @click="moveToBefore" outlined>취소</v-btn>
                    </v-col>
                </v-row>
            </v-form>
        </v-col>
    </v-row>

</v-container>
</template>

<script>
import axios from 'axios'

export default {
    data() {
        return {
            pageID: '',
            admin: false,
            titles: [{
                    text: '제목을 선택해주세요',
                    value: 'default',
                    content: '',
                    type: 'all',
                    disabled: true,
                }, {
                    text: '상품 문의입니다',
                    value: 'product',
                    content: '상품 문의 관련 text',
                    type: 'productQnA',
                },
                {
                    text: '일반 문의입니다',
                    value: 'general',
                    content: '일반 문의 관련 text',
                    type: 'productQnA',
                }, {
                    text: '배송 문의입니다',
                    value: 'delivery',
                    content: '배송 문의 관련 text',
                    type: 'deliveryQnA',
                }, {
                    text: '주문 취소 문의입니다',
                    value: 'cancel',
                    content: '주문 취소 문의 관련 text',
                    type: 'beforeDeliveryQnA',
                },
                {
                    text: '상품 변경 문의입니다',
                    value: 'change',
                    content: '상품 변경 문의 관련 text',
                    type: 'beforeDeliveryQnA',
                },
                {
                    text: '주소 변경 문의입니다',
                    value: 'changeAddress',
                    content: '주소 변경 문의 관련 text',
                    type: 'beforeDeliveryQnA',
                }, {
                    text: '반품 문의입니다',
                    value: 'return',
                    content: '반품 문의 관련 text',
                    type: 'afterDeliveryQnA',
                },
                {
                    text: '교환 문의입니다',
                    value: 'exchange',
                    content: '교환 문의 관련 text',
                    type: 'afterDeliveryQnA',
                },
                {
                    text: '불량 상품/오배송 문의입니다',
                    value: 'error',
                    content: '불량 상품/오배송 문의 관련 text',
                    type: 'afterDeliveryQnA',
                },
            ],
            faqType: [{
                text: '상품 관련',
                value: 'product',
            }, {
                text: '배송 관련',
                value: 'delivery',
            }, {
                text: '교환/반품 관련',
                value: 'return',
            }, {
                text: '기타 관련',
                value: 'etc',
            }, ],
            rules: [v => v.length <= 600 || 'Max 600 characters'],
            num: '',
            titleSelected: 'default',
            titleDetail: '',
            faqTypeSelected: '',
            originalNo: '',
            content: '',
            image1: '',
            image2: '',
            image3: '',
            image4: '',
            image5: '',
            secret: true,
        }
    },
    methods: {
        currentURL() {
            let pageList = ['notice', 'faq']
            for (let i = 0; i < pageList.length; i++) {
                if (this.pageID == pageList[i]) {
                    this.admin = true;
                    this.titleSelected = this.pageID;
                }
            }
            if (this.admin == false) {
                this.setSelectItems();
            }
        },
        setSelectItems() {
            if (this.pageID != '') {
                for (let i = this.titles.length - 1; i > 0; i--) {
                    if (this.pageID != this.titles[i].type) {
                        this.titles.splice(i, 1);
                    }
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
        qnaForm() {
            if (this.pageID != 'notice' && this.pageID != 'faq') {
                if (this.titleSelected == 'default') {
                    alert('제목을 선택해주세요')
                }
            }
            axios({
                method: 'post',
                url: `/api/qna/insertqna`,
                data: {
                    type: this.titleSelected,
                    originalNo: null,
                    reply: false,
                    content: this.content,
                    id: "user123",
                    secret: this.secret,
                    image: "",
                }
            }).then((res)=>{
                console.log(res.data, res.status);
                alert("문의글 등록 완료");
                this.$router.go(-1);
            }).catch((err)=>{
                console.log(err);
            })
            
           
            // // notice or faq or qna관련
            // console.log(this.titleSelected);

            // // notice or faq일 경우 제목 보내는 용도
            // console.log(this.titleDetail);

            // // 아닐 경우 어떤 종류의 문의인지 찾기
            // console.log(this.titleSelected);

            // // 현재 내용
            // console.log(this.content);

            // 파일은 방법 찾아보시거나 일단 임시로 넣어서 실험하시면 됩니다

            // // 비밀글 여부
            // console.log(this.secret);

            //axios status==200 안으로 넣어야 함
            // alert('완료');
            // this.$router.go(-1);
        },
        replyForm(){
              axios({
                method: 'post',
                url: `/api/qna/insertqna`,
                data:{
                    type: this.titleSelected,
                    originalNo: this.originalNo,
                    reply: false,
                    content: this.content,
                    id: "admin",
                    secret: true,
                    image: ""
                }
            }).then((res)=>{
                console.log(res.data, res.status);
                alert("댓글 등록 완료");
                this.$router.go(-1);
            }).catch((err)=>{
                console.log(err);
            })
        },
        qnaFormUpdate() {
            // this.sendType => 게시글 종류(notice, faq, qna(product, delivery) 등)

            //axios status==200 안으로 넣어야 함
            // alert('완료');
            // this.$router.go(-1);
            axios({
                method: 'patch',
                url: `/api/qna/updateqna`,
                params: {
                    qnaNo : this.num,
                    type : this.titleSelected,
                    content : this.content,
                    secret : this.secret,
                    image: ""
                }
            }).then((res)=>{
                console.log(res.data, res.status);
                alert("수정 완료");
                this.$router.go(-1);
            }).catch((err)=>{
                console.log(err);
            })
        },

        moveToBefore() {
            this.$router.go(-1);
        },
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
        this.num = this.$route.params.num;
        this.originalNo = this.$route.params.original;
        if (this.num != '' && this.num != undefined) {
            // 기존 정보 가져와서 넣어주기
            // 만약 sendType이 notice나 faq면 관리자이니 admin true로 변경
        } else if (this.originalNo != '' && this.originalNo != undefined) {
            this.admin = true;
            this.titleSelected = 'reply'; 
        } else {
            this.currentURL();
        }
    }
}
</script>

<style>

</style>
