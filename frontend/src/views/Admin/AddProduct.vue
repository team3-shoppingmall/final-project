<template>
<v-container fluid>
    <v-row justify="center">
        <v-col cols="9">
            <v-simple-table>
                <tbody>
                    <tr>
                        <td style="width:20%">상품명</td>
                        <td>
                            <v-text-field hide-details v-model="product.productName"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 타입</td>
                        <td>
                            <v-select v-model="typeSelected" :items="types" hide-details></v-select>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 가격</td>
                        <td>
                            <v-text-field hide-details v-model="product.price"></v-text-field>
                        </td>
                    </tr>
                    <tr v-if="pageID != undefined">
                        <td>할인가</td>
                        <td>
                            <v-text-field hide-details v-model="product.discount"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>재고</td>
                        <td>
                            <v-text-field hide-details v-model="product.amount"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 색상</td>
                        <td>
                            <v-combobox v-model="colorList" multiple chips deletable-chips hide-details clearable append-icon="" class="mt-0"></v-combobox>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 사이즈</td>
                        <td>
                            <v-combobox v-model="sizeList" multiple chips deletable-chips hide-details clearable append-icon="" class="mt-0"></v-combobox>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <v-row justify="space-between" align="center">
                                <v-col>
                                    상품 이미지
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="imagePlus" color="primary">mdi-plus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                        <td>
                            <v-row v-for="(idx) in imageFiles.length" :key="idx" align="center" dense>
                                <v-col>
                                    <v-file-input v-model="imageFiles[idx-1]" accept="image/*" truncate-length="100" @change="onImageChange(idx-1)"></v-file-input>
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="imageMinus(idx-1);" color="primary">mdi-minus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <v-row justify="space-between" align="center">
                                <v-col>
                                    상품 상세 이미지
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="detailImagePlus" color="primary">mdi-plus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                        <td>
                            <v-row v-for="(idx) in detailImageFiles.length" :key="idx" align="center" dense>
                                <v-col>
                                    <v-file-input v-model="detailImageFiles[idx-1]" accept="image/*" truncate-length="50" @change="onDetailImageChange(idx-1)"></v-file-input>
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="detailImageMinus(idx-1);" color="primary">mdi-minus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                </tbody>
            </v-simple-table>
            <v-row justify="center">
                <v-col align="center">
                    <v-btn @click="addFile" color="primary" class="mr-5" v-if="this.pageID == undefined">등록</v-btn>
                    <v-btn @click="updateFile" color="primary" class="mr-5" v-if="this.pageID != undefined">수정</v-btn>
                    <v-btn color="primary" class="ml-5">초기화</v-btn>
                </v-col>
            </v-row>

            <v-spacer class="mt-5 md-5"></v-spacer>
            <v-row>
                <v-col>
                    <v-row v-for="(idx) in imageUrl.length" :key="idx" justify="center">
                        <v-lazy :options="{threshold: .5}" transition="fade-transition">
                            <v-col cols="auto" v-if="imageUrl[idx-1] != null">
                                <div>상품 이미지 {{idx}}번 : {{imageFiles[idx-1].name}}</div>
                                <v-img v-if="imageUrl[idx-1]" :src="imageUrl[idx-1]" />
                            </v-col>
                        </v-lazy>
                    </v-row>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-row v-for="(idx) in detailImageUrl.length" :key="idx" justify="center">
                        <v-lazy :options="{threshold: .5}" transition="fade-transition">
                            <v-col cols="auto" v-if="detailImageUrl[idx-1] != null">
                                <div>상세 이미지 {{idx}}번 : {{detailImageFiles[idx-1].name}}</div>
                                <v-img v-if="detailImageUrl[idx-1]" :src="detailImageUrl[idx-1]" />
                            </v-col>
                        </v-lazy>
                    </v-row>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'

