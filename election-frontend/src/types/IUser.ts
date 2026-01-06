
export interface AuthUser {
  id: number
  name: string
  email: string
  role: 'USER' | 'ADMIN'
  quizBestMatch: string
  createdAt: string
}
