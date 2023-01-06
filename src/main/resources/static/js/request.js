axios.interceptors.response.use(resp => {
    if (resp.request.responseType === 'blob') {
        let blob = new Blob([resp.data])
        let contentDisposition = resp.headers['content-disposition'];
        let fileName = decodeURI(contentDisposition.replace("attachment;filename=", ""));
        if ('download' in document.createElement('a')) { //支持a标签download的浏览器
            const link = document.createElement('a')//创建a标签
            link.download = fileName//a标签添加属性
            link.style.display = 'none'
            link.href = URL.createObjectURL(blob)
            document.body.appendChild(link)
            link.click()//执行下载
            URL.revokeObjectURL(link.href) //释放url
            document.body.removeChild(link)//释放标签
        } else { //其他浏览器
            navigator.msSaveBlob(blob, fileName)
        }
        return;
    }
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
