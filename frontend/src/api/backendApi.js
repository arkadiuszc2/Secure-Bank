import axios from 'axios'

export const backendApi = (url) => {
  const backendUrl = 'http://localhost:8080';

  const client = axios.create({
    baseURL: backendUrl + url,
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    }
  })

  return client
}