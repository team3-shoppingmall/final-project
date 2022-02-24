<template>
<v-container fluid>
    <v-row>
        <v-col cols="6">
            <v-simple-table>
                <tbody>
                    <tr>
                        <td>상품명</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 타입</td>
                        <td>
                            <v-select v-model="typeSelected" :items="types"></v-select>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 가격</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 색상</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 사이즈</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>재고</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td> 파일 첨부 </td>
                        <td>
                            <v-file-input accept="image/*"></v-file-input>
                        </td>
                    </tr>
                </tbody>
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
            productName: '',
            imageName: 'image.jpg',
            price: 0,
            color: '',
            size: '',
            amount: 0,
            detailImageName: 'detail.jpg',
            image1: null,
            image2: null,
            iamgefile1: null,
            imagefile2: null,

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
            typeSelected: null,

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
        pickFile1() {
            let input = this.$refs.fileInput1.files[0];
            if (input) {
                let reader = new FileReader
                reader.onload = e => {
                    this.image1 = e.target.result
                }
                reader.readAsDataURL(input)
                this.$emit('input', input)
            }
            this.imagename = input.name
            this.imageFile1 = input;
            // this.formData.append('fileList', input[0])

        },
        pickFile2() {
            let input = this.$refs.fileInput2.files[0];
            console.log(input);
            if (input) {
                let reader = new FileReader
                reader.onload = e => {
                    // this.image2.push(e.target.result)
                    this.image2 = e.target.result;
                }
                reader.readAsDataURL(input)
                this.$emit('input', input)
            }
            this.detailimagename = input.name
            this.imageFile2 = input;
            // this.formData.append('fileList', input[0])
        },
        async sendFile() {
            const checked = document.getElementsByClassName('inputData')
            let checkLength = checked.length
            for (let i = 0; i < checkLength; i++) {
                if (checked[i].value.length == 0) {
                    alert(`${checked[i].name} 을/를 입력하세요`)
                    checked[i].focus();
                    return;
                }
            }
            if (this.image1 == null) {
                alert('이미지를 입력하세요')
                return;
            }
            if (this.image2 == null) {
                alert('상세 이미지를 입력하세요')
                return;
            }
            if (this.option2name == '') {
                this.option2 = '';
            }

            let data = {
                sellerid: this.getLogin.user_id,
                productname: this.productname,
                ptype: this.ptype,
                price: this.price,
                amount: this.amount,
                option1: this.option1name + ';' + this.option1,
                option2: this.option2name + ';' + this.option2,
                imagename: this.imagename,
                detailimagename: this.detailimagename,
            }
            this.formData = new FormData();
            this.formData.append('data', new Blob([JSON.stringify(data)], {
                type: "application/json"
            }))
            this.formData.append('fileList', this.imageFile1)
            this.formData.append('fileList', this.imageFile2)
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
    }
}
</script>

<style>

</style>
