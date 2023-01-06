axios.interceptors.response.use(resp => {
    return resp;
}, resp => {
    console.log(resp)
    if (!resp.response && resp.code === 'ECONNABORTED' && resp.request.readyState === 4 && resp.request.status === 0) {
        ELEMENT.Message.error("请求超时啦~");
    } else {
        //401时可跳转登录
        if (resp.response && (resp.response.status === 400 || resp.response.status === 403)) {
            ELEMENT.Message.error(resp.response.data.msg);
        } else {
            ELEMENT.Message.error("出错啦~~~");
        }
    }
})

//get请求
function getRequest(url, params = {}, timeout = 2000, responseType = 'json') {
    return new Promise((resolve, reject) => {
        axios.get(url, {
            params,
            timeout,
            responseType
        }).then(resp => {
            if (resp) {
                resolve(resp.data)
            }
        }).catch(error => {
            reject(error)
        })
    })
}

//post请求
function postRequest(url, params = {}, timeout = 2000, responseType = 'json') {
    return new Promise((resolve, reject) => {
        axios.post(url, params, {
            timeout: timeout,
            responseType: responseType
        }).then(resp => {
            if (resp) {
                resolve(resp.data)
            }
        }).catch(error => {
            reject(error)
        })
    })
}
