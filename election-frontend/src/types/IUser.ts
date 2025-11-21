
export interface AuthUser {
  id?: number
  name: string
  email: string
  role: 'USER' | 'ADMIN'
  quizCompleted: boolean
  quizBestMatch: string
}
