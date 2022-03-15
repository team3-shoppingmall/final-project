<template>
<span v-if="this.id != undefined">
    <span v-if="getLogin == null || getLogin.user.authority == 'ROLE_USER'">{{ returnId }}</span>
    <span v-if="getLogin != null && getLogin.user.authority == 'ROLE_ADMIN'">{{id}}</span>
</span>
</template>

<script>
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
        returnId() {
            if (this.id == 'spring' || (this.getLogin != null && this.id == this.getLogin.user.id)) {
                if (this.id.slice(0, 5) == 'naver') {
                    return this.id.slice(0, 10);
                } else if (this.id.slice(0, 5) == 'kakao') {
                    return this.id.slice(0, 10);
                } else {
                    return this.id;
                }
            } else {
                return this.id.slice(0, 4) + '****';
            }
        }
    },
    props: ['id']
}
</script>

<style>

</style>
