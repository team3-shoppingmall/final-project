<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="7">
            <div class="text-h3" v-if="originalNo == undefined">글쓰기</div>
            <div class="text-h3" v-if="originalNo != undefined">답변</div>
            <div v-if="productNo != 0 && productNo != undefined">
                <ProductDetailDisplay :productNo="productNo" />
            </div>
            <v-divider class="my-5"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr v-if="originalNo == undefined && pageID != 'review'">
                                <td style="width:10%"> 제목 </td>
                                <td>
                                    <v-select v-model="titleSelected" :items="titles" v-if="!admin" @change="setContent(titleSelected)"></v-select>
                                    <v-text-field v-model="titleDetail" v-if="admin"></v-text-field>
                                </td>
                            </tr>
                            <tr v-if="this.pageID == 'faq'">
                                <td style="width:10%"> 종류 </td>
                                <td>
                                    <v-select v-model="faqTypeSelected" :items="faqType"></v-select>
                                </td>
                            </tr>
                            <tr v-if="this.pageID == 'review'">
                                <td style="width:10%"> 별점 </td>
                                <td>
                                    <v-rating background-color="grey lighten-2" color="orange" empty-icon="mdi-star-outline" full-icon="mdi-star" hover length="5" size="64" v-model="star"></v-rating>
                                </td>
                            </tr>
                            <tr>
                                <td> 내용 </td>
                                <td>
                                    <v-row>
                                        <v-col>
                                            <div v-html="content" style="border:1px black solid"></div>
                                            <div style="border:1px black solid">{{content}}</div>
                                            <ckeditor :editor="editor" v-model="content" :config="editorConfig"></ckeditor>
                                            <span :class="contentColor">{{content.length}}/2000</span>
                                        </v-col>
                                    </v-row>
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
                            <tr v-if="!admin && pageID != 'review'">
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
                        <v-btn @click="replyForm" outlined>답변 작성</v-btn>
                    </v-col>
                    <!-- <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="form" outlined>작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="formUpdate" outlined>수정</v-btn>
                    </v-col> -->

                    <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="noticeForm" outlined>Notice 작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="noticeFormUpdate" outlined>Notice 수정</v-btn>
                    </v-col>

                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="reviewFormUpdate" outlined>review 수정</v-btn>
                    </v-col>

                    <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="qnaForm" outlined>QNA 작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="qnaFormUpdate" outlined>QNA 수정</v-btn>
                    </v-col>

                    <v-col cols="auto" v-if="(num == '' || num == undefined) && originalNo == undefined">
                        <v-btn @click="faqForm" outlined>FAQ 작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != '' && num != undefined">
                        <v-btn @click="faqFormUpdate" outlined>FAQ 수정</v-btn>

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
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import ProductDetailDisplay from '@/components/ProductDetailDisplay.vue'
export default {
    components: {
        ProductDetailDisplay,
    },
    data() {
        return {
            editor: ClassicEditor,
            editorConfig: {
                ckfinder: {},
            },
            pageID: '',
            num: '',
            originalNo: '',
            productNo: '',
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
                content: '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>&#8251;게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br></p>',
                type: 'product',
            }, {
                text: '상품 문의입니다',
                value: 'product',
                content: '<p>이곳은 상품문의를 위한 게시판입니다.<br><br>&#8251;게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br></p>',
                type: 'productQnA',
            }, {
                text: '일반 문의입니다',
                value: 'general',
                content: '<p>이곳은 일반문의를 위한 게시판입니다.<br>상품과 관련된 문의는 제목을 상품문의로 선택해주세요!<br><br>&#8251;게시판 성격에 맞지 않는 내용을 문의주실 경우 처리가 불가할 수 있습니다.<br><br>---------------------------------------------<br></p>',
                type: 'productQnA',
            }, {
                text: '배송 문의입니다',
                value: 'delivery',
                content: "<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> '배송전 변경/취소' </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호:</p>",
                type: 'deliveryQnA',
            }, {
                text: '주문 취소 문의입니다',
                value: 'cancel',
                content: "<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에 '전체취소' 꼭! 기재해주세요!)<br><br><strong>주문번호 :</strong><br><strong>상품명(옵션포함기재):</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호:</strong><br><strong>은행사 :</strong><br><strong>예금주명 :</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>",
                type: 'beforeDeliveryQnA',
            }, {
                text: '상품 변경 문의입니다',
                value: 'change',
                content: '<p>♥해당 양식에 정확한 상품명을 기재해주셔야 처리가 가능합니다♥<br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*상품변경*<br><br>주문번호 :<br>변경전 상품명 (사이즈,컬러) :<br>변경후 상품명 (사이즈,컬러) :</p>',
                type: 'beforeDeliveryQnA',
            }, {
                text: '주소 변경 문의입니다',
                value: 'changeAddress',
                content: '<p>♥해당 양식에 정확한 주문번호를 기재해주셔야 처리가 가능합니다♥<br><br><br>-----------------------------------------------<br>*주소지 변경*<br><br>주문번호 :<br>변경 주소지(번지수포함) :</p>',
                type: 'beforeDeliveryQnA',
            }, {
                text: '반품 문의입니다',
                value: 'return',
                content: '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*반품*<br><br><strong>주문번호 :</strong><br><strong>상품명(사이즈,컬러):</strong><br><strong>반품 사유 :</strong><br><strong>&#8251;결제시 입금한 이름의 계좌번호&#8251;</strong><br><strong>환불계좌번호(은행사포함) :</strong><br><strong>예금주명 :</strong><br><br>(수령주소지로 자동 회수접수)<br>&#8251;회수주소 변경 원하실 경우에만 새주소지와 함께 기재해 주세요.<br><br>회수주소:<br><br>--------------------------------------<br>&nbsp;</p>',
                type: 'afterDeliveryQnA',
            }, {
                text: '교환 문의입니다',
                value: 'exchange',
                content: '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>--------------------------------------<br>*교환*<br><br><strong>주문번호 :</strong><br><strong>교환전 상품명(사이즈,컬러) :</strong><br><strong>교환후 상품명(사이즈,컬러) :</strong><br><strong>왕복 배송비 입금자명/입금날짜 :</strong><br><br>(수령주소지로 자동 회수접수)<br>※회수/교환상품수령지 변경 원하실 경우에만 새주소지 함께 기재해 주세요.<br><br>회수주소:<br>교환상품 수령 주소:<br>&nbsp;</p>',
                type: 'afterDeliveryQnA',
            }, {
                text: '불량 상품/오배송 문의입니다',
                value: 'error',
                content: '<p>♥해당 양식에 정확한 정보를 기재해주셔야 처리가 가능합니다♥<br><br>--------------------------------------<br>*불량/오배송*<br><br><strong>▷바코드(검수완료)사진</strong><br><strong>▷불량사진</strong><br><strong>(필수첨부 부탁드립니다!)</strong><br><br>주문번호 :<br>교환/반품 (원하시는 처리 선택해주세요!) :<br>상품 수령일자 : ( / )<br>반품 접수일자 : ( / )<br>상품명(사이즈,컬러) :<br>불량/오배송 사유 :<br>검수번호(숫자나 알파벳) :<br><br>(상품바코드옆 검수자 숫자한자리/두자리를 기재합니다.)</p>',
                type: 'afterDeliveryQnA',
            }, ],
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
            titleSelected: 'default',
            titleDetail: '',
            faqTypeSelected: '',
            star: '',
            content: '',
            contentColor: 'black--text',
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
                    return;
                }
            }

            axios({
                method: 'post',
                url: `/api/qna/insertqna`,
                data: {
                    type: this.titleSelected,
                    reply: false,
                    content: this.content,
                    id: "user123",
                    secret: this.secret,
                    image: "image1.jpg"
                }
            }).then((res) => {
                if (res.status == 200) {
                    console.log(res.data);
                    alert("문의글이 등록되었습니다.");
                    this.$router.go(-1);
                }
            }).catch((err) => {
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

            // alert('완료');
            // this.$router.go(-1);
        },
        noticeForm() {
            axios({
                method: 'post',
                url: `/api/notice/insertNotice`,
                data: {
                    title: this.titleDetail,
                    content: this.content,
                    id: "admin123",
                    image: "",
                }
            }).then((res) => {
                {
                    console.log(res.data, res.status);
                    alert("공지사항 등록 완료");
                    this.$router.go(-1);
                }
            }).catch((err) => {
                alert("등록 실패");
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
        getReview() {
            axios({
                method: 'get',
                url: `/api/review/detail`,
                params: {
                    reviewNo: this.num
                }
            }).then((res) => {
                this.content = res.data.content;
                this.star = res.data.star;
            })
        },
        getQnA() {
            axios({
                method: 'get',
                url: `/api/qna/getqnabyqnaNo`,
                params: {
                    qnaNo: this.num
                }
            }).then((res) => {
                this.titleSelected = res.data.type;
                this.content = res.data.content;
            }).catch((err) => {
                console.log(err);
            })
        },
        getNotice() {
            axios.get(`/api/notice/list/${this.num}`).then (res => {
                this.titleDetail = res.data.title;
                this.content = res.data.content;
                console.log(res.status);
            }).catch((err) => {
                alert("목록을 불러오는데 실패했습니다.");
                console.log(err);
            })
        },

        noticeFormUpdate() {
            axios({
                method: 'patch',
                url: `/api/notice/updateNotice`,
                params: {
                    noticeNo: this.num,
                    title: this.titleDetail,
                    content: this.content,
                    image: "",
                }
            }).then((res) => {
                {
                    this.titleDetail = res.data.content;
                    this.content = res.data.content;
                    console.log(res.data, res.status);
                    alert("공지사항 수정 완료");
                    this.$router.go(-1);
                }
            }).catch((err) => {
                alert("수정 실패");
                console.log(err);
            })
        },

        moveToBefore() {
            this.$router.go(-1);
        },

        reviewFormUpdate() {
            axios({
                    method: 'patch',
                    url: `/api/review/update`,
                    params: {
                        reviewNo: this.num,
                        content: this.content,
                        star: this.star
                    }
                })
                .then(() => {
                    alert("수정이 완료되었습니다.")
                    this.$router.go(-1);
                }).catch((err) => {
                    alert('수정에 실패하셨습니다.');
                    console.log(err);
                })
        },
        replyForm() {
            console.log(this.pageID);
            axios({
                method: 'post',
                url: `/api/qna/insertReply`,
                data: {
                    type: this.pageID + 'Reply',
                    originalNo: this.originalNo,
                    content: this.content,
                    id: "admin",
                    image: "image1.jpg"
                }
            }).then((res) => {
                console.log(res.data, res.status);
                alert("답변 등록 완료");
                this.$router.go(-2);
            }).catch((err) => {
                console.log(err);
            })
        },
        faqForm() {
            axios.post('/api/faq/insertfaq', {
                    type: this.faqTypeSelected,
                    title: this.titleDetail,
                    content: this.content
                })

                .then((res) => {
                    if (res.status == 200) {
                        console.log(res.data, res.status);
                        alert("FAQ 등록 완료");
                        this.$router.go(-1);
                    }
                }).catch((err) => {
                    console.log(err);
                })

        },
        faqFormUpdate() {
            axios({
                    method: 'patch',
                    url: `/api/faq/updatefaq`,
                    params: {
                        faqNo: this.num,
                        type: this.faqTypeSelected,
                        title: this.titleDetail,
                        content: this.content,

                    }
                })
                .then((res) => {
                    if (res.status == 200) {
                        alert("수정이 완료되었습니다.")
                        this.$router.go(-1);
                    }
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
                    qnaNo: this.num,
                    type: this.titleSelected,
                    content: this.content,
                    secret: this.secret,
                    image: ""
                }
            }).then((res) => {
                console.log(res.data);
                alert("수정이 완료되었습니다.");
                this.$router.go(-1);
            }).catch((err) => {
                console.log(err);
                alert("수정에 실패했습니다.");
            })
        },
        setContent(target) {
            for (let i = 0; i < this.titles.length; i++) {
                if (target == this.titles[i].value) {
                    this.content = this.titles[i].content;
                }
            }
        }
        /* 수정기능 완성 x
        faqFormUpdate() {

            axios({
                method: 'patch',
                url: `/api/faq/updatefaq`,
                params: {
                    faqNo: this.faqNo,
                    type: this.faqTypeSelected,
                    title: this.titleDetail,
                    content: this.content,

                }
            }).then((res) => {
                console.log(res.data, res.status);
                alert("수정 완료");
                this.$router.go(-1);
            }).catch((err) => {
                console.log(err);
            })
        }, */
    },

    watch: {
        content: {
            handler() {
                // 2000자를 넘어서면 빨간 글씨
                if (this.content.length > 2000) {
                    this.contentColor = 'red--text';
                } else {
                    this.contentColor = 'black--text';
                }
            }
        }
    },
    mounted() {
        // notice, review, faq, qna
        this.pageID = this.$route.params.id;
        // 수정용 게시글 번호
        this.num = this.$route.params.num;
        // 답글용 원글 번호
        this.originalNo = this.$route.params.original;
        // 상세 페이지에서 문의 버튼 클릭 시 상품 번호
        this.productNo = this.$route.params.productNo;

        // 수정
        if (this.num != '' && this.num != undefined) {
            if (this.pageID == 'notice' || this.pageID == 'faq') {
                this.admin = true;
            }
            this.titleSelected = this.pageID;
            if (this.pageID == 'notice') {
                this.getNotice();
            } else if (this.pageID == 'review') {
                this.getReview();
            } else if (this.pageID == 'faq') {
                this.getFAQ();
            } else if (this.pageID == 'qna') {
                this.getQnA();
            }
        } else if (this.originalNo != '' && this.originalNo != undefined) {
            // 답변
            this.admin = true;
            this.titleSelected = 'reply';
        } else {
            // 단순 글쓰기 
            this.currentURL();
        }
    }
}
</script>

<style>

</style>
