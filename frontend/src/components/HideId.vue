<template>
<span>{{ id.slice(0,4) + '****' }}</span>
</template>

<script>
export default {
    props: ['id']
}
</script>

<style>

</style>
