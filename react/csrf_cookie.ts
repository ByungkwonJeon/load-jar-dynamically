import axios from 'axios';

// 쿠키 값을 가져오는 유틸 함수
function getCookie(name) {
  const matches = document.cookie.match(new RegExp(
    '(?:^|; )' + name.replace(/([$?*|{}\[\]\\\/+^])/g, '\\$1') + '=([^;]*)'
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

// Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // 서버 주소
  withCredentials: true, // 쿠키 포함 요청
});

// 요청 인터셉터로 X-XSRF-TOKEN 추가
apiClient.interceptors.request.use(config => {
  const xsrfToken = getCookie('XSRF-TOKEN'); // 쿠키에서 가져오기
  if (xsrfToken) {
    config.headers['X-XSRF-TOKEN'] = xsrfToken; // 헤더로 추가
  }
  return config;
}, error => {
  return Promise.reject(error);
});

export default apiClient;