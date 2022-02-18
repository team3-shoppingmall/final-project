<template>
<v-container >
    <v-row justify="center" style="height: 965px">
        <v-col cols="3" align-self="center" >
            <v-form ref="form" > 
                <v-text-field v-model="id" :rules="rules.id" counter="25" hint="This field uses counter prop" label="ID" required></v-text-field>
                <v-text-field v-model="password" :rules="rules.pwd" label="PASSWORD" type="password" required></v-text-field>
                <v-row class="mt-10" justify="center">
                    <v-btn class="mr-5" color="success" @click="signIn">
                        sign In
                    </v-btn>
                    <v-btn color="accent" @click="signUp">
                        sign Up
                    </v-btn>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            id: '',
            pwd: '',
            rules: {
                id: [v => !!v || '아이디는 필수 입력사항입니다.',
                    v => /^[a-zA-Z0-9]*$/.test(v) || '아이디는 영문+숫자만 입력 가능합니다.',
                ],
                pwd: [v => !!v || '비밀번호는 필수 입력사항입니다.', ],
            }
        }
    },
    methods: {
        signIn() {
            let validate = this.$refs.form.validate()
            if (validate) {
                this.$router.push('/');
            }
        },
        signUp() {
            this.$router.push('/authentication/signUp');
        },
    }
}
</script>

<style>

</style>
