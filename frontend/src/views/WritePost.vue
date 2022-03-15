<template>
<v-container>
    <v-row justify="center">
        <v-col align-self="center" cols="9">
            <div class="text-h3" v-if="originalNo == undefined && !(pageID == 'qna' && admin == true)">글쓰기</div>

            <!-- 답글일 경우 출력 -->
            <div class="text-h3" v-if="originalNo != undefined || (pageID == 'qna' && admin == true)">답변</div>
            <div v-if="productNo != 0 && productNo != undefined">
                <ProductDetailDisplay :productNo="productNo" />
            </div>

            <v-divider class="my-5"></v-divider>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <tbody>
                            <tr v-if="originalNo == undefined && pageID != 'review' && !(pageID == 'qna' && admin == true)">
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
                                            <ckeditor :editor="editor" v-model="content" :config="editorConfig"></ckeditor>
                                            <span :class="contentColor" v-if="pageID != 'notice'">{{content.length}}/2000</span>
                                            <span :class="contentColor" v-if="pageID == 'notice'">{{content.length}}/10000</span>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                            <tr v-if="originalNo == undefined && pageID != 'faq' && !(pageID == 'qna' && admin == true)">
                                <td> 파일 첨부 </td>
                                <td>
                                    <v-row v-if="pageID != 'review'">
                                        <v-col cols="3" v-for="(idx) in imageFiles.length" :key="idx" align="center">
                                            <v-card :loading="false" class="my-5">
                                                <v-card-title>
                                                    <v-img height="250px" width="150px" :src="imageUrl[idx-1]" contain @click="fileInputClick(idx-1)" />
                                                </v-card-title>
                                                <v-card-actions>
                                                    <v-file-input v-model="imageFiles[idx-1]" :id="`fileInput${idx-1}`" accept="image/*" truncate-length="14" class="pa-0" hide-details @change="onImageChange(idx-1)"></v-file-input>
                                                </v-card-actions>
                                            </v-card>
                                        </v-col>
                                    </v-row>
                                    <v-row v-if="pageID == 'review'">
                                        <v-col align="center">
                                            <v-card :loading="false" class="mx-auto my-5">
                                                <v-card-title>
                                                    <v-img max-height="250" :src="imageUrl[0]" min-height="250" contain @click="fileInputClick(0)" />
                                                </v-card-title>
                                                <v-card-actions>
                                                    <v-file-input v-model="imageFiles[0]" :id="`fileInput0`" accept="image/*" truncate-length="14" class="pa-0" hide-details @change="onImageChange(0)"></v-file-input>
                                                </v-card-actions>
                                            </v-card>
                                        </v-col>
                                    </v-row>
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
                    <v-col cols="auto" v-if="num == undefined">
                        <v-btn @click="form" color="primary">작성</v-btn>
                    </v-col>
                    <v-col cols="auto" v-if="num != undefined">
                        <v-btn @click="updateForm" color="primary">수정</v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn @click="moveToBefore" color="primary">취소</v-btn>
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
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
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
                content: '<p>★배송전 상품 변경/취소/환불/주소지변경 등 처리는<br>꼭!<strong> &#39;배송전 변경/취소&#39; </strong>게시판에 남겨주세요!★<br><br>---------------------------------------------<br>주문번호:</p>',
                type: 'deliveryQnA',
            }, {
                text: '주문 취소 문의입니다',
                value: 'cancel',
                content: "<p><br>예치금 환불 시 철회 불가능하며<br>쿠폰/적립금과 중복 사용 불가능합니다.<br>쿠폰은 일회성으로 변심 취소 시 자동삭제/재지급이 불가능합니다.<br>이점 꼭 참고하여 예치금 환불 요청 부탁드리겠습니다:)<br><br>-----------------------------------------------<br>*주문취소*(전체취소시 상품명에  &#39;전체취소&#39; 꼭! 기재해주세요!)<br><br><strong>주문번호 :</strong><br><strong>상품명(옵션포함기재):</strong><br><strong>※결제시 입금한 이름의 계좌번호※</strong><br><strong>환불계좌번호:</strong><br><strong>은행사 :</strong><br><strong>예금주명 :</strong><br><br>♥해당 양식에 정확한 상품명 남겨주셔야 처리가 가능합니다♥</p>",
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
            star: 0,
            content: '',
            contentColor: 'black--text',
            imageFiles: [null],
            imageUrl: [null],
            secret: true,
            id: '',
        }
    },
    methods: {
        // 글쓰기일 경우 초기 설정
        currentURL() {
            if (this.pageID == 'notice' || this.pageID == 'faq') {
                this.admin = true;
                this.titleSelected = this.pageID;
            } else {
                for (let i = this.titles.length - 1; i > 0; i--) {
                    if (this.pageID != this.titles[i].type) {
                        this.titles.splice(i, 1);
                    }
                }
            }
            for (let j = this.imageFiles.length; j < 4; j++) {
                this.imageFiles.push(null);
            }
        },

        // 질문 종류 선택 시 내용에 양식 넣어줌
        setContent(target) {
            for (let i = 0; i < this.titles.length; i++) {
                if (target == this.titles[i].value) {
                    this.content = this.titles[i].content;
                }
            }
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

        // 취소
        moveToBefore() {
            this.$router.go(-1);
        },

        form() {
            if (this.admin) {
                if (this.titleDetail == '' && this.originalNo == undefined) {
                    alert('제목을 입력해주세요');
                    return;
                }
            } else {
                if (this.titleSelected == 'default') {
                    alert('제목을 선택해주세요')
                    return;
                }
            }

            if (this.content == '') {
                alert('내용을 입력해주세요');
                return;
            }

            let limit = 2000;
            if (this.pageID == 'notice') {
                limit = 10000;
            }
            if (this.content.length > limit) {
                alert('글자 수 제한을 넘기셨습니다');
                return;
            }

            if (this.originalNo != undefined) {
                this.replyForm();
            } else if (this.pageID == 'notice') {
                this.noticeForm();
            } else if (this.pageID == 'faq') {
                this.faqForm();
            } else {
                this.qnaForm();
            }
        },
        getData() {
            this.titleSelected = this.pageID;
            if (this.pageID == 'notice') {
                this.admin = true;
                this.getNotice();
            } else if (this.pageID == 'review') {
                this.getReview();
            } else if (this.pageID == 'faq') {
                this.admin = true;
                this.getFAQ();
            } else if (this.pageID == 'qna') {
                this.getQnA();
            }
        },
        updateForm() {
            if (!(this.pageID == 'qna' && this.admin)) {
                if (this.admin) {
                    if (this.titleDetail == '') {
                        alert('제목을 입력해주세요');
                        return;
                    }
                } else {
                    if (this.titleSelected == 'default') {
                        alert('제목을 선택해주세요')
                        return;
                    }
                }
            }

            if (this.content == '') {
                alert('내용을 입력해주세요');
                return;
            }

            let limit = 2000;
            if (this.pageID == 'notice') {
                limit = 10000;
            }
            if (this.content.length > limit) {
                alert('글자 수 제한을 넘기셨습니다');
                return;
            }

            if (this.pageID == 'notice') {
                this.noticeFormUpdate();
            } else if (this.pageID == 'review') {
                this.reviewFormUpdate();
            } else if (this.pageID == 'faq') {
                this.faqFormUpdate();
            } else {
                this.qnaFormUpdate();
            }
        },

        // 답변 작성
        replyForm() {
            axios({
                method: 'post',
                url: `/api/qna/insertReply`,
                data: {
                    type: this.pageID + 'Reply',
                    originalNo: this.originalNo,
                    content: this.content,
                    id: this.id,
                }
            }).then(() => {
                alert("답변 등록 완료");
                this.$router.go(-2);
            }).catch((err) => {
                console.log(err);
            })
        },

        // 글쓰기
        noticeForm() {
            let image = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    if (image == null) {
                        image = this.imageFiles[i].name;
                    } else {
                        image = image + ";" + this.imageFiles[i].name;
                    }
                }
            }
            let data = {
                title: this.titleDetail,
                content: this.content,
                id: this.id,
                image: image,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    formData.append(`fileList`, this.imageFiles[i])
                }
            }
            axios.post(`/api/notice/insertNotice`, formData)
                .then(() => {
                    alert("공지사항 등록 완료");
                    this.$router.go(-1);
                }).catch((err) => {
                    alert("등록 실패");
                    console.log(err);
                })
        },
        faqForm() {
            axios.post('/api/faq/insertfaq', {
                    type: this.faqTypeSelected,
                    title: this.titleDetail,
                    content: this.content
                })

                .then(() => {
                    alert("FAQ 등록 완료");
                    this.$router.go(-1);
                }).catch((err) => {
                    console.log(err);
                })

        },
        qnaForm() {
            let image = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    if (image == null) {
                        image = this.imageFiles[i].name;
                    } else {
                        image = image + ";" + this.imageFiles[i].name;
                    }
                }
            }
            let data = {
                productNo: this.productNo,
                type: this.titleSelected,
                reply: false,
                content: this.content,
                id: this.id,
                secret: this.secret,
                image: image,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    formData.append(`fileList`, this.imageFiles[i])
                }
            }
            axios.post(`/api/qna/insertqna`, formData)
                .then(() => {
                    alert("문의글이 등록되었습니다.");
                    this.$router.go(-1);
                }).catch((err) => {
                    console.log(err);
                })
        },
        // 수정 시 기존 데이터 넣기
        getNotice() {
            axios.get(`/api/notice/list/${this.num}`)
                .then((res) => {
                    this.titleDetail = res.data.title;
                    this.content = res.data.content;
                    let imageList = null;
                    if (res.data.image != null) {
                        imageList = res.data.image.split(';');
                        for (let i = 0; i < imageList.length; i++) {
                            axios.get(`/api/notice/noticeImage/${this.num}/${imageList[i]}`, {
                                    responseType: "blob",
                                })
                                .then(res => {
                                    if (i == 0) {
                                        this.imageFiles.pop();
                                    }
                                    var file = new File([res.data], imageList[i], {
                                        type: "image/*",
                                        lastModified: Date.now()
                                    });
                                    this.imageFiles.push(file);
                                    this.onImageChange(i);

                                    if (i == imageList.length - 1) {
                                        for (let j = i + 1; j < 4; j++) {
                                            this.imageFiles.push(null);
                                        }
                                    }
                                })
                        }
                    }
                }).catch((err) => {
                    alert("정보를 불러오는데 실패했습니다.");
                    console.log(err);
                })
        },
        getReview() {
            axios.get(`/api/review/getReview/${this.num}`)
                .then((res) => {
                    this.content = res.data.content;
                    this.star = res.data.star;
                    if (res.data.image != null) {
                        axios.get(`/api/review/reviewImage/${this.num}/${res.data.image}`, {
                                responseType: "blob",
                            })
                            .then(res => {
                                this.imageFiles.pop();
                                let file = new File([res.data], res.data.image, {
                                    type: "image/*",
                                    lastModified: Date.now()
                                });
                                this.imageFiles.push(file);
                                this.onImageChange(0);
                            })
                    }
                }).catch((err) => {
                    alert("정보를 불러오는데 실패했습니다.");
                    console.log(err);
                })
        },
        getFAQ() {
            axios.get(`/api/faq/getFaq/${this.num}`)
                .then((res) => {
                    this.titleDetail = res.data.title;
                    this.faqTypeSelected = res.data.type;
                    this.content = res.data.content;
                }).catch((err) => {
                    alert("정보를 불러오는데 실패했습니다.");
                    console.log(err);
                })
        },
        getQnA() {
            axios.get(`/api/qna/getQna/${this.num}`)
                .then(res => {
                    if (res.data.id == 'spring') {
                        this.admin = true;
                    }
                    this.titleSelected = res.data.type;
                    this.content = res.data.content;
                    this.secret = res.data.secret;
                    if (res.data.image != null) {
                        let imageList = res.data.image.split(';');
                        for (let i = 0; i < imageList.length; i++) {
                            axios.get(`/api/qna/qnaImage/${this.num}/${imageList[i]}`, {
                                    responseType: "blob",
                                })
                                .then(res => {
                                    if (i == 0) {
                                        this.imageFiles.pop();
                                    }
                                    var file = new File([res.data], imageList[i], {
                                        type: "image/*",
                                        lastModified: Date.now()
                                    });
                                    this.imageFiles.push(file);
                                    this.onImageChange(i);

                                    if (i == imageList.length - 1) {
                                        for (let j = i + 1; j < 4; j++) {
                                            this.imageFiles.push(null);
                                        }
                                    }
                                })
                        }
                    }
                }).catch((err) => {
                    alert("정보를 불러오는데 실패했습니다.");
                    console.log(err);
                })
        },

        // 수정
        noticeFormUpdate() {
            let image = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    if (image == null) {
                        image = this.imageFiles[i].name;
                    } else {
                        image = image + ";" + this.imageFiles[i].name;
                    }
                }
            }
            let data = {
                noticeNo: this.num,
                title: this.titleDetail,
                content: this.content,
                image: image,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    formData.append(`fileList`, this.imageFiles[i])
                }
            }
            axios.patch(`/api/notice/updateNotice`, formData)
                .then(() => {
                    alert("공지사항 수정 완료");
                    this.$router.go(-1);
                }).catch((err) => {
                    alert("수정 실패");
                    console.log(err);
                })
        },
        reviewFormUpdate() {
            let image = null;
            if (this.imageFiles[0] != null) {
                image = this.imageFiles[0].name;
            }
            let data = {
                reviewNo: this.num,
                content: this.content,
                star: this.star,
                image: image,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));

            formData.append(`fileList`, this.imageFiles[0]);
            axios.patch(`/api/review/update`, formData)
                .then(() => {
                    alert("수정이 완료되었습니다.")
                    this.$router.go(-1);
                }).catch((err) => {
                    alert('수정에 실패하셨습니다.');
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
            }).then(() => {
                alert("FAQ 수정 완료");
                this.$router.go(-1);
            })
        },
        qnaFormUpdate() {
            let image = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    if (image == null) {
                        image = this.imageFiles[i].name;
                    } else {
                        image = image + ";" + this.imageFiles[i].name;
                    }
                }
            }
            let data = {
                productNo: this.productNo,
                qnaNo: this.num,
                type: this.titleSelected,
                content: this.content,
                secret: this.secret,
                image: image,
            };
            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }));
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] != null) {
                    formData.append(`fileList`, this.imageFiles[i])
                }
            }
            axios.patch(`/api/qna/updateqna`, formData)
                .then(() => {
                    alert("수정이 완료되었습니다.");
                    this.$router.go(-1);
                }).catch((err) => {
                    console.log(err);
                    alert("수정에 실패했습니다.");
                })
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        content: {
            handler() {
                let limit = 2000;
                if (this.pageID == 'notice') {
                    limit = 10000;
                }
                // 2000자를 넘어서면 빨간 글씨(공지사항은 1만자)
                if (this.content.length > limit) {
                    this.contentColor = 'red--text';
                } else {
                    this.contentColor = 'black--text';
                }
            }
        },
    },
    mounted() {
        this.id = this.getLogin.user.id;
        // notice, review, faq, qna
        this.pageID = this.$route.params.id;
        // 수정용 게시글 번호
        this.num = this.$route.params.num;
        // 답글용 원글 번호
        this.originalNo = this.$route.params.original;
        // 상세 페이지에서 문의 버튼 클릭 시 상품 번호
        this.productNo = this.$route.params.productNo;

        if (this.$route.params.num != undefined) {
            // 수정
            this.getData();
        } else if (this.$route.params.original != undefined) {
            // 답변
            this.admin = true;
        } else {
            // 단순 글쓰기 
            this.currentURL();
        }
    }
}
</script>

<style>

</style>
