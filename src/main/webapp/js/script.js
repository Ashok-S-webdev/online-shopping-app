document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault()
    const formData = new FormData(this)

    fetch('/online-shopping-app/api/login', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(result => {
        if (result.status === 'success') {
            console.log('successful')
            window.location.href = 'verify-otp.html'
        } else {
            document.getElementById('error-message').style.display = 'block'
        }
    })
})
// document.getElementById("login-form").addEventListener('submit', async function (event) {
//     event.preventDefault();

//     const formData = new FormData(this);

//     try {
//         const response = await fetch(this.action, {
//             method: this.method,
//             body: new URLSearchParams(formData)
//         });

//         const result = await response.json();

//         if (result.status === 'success') {
//             // If login is successful, redirect based on role
//             if (result.role === 'user') {
//                 window.location.href = "user/user.html";
//             } else if (result.role === 'admin') {
//                 window.location.href = "admin/admin.html";
//             }
//         } else {
//             // If login fails, show error message
//             document.getElementById('error-message').innerText = result.message;
//         }
//     } catch (error) {
//         console.error("Unexpected error occurred");
//         document.getElementById('error-message').innerText = "Unexpected error occurred";
//     }
// });
