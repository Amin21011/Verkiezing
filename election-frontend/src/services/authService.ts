import { ref } from 'vue'
import { jwtDecode } from "jwt-decode";
import type { AuthUser } from '@/types/IUser.ts'

export const API_URL = 'http://localhost:8080/auth'
export const authUser = ref<string | null>(null)
export const authToken = ref<string | null>(localStorage.getItem("token"));

export async function register(name: string, email: string, password: string) {
  const res = await fetch(`${API_URL}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, email, password }),
  })
  if (!res.ok) throw new Error(await res.text())

  const data = await res.json()
  authUser.value = data.name
  authToken.value = data.token

  return data
}

interface JwtPayload {
  sub: string // email
  name: string
  userId: number
  role: "USER" | "ADMIN"
}

export async function login(email: string, password: string) {
  const res = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) throw new Error("Login mislukt");

  const data = await res.json();
  authToken.value = data.token;
  localStorage.setItem("token", data.token);

  return data;
}

export function getAuthUser() {
  if (!authToken.value) return null;

  const decoded = jwtDecode<JwtPayload>(authToken.value);
  return {
    name: decoded.name,
    email: decoded.sub,
    role: decoded.role
  };
}

export function logout() {
  authToken.value = null;
  localStorage.removeItem("token");
}


export async function getCurrentUser(): Promise<AuthUser | null> {
  const token = localStorage.getItem('token')
  if (!token) return null

  const response = await fetch('http://localhost:8080/auth/me', {
    headers: { Authorization: `Bearer ${token}` }
  })

  if (!response.ok) return null
  return await response.json() as AuthUser
}


export function getToken(): string | null {
  return authToken.value ?? localStorage.getItem("token")
}
