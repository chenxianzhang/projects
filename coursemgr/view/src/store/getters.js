const getters = {
    token: state => state.user.token,
    roles: state => state.user.roles,
    name: state => state.user.name,
    loginRole: state => state.user.loginRole,
    permissionRouters: state => state.permission.routers,
    addRouters: state => state.permission.addRouters,
    cachedViews: state => state.tagsView.cachedViews,
    visitedViews: state => state.tagsView.visitedViews
}
export default getters
