<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser, getToken } from '@/services/authService'
import ConfirmModal from '@/components/ConfirmModal.vue'
import { useAdminUsers } from '@/composables/useAdminUsers'
import { type Topic, useForumPosts } from '@/composables/useForumPosts'
import { useSecuritySettings } from '@/composables/useSecuritySettings'
import type { AuthUser } from '@/types/IUser.ts'
import router from '@/router'

const activeTab = ref<'overview' | 'posts' | 'topics' | 'security'>('overview')
const currentUser = ref<AuthUser | null>(null)
const loading = ref(true)
const admin = useAdminUsers()
const forum = useForumPosts()
const security = useSecuritySettings()

const { users, admins, normalUsers } = admin
const { myPosts, allPosts } = forum

const {
  oldPassword,
  newPassword,
  updatedName,
  updatedEmail,
  saveProfile,
  confirmPassword,
  message,
  error,
  birthDate,
  changePassword,
  saveBirthDate,
} = security

const topics = ref<Topic[]>([])

async function loadTopics() {
  const res = await fetch('http://localhost:8080/api/topics')
  topics.value = await res.json()
}

function tabClass(tab: typeof activeTab.value) {
  return ['transition', activeTab.value === tab ? 'border-l-2 border-[var(--accent)] bg-[var(--highlight)]' : 'border-l-2 border-transparent hover:bg-[var(--highlight)]/40']
}

onMounted(async () => {
  currentUser.value = await getCurrentUser()
  if (currentUser.value) {
    updatedName.value = currentUser.value.name
    updatedEmail.value = currentUser.value.email
  }
  await admin.loadUsers()
  await forum.loadPosts(currentUser.value?.email)
  await loadTopics()
  loading.value = false
})

const showConfirmModal = ref(false)
const pendingAction = ref<null | (() => Promise<void>)>(null)

function openConfirm(action: () => Promise<void>) {
  pendingAction.value = action
  showConfirmModal.value = true
}

async function confirmAction() {
  if (!pendingAction.value) return

  await pendingAction.value()
  pendingAction.value = null
  showConfirmModal.value = false
}

function cancelConfirm() {
  pendingAction.value = null
  showConfirmModal.value = false
}

const showDeleteAccountModal = ref(false)
const deletingAccount = ref(false)

function openDeleteAccountModal() {
  showDeleteAccountModal.value = true
}

function closeDeleteAccountModal() {
  showDeleteAccountModal.value = false
}

