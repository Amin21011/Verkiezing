<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser, updateUser, changePassword } from '@/services/authService'
import type { AuthUser } from '@/types/IUser.ts'

const user = ref<AuthUser | null>(null)
const loading = ref(true)
const editMode = ref(false)
const updatedName = ref('')
const updatedEmail = ref('')
const saving = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const passwordMessage = ref('')
const passwordError = ref('')
const changing = ref(false)

onMounted(async () => {
  try {
    user.value = await getCurrentUser()
    if (user.value) {
      updatedName.value = user.value.name
      updatedEmail.value = user.value.email
    }
  } finally {
    loading.value = false
  }
})

async function saveProfile() {
  if (!user.value) return
  saving.value = true
  successMessage.value = ''
  errorMessage.value = ''

  try {
    const updated = await updateUser(updatedName.value, updatedEmail.value)
    user.value = updated
    successMessage.value = 'Profiel succesvol bijgewerkt'
    editMode.value = false
  } catch (e) {
    errorMessage.value = 'Er ging iets mis bij het bijwerken'
  } finally {
    saving.value = false
  }
}

async function handleChangePassword() {
  passwordMessage.value = ''
  passwordError.value = ''

  if (newPassword.value !== confirmPassword.value) {
    passwordError.value = 'Nieuwe wachtwoorden komen niet overeen.'
    return
  }

  changing.value = true
  try {
    const res = await changePassword(oldPassword.value, newPassword.value)
    passwordMessage.value = res.message || 'Wachtwoord succesvol gewijzigd'
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
  } catch (e) {
    passwordError.value = 'Wachtwoord wijzigen mislukt'
  } finally {
    changing.value = false
  }
}
</script>

<template>
  <section class="relative min-h-screen bg-paper text-ink font-body flex flex-col items-center py-20 px-6 md:px-10">
    <div class="absolute inset-0 bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] opacity-[0.06] pointer-events-none"></div>

    <header class="text-center mb-12 border-b border-ink/30 pb-6 max-w-2xl">
      <h1 class="text-4xl md:text-5xl font-serif font-bold tracking-tight leading-tight">
        Mijn Account
      </h1>
      <p class="text-gray-600 italic mt-2">Jouw persoonlijke redactionele ruimte.</p>
    </header>

    <div v-if="loading" class="animate-pulse text-gray-500 text-lg mt-10">
      Gebruikersgegevens worden geladen...
    </div>

    <div v-else class="relative z-10 w-full max-w-3xl bg-white/70 backdrop-blur-md rounded-xl shadow-[0_6px_20px_rgba(0,0,0,0.1)] border border-gray-300/40 p-8 md:p-10">

      <div class="text-center mb-10">
        <h2 class="text-3xl font-serif font-semibold text-ink mb-2">
          Welkom terug, {{ user?.name }}
        </h2>
        <p class="text-gray-700 text-sm italic">
          Je bevindt je in de hoofdredactie van jouw profiel.
        </p>
      </div>

      <div v-if="!editMode" class="divide-y divide-gray-300/40 border-y border-gray-300/40 mb-10">
        <div class="py-4 flex justify-between items-center">
          <span class="font-semibold text-gray-600 uppercase text-sm tracking-wide">Naam</span>
          <span class="text-lg font-medium">{{ user?.name }}</span>
        </div>
        <div class="py-4 flex justify-between items-center">
          <span class="font-semibold text-gray-600 uppercase text-sm tracking-wide">E-mail</span>
          <span class="text-lg font-medium">{{ user?.email }}</span>
        </div>
      </div>

      <div v-else class="space-y-6 mb-10">
        <div>
          <label class="block text-gray-700 font-semibold mb-1">Naam</label>
          <input v-model="updatedName" class="w-full border border-gray-300 rounded-lg p-3 bg-white/80 focus:ring-2 focus:ring-gray-500 outline-none transition-all" />
        </div>
        <div>
          <label class="block text-gray-700 font-semibold mb-1">E-mail</label>
          <input v-model="updatedEmail" class="w-full border border-gray-300 rounded-lg p-3 bg-white/80 focus:ring-2 focus:ring-gray-500 outline-none transition-all" />
        </div>
      </div>

      <div class="mt-8 flex flex-wrap justify-center gap-4">
        <button v-if="!editMode" @click="editMode = true" class="px-8 py-3 font-serif bg-ink text-paper text-lg rounded-full hover:bg-gray-400 transition-all duration-200 shadow-md hover:shadow-lg">
          Profiel Bijwerken ✍️
        </button>
        <button v-else @click="saveProfile" :disabled="saving" class="px-8 py-3 font-serif bg-green-700 text-paper text-lg rounded-full hover:bg-green-600 transition-all duration-200 shadow-md hover:shadow-lg">
          {{ saving ? 'Opslaan...' : 'Opslaan ✅' }}
        </button>
        <button v-if="editMode" @click="editMode = false" class="px-8 py-3 font-serif bg-gray-500 text-paper text-lg rounded-full hover:bg-gray-400 transition-all duration-200 shadow-md hover:shadow-lg">
          Annuleren
        </button>
      </div>

      <p v-if="successMessage" class="text-green-700 mt-6 text-center font-medium">{{ successMessage }}</p>
      <p v-if="errorMessage" class="text-red-600 mt-6 text-center font-medium">{{ errorMessage }}</p>

      <article class="mt-14 border border-gray-300/40 rounded-lg p-6 md:p-8 bg-white/50 shadow-inner">
        <header class="mb-4 flex items-center justify-between">
          <h3 class="text-xl font-serif font-semibold text-ink">Wachtwoord wijzigen</h3>
          <span class="text-xs uppercase text-gray-500 tracking-widest font-semibold">Beveiliging</span>
        </header>

        <div class="space-y-5">
          <div>
            <label class="block text-gray-700 font-semibold mb-1">Huidig wachtwoord</label>
            <input v-model="oldPassword" type="password" class="w-full border border-gray-300 rounded-lg p-3 bg-white/80 focus:ring-2 focus:ring-gray-500 outline-none transition-all" />
          </div>
          <div>
            <label class="block text-gray-700 font-semibold mb-1">Nieuw wachtwoord</label>
            <input v-model="newPassword" type="password" class="w-full border border-gray-300 rounded-lg p-3 bg-white/80 focus:ring-2 focus:ring-gray-500 outline-none transition-all" />
          </div>
          <div>
            <label class="block text-gray-700 font-semibold mb-1">Bevestig nieuw wachtwoord</label>
            <input v-model="confirmPassword" type="password" class="w-full border border-gray-300 rounded-lg p-3 bg-white/80 focus:ring-2 focus:ring-gray-500 outline-none transition-all" />
          </div>

          <div class="flex justify-center mt-6">
            <button @click="handleChangePassword" :disabled="changing" class="px-8 py-3 font-serif bg-ink text-paper text-lg rounded-full hover:bg-gray-400 transition-all duration-200 shadow-md hover:shadow-lg">
              {{ changing ? 'Bezig...' : 'Wachtwoord opslaan 🔒' }}
            </button>
          </div>

          <p v-if="passwordMessage" class="text-green-700 mt-4 text-center">{{ passwordMessage }}</p>
          <p v-if="passwordError" class="text-red-600 mt-4 text-center">{{ passwordError }}</p>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.font-body { font-family: 'Inter', system-ui, sans-serif; }
.font-serif { font-family: 'Playfair Display', 'Times New Roman', serif; }
.text-ink { color: #1a1a1a; }
.bg-paper { background-color: #f9f7f4; }
</style>
