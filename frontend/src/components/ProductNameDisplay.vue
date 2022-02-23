<template>
<span>{{ productName }}</span>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            productName: '',
        }
    },
    props: ['productNo'],
    methods: {
        getProductName() {
            if (this.productNo == undefined || this.productNo == 0) {
                this.productName = '';
                return;
            } else {
                axios.get(`/api/product/getProduct/${this.productNo}`).then(res => {
                    if (res.status == 200) {
                        this.productName = res.data.productName;
                        return;
                    } else {
                        this.productName = 'error';
                    }
                })
            }
        }
    },
    mounted() {
        this.getProductName();
    }
}
</script>

<style>

</style>
