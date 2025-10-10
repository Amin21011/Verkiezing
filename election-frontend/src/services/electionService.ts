import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/electionresults/'

export const getTopParties = async (limit = 3) => {
  const res = await axios.get(`${API_BASE_URL}parties/top`, {
    params: { limit }
  })
  return res.data
}
