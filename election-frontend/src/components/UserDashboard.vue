<script setup lang="ts">
import { ref, onMounted } from "vue";
import { loadUserProfile, saveUserProfile, updatePassword } from "@/services/AccountService";
import type { AuthUser } from "@/types/IUser";

const user = ref<AuthUser | null>(null);
const loading = ref(true);

const editMode = ref(false);
const updatedName = ref("");
const updatedEmail = ref("");
const saving = ref(false);
const successMessage = ref("");
const errorMessage = ref("");

const oldPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const passwordMessage = ref("");
const passwordError = ref("");
const changing = ref(false);

onMounted(async () => {
  try {
    user.value = await loadUserProfile();
    if (user.value) {
      updatedName.value = user.value.name;
      updatedEmail.value = user.value.email;
    }
  } finally {
    loading.value = false;
  }
});

async function saveProfile() {
  try {
    saving.value = true;
    successMessage.value = "";
    errorMessage.value = "";

    const updated = await saveUserProfile(updatedName.value, updatedEmail.value);
    user.value = updated;

    successMessage.value = "Profiel succesvol bijgewerkt!";
    editMode.value = false;
  } catch {
    errorMessage.value = "Opslaan mislukt.";
  } finally {
    saving.value = false;
  }
}

async function handleChangePassword() {
  passwordMessage.value = "";
  passwordError.value = "";

  if (newPassword.value !== confirmPassword.value) {
    passwordError.value = "Nieuwe wachtwoorden komen niet overeen.";
    return;
  }

  try {
    changing.value = true;
    const res = await updatePassword(oldPassword.value, newPassword.value);

    passwordMessage.value = res.message || "Wachtwoord gewijzigd!";
    oldPassword.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
  } catch {
    passwordError.value = "Wachtwoord wijzigen mislukt.";
  } finally {
    changing.value = false;
  }
}
</script>

