import { ref } from 'vue'
import type { AuthUser } from '@/types/IUser.ts'
import { authFetch } from '@/helpers/authFetch.ts'
import { jwtDecode } from 'jwt-decode'

export const API_URL = `${import.meta.env.VITE_API_URL}/auth`
export const authUser = ref<string | null>(null)
export const authToken = ref<string | null>(localStorage.getItem("token"));

interface JwtPayload {
  sub: string // email
  name: string
  userId: number
  role: "USER" | "ADMIN"
}

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
  localStorage.setItem("token", data.token);

  return data
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

  const response = await authFetch(`${API_URL}/me`)

  if (!response.ok) return null
  return await response.json() as AuthUser
}

export async function updateUser(name: string, email: string) {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Geen token gevonden");

  const res = await fetch(`${API_URL}/update`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ name, email }),
  });

  if (!res.ok) throw new Error(await res.text());
  return await res.json();
}

export function getToken(): string | null {
  return authToken.value ?? localStorage.getItem("token")
}

export async function changePassword(oldPassword: string, newPassword: string) {
  const token = localStorage.getItem("token")
  if (!token) throw new Error("Geen token gevonden")

  const res = await fetch(`${API_URL}/change-password`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ oldPassword, newPassword }),
  })

  if (!res.ok) throw new Error(await res.text())
  return await res.json()
}

export async function verifyResetIdentity(
  email: string,
  birthDate: string
): Promise<void> {
  const res = await fetch(`${API_URL}/verify`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, birthDate }),
  });

  if (!res.ok) {
    throw new Error("Verification failed");
  }
}

export async function resetPassword(
  email: string,
  newPassword: string
): Promise<void> {
  const res = await fetch(`${API_URL}/confirm`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, newPassword }),
  });

  if (!res.ok) {
    throw new Error("Password reset failed");
  }
}
