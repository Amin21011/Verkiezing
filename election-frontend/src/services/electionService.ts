import axios from 'axios'

const API_BASE_URL = 'http://oege.ie.hva.nl:9122/api/'

export const getTopParties = async (limit = 5) => {
  const res = await axios.get(`${API_BASE_URL}parties/top`, {
    params: { limit }
  })
  return res.data
}

export const getTopCandidatesByParty = async (partyId: string, limit = 5) => {
  const res = await axios.get(`${API_BASE_URL}candidates/top`, {
    params: { partyId, limit },
  })
  return res.data
}
