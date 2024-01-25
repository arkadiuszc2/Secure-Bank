import axios from 'axios'

export const backendApi = (url) => {
  const backendUrl = 'http://localhost:8080';

  const client = axios.create({
    baseURL: backendUrl + url,
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    }, withCredentials: true
  })

  client.interceptors.response.use(response => {
    return response
  }, function (error) {
    console.log('Error: An error occurred while calling backend', error)
    console.log(error.response.data)

    alert(JSON.stringify(error.response.data));

    if (error.response) {
      if (error.response.status === 404) {
        return { status: error.response.status }
      }

      return Promise.reject(error.response)
    } else if (error.request) {
      return Promise.reject("Error: No response from the server")
    } else {

    }
  })


  return client;
}