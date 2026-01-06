import { ref } from 'vue'
import { getToken } from '@/services/authService'

export interface ForumPost {
  id: number
  title: string
  postedAt: string
  user: { email: string }
}

export interface Topic {
  id: number
  name: string
}

export function useForumPosts() {
  const allPosts = ref<ForumPost[]>([])
  const myPosts = ref<ForumPost[]>([])

  async function loadPosts(currentEmail?: string) {
    const res = await fetch('http://localhost:8080/api/forum/posts')
    const data = await res.json()

    allPosts.value = data
    myPosts.value = currentEmail
      ? data.filter((p: ForumPost) => p.user?.email === currentEmail)
      : []
  }

  async function deletePost(id: number) {
    await fetch(`http://localhost:8080/api/forum/posts/${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${getToken()}`
      }
    })
    myPosts.value = myPosts.value.filter(p => p.id !== id)
  }

  return {
    allPosts,
    myPosts,
    loadPosts,
    deletePost,
  }
}
