<template>
<v-card v-if="dataLoaded">
    <v-list-item three-line>
        <v-list-item-avatar tile size="100" color="grey">
            <v-img :src="`/api/product/productImage/${product.productNo}/${image}`"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
            <v-list-item-title class="text-h5 mb-1">
                {{product.productName}}
                - <span v-if="sizeOption != ''">{{sizeOption.length}} size</span>
                <span v-if="sizeOption == ''">{{colorOption.length}} color</span>
            </v-list-item-title>
            <v-list-item-subtitle>
                <span v-if="product.discount != 0 && product.amount > 0 && product.onSale == true" class="text-decoration-line-through">
                    {{product.price}}원
                </span>
                <span v-if="product.discount == 0 && product.amount > 0 && product.onSale == true">
                    {{product.price}}원
                </span>
                <span v-if="product.amount == 0 || product.onSale == false" class="red--text">
                    품절 상품입니다
                </span>
                <span v-if="product.discount != 0 && product.amount > 0 && product.onSale == true">
                    {{product.price-product.discount}}원
                </span>
            </v-list-item-subtitle>
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
            image: '',
            dataLoaded: false,
        }
    },
    props: ['productNo'],
    methods: {
        getProduct() {
            this.dataLoaded = false;
            axios.get(`/api/product/getProduct/${this.productNo}`).then(res => {
                this.product = res.data;
                this.image = this.product.imageName.split(';')[0];
                if (this.product.color != null) {
                    this.colorOption = this.product.color.split(';');
                }
                if (this.product.size != null) {
                    this.sizeOption = this.product.size.split(';');
                }
                console.log(this.product);
            }).catch(() => {
                this.product = [];
            }).finally(
                this.dataLoaded = true,
            )
        }
    },
    mounted() {
        this.getProduct();
    }
}
</script>

<style>

</style>
