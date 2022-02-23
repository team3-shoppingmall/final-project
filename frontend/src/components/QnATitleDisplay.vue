<template>
<span>
    <span v-if="title == '답변합니다'">
        <v-icon>mdi-arrow-right-bottom</v-icon>
    </span>
    {{title}}
</span>
</template>

<script>
export default {
    data() {
        return {
            titles: [{
                    text: '상품 문의입니다',
                    value: 'product',
                },
                {
                    text: '일반 문의입니다',
                    value: 'general',
                },
                {
                    text: '배송 문의입니다',
                    value: 'delivery',
                },
                {
                    text: '주문 취소 문의입니다',
                    value: 'cancel',
                },
                {
                    text: '상품 변경 문의입니다',
                    value: 'change',
                },
                {
                    text: '주소 변경 문의입니다',
                    value: 'changeAddress',
                },
                {
                    text: '반품 문의입니다',
                    value: 'return',
                },
                {
                    text: '교환 문의입니다',
                    value: 'exchange',
                },
                {
                    text: '불량 상품/오배송 문의입니다',
                    value: 'error',
                },
                {
                    text: '답변합니다',
                    value: 'productReply',
                },
                {
                    text: '답변합니다',
                    value: 'deliveryReply',
                },
                {
                    text: '답변합니다',
                    value: 'beforeDeliveryReply',
                },
                {
                    text: '답변합니다',
                    value: 'afterDeliveryReply',
                },
            ],
        }
    },
    computed: {
        title() {
            for (let i = 0; i < this.titles.length; i++) {
                if (this.type == this.titles[i].value) {
                    return this.titles[i].text;
                }
            }
            return 'error';
        }
    },
    props: [`type`]
}
</script>

<style>

</style>
