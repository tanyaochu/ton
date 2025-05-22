document.addEventListener('DOMContentLoaded', function () {
    // 获取表列表
    fetch('/getTableServlet', {
        method: "GET",
        headers:{
            'token' : localStorage.getItem("authToken")
        }
    })
        .then(response => response.json())
        .then(tables => {
            document.getElementById('loading').style.display = 'none';
            renderTableList(tables);
        })
        .catch(error => {
            console.error('加载失败:', error);
            document.getElementById('loading').textContent = '列表加载失败,请访问：/login重新登录';
        });
});

// 渲染表格列表
function renderTableList(tables) {
    const container = document.getElementById('tableList');
    tables.forEach(tableName => {
        const div = document.createElement('div');
        div.className = 'table-item';
        div.innerHTML = `
            <span>${tableName}</span>
            <button class="download-btn" 
                    onclick="handleDownload('${tableName}')">下载</button>
        `;
        container.appendChild(div);
    });
}

// 处理下载请求
function handleDownload(tableName) {
    // 创建表单数据
    const formData = new FormData();
    formData.append('tableName', tableName);

    // 发送请求
    fetch('/downloadTableServlet', {
        method: 'POST',
        body: formData,
        headers:{
            'token' : localStorage.getItem("authToken")
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text)
                });
            }
            return response.blob();
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${tableName}.xlsx`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
        })
        .catch(error => {
            alert('下载失败: ' + error.message);
        });
}
