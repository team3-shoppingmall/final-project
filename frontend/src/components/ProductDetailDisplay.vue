<template>
<v-card outlined class="ma-5">
    <v-list-item three-line>
        <v-list-item-avatar tile size="100" color="grey">이미지</v-list-item-avatar>
        <v-list-item-content>
            <v-list-item-title class="text-h5 mb-1">
                {{product.productName}}
                - <span v-if="sizeOption != ''">{{sizeOption.length}} size</span>
                <span v-if="sizeOption == ''">{{colorOption.length}} color</span>
            </v-list-item-title>
            <v-list-item-subtitle>{{product.price}}원</v-list-item-subtitle>

            <v-btn outlined :to="`/productDetail/${product.productNo}`" max-width="150">
                상품 상세보기
            </v-btn>
        </v-list-item-content>
    </v-list-item>
</v-card>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            product: '',
            sizeOption: '',
            colorOption: '',
        }
    },
    props: ['productNo'],
    methods: {
        getProduct() {
            axios.get(`/api/product/getProduct/${this.productNo}`).then(res => {
                if (res.status == 200) {
                    this.product = res.data;
                    if (this.product.color != null) {
                        this.colorOption = this.product.color.split(';');
                    }
                    if (this.product.size != null) {
                        this.sizeOption = this.product.size.split(';');
                    }
                } else {
                    this.product = 'error';
                }
            })
        }
    },
    mounted() {
        this.getProduct();
    }
}
</script>

<style>

</style>
