<template>
<v-app>
    <v-app-bar color="primary" app dark height="60px">
        <v-app-bar-nav-icon @click="visible= !visible"></v-app-bar-nav-icon>
        <v-row justify="end">
            <v-col cols="auto">
                <v-btn color="primary" :to="'/'">
                    <v-icon>mdi-home</v-icon>
                </v-btn>
            </v-col>
        </v-row>
    </v-app-bar>
    <v-navigation-drawer app :value="visible" >
        <v-list-item>
            <v-list-item-content class="pa-2">
                <v-list-item-title class="text-h6">
                    Admin Page
                </v-list-item-title>
                <v-list-item-subtitle>
                    subtext
                </v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list dense nav>
            <v-list-item v-for="item in items" :key="item.title" link :to="item.to" active-class="primary white--text" >
                <v-list-item-icon>
                    <v-icon>{{ item.icon }}</v-icon>
                </v-list-item-icon>

                <v-list-item-content>
                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
    <v-main>
        <router-view />
    </v-main>
</v-app>
</template>

<script>
export default {
    data() {
        return {
            visible: true,
            items: [{
                    title: '상품 관리',
                    icon: 'mdi-view-dashboard',
                    to: '/admin/admin1'
                },
                {
                    title: '상품 등록',
                    icon: 'mdi-image',
                    to: '/admin/admin2'
                },
                {
                    title: '주문 관리',
                    icon: 'mdi-help-box',
                    to: '/admin/admin3'
                },
                {
                    title: '판매 정산',
                    icon: 'mdi-help-box',
                    to: '/admin/admin4'
                },
                {
                    title: '배너 관리',
                    icon: 'mdi-help-box',
                    to: '/admin/admin5'
                },
            ],

        }
    },
    components: {},
    methods:{
    
    }

}
</script>

<style scoped>
</style>
