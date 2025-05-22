document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault(); // 阻止表单默认提交行为

    // 获取表单数据
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // 隐藏之前可能显示的消息
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';

    // 发送数据到后端验证
    fetch('/loginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('网络响应不正常');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                // 登录成功
                document.getElementById('successMessage').textContent = '登录成功，正在跳转...';
                document.getElementById('successMessage').style.display = 'block';

                // 保存token到本地存储
                if (data.token) {
                    localStorage.setItem('authToken', data.token);
                }

                // 跳转到主页或用户仪表板
                window.location.href = data.redirectUrl || '/static/html/index.html';
            } else {
                // 登录失败，显示错误信息
                document.getElementById('errorMessage').textContent = data.message || '用户名或密码错误';
                document.getElementById('errorMessage').style.display = 'block';
            }
        })
        .catch(error => {
            console.error('登录错误:', error);
            document.getElementById('errorMessage').textContent = '登录过程中发生错误，请稍后再试';
            document.getElementById('errorMessage').style.display = 'block';
        });
});

