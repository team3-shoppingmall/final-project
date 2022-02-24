<template>
<span>
    <!-- <span>{{ returnId }}</span> -->
    <span>{{id}}</span>
</span>
</template>

<script>
export default {
    computed: {
        returnId() {
            if (this.id == 'Spring') {
                return this.id;
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
