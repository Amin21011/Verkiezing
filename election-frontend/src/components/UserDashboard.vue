<script setup lang="ts">
import { ref, onMounted } from "vue";
import router from "@/router";
import { getToken } from "@/services/authService";
import { loadUserProfile, saveUserProfile, updatePassword } from "@/services/AccountService";
import type { AuthUser } from "@/types/IUser";
import ConfirmModal from '@/components/ConfirmModal.vue'

const user = ref<AuthUser | null>(null);
const loading = ref(true);
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
const birthDate = ref("");
const birthDateMessage = ref("");
const birthDateError = ref("");
const savingBirthDate = ref(false);
const activeTab = ref<"profile" | "posts">("profile");

interface ForumPost {
  id: number;
  title: string;
  postedAt: string;
  user: { email: string };
}

const myPosts = ref<ForumPost[]>([]);
const deletingId = ref<number | null>(null);
const showDeleteModal = ref(false)
const postToDelete = ref<number | null>(null)

onMounted(async () => {
  try {
    user.value = await loadUserProfile();
    if (user.value) {
      updatedName.value = user.value.name;
      updatedEmail.value = user.value.email;
      await fetchMyPosts();
    }
  } finally {
    loading.value = false;
  }
});

async function fetchMyPosts() {
  if (!getToken()) return;

  const res = await fetch("http://localhost:8080/api/forum/posts");
  const all = await res.json();

  myPosts.value = all.filter(
    (p: ForumPost) => p.user?.email === user.value?.email
  );
}

function goToPost(id: number) {
  router.push(`/forum/${id}`);
}

