import { ref } from 'vue'
import { getToken } from '@/services/authService'
import { saveUserProfile, updatePassword } from '@/services/AccountService'
import type { AuthUser } from '@/types/IUser.ts'

export function useSecuritySettings() {
  const user = ref<AuthUser | null>(null);
  const oldPassword = ref('')
  const newPassword = ref('')
  const confirmPassword = ref('')
  const message = ref('')
  const error = ref('')
  const birthDate = ref('')
  const saving = ref(false)
  const updatedName = ref('')
  const updatedEmail = ref('')

  async function changePassword() {
    error.value = ''
    message.value = ''

    if (newPassword.value !== confirmPassword.value) {
      error.value = 'Nieuwe wachtwoorden komen niet overeen.'
      return
    }

    saving.value = true
    try {
      const res = await updatePassword(oldPassword.value, newPassword.value)
      message.value = res.message || 'Wachtwoord gewijzigd'
      oldPassword.value = newPassword.value = confirmPassword.value = ''
    } catch {
      error.value = 'Wachtwoord wijzigen mislukt'
    } finally {
      saving.value = false
    }
  }

  async function saveBirthDate() {
    saving.value = true
    try {
      await fetch('http://localhost:8080/api/account/birthdate', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${getToken()}`,
        },
        body: JSON.stringify({ birthDate: birthDate.value }),
      })
      message.value = 'Geboortedatum opgeslagen'
    } catch {
      error.value = 'Opslaan mislukt'
    } finally {
      saving.value = false
    }
  }

  async function saveProfile() {
    try {
      saving.value = true;
      message.value = "";
      error.value = "";

      const updated = await saveUserProfile(updatedName.value, updatedEmail.value);
      user.value = updated;

      message.value = "Profiel succesvol bijgewerkt!";
    } catch {
      error.value = "Opslaan mislukt.";
    } finally {
      saving.value = false;
    }
  }

  return {
    oldPassword,
    newPassword,
    confirmPassword,
    birthDate,
    message,
    error,
    saving,
    updatedName,
    updatedEmail,
    saveProfile,
    changePassword,
    saveBirthDate,
  }
}


