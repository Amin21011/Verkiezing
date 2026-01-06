import { ref, computed } from 'vue'
import type { AuthUser } from '@/types/IUser'
import { fetchAllUsers, deleteUser, updateUserRole } from '@/services/adminService'

export function useAdminUsers() {
  const users = ref<AuthUser[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const admins = computed(() => users.value.filter(u => u.role === 'ADMIN'))
  const normalUsers = computed(() => users.value.filter(u => u.role !== 'ADMIN'))

  async function loadUsers() {
    loading.value = true
    try {
      users.value = await fetchAllUsers()
    } catch {
      error.value = 'Gebruikers konden niet geladen worden'
    } finally {
      loading.value = false
    }
  }

  async function promote(u: AuthUser) {
    await updateUserRole(u.id, 'ADMIN')
    await loadUsers()
  }

  async function demote(u: AuthUser, currentUserId?: number) {
    if (u.id === currentUserId) return
    await updateUserRole(u.id, 'USER')
    await loadUsers()
  }

  async function remove(u: AuthUser) {
    await deleteUser(u.id)
    await loadUsers()
  }

  return {
    users,
    admins,
    normalUsers,
    loading,
    error,
    loadUsers,
    promote,
    demote,
    remove,
  }
}
