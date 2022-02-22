<template>
<span>
    {{faqType}}
</span>
</template>

<script>
export default {
    data() {
        return {
            types: [{
                    text: '상품 관련',
                    value: 'product',
                },
                {
                    text: '배송 관련',
                    value: 'delivery',
                },
                {
                    text: '교환/반품 관련',
                    value: 'return',
                },
                {
                    text: '기타 관련',
                    value: 'etc',
                },
            ],
        }
    },
    computed: {
        faqType() {
            for(let i=0;i<this.types.length;i++){
              if(this.type == this.types[i].value){
                return this.types[i].text;
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