export default {
    data() {
        return {
            pageID: '',
            product: {
                productName: null,
                price: '',
                discount: '',
                amount: '',
            },
            typeSelected: null,
            colorList: [],
            sizeList: [],
            imageFiles: [null],
            imageUrl: [null],
            detailImageFiles: [null],
            detailImageUrl: [null],

            types: [{
                text: '기준 선택',
                value: null,
                disabled: true,
            }, {
                text: 'OUTER>자켓',
                value: 'outer;jacket',
            }, {
                text: 'OUTER>코트',
                value: 'outer;coat',
            }, {
                text: 'OUTER>가디건',
                value: 'outer;cardigan',
            }, {
                text: 'OUTER>점퍼',
                value: 'outer;jumper',
            }, {
                text: 'SKIRT>미니',
                value: 'skirt;mini',
            }, {
                text: 'SKIRT>미디/롱',
                value: 'skirt;midi-long',
            }, ],
        };
    },
    methods: {
        imagePlus() {
            this.imageFiles.push(null);
            this.imageUrl.push(null);
        },
        imageMinus(index) {
            this.imageFiles.splice(index, 1);
            this.imageUrl.splice(index, 1);
        },
        detailImagePlus() {
            this.detailImageFiles.push(null);
            this.detailImageUrl.push(null);
        },
        detailImageMinus(index) {
            this.detailImageFiles.splice(index, 1);
            this.detailImageUrl.splice(index, 1);
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
        onDetailImageChange(index) {
            const file = this.detailImageFiles[index];
            if (file) {
                this.detailImageUrl[index] = URL.createObjectURL(file);
                URL.revokeObjectURL(file);
            } else {
                this.detailImageUrl[index] = null;
            }
        },
        addFile() {
            if (this.product.productName == null) {
                alert('상품명을 입력해주세요');
                return;
            }
            if (this.typeSelected == null) {
                alert('상품 타입을 선택해주세요');
                return;
            }
            if (!(this.product.price > 0 && (this.product.price == Math.round(this.product.price)) && this.product.price != '')) {
                alert('상품 가격이 유효하지 않습니다')
                return;
            }
            if (!(this.product.amount >= 0 && (this.product.amount == Math.round(this.product.amount)) && this.product.amount != '')) {
                alert('상품 수량이 유효하지 않습니다')
                return;
            }
            if (this.colorList.length == 0 && this.sizeList.length == 0) {
                alert('색상, 사이즈 중 하나를 추가해주세요');
                return;
            }
            if (this.imageFiles.length == 0) {
                alert('상품 사진을 추가해주세요');
                return;
            }
            if (this.detailImageFiles.length == 0) {
                alert('상품 상세 사진을 추가해주세요');
                return;
            }

            let type1 = null;
            let type2 = null;
            type1 = this.typeSelected.split(';')[0];
            type2 = this.typeSelected.split(';')[1];

            let color = null;
            for (let i = 0; i < this.colorList.length; i++) {
                if (color == null) {
                    color = this.colorList[i];
                } else {
                    color = color + ";" + this.colorList[i];
                }
            }

            let size = null;
            for (let i = 0; i < this.sizeList.length; i++) {
                if (size == null) {
                    size = this.sizeList[i];
                } else {
                    size = size + ";" + this.sizeList[i];
                }
            }

            let imageName = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] == null) {
                    alert('파일을 추가하거나 입력칸을 삭제해주세요')
                    return;
                }
                if (imageName == null) {
                    imageName = this.imageFiles[i].name;
                } else {
                    imageName = imageName + ";" + this.imageFiles[i].name;
                }
            }

            let detailImageName = null;
            for (let i = 0; i < this.detailImageFiles.length; i++) {
                if (this.detailImageFiles[i] == null) {
                    alert('파일을 추가하거나 입력칸을 삭제해주세요')
                    return;
                }
                if (detailImageName == null) {
                    detailImageName = this.detailImageFiles[i].name;
                } else {
                    detailImageName = detailImageName + ";" + this.detailImageFiles[i].name;
                }
            }

            let data = {
                productName: this.product.productName,
                type1: type1,
                type2: type2,
                imageName: imageName,
                price: this.product.price,
                color: color,
                size: size,
                amount: this.product.amount,
                detailImageName: detailImageName,
            }

            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }))

            for (let i = 0; i < this.imageFiles.length; i++) {
                formData.append(`fileList`, this.imageFiles[i])
            }
            for (let i = 0; i < this.detailImageFiles.length; i++) {
                formData.append(`fileList`, this.detailImageFiles[i])
            }
            console.log(formData);
            axios.post('/api/product/insertProduct', formData)
                .then(res => {
                    console.log(res.status);
                    alert("상품을 추가하셨습니다");
                    this.$router.go();
                }).catch(err => {
                    if (err.response.status === 404)
                        alert("error")
                })
        },
        updateFile() {
            if (this.product.productName == null) {
                alert('상품명을 입력해주세요');
                return;
            }
            if (this.typeSelected == null) {
                alert('상품 타입을 선택해주세요');
                return;
            }
            if (!(this.product.price > 0 && (this.product.price == Math.round(this.product.price)) && this.product.price != '')) {
                alert('상품 가격이 유효하지 않습니다')
                return;
            }
            if (!(this.product.amount >= 0 && (this.product.amount == Math.round(this.product.amount)) && this.product.amount != '')) {
                alert('상품 수량이 유효하지 않습니다')
                return;
            }
            if (this.colorList.length == 0 && this.sizeList.length == 0) {
                alert('색상, 사이즈 중 하나를 추가해주세요');
                return;
            }
            if (this.imageFiles.length == 0) {
                alert('상품 사진을 추가해주세요');
                return;
            }
            if (this.detailImageFiles.length == 0) {
                alert('상품 상세 사진을 추가해주세요');
                return;
            }

            let type1 = null;
            let type2 = null;
            type1 = this.typeSelected.split(';')[0];
            type2 = this.typeSelected.split(';')[1];

            let color = null;
            for (let i = 0; i < this.colorList.length; i++) {
                if (color == null) {
                    color = this.colorList[i];
                } else {
                    color = color + ";" + this.colorList[i];
                }
            }

            let size = null;
            for (let i = 0; i < this.sizeList.length; i++) {
                if (size == null) {
                    size = this.sizeList[i];
                } else {
                    size = size + ";" + this.sizeList[i];
                }
            }

            let imageName = null;
            for (let i = 0; i < this.imageFiles.length; i++) {
                if (this.imageFiles[i] == null) {
                    alert('파일을 추가하거나 입력칸을 삭제해주세요')
                    return;
                }
                if (imageName == null) {
                    imageName = this.imageFiles[i].name;
                } else {
                    imageName = imageName + ";" + this.imageFiles[i].name;
                }
            }

            let detailImageName = null;
            for (let i = 0; i < this.detailImageFiles.length; i++) {
                if (this.detailImageFiles[i] == null) {
                    alert('파일을 추가하거나 입력칸을 삭제해주세요')
                    return;
                }
                if (detailImageName == null) {
                    detailImageName = this.detailImageFiles[i].name;
                } else {
                    detailImageName = detailImageName + ";" + this.detailImageFiles[i].name;
                }
            }

            let data = {
                productName: this.product.productName,
                type1: type1,
                type2: type2,
                imageName: imageName,
                price: this.product.price,
                color: color,
                size: size,
                amount: this.product.amount,
                detailImageName: detailImageName,
            }

            let formData = new FormData();
            formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }))

            for (let i = 0; i < this.imageFiles.length; i++) {
                formData.append(`fileList`, this.imageFiles[i])
            }
            for (let i = 0; i < this.detailImageFiles.length; i++) {
                formData.append(`fileList`, this.detailImageFiles[i])
            }
            console.log(formData);
            axios.post('/api/product/insertProduct', formData)
                .then(res => {
                    console.log(res.status);
                    alert("상품을 추가하셨습니다");
                    this.$router.go();
                }).catch(err => {
                    if (err.response.status === 404)
                        alert("error")
                })
        },
        getProduct() {
            axios.get(`/api/product/getProduct/${this.pageID}`)
                .then(res => {
                    this.product = res.data;
                    this.typeSelected = this.product.type1 + ";" + this.product.type2;
                    if (this.product.color != null) {
                        this.colorList = this.product.color.split(';');
                    }
                    if (this.product.size != null) {
                        this.sizeList = this.product.size.split(';');
                    }
                    let imageList = this.product.imageName.split(';');
                    for (let i = 0; i < imageList.length; i++) {
                        axios.get(`/api/product/productImage/${this.pageID}/${imageList[i]}`, {
                                responseType: "blob",
                            })
                            .then(res => {
                                if (i > 0) {
                                    this.imageFiles.push(null);
                                }
                                var file = new File([res.data], imageList[i], {
                                    type: "image/*",
                                    lastModified: Date.now()
                                });
                                this.imageFiles[i] = file;
                                this.onImageChange(i);
                            })
                    }

                    let detailImageList = this.product.detailImageName.split(';');
                    for (let i = 0; i < detailImageList.length; i++) {
                        axios.get(`/api/product/detailImage/${this.pageID}/${detailImageList[i]}`, {
                                responseType: "blob",
                            })
                            .then(res => {
                                if (i > 0) {
                                    this.detailImageFiles.push(null);
                                }
                                var file = new File([res.data], detailImageList[i]);
                                this.detailImageFiles[i] = file;
                                this.onDetailImageChange(i);
                            })
                    }
                })
        },
        blobToFile(theBlob, fileName) {
            theBlob.lastModifiedDate = new Date();
            theBlob.name = fileName;
            return theBlob;
        }
    },
    watch: {
        imageFiles(val) {
            if (val.length > 4) {
                this.$nextTick(() => this.imageFiles.pop());
                alert('상품 이미지는 4개까지 가능합니다');
            }
        },

        detailImageFiles(val) {
            if (val.length > 20) {
                this.$nextTick(() => this.detailImageFiles.pop());
                alert('상세 이미지는 20개까지 가능합니다');
            }
        },
    },
    mounted() {
        this.pageID = this.$route.params.id;
        if (this.pageID != undefined) {
            this.getProduct();
        }
    }
}
</script>

<style>

</style>
