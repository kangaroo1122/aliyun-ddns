const api = {
    getDomainList: params => getRequest("/domains", params),
    refreshRecord: params => postRequest("/refresh", params)
}
