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
                        <td>상품 타입1</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
                        </td>
                    </tr>
                    <tr>
                        <td>상품 타입2</td>
                        <td>
                            <v-text-field hide-details></v-text-field>
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

                </tbody>
            </v-simple-table>
        </v-col>
    </v-row>
    <div v-html="editorData"></div>
    <div>
        {{editorData}}
    </div>
    <v-row>
        <v-col>
            <ckeditor :editor="editor" v-model="editorData" :config="editorConfig"></ckeditor>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

export default {

    data() {
        return {
            testImage: '',
            editor: ClassicEditor,
            editorData: '<p>Content of the editor.</p>',
            editorConfig: {
                // The configuration of the editor.
                ckfinder: {
                    // baseUrl: `http://localhost:3000/uploads`,
                    // source: `http://localhost:3000/uploads`,
                    // uploadUrl: `http://localhost:8085/api/files/test`,
                    // filebrowserUploadUrl: `http://localhost:3000/api/files/test`,
                    // filebrowserImageUploadUrl: `http://localhost:3000/api/files/test`,
                    // filebrowserBrowseUrl: `http://localhost:3000/uploads`,
                    // filebrowserImageBrowseUrl: `http://localhost:3000/uploads`,
                    // filebrowserFlashUploadUrl: `http://localhost:3000/api/files/test`,
                    // filebrowserFlashBrowseUrl: `http://localhost:3000/uploads`,
                },
            },
            // plugins: [Base64UploadAdapter],
        };
    },
    methods: {}
}
</script>

<style>

</style>
