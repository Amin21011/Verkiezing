import { getToken } from "@/services/authService";
const API_URL = import.meta.env.VITE_API_URL;
const BASE = `${API_URL}/auth/admin`;

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + getToken(),
  };
}

export async function fetchAllUsers() {
  const res = await fetch(`${BASE}/users`, {
    headers: authHeaders(),
  });

  if (!res.ok) {
    throw new Error("Gebruikers ophalen mislukt");
  }

  return await res.json();
}


export async function updateUserRole(id: number, role: "USER" | "ADMIN") {
  const res = await fetch(`${BASE}/${id}/role?role=${role}`, {
    method: "PUT",
    headers: authHeaders(),
  });

  if (!res.ok) {
    throw new Error("Rol aanpassen mislukt");
  }

  return await res.json();
}

export async function deleteUser(id: number) {
  const res = await fetch(`${BASE}/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  });

  if (!res.ok) {
    throw new Error("Gebruiker verwijderen mislukt");
  }

  return await res.json();
}