async function confirmDeleteAccount() {
  try {
    deletingAccount.value = true

    await fetch("http://localhost:8080/api/auth/account", {
      method: "DELETE",
      headers: {
        Authorization: "Bearer " + getToken(),
      },
    })

    localStorage.removeItem("token")
    router.push("/")

  } catch {
    alert("Account verwijderen mislukt.")
  } finally {
    deletingAccount.value = false
    showDeleteAccountModal.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-paper text-ink font-body">
    <div class="max-w-[1400px] mx-auto grid grid-cols-1 lg:grid-cols-[260px_1fr]">
      <aside class="sticky top-0 h-screen border-r border-ink/20 bg-paper px-6 py-10 space-y-10">
        <div class="space-y-1">
          <p class="font-serif font-bold text-lg"> {{ currentUser?.name }} </p>
          <p class="text-xs italic text-graymain"> {{ currentUser?.email }} </p>
          <p class="text-xs text-graymain uppercase tracking-widest">
            Lid sinds {{ currentUser?.createdAt }}
          </p>
        </div>
        <nav class="space-y-1 text-sm"> <button @click="activeTab = 'overview'" :class="tabClass('overview')" class="w-full px-4 py-2 text-left"> Overzicht </button>
          <button @click="activeTab = 'posts'" :class="tabClass('posts')" class="w-full px-4 py-2 text-left"> Mijn posts </button>
          <button @click="activeTab = 'topics'" :class="tabClass('topics')" class="w-full px-4 py-2 text-left"> Thema's </button>
          <button @click="activeTab = 'security'" :class="tabClass('security')" class="w-full px-4 py-2 text-left"> Instellingen </button>
        </nav>
      </aside>

      <main class="px-10 py-16 space-y-20">
        <p v-if="loading" class="italic text-graymain"> Gegevens laden… </p>
      <section v-if="!loading && activeTab === 'overview'" class="space-y-20">
        <header class="space-y-3">
          <p class="uppercase tracking-[0.35em] text-[11px] text-graymain"> Welkom terug </p>
          <h1 class="text-4xl font-serif font-bold"> Redactiebeheer </h1>
          <p class="text-graymain max-w-xl"> Beheer gebruikers, rollen en moderatie vanuit één centrale omgeving. </p>
        </header>

        <div class="grid grid-cols-1 sm:grid-cols-3 gap-8">
          <div class="border border-ink/30 p-4 text-center"> <p class="uppercase text-[11px] tracking-widest text-graymain"> Gebruikers </p>
          <p class="text-3xl font-serif font-bold"> {{ users.length }} </p> </div>
          <div class="border border-ink/30 p-4 text-center">
            <p class="uppercase text-[11px] tracking-widest text-graymain"> Beheerders </p>
            <p class="text-3xl font-serif font-bold"> {{ admins.length }} </p> </div>
        <div class="border border-ink/30 p-4 text-center">
          <p class="uppercase text-[11px] tracking-widest text-graymain"> Actieve Posts </p>
          <p class="text-3xl font-serif font-bold"> {{ allPosts.length }} </p> </div>
        </div>

        <section class="border border-ink/30 shadow-soft p-10 space-y-6"> <h2 class="text-2xl font-serif font-bold"> Beheerders </h2>
          <div v-for="adminAcc in admins" :key="adminAcc.id" class="flex justify-between items-center border-b border-ink/20 pb-3"> <div>
            <p class="font-medium">{{ adminAcc.name }}</p>
            <p class="text-xs italic text-graymain"> {{ adminAcc.email }} </p>
          </div>
            <button v-if="adminAcc.id !== currentUser?.id" class="border border-ink px-3 py-1 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition" @click="admin.demote(adminAcc, currentUser?.id)">
              Terug naar gebruiker </button>
            <span v-else class="text-xs italic text-graymain"> Jij </span>
          </div>
        </section>

      <section class="border border-ink/30 shadow-soft p-10 space-y-6"> <h2 class="text-2xl font-serif font-bold"> Gebruikersbeheer </h2>
        <div class="overflow-x-auto border border-ink/20">
        <table class="min-w-full text-sm">
          <thead class="border-b border-ink/20 uppercase text-[11px] tracking-widest text-graymain">
          <tr> <th class="py-3 px-3 text-left">Naam</th>
          <th class="py-3 px-3 text-left">E-mail</th>
            <th class="py-3 px-3 text-left">Rol</th>
            <th class="py-3 px-3 text-right">Acties</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="u in normalUsers" :key="u.id" class="border-t border-ink/10 hover:bg-[var(--highlight)]/30">
            <td class="py-3 px-3">{{ u.name }}</td>
            <td class="py-3 px-3">{{ u.email }}</td>
            <td class="py-3 px-3"> <span class="px-3 py-1 text-[11px] uppercase tracking-widest border border-ink/30"> {{ u.role }} </span> </td>
            <td class="py-3 px-3 text-right space-x-2">
              <button @click="admin.promote(u)" class="border border-ink px-3 py-1 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition"> Admin maken </button>
              <button @click="openConfirm(() => admin.remove(u))" class="border border-ink/30 px-3 py-1 text-xs uppercase tracking-widest text-red-600 hover:bg-red-600 hover:text-white transition">
                Verwijder
              </button>
            </td>
          </tr>
          </tbody>
        </table>
        </div>
      </section>
      </section>

        <section v-if="!loading && activeTab === 'posts'" class="space-y-10">
          <header> <h2 class="text-3xl font-serif font-bold"> Mijn forum posts </h2>
            <p class="italic text-graymain"> Jouw bijdragen aan het forum </p>
          </header>
          <div v-if="myPosts.length === 0" class="italic text-graymain"> Je hebt nog geen posts geplaatst. </div>
            <div v-else class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <article v-for="post in myPosts" :key="post.id" class="group relative border border-ink/30 bg-paper p-6 shadow-soft hover:shadow-lg transition cursor-pointer">
              <h3 class="font-serif font-bold text-lg mb-2"> {{ post.title }} </h3>
              <p class="text-xs italic text-graymain"> {{ new Date(post.postedAt).toLocaleDateString('nl-NL') }} </p>
              <button @click="openConfirm(() => forum.deletePost(post.id))">
                🗑
              </button>
            </article>
          </div>
        </section>

        <section v-if="!loading && activeTab === 'topics'" class="space-y-10">
          <header>
            <h2 class="text-3xl font-serif font-bold"> Thema's beheren </h2>
            <p class="italic text-graymain"> Forumcategorieën en structuur </p>
          </header>

          <div class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <article v-for="topic in topics" :key="topic.id" class="border border-ink/30 bg-paper p-6 shadow-soft hover:shadow-lg transition">
              <p class="uppercase text-[11px] tracking-widest text-graymain"> Topic </p>
              <h3 class="font-serif font-bold text-xl mt-2"> {{ topic.name }} </h3>
            </article>
          </div>
        </section>

        <section v-if="!loading && activeTab === 'security'" class="space-y-5">
        <header>
          <h2 class="text-3xl font-serif font-bold"> Account Gegevens </h2>
        </header>

          <section class="pt-6 border-t border-ink/20 space-y-4">
            <h2 class="text-sm uppercase tracking-widest text-graymain"> Profiel </h2>

            <div class="space-y-4 max-w-md">
              <input v-model="updatedName" class="w-full border p-3" placeholder="Naam" />
              <input v-model="updatedEmail" class="w-full border p-3" placeholder="E-mailadres" />
              <button @click="saveProfile" class="border border-ink px-5 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition">
                Opslaan </button>

              <p v-if="message" class="text-green-600 text-sm">
                {{ message }} </p>
              <p v-if="error" class="text-red-600 text-sm">
                {{ error }} </p>
            </div>
          </section>

          <section class="pt-6 border-t border-ink/20 space-y-4">
            <h2 class="text-sm uppercase tracking-widest text-graymain"> Wachtwoord </h2>
            <div class="space-y-4 max-w-md"> <input type="password" v-model="oldPassword" class="w-full border p-3" placeholder="Huidig wachtwoord" />
              <input type="password" v-model="newPassword" class="w-full border p-3" placeholder="Nieuw wachtwoord" /> <input type="password" v-model="confirmPassword" class="w-full border p-3" placeholder="Bevestig nieuw wachtwoord" />
              <button @click="changePassword" class="border border-ink px-5 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition"> Wijzig wachtwoord </button>
              <p v-if="message" class="text-green-600 text-sm"> {{ message }} </p>
              <p v-if="error" class="text-red-600 text-sm"> {{ error }} </p>
            </div>
          </section>

          <section class="pt-6 border-t border-ink/20 space-y-4"> <h2 class="text-sm uppercase tracking-widest text-graymain"> Extra beveiliging </h2>
            <p class="text-sm text-graymain max-w-md"> Gebruikt bij het herstellen van je wachtwoord. </p>
            <div class="flex gap-4 max-w-md"> <input type="date" v-model="birthDate" class="border p-3 w-full" />
              <button @click="saveBirthDate" class="border border-ink px-4 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition"> Opslaan </button>
            </div> <p v-if="message" class="text-green-600 text-sm"> {{ message }} </p>
            <p v-if="error" class="text-red-600 text-sm"> {{ error }} </p>
          </section>

          <section class="pt-10 border-t border-ink/20 space-y-4">
            <h2 class="text-sm uppercase tracking-widest text-graymain">
              Geavanceerde acties
            </h2>

            <p class="text-sm text-graymain max-w-md">
              Het verwijderen van je account is permanent.
              Al je forum posts en gegevens worden definitief verwijderd.
            </p>

            <button @click="openDeleteAccountModal" class="border border-red-600 text-red-600 px-5 py-2
           text-xs uppercase tracking-widest
           hover:bg-red-600 hover:text-white transition">
              Verwijder dit account
            </button>
          </section>

        </section>
        <ConfirmModal v-if="showConfirmModal" title="Verwijderen" message="Weet je het zeker?" @confirm="confirmAction" @cancel="cancelConfirm" />
        <ConfirmModal v-if="showDeleteAccountModal" title="Account verwijderen" message="Weet je zeker dat je dit account wilt verwijderen?" @cancel="closeDeleteAccountModal" @confirm="confirmDeleteAccount" />

      </main>
    </div>
  </div>
</template>
