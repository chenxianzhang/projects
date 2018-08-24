const getters = {
    token: state => state.user.token,
    roles: state => state.user.roles,
    name: state => state.user.name,
    permissionRouters: state => state.permission.routers,
    addRouters: state => state.permission.addRouters,
    courseId: state => state.course.cId,
}
export default getters
