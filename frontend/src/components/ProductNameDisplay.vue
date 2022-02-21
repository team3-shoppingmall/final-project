<template>
<span>{{ productname }}</span>
</template>

<script>
// import axios from 'axios'
export default {
    computed: {
        productname() {
            // if(this.productno == undefined){
            //     return '';
            // }
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
