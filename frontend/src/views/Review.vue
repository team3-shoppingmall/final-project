<template>
<v-container>
    <v-row>
        <v-col cols="2" class="text-h5">
            Review
        </v-col>
    </v-row>
    <v-row justify="center">
        <v-col cols="auto" v-for="item in items" :key="item.title">
            <router-link :to="item.to">
                {{item.title}}
            </router-link>
        </v-col>
        <v-list
        dense
        nav
      >
        <v-list-item
          v-for="item in items"
          :key="item.title"
          link
          :to="item.to"
          active-class="primary"
          class="py-1"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            items: [{
                    title: '공지사항',
                    to: "/notice"
                },
                {
                    title: '자주 묻는 질문',
                    to: "/faq"
                },
                {
                    title: '사용후기',
                    to: "/review"
                },
            ],
        }
    },
    method: {
        toNotice() {
            this.$router.go();
        }
    }
}
</script>

<style>

</style>