<template>
  <section class="relative min-h-screen bg-paper text-ink font-body flex flex-col items-center py-20 px-6 md:px-10">
    <div class="absolute inset-0 opacity-[0.05] bg-[url('https://www.transparenttextures.com/patterns/newsprint.png')] pointer-events-none"></div>

    <header class="text-center mb-12 max-w-xl">
      <h1 class="text-4xl md:text-5xl font-serif font-bold tracking-tight">Mijn Account</h1>
      <p class="text-gray-600 italic mt-2">Een overzicht van jouw persoonlijke gegevens en voorkeuren.</p>
    </header>

    <div v-if="loading" class="animate-pulse text-gray-500 text-lg mt-10">Gegevens worden geladen...</div>

    <div v-else class="relative z-10 w-full max-w-3xl space-y-12">

      <div class="bg-white shadow rounded-xl border border-gray-200 px-10 py-8">
        <h2 class="text-3xl font-serif font-semibold text-center mb-2">Welkom terug, {{ user?.name }}</h2>
        <p class="text-gray-700 text-center italic">Hier vind je al jouw persoonlijke instellingen.</p>
      </div>

      <div class="bg-white shadow rounded-xl border border-gray-200 p-8">
        <header class="mb-4">
          <h3 class="text-2xl font-serif font-bold">Persoonlijke Gegevens</h3>
          <p class="text-sm text-gray-500">Bekijk of wijzig jouw basisinformatie</p>
        </header>

        <div v-if="!editMode" class="space-y-4">
          <div class="flex justify-between border-b pb-3">
            <span class="font-semibold text-gray-600">Naam</span>
            <span>{{ user?.name }}</span>
          </div>
          <div class="flex justify-between pt-3">
            <span class="font-semibold text-gray-600">E-mail</span>
            <span>{{ user?.email }}</span>
          </div>

          <div class="mt-6 flex justify-center">
            <button
              @click="editMode = true"
              class="bg-stone-400 text-paper px-6 py-2 rounded-full shadow hover:text-white transition">
              Wijzig Gegevens
            </button>
          </div>
        </div>

        <div v-else class="space-y-6">
          <div>
            <label class="font-semibold">Naam</label>
            <input v-model="updatedName" class="w-full border rounded-lg p-3 mt-1" />
          </div>

          <div>
            <label class="font-semibold">E-mail</label>
            <input v-model="updatedEmail" class="w-full border rounded-lg p-3 mt-1" />
          </div>

          <div class="flex gap-4 mt-4">
            <button @click="saveProfile"
              :disabled="saving"
              class="bg-green-700 text-white flex-1 py-2 rounded-full hover:bg-green-800 shadow transition">
              {{ saving ? "Opslaan..." : "Opslaan" }}
            </button>

            <button @click="editMode = false"
              class="bg-gray-500 text-white flex-1 py-2 rounded-full hover:bg-gray-600 shadow transition">
              Annuleren
            </button>
          </div>

          <p v-if="successMessage" class="text-green-700 mt-4">{{ successMessage }}</p>
          <p v-if="errorMessage" class="text-red-600 mt-4">{{ errorMessage }}</p>
        </div>
      </div>

      <article class="bg-white shadow rounded-xl border border-gray-200 p-8">
        <header class="mb-6">
          <h3 class="text-2xl font-serif font-bold">Politieke Match Analyse</h3>
          <p class="text-sm text-gray-500">Gebaseerd op jouw quizantwoorden</p>
        </header>

        <div v-if="!user?.quizBestMatch" class="text-center py-6">
          <p class="text-gray-600 mb-4">Je hebt de quiz nog niet ingevuld.</p>
          <button @click="$router.push('/quiz')"
            class="bg-ink text-white px-6 py-2 rounded-full shadow hover:bg-gray-700 transition">
            Start Nu
          </button>
        </div>

        <div v-else>
          <div class="flex items-center gap-6">
            <div class="w-24 h-24 bg-indigo-700 text-white flex items-center justify-center rounded-full text-2xl font-bold shadow-inner">
              {{ user.quizBestMatch?.slice(0, 2) }}
            </div>

            <div>
              <h4 class="text-xl font-semibold">
                Beste Match: <span class="text-indigo-700">{{ user.quizBestMatch }}</span>
              </h4>
              <p class="text-gray-600 text-sm mt-2">
                Deze partij sluit momenteel het meest aan bij jouw waarden en antwoorden.
              </p>
            </div>
          </div>

          <div class="mt-6">
            <button @click="$router.push('/quiz')"
              class="bg-indigo-600 text-white px-5 py-2 rounded-full shadow hover:bg-indigo-700 transition">
              Quiz Opnieuw Doen
            </button>
          </div>
        </div>
      </article>

      <article class="bg-white shadow rounded-xl border border-gray-200 p-8">
        <header class="mb-6">
          <h3 class="text-2xl font-serif font-bold">Wachtwoord Wijzigen</h3>
          <p class="text-sm text-gray-500">Beveilig jouw account</p>
        </header>

        <div class="space-y-5">
          <div>
            <label class="font-semibold">Huidig wachtwoord</label>
            <input v-model="oldPassword" type="password" class="w-full border rounded-lg p-3 mt-1" />
          </div>

          <div>
            <label class="font-semibold">Nieuw wachtwoord</label>
            <input v-model="newPassword" type="password" class="w-full border rounded-lg p-3 mt-1" />
          </div>

          <div>
            <label class="font-semibold">Bevestig nieuw wachtwoord</label>
            <input v-model="confirmPassword" type="password" class="w-full border rounded-lg p-3 mt-1" />
          </div>

          <button @click="handleChangePassword" :disabled="changing"
            class="w-full text-black py-3 rounded-full shadow hover:bg-gray-400 transition">
            {{ changing ? "Bezig..." : "Opslaan" }}
          </button>

          <p v-if="passwordMessage" class="text-green-700">{{ passwordMessage }}</p>
          <p v-if="passwordError" class="text-red-600">{{ passwordError }}</p>
        </div>
      </article>
    </div>
  </section>
</template>

<style scoped>
.font-body { font-family: 'Inter', system-ui, sans-serif; }
.font-serif { font-family: 'Playfair Display', serif; }
.text-ink { color: #1a1a1a; }
.bg-paper { background-color: #f9f7f4; }
</style>
