import { getToken } from "../services/authService";

export async function authFetch(url: string, options: RequestInit = {}) {
  const token = getToken();

  if (!token) throw new Error("Niet ingelogd");

  return fetch(url, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: `Bearer ${token}`
    }
  });
}