async function deletePost(id: number) {
  if (!confirm("Weet je zeker dat je deze post wilt verwijderen?")) return;

  deletingId.value = id;

  await fetch(`http://localhost:8080/api/forum/posts/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: "Bearer " + getToken(),
    },
  });

  myPosts.value = myPosts.value.filter(p => p.id !== id);
  deletingId.value = null;
}

async function saveProfile() {
  try {
    saving.value = true;
    successMessage.value = "";
    errorMessage.value = "";

    const updated = await saveUserProfile(updatedName.value, updatedEmail.value);
    user.value = updated;

    successMessage.value = "Profiel succesvol bijgewerkt!";
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
    oldPassword.value = newPassword.value = confirmPassword.value = "";
  } catch {
    passwordError.value = "Wachtwoord wijzigen mislukt.";
  } finally {
    changing.value = false;
  }
}

async function saveBirthDate() {
  try {
    savingBirthDate.value = true;
    birthDateMessage.value = "";
    birthDateError.value = "";

    const res = await fetch("http://localhost:8080/api/account/birthdate", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + getToken(),
      },
      body: JSON.stringify({ birthDate: birthDate.value }),
    });

    if (!res.ok) throw new Error();

    birthDateMessage.value = "Geboortedatum veilig opgeslagen.";
  } catch {
    birthDateError.value = "Opslaan mislukt.";
  } finally {
    savingBirthDate.value = false;
  }
}

function openDeleteModal(id: number) {
  postToDelete.value = id
  showDeleteModal.value = true
}
function closeDeleteModal() {
  showDeleteModal.value = false
  postToDelete.value = null
}
async function confirmDelete() {
  if (!postToDelete.value) return

  await deletePost(postToDelete.value)
  closeDeleteModal()
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
          <p class="uppercase text-[10px] tracking-widest text-graymain">
            Account
          </p>
          <p class="font-serif font-bold text-lg leading-tight">
            {{ user?.name }}
          </p>
          <p class="text-xs italic text-graymain">
            {{ user?.email }}
          </p>
        </div>

        <nav class="space-y-1 text-sm">
          <button @click="activeTab = 'profile'" :class="activeTab === 'profile' ? 'border-l-2 border-[var(--accent)] bg-[var(--highlight)]' : 'border-l-2 border-transparent hover:bg-[var(--highlight)]/40'" class="w-full px-4 py-2 text-left transition">
            Profiel & beveiliging
          </button>

          <button @click="activeTab = 'posts'" :class="activeTab === 'posts' ? 'border-l-2 border-[var(--accent)] bg-[var(--highlight)]' : 'border-l-2 border-transparent hover:bg-[var(--highlight)]/40'" class="w-full px-4 py-2 text-left transition">
            Mijn forum posts
            <span class="ml-1 text-xs italic text-graymain">
              ({{ myPosts.length }})
            </span>
          </button>
        </nav>
      </aside>

      <main class="px-10 py-16 space-y-20">
        <p v-if="loading" class="italic text-graymain">
          Gegevens laden…
        </p>

        <section v-if="!loading && activeTab === 'profile'" class="space-y-20 max-w-3xl">
          <header class="space-y-3">
            <p class="uppercase tracking-[0.35em] text-[11px] text-graymain">
              Welkom terug
            </p>
            <h1 class="text-4xl font-serif font-bold">
              {{ user?.name }}
            </h1>
            <p class="text-graymain max-w-xl">
              Beheer hier je gegevens, beveiliging en persoonlijke inzichten.
            </p>
          </header>

          <section class="pl-6 border-l-2 border-ink/30 space-y-4">
            <h2 class="text-lg font-serif font-semibold">
              Politieke match
            </h2>

            <div v-if="!user?.quizBestMatch" class="text-sm text-graymain">
              <p>Je hebt de quiz nog niet ingevuld.</p>
              <button @click="$router.push('/quiz')" class="mt-3 border border-ink px-4 py-1.5 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition">
                Start quiz
              </button>
            </div>

            <div v-else class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-full border border-ink flex items-center justify-center font-serif font-bold text-sm">
                {{ user.quizBestMatch.slice(0,2).toUpperCase() }}
              </div>
              <div class="text-sm">
                <p>
                  Beste match:
                  <span class="text-[var(--accent)] font-medium">
                    {{ user.quizBestMatch }}
                  </span>
                </p>
                <button @click="$router.push('/quiz')" class="text-xs underline text-graymain hover:text-ink">
                  Quiz opnieuw doen
                </button>
              </div>
            </div>
          </section>

          <section class="space-y-6">
            <h2 class="text-xl font-serif font-bold">
              Profielgegevens </h2>
            <div class="space-y-4 max-w-md">
              <input v-model="updatedName" class="w-full border p-3" placeholder="Naam" />
              <input v-model="updatedEmail" class="w-full border p-3" placeholder="E-mailadres" />
              <button @click="saveProfile" class="border border-ink px-5 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition">
                Opslaan </button>

              <p v-if="successMessage" class="text-green-600 text-sm">
                {{ successMessage }} </p>
              <p v-if="errorMessage" class="text-red-600 text-sm">
                {{ errorMessage }} </p>
            </div>
          </section>

          <section class="space-y-6">
            <h2 class="text-xl font-serif font-bold">
              Beveiliging
            </h2>

            <div class="space-y-4 max-w-md">
              <input type="password" v-model="oldPassword" class="w-full border p-3" placeholder="Huidig wachtwoord" />
              <input type="password" v-model="newPassword" class="w-full border p-3" placeholder="Nieuw wachtwoord" />
              <input type="password" v-model="confirmPassword" class="w-full border p-3" placeholder="Bevestig nieuw wachtwoord" />

              <button @click="handleChangePassword" class="border border-ink px-5 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition">
                Wijzig wachtwoord
              </button>

              <p v-if="passwordMessage" class="text-green-600 text-sm">
                {{ passwordMessage }}
              </p>
              <p v-if="passwordError" class="text-red-600 text-sm">
                {{ passwordError }}
              </p>
            </div>
          </section>

          <section class="pt-6 border-t border-ink/20 space-y-4">
            <h2 class="text-sm uppercase tracking-widest text-graymain">
              Extra beveiliging
            </h2>

            <p class="text-sm text-graymain max-w-md">
              Gebruikt bij het herstellen van je wachtwoord.
            </p>

            <div class="flex gap-4 max-w-md">
              <input type="date" v-model="birthDate" class="border p-3 w-full" />
              <button @click="saveBirthDate" class="border border-ink px-4 py-2 text-xs uppercase tracking-widest hover:bg-ink hover:text-paper transition">
                Opslaan
              </button>
            </div>

            <p v-if="birthDateMessage" class="text-green-600 text-sm">
              {{ birthDateMessage }}
            </p>
            <p v-if="birthDateError" class="text-red-600 text-sm">
              {{ birthDateError }}
            </p>
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


        <section v-if="!loading && activeTab === 'posts'" class="space-y-10">
          <header class="space-y-2">
            <h1 class="text-3xl font-serif font-bold">
              Mijn forum posts
            </h1>
            <p class="italic text-graymain">
              Jouw bijdragen aan het publieke debat
            </p>
          </header>

          <div v-if="myPosts.length === 0" class="italic text-graymain">
            Je hebt nog geen posts geplaatst.
          </div>

          <div v-else class="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            <article v-for="post in myPosts" :key="post.id" class="group relative border border-ink/30 bg-paper p-6
                     hover:shadow-lg transition cursor-pointer" @click="goToPost(post.id)">
              <button @click.stop="openDeleteModal(post.id)" class="absolute top-3 right-3 opacity-0 group-hover:opacity-100 text-graymain hover:text-red-600 transition">
                🗑
              </button>

              <h3 class="font-serif font-bold text-lg mb-2">
                {{ post.title }}
              </h3>

              <p class="text-xs italic text-graymain">
                {{ new Date(post.postedAt).toLocaleDateString('nl-NL') }}
              </p>
            </article>
          </div>
        </section>
      </main>
    </div>

    <ConfirmModal v-if="showDeleteModal" title="Post verwijderen" message="Weet je zeker dat je deze post wilt verwijderen?" @cancel="closeDeleteModal" @confirm="confirmDelete" />
    <ConfirmModal v-if="showDeleteAccountModal" title="Account verwijderen" message="Weet je zeker dat je dit account wilt verwijderen?" @cancel="closeDeleteAccountModal" @confirm="confirmDeleteAccount" />
  </div>
</template>
