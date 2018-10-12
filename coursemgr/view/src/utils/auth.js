import Cookies from 'js-cookie'

const TokenKey = 'User-Token'
const LOGIN_ROLE = 'LOGIN_ROLE'
const COURSE_ID = 'COURSE_ID'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
// 暂时先使用cookie存储
export function getLoginRole() {
  return Cookies.get(LOGIN_ROLE)
}

export function setLoginRole(loginRole) {
  return Cookies.set(LOGIN_ROLE, loginRole)
}

export function removeLoginRole() {
  return Cookies.remove(LOGIN_ROLE)
}

export function getCourseId() {
  return Cookies.get(COURSE_ID)
}

export function setCourseId(courseId) {
  return Cookies.set(COURSE_ID, courseId)
}

export function removeCourseId() {
  return Cookies.remove(COURSE_ID)
}
