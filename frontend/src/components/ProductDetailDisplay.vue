<template>
<div>
    <!-- <span>{{ productname }}</span> -->
    <span>상품정보</span>
</div>
</template>

<script>
// import axios from 'axios'
export default {
    computed: {
        productname() {
            // axios.get(`/api/member/getProductname/${this.productno}`).then(res => {
            //     if (res.status == 200) {
            //         return res.data;
            //     }
            // })
            return 'error';
        }
    },
    props: ['productno']
}
</script>

<style>

</style>
