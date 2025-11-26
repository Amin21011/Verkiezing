import { getCurrentUser, updateUser, changePassword } from "@/services/authService";
import type { AuthUser } from "@/types/IUser";

export async function loadUserProfile(): Promise<AuthUser | null> {
  return await getCurrentUser();
}

export async function saveUserProfile(name: string, email: string) {
  return await updateUser(name, email);
}

export async function updatePassword(oldPass: string, newPass: string) {
  return await changePassword(oldPass, newPass);
}
