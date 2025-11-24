<script setup lang="ts">
import { defineProps, defineEmits, ref, watch } from 'vue'

const props = defineProps<{ modelValue: string }>()
const emit = defineEmits<{ (e: 'update:modelValue', value: string): void; (e: 'search', value: string): void }>()

const searchValue = ref(props.modelValue)

// Watch props to update local ref
watch(() => props.modelValue, (val) => {
  searchValue.value = val
})

const onInput = () => {
  emit('update:modelValue', searchValue.value)
  emit('search', searchValue.value)
}
</script>

<template>
  <div class="search-wrapper">
    <input
      v-model="searchValue"
      @input="onInput"
      type="text"
      placeholder="Zoeken..."
      class="search-input"
    />
    <span class="search-icon">🔍</span>
  </div>
</template>


<style scoped>
.search-wrapper {
  position: relative;
}

.search-input {
  border: none;
  border-radius: 9999px;
  padding: 0.5rem 2rem 0.5rem 1rem;
  font-size: 0.875rem;
  background-color: #f5f5f5;
  transition: all 0.2s ease;
  width: 160px;
}

.search-input:focus {
  outline: none;
  background-color: #fff;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

.search-icon {
  position: absolute;
  right: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  color: #888;
  font-size: 1rem;
}
</style>
