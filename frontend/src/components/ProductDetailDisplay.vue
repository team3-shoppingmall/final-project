<template>
<span>{{ product }}</span>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            product: '',
        }
    },
    props: ['productNo'],
    methods: {
        getProduct() {
            if (this.productNo == undefined || this.productNo == 0) {
                this.product = '';
                return;
            } else {
                axios.get(`/api/product/getProduct/${this.productNo}`).then(res => {
                    if (res.status == 200) {
                        this.product = res.data;
                        return;
                    } else {
                        this.product = 'error';
                    }
                })
            }
        }
    },
    mounted() {
        this.getProduct();
    }
}
</script>

<style>

</style>
