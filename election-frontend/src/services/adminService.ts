import { getToken } from "@/services/authService";

const BASE = "http://localhost:8080/api/auth/admin";

function authHeaders() {
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + getToken(),
  };
}

export async function fetchAllUsers() {
  const res = await fetch(`${BASE}`, { headers: authHeaders() });
  return await res.json();
}

export async function deleteUser(id: number) {
  const res = await fetch(`${BASE}/${id}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  return await res.json();
}

export async function updateUserRole(id: number, role: string) {
  const res = await fetch(`${BASE}/${id}/role?role=${role}`, {
    method: "PUT",
    headers: authHeaders(),
  });
  return await res.json();
}
