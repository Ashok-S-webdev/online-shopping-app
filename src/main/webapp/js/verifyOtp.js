document.getElementById('otp-form').addEventListener('submit', async function(event) {
    event.preventDefault()
    const formData = new FormData(this)

    fetch('/online-shopping-app/api/otp/verify', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.status === 'success') {
            console.log('successful')
            if (result.role === 'admin'){
                window.location.href = 'admin/admin.html'
            } else {
                window.location.href = 'user/user.html'
            }
        } else {
            document.getElementById('error-message').style.display = 'block'
        }
    })
})

document.getElementById('logout-button').addEventListener('click', async function() {
    try {
        const response = await fetch('/online-shopping-app/api/logout', {
            method: 'POST',
        });

        if (response.redirected) {
            window.location.href = response.url;
        }
    } catch (error) {
        console.error('Error during logout:', error);
        alert('An error occurred during logout.');
    }
})