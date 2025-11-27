<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCurrentUser } from '@/services/authService'
import type { AuthUser } from '@/types/IUser.ts'
import { jwtDecode } from 'jwt-decode'

interface Poll {
  id: number
  question: string
  options: string[]
  votes: number[]
}

interface JwtPayload {
  sub: string
  name: string
  userId: number
  role: 'USER' | 'ADMIN'
}

// User state
const user = ref<AuthUser | null>(null)
const loading = ref(true)
const polls = ref<Poll[]>([])
const message = ref('')

// Admin inputs
const newQuestion = ref('')
const newOptions = ref('')

// Edit mode
const editingPollId = ref<number | null>(null)
const editQuestion = ref('')
const editOptions = ref('')

// LocalStorage votes voorkomen
const userVotes = ref<Record<number, number>>({})

const API_URL = `${import.meta.env.VITE_API_URL}/polls`

function loadUserVotes() {
  const saved = localStorage.getItem('userVotes')
  userVotes.value = saved ? JSON.parse(saved) : {}
}

function saveUserVotes() {
  localStorage.setItem('userVotes', JSON.stringify(userVotes.value))
}

// Polls ophalen
async function loadPolls() {
  const res = await fetch(API_URL)
  polls.value = await res.json()
}

// Stemmen
async function vote(pollId: number, optionIndex: number) {
  const previous = userVotes.value[pollId]

  if (previous === optionIndex) {
    message.value = 'Je hebt al gestemd!'
    setTimeout(() => (message.value = ''), 2000)
    return
  }

  if (previous !== undefined) {
    await fetch(`${API_URL}/${pollId}/reset/${previous}`, { method: 'PUT' })
  }

  await fetch(`${API_URL}/${pollId}/vote/${optionIndex}`, { method: 'POST' })

  userVotes.value[pollId] = optionIndex
  saveUserVotes()
  loadPolls()
}

// ADMIN → Aanmaken
async function createPoll() {
  const options = newOptions.value
    .split(',')
    .map(o => o.trim())
    .filter(o => o.length > 0)

  if (!newQuestion.value || options.length === 0) return

  await fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      question: newQuestion.value,
      options,
    }),
  })

  newQuestion.value = ''
  newOptions.value = ''
  loadPolls()
}

// ADMIN → Verwijderen
async function deletePoll(id: number) {
  await fetch(`${API_URL}/${id}`, { method: 'DELETE' })
  loadPolls()
}

// ADMIN → Bewerken starten
function startEdit(poll: Poll) {
  editingPollId.value = poll.id
  editQuestion.value = poll.question
  editOptions.value = poll.options.join(', ')
}

// ADMIN → Bewerking opslaan
async function saveEdit() {
  if (editingPollId.value === null) return

  const options = editOptions.value
    .split(',')
    .map(o => o.trim())
    .filter(o => o.length > 0)

  if (!editQuestion.value || options.length === 0) return

  await fetch(`${API_URL}/${editingPollId.value}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      question: editQuestion.value,
      options,
    }),
  })

  editingPollId.value = null
  loadPolls()
}

// Load user from JWT or backend
onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token) {
    const decoded = jwtDecode<JwtPayload>(token)
    user.value = {
      name: decoded.name,
      email: decoded.sub,
      role: decoded.role,
      id: decoded.userId,
      quizBestMatch: '',
    }
  }

  const current = await getCurrentUser()
  if (current) user.value = current

  loading.value = false
  loadUserVotes()
  loadPolls()
})
</script>

<template>
  <main class="p-10 max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold mb-6">Admin Poll Dashboard</h1>

    <div v-if="loading">Loading...</div>

    <div v-else>
      <!-- ADMIN: Poll aanmaken -->
      <div v-if="user?.role === 'ADMIN'" class="p-4 bg-gray-100 rounded mb-6">
        <h2 class="font-bold mb-2">Poll aanmaken</h2>
        <input v-model="newQuestion" class="border p-2 w-full mb-2" placeholder="Vraag" />
        <input v-model="newOptions" class="border p-2 w-full mb-2" placeholder="Opties, gescheiden door komma" />
        <button @click="createPoll" class="bg-blue-600 text-white px-4 py-2 rounded">Toevoegen</button>
      </div>

      <div v-if="message" class="bg-yellow-100 p-2 mb-3 rounded text-center">{{ message }}</div>
      <p v-if="polls.length === 0" class="italic text-gray-600">Geen polls beschikbaar.</p>

      <!-- POLLS -->
      <div v-for="poll in polls" :key="poll.id" class="border p-4 rounded mb-6 shadow">
        <!-- EDIT MODE -->
        <div v-if="editingPollId === poll.id">
          <h3 class="font-bold mb-2">Poll bewerken</h3>
          <input v-model="editQuestion" class="border p-2 w-full mb-2" />
          <input v-model="editOptions" class="border p-2 w-full mb-2" />
          <button @click="saveEdit" class="bg-green-600 text-white px-4 py-2 rounded mr-2">Opslaan</button>
          <button @click="editingPollId = null" class="bg-gray-500 text-white px-4 py-2 rounded">Annuleer</button>
        </div>

        <!-- VIEW MODE -->
        <div v-else>
          <h3 class="font-semibold text-lg mb-3">{{ poll.question }}</h3>
          <div class="space-y-2">
            <div
              v-for="(option, i) in poll.options"
              :key="i"
              @click="vote(poll.id, i)"
              class="border rounded p-3 cursor-pointer flex justify-between"
              :class="{ 'bg-green-200 font-bold border-green-600': userVotes[poll.id] === i }"
            >
              <span>{{ option }}</span>
              <span>{{ poll.votes.reduce((a,b)=>a+b,0) > 0 ? Math.round((poll.votes[i]/poll.votes.reduce((a,b)=>a+b,0))*100)+'%' : '0%' }}</span>
            </div>
          </div>

          <div v-if="user?.role === 'ADMIN'" class="mt-4 flex gap-2">
            <button @click="startEdit(poll)" class="bg-yellow-500 text-white px-3 py-1 rounded">Bewerken</button>
            <button @click="deletePoll(poll.id)" class="bg-red-500 text-white px-3 py-1 rounded">Verwijderen</button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
