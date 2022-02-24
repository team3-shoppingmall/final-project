<template>
<v-container fluid>
    <v-row>
        <v-col cols="9">
            <v-simple-table>
                <tbody>
                    <tr>
                        <td style="width:20%">상품명</td>
                        <td>
                            <v-text-field hide-details v-model="productName"></v-text-field>
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
                            <v-text-field hide-details v-model="price"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 색상</td>
                        <td>
                            <v-combobox v-model="colorList" multiple persistent-hint small-chips deletable-chips hide-details clearable append-icon=""></v-combobox>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 사이즈</td>
                        <td>
                            <v-combobox v-model="sizeList" multiple persistent-hint small-chips deletable-chips hide-details clearable append-icon=""></v-combobox>
                        </td>
                    </tr>
                    <tr>
                        <td>재고</td>
                        <td>
                            <v-text-field hide-details v-model="amount"></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <v-row justify="space-between" align="center">
                                <v-col>
                                    상품 이미지
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="imageFiles.push(null)" color="primary">mdi-plus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                        <td>
                            <v-row v-for="(idx) in imageFiles.length" :key="idx" align="center" dense>
                                <v-col>
                                    <v-file-input v-model="imageFiles[idx-1]" accept="image/*" truncate-length="50"></v-file-input>
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="imageFiles.splice(idx-1, 1);" color="primary">mdi-minus</v-icon>
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
                                    <v-icon @click="detailImageFiles.push(null)" color="primary">mdi-plus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                        <td>
                            <v-row v-for="(idx) in detailImageFiles.length" :key="idx" align="center" dense>
                                <v-col>
                                    <v-file-input v-model="detailImageFiles[idx-1]" accept="image/*" truncate-length="50"></v-file-input>
                                </v-col>
                                <v-col cols="auto">
                                    <v-icon @click="detailImageFiles.splice(idx-1, 1);" color="primary">mdi-minus</v-icon>
                                </v-col>
                            </v-row>
                        </td>
                    </tr>
                </tbody>
                <v-btn @click="sendFile">등록</v-btn>
            </v-simple-table>
        </v-col>
    </v-row>
    <!-- <div v-html="editorData"></div>
    <div>
        {{editorData}}
    </div>
    <v-row>
        <v-col>
            <ckeditor :editor="editor" v-model="editorData" :config="editorConfig"></ckeditor>
        </v-col>
    </v-row> -->
</v-container>
</template>

<script>
import axios from 'axios'
// import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

export default {

    data() {
        return {
            productName: null,
            typeSelected: null,
            price: 0,
            colorList: [],
            sizeList: [],
            amount: 0,
            imageFiles: [null],
            detailImageFiles: [null],

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

            // editor: ClassicEditor,
            // editorData: '<p>Content of the editor.</p>',
            // editorConfig: {
            //     // The configuration of the editor.
            //     ckfinder: {
            //         // baseUrl: `http://localhost:3000/uploads`,
            //         // source: `http://localhost:3000/uploads`,
            //         // uploadUrl: `http://localhost:8085/api/files/test`,
            //         // filebrowserUploadUrl: `http://localhost:3000/api/files/test`,
            //         // filebrowserImageUploadUrl: `http://localhost:3000/api/files/test`,
            //         // filebrowserBrowseUrl: `http://localhost:3000/uploads`,
            //         // filebrowserImageBrowseUrl: `http://localhost:3000/uploads`,
            //         // filebrowserFlashUploadUrl: `http://localhost:3000/api/files/test`,
            //         // filebrowserFlashBrowseUrl: `http://localhost:3000/uploads`,
            //     },
            // },
            // plugins: [Base64UploadAdapter],
        };
    },
    methods: {
        async sendFile() {
            if (this.productName == null) {
                alert('상품명을 입력해주세요');
                return;
            }
            if (this.typeSelected == null) {
                alert('상품 타입을 선택해주세요');
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
                productName: this.productName,
                type1: type1,
                type2: type2,
                imageName: imageName,
                price: this.price,
                color: color,
                size: size,
                amount: this.amount,
                detailImageName: this.detailImageName,
            }

            this.formData = new FormData();
            this.formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }))

            for (let i = 0; i < this.imageFiles.length; i++) {
                this.formData.append('fileList', this.files[i])
            }
            for (let i = 0; i < this.detailImageFiles.length; i++) {
                this.formData.append('fileList', this.files[i])
            }

            axios.post('/api/product/insertProduct', this.formData)
                .then(res => {
                    console.log(res.status);
                    alert("상품을 추가하셨습니다");
                    this.$router.go();
                }).catch(err => {
                    if (err.response.status === 404)
                        alert("error")
                })
        },
    },
}
</script>

<style>

</style>
