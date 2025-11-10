import { ref } from 'vue'

export const toast = ref<{ message: string; type: string } | null>(null)

export function showToast(message: string, type: 'info' | 'error' | 'success' = 'info') {
  toast.value = { message, type }
  setTimeout(() => (toast.value = null), 4000)
}
