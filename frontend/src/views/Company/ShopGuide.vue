<template>
<v-container>
    <v-row justify="center" class="mt-1">
        <v-col xs="12" sm="12" md="12" lg="9" xl="9">
            <div class="text-h3 mb-5">SHOP GUIDE</div>
            <v-card elevation="0" v-for="(content, idx) in contents" :key="idx" :id="`${content.value}Selected`">
                <v-card-title>
                    <v-row justify="center" class="pa-5">
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('signUp')" :color="content.value == 'signUp' ? 'secondary' : ''">회원가입 안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('order')" :color="content.value == 'order' ? 'secondary' : ''">주문 안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('pay')" :color="content.value == 'pay' ? 'secondary' : ''">결제안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('delivery')" :color="content.value == 'delivery' ? 'secondary' : ''">배송안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('exchange')" :color="content.value == 'exchange' ? 'secondary' : ''">교환/반품안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('return')" :color="content.value == 'return' ? 'secondary' : ''">환불안내</v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn @click="scrollTo('else')" :color="content.value == 'else' ? 'secondary' : ''">기타안내</v-btn>
                        </v-col>
                    </v-row>
                </v-card-title>
                <v-card-text>
                    <div v-html="content.text"></div>
                </v-card-text>
            </v-card>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            contents: [{
                text: '<h3>회원가입 안내</h3><p>[회원가입] 메뉴를 통해 이용약관, 개인정보보호정책 동의 및 일정 양식의 가입항목을 기입함으로써 회원에 가입되며, 가입 즉시 서비스를 무료로 이용하실 수 있습니다.<br>주문하실 때에 입력해야하는 각종 정보들을 일일이 입력하지 않으셔도 됩니다. 공동구매, 경매행사에 항상 참여하실 수 있습니다. 회원을 위한 이벤트 및 각종 할인행사에 참여하실 수 있습니다.</p>',
                value: 'signUp'
            }, {
                text: '<h3>주문 안내</h3><p>상품주문은 다음단계로 이루어집니다.<br><br>- Step1: 상품검색<br>- Step2: 장바구니에 담기<br>- Step3: 회원ID 로그인<br>- Step4: 주문서 작성<br>- Step5: 결제방법선택 및 결제<br>',
                value: 'order'
            }, {
                text: '<h3>결제안내</h3><p>고액결제의 경우 안전을 위해 카드사에서 확인전화를 드릴 수도 있습니다. 확인과정에서 도난 카드의 사용이나 타인 명의의 주문등 정상적인 주문이 아니라고 판단될 경우 임의로 주문을 보류 또는 취소할 수 있습니다. &nbsp;<br><br>무통장 입금은 상품 구매 대금은 PC뱅킹, 인터넷뱅킹, 텔레뱅킹 혹은 가까운 은행에서 직접 입금하시면 됩니다. &nbsp;<br>주문 시 입력한 입금자명과 실제입금자의 성명이 반드시 일치하여야 하며, 7일 이내로 입금을 하셔야 하며 입금되지 않은 주문은 자동취소 됩니다.<br><br>A/S 책임자 : 070-1234-1234 스프링 고객센터</p><p>품질보증기준: 전자상거래 소비자 보호법에 의거하여 소비자 청약철회 가능한 기준에 따름.</p><p>구매자가 미성년자일 경우 법정 대리인이 계약에 동의하지 않을 때 구매를 취소할 수 있습니다.</p>',
                value: 'pay'
            }, {
                text: '<h3>배송안내</h3><ul><li>배송 방법 : 택배</li><li>배송 지역 : 전국지역</li><li>배송 비용 : 조건부 무료 : 주문 금액 50,000원 미만일 때 배송비 2,500원을 추가합니다.</li><li>배송 기간 : 2일 ~ 7일</li><li>배송 안내 :</li></ul><p>스프링는 대한민국 택배 no.1 우체국택배를 이용하여<br>가장 안전하고, 신속하게 배송하여 드립니다 :)<br>대부분 출고 다음날에 바로 도착하며 (주말 제외)<br>지역 택배 기사님들의 일정과 기상상황에 따라 변동이 있을 수 있습니다.<br>기본 배송 준비일은 입고지연 상품 제외, 2~5일 정도가 소요되고 있습니다.</p>',
                value: 'delivery'
            }, {
                text: '<h3>교환/반품안내</h3><p>[교환/반품 안내]<br>물품 수령 후(택배 도착일자 기준) 7일 이내에 Q&amp;A "배송 후 교환/반품" 게시판 또는 고객센터 [070-1234-1234]</p><p>로 반드시 접수 해주세요. 글쓰기시 양식이 자동으로 등록되어 있으며, 사전에 신청해 주신 상품에 한해서만 교환/반품이 가능합니다.<br>접수 시 스프링에서 우체국 택배 회수접수를 도와드리고 있습니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>*패킹하여 보내실 때는 물품 수령시와 동일하게 포장해 주세요.<br>택에 손상이 있는 경우에는 반품과 교환이 모두 불가합니다.<br>성함,주소,전화번호,보내시는 상품,사유등 반품카드 양식에 맞게 적어 보내주셔야 처리가 가능합니다.</p><p>&nbsp;</p><p>&nbsp;</p><p>보내시는 주소지 : 서울시 가나구 다라동 87-3, 2층 202호 스프링</p><p>&nbsp;</p><p>&nbsp;</p><p>&lt;교환반품 불가사항&gt;<br>-상품 수령 후 7일 이상 경과된 경우<br>-상품 구매시 교환/환불 불가능이 명시되어 있는경우<br>-사용 흔적(집냄새,향수냄새,체취) / 텍 제거 및 바코드 훼손, 오염이 발견된 상품<br>-수영복, 세일상품</p><p>-배송시 생긴 구김, 마감 박음질, 제작과정에서 발생하는 냄새나 초크자국 등 대량생산으로 인해 생긴 사유는 불량으로 간주되지 않습니다.</p><p><br>*불량상품의 재발송 시 왕복배송비는 스프링가 부담합니다.</p>',
                value: 'exchange'
            }, {
                text: '<h3>환불안내</h3><p>반품상품을 보내주신 후 평균 7일 이내에 결제 금액을 환불해 드립니다.</p><p>&nbsp;</p><p>-카드취소 후 카드사 전산처리 기간 최대 3-7일</p><p>-현금결제 취소 후 입금되는 시간 최대 1-3일</p><p><br>신용카드로 결제하신 경우는 신용카드 승인을 취소하여 결제 대금이 청구되지 않게 합니다.<br>(단, 신용카드 결제일자에 맞추어 대금이 청구 될수 있으면 이경우 익월 신용카드 대금청구시 카드사에서 환급 처리됩니다.)</p>',
                value: 'return'
            }, {
                text: '<h3>기타안내</h3><p><strong>이용기간</strong><br>배송완료 체크시점에 주문으로 발생한 적립금이 적립됩니다. <br><br><strong>이용조건</strong><br>적립금 사용시 최대구매가능적립금(적립금 1회 사용 가능 최대금액)은 "한도제한없음" 입니다.<br><br><strong>소멸조건</strong><br>주문취소/환불시에 상품구매로 적립된 적립금은 함께 취소됩니다. 회원 탈퇴시에는 적립금은 자동적으로 소멸됩니다.</p>',
                value: 'else'
            }, ],
        }
    },
    methods: {
        scrollTo(tag) {
            let scroll = 0;
            if (tag == 'signUp') {
                scroll = document.querySelector("#signUpSelected").offsetTop;
            } else if (tag == 'order') {
                scroll = document.querySelector("#orderSelected").offsetTop;
            } else if (tag == 'pay') {
                scroll = document.querySelector("#paySelected").offsetTop;
            } else if (tag == 'delivery') {
                scroll = document.querySelector("#deliverySelected").offsetTop;
            } else if (tag == 'exchange') {
                scroll = document.querySelector("#exchangeSelected").offsetTop;
            } else if (tag == 'return') {
                scroll = document.querySelector("#returnSelected").offsetTop;
            } else if (tag == 'else') {
                scroll = document.querySelector("#elseSelected").offsetTop;
            }
            window.scrollTo({
                top: scroll,
            });
        },
    }
}
</script>

<style>

</style>
