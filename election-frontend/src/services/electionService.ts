import axios from 'axios'

export const API_URL = `${import.meta.env.VITE_API_URL}`

export const getTopParties = async (limit = 5) => {
  const res = await axios.get(`${API_URL}/parties/top`, {
    params: { limit }
  })
  return res.data
}

export const getTopCandidatesByParty = async (partyId: string, limit = 5) => {
  const res = await axios.get(`${API_URL}/results/candidates/top`, {
    params: { partyId, limit },
  })
  return res.data
}
