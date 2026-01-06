import { ref, watch } from 'vue'

const isDark = ref(localStorage.getItem('theme') === 'dark')

export function useTheme() {
  watch(
    isDark,
    (value) => {
      document.documentElement.classList.toggle('dark', value)
      localStorage.setItem('theme', value ? 'dark' : 'light')
    },
    { immediate: true }
  )

  function toggleTheme() {
    isDark.value = !isDark.value
  }

  return { isDark, toggleTheme }
}
